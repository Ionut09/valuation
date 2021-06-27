package co.arbelos.gtm.valuation.service.vin;

import co.arbelos.gtm.core.domain.vin.VinQuery;
import co.arbelos.gtm.core.domain.vin.VinQueryMapperV2;
import co.arbelos.gtm.core.dto.web.vin.DecodedVinDTO;
import co.arbelos.gtm.core.repository.jato.VersionRepository;
import co.arbelos.gtm.core.repository.vin.VinQueryMapperRepository;
import co.arbelos.gtm.core.repository.vin.VinQueryMapperV2Repository;
import co.arbelos.gtm.core.repository.vin.VinQueryRepository;
import co.arbelos.gtm.valuation.service.vin.soap.SoapClient;
import co.arbelos.gtm.valuation.service.vin.util.VinUtil;
import gtestimate.wsdl.GTIWSResponse;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class VinService {

    private final Logger log = LoggerFactory.getLogger(VinService.class);

    private final SoapClient soapClient;
    private final VinQueryRepository vinQueryRepository;
    private final VersionRepository versionRepository;
    private final VinQueryMapperRepository vinQueryMapperRepository;
    private final VinQueryMapperV2Repository vinQueryMapperV2Repository;

    public List<String> gtmotiveBodyTypes = Arrays.asList("all terrain", "autovehicul comercial", "cabrio", "coupe", "sedan", "station wagon", "suv", "volum mare");


    public VinService(@Qualifier("soapClient") SoapClient soapClient,
                      VinQueryRepository vinQueryRepository,
                      VersionRepository versionRepository,
                      VinQueryMapperRepository vinQueryMapperRepository,
                      VinQueryMapperV2Repository vinQueryMapperV2Repository) {
        this.soapClient = soapClient;
        this.vinQueryRepository = vinQueryRepository;
        this.versionRepository = versionRepository;
        this.vinQueryMapperRepository = vinQueryMapperRepository;
        this.vinQueryMapperV2Repository = vinQueryMapperV2Repository;
    }

    public DecodedVinDTO decodeVIN(String vin) {

        // verify if vin already exists in database
        VinQuery vinQuery = Optional
            .ofNullable(vinQueryRepository.findOneByVin(vin))
            .orElseGet(() -> {
                GTIWSResponse response = soapClient.getCarInformation(vin);
                VinQuery newVinQuery = new VinQuery(vin, response.getGTIWSResult());
                vinQueryRepository.save(newVinQuery);

                return newVinQuery;
            });

        Document document = convertStringToXMLDocument(vinQuery.getData());

        String umc = document.getElementsByTagName("umc").item(0).getFirstChild().getTextContent().toUpperCase();
        String make = document.getElementsByTagName("make").item(0).getFirstChild().getTextContent().toUpperCase();
        String model = document.getElementsByTagName("model").item(0).getFirstChild().getTextContent().toUpperCase();
        Node equipmentList = document.getElementsByTagName("equipmentList").item(0);
        NodeList equipmentListNodes = equipmentList.getChildNodes();

        List<String> equipmentCodes = new ArrayList<>();
        for(int i=0; i < equipmentListNodes.getLength(); i++) {
            String value = equipmentListNodes.item(i).getFirstChild().getNextSibling().getTextContent();

            if (value.contains("/")) {
                String[] values = value.split("/");
                equipmentCodes.addAll(Arrays.asList(values));
            } else {
                equipmentCodes.add(value);
            }
        }

        AtomicReference<String>     bodyType     = new AtomicReference<>();
        AtomicReference<Integer>    doorsNo      = new AtomicReference<>();
        AtomicReference<String>     fuelType     = new AtomicReference<>();
        AtomicReference<LocalDate>  manufDate    = new AtomicReference<>();
        AtomicReference<Integer>    horsePower   = new AtomicReference<>();
        AtomicReference<Integer>    engineKw     = new AtomicReference<>();
        AtomicReference<String> engineManufactureCode = new AtomicReference<>();
        AtomicReference<String>     gearbox     = new AtomicReference<>();
        AtomicReference<Integer>    cc          = new AtomicReference<>();


        for(int i=0; i < equipmentListNodes.getLength(); i++) {
            Optional.ofNullable(
                equipmentListNodes.item(i)
                .getFirstChild()
                .getNextSibling()
                .getNextSibling()
                .getNextSibling()
                .getNextSibling()
            ).ifPresent(element -> {
                if (element.getParentNode().getChildNodes().getLength() == 7) {
                    element = element.getPreviousSibling();
                }

                if (element.getFirstChild().getTextContent().equals("CUTIE DE VITEZE")) {
                    gearbox.set(element.getNextSibling().getNextSibling().getTextContent());
                }

                if (element.getFirstChild().getTextContent().equals("CAROSERIE")) {
                    try {
                        if (gtmotiveBodyTypes.contains(element.getNextSibling().getTextContent().toLowerCase())) {
                            bodyType.set(element.getNextSibling().getTextContent());
                        }
                        if (gtmotiveBodyTypes.contains(element.getNextSibling().getNextSibling().getFirstChild().getFirstChild().getTextContent().toLowerCase())) {
                            bodyType.set(element.getNextSibling().getNextSibling().getTextContent());
                        }
                    } catch (Exception e) {
                        log.error("[VIN QUERY] Error parsing body type: {}", e.getMessage());
                    }

                    try {
                        if (element.getParentNode().getChildNodes().getLength() == 7) {
                            doorsNo.set(Integer.parseInt(element
                                .getParentNode()
                                .getFirstChild()
                                .getNextSibling()
                                .getFirstChild()
                                .getTextContent()
                                .replaceAll("\\D+", "")));
                        } else {
                            doorsNo.set(Integer.parseInt(element
                                .getParentNode()
                                .getFirstChild()
                                .getNextSibling()
                                .getNextSibling()
                                .getFirstChild()
                                .getTextContent()
                                .replaceAll("\\D+", "")));
                        }
                    } catch (Exception e) {
                        log.error("[VIN QUERY] Error parsing number of doors: {}", e.getMessage());
                    }
                }

                if (element.getFirstChild().getTextContent().equals("MOTOR")) {
                    if (!VinUtil.getFuel(element.getNextSibling().getTextContent()).equals("UNDEFINED")) {
                        try {
                            Matcher matcher = Pattern.compile("\\d+").matcher(element.getPreviousSibling().getPreviousSibling().getTextContent());
                            matcher.find();
                            cc.set(Integer.valueOf(matcher.group()));
                        } catch (Exception e) {
                            log.error("[VIN QUERY] Error parsing cc: {}", e.getMessage());
                        }

                        try {
                            fuelType.set(VinUtil.getFuel(element.getNextSibling().getTextContent()));
                            engineManufactureCode.set(element.getParentNode().getFirstChild().getNextSibling().getTextContent());
                            String[] parts = element.getPreviousSibling().getPreviousSibling().getFirstChild().getTextContent().split(" ");
                            for (String s : parts) {
                                if (s.contains("KW")) {
                                    engineKw.set(new Scanner(s).useDelimiter("\\D+").nextInt());
                                }
                            }
                        } catch (Exception e) {
                            log.error("[VIN QUERY] Error parsing fuel type: {}", e.getMessage());
                        }

                        try {
                            horsePower.set(Integer.parseInt(
                                StringUtils.substringBetween(
                                    element.getPreviousSibling().getPreviousSibling().getFirstChild().getTextContent(),
                                    "(", ")"
                                )));
                        } catch (Exception e) {
                            log.error("[VIN QUERY] Error parsing horse power: {}", e.getMessage());

                        }

                    }
                }

                if (element.getFirstChild().getTextContent().equals("DATA FABRICATIE")) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

                    try {
                        Date d = simpleDateFormat.parse(
                            element
                                .getParentNode()
                                .getFirstChild()
                                .getTextContent()
                        );

                        LocalDateTime conv = LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
                        manufDate.set(conv.toLocalDate());
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }
                }
            });
        }

        List<VinQueryMapperV2> mappers = vinQueryMapperV2Repository.findAllByUmc(umc);

        List<VinQueryMapperV2> mappersFiltered = mappers.stream()
            .filter(m -> {
                if (doorsNo.get() != null && m.getJatoDoorsNo() != null) {
                    if (doorsNo.get() == m.getJatoDoorsNo()) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            })
            .filter(m -> {
                if (bodyType.get() != null && m.getBody() != null) {
                    if (bodyType.get().toLowerCase().equals(m.getBody().toLowerCase())) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            })
            .filter(m -> {
                if (manufDate.get() != null && m.getSellingStartYear() != null) {
                    if (manufDate.get().getYear() >= (m.getSellingStartYear() - 1)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            })
            .filter(m -> {
                if (manufDate.get() != null && m.getSellingEndYear() != null) {
                    if (manufDate.get().getYear() <= m.getSellingEndYear()) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            })
            .collect(Collectors.toList());


        VinQueryMapperV2 result = mappersFiltered.get(0);

        if (manufDate.get() == null) {
            manufDate.set(LocalDate.of(result.getSellingStartYear(), 06, 01));
        }

        DecodedVinDTO decodedVinDTO = DecodedVinDTO
            .builder()
            .manufacturer(result.getJatoMake())
            .model(result.getJatoModel())
            .body(result.getJatoBody())
            .doorsNo(result.getJatoDoorsNo())
            .date(manufDate.get())
            .fuelType(fuelType.get())
            .extraOptionCodes(equipmentCodes)
            .horsePower(horsePower.get()) // +- 1 remove the column
            .engineKw(engineKw.get()) // keep it for search
            .ampatament(result.getJatoAmpatament())
            .engineManufactureCode(engineManufactureCode.get())
            .gearbox(gearbox.get())
            .cc(cc.get())
            .build();

        return decodedVinDTO;
    }

    private Document convertStringToXMLDocument(String xmlString)
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));

            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
