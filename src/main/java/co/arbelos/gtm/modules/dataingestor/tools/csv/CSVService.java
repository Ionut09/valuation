package co.arbelos.gtm.modules.dataingestor.tools.csv;

import co.arbelos.gtm.core.domain.marketobservation.MarketData;
import co.arbelos.gtm.core.domain.marketobservation.MarketDataIngestHistory;
import co.arbelos.gtm.core.dto.web.administration.AutovitUploadDTO;
import co.arbelos.gtm.core.dto.web.administration.TradeInUploadDTO;
import co.arbelos.gtm.core.repository.marketobservation.MarketDataIngestHistoryRepository;
import co.arbelos.gtm.core.repository.marketobservation.MarketDataRepository;
import co.arbelos.gtm.core.util.AutovitUtil;
import co.arbelos.gtm.modules.dataingestor.tools.csv.exceptions.MalformedCSVException;
import co.arbelos.gtm.modules.dataingestor.tools.csv.mapper.AutovitMapper;
import co.arbelos.gtm.modules.dataingestor.tools.csv.mapper.TradeInMapper;
import co.arbelos.gtm.modules.dataingestor.tools.currency.CurrencyService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.jms.Queue;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class CSVService {

    private static final Logger log = LoggerFactory.getLogger(CSVService.class);

    private final MarketDataRepository marketDataRepository;
    private final MarketDataIngestHistoryRepository marketDataIngestHistoryRepository;

    private final Queue autovitQueue;
    private final Queue tradeinQueue;
    private final JmsTemplate jmsTemplate;

    private boolean lock = false;
    private boolean lock2 = false;
    private boolean lock3 = false;

    public CSVService(MarketDataRepository marketDataRepository,
                      MarketDataIngestHistoryRepository marketDataIngestHistoryRepository,
                      @Qualifier("inmemory.autovit") Queue autovitQueue,
                      @Qualifier("inmemory.tradein") Queue tradeinQueue,
                      JmsTemplate jmsTemplate) {
        this.marketDataRepository = marketDataRepository;
        this.marketDataIngestHistoryRepository = marketDataIngestHistoryRepository;
        this.autovitQueue = autovitQueue;
        this.tradeinQueue = tradeinQueue;
        this.jmsTemplate = jmsTemplate;
    }

    @Transactional
    public List<String> handleFile(MultipartFile multipartFile) {
        lock = false;
        AutovitUploadDTO autovitUploadDTO = parseFile(multipartFile);
        lock = true;

        jmsTemplate.convertAndSend(autovitQueue, autovitUploadDTO);

        return autovitUploadDTO.getErrors();
    }

    @Transactional
    public List<String> handleFileTradeIn(MultipartFile multipartFile) {
        lock2 = false;
        TradeInUploadDTO tradeInUploadDTO = parseFileTradeIn(multipartFile);
        lock2 = true;

        jmsTemplate.convertAndSend(tradeinQueue, tradeInUploadDTO);

        return tradeInUploadDTO.getErrors();
    }

    @Transactional
    public List<String> handleAutovitExcelFile(MultipartFile multipartFile) {
        lock3 = false;
        AutovitUploadDTO autovitUploadDTO = parseFileAutovitExcel(multipartFile);
        lock3 = true;

        jmsTemplate.convertAndSend(autovitQueue, autovitUploadDTO);

        return autovitUploadDTO.getErrors();
    }

    @JmsListener(destination = "inmemory.autovit")
    public void persistFiles(AutovitUploadDTO autovitUploadDTO){
        if (autovitUploadDTO == null || autovitUploadDTO.getAds() == null) {
            lock = false;
            lock3 = false;
            return;
        }
        log.info("Persisting new autovit files [ {} ]!", autovitUploadDTO.getAds().size());

        autovitUploadDTO.getAds().forEach(el -> el.setIngestFileId(autovitUploadDTO.getIngestHistoryId()));
        marketDataRepository.saveAll(autovitUploadDTO.getAds());

        log.info("Persisting autovit finished, saved: [ {} ]!", autovitUploadDTO.getAds().size());
        lock = false;
        lock3 = false;
    }

    @JmsListener(destination = "inmemory.tradein")
    public void persistTradeInFiles(TradeInUploadDTO tradeInUploadDTO){
        if (tradeInUploadDTO == null || tradeInUploadDTO.getAds() == null) {
            lock2 = false;
            return;
        }
        log.info("Persisting new tradeIn files [ {} ]!", tradeInUploadDTO.getAds().size());

        tradeInUploadDTO.getAds().forEach(el -> el.setIngestFileId(tradeInUploadDTO.getIngestHistoryId()));
        marketDataRepository.saveAll(tradeInUploadDTO.getAds());

        log.info("Persisting tradeIn finished, saved: [ {} ]!", tradeInUploadDTO.getAds().size());
        lock2 = false;
    }

    public boolean getLock() {
        return this.lock;
    }
    public boolean getLock2() {
        return this.lock2;
    }
    public boolean getLock3() {
        return this.lock3;
    }


    private AutovitUploadDTO parseFile(MultipartFile multipartFile) {
       List<MarketData> ads = new ArrayList<>();
       List<String> errors = new ArrayList<>();

       AutovitUploadDTO autovitUploadDTO = new AutovitUploadDTO();

       try (final Reader reader = new InputStreamReader(multipartFile.getInputStream())) {
           final CSVParser parser = new CSVParser(
               reader,
               CSVFormat.RFC4180
                        .withFirstRecordAsHeader()
                        .withIgnoreEmptyLines(true)
                        .withTrim());

           parser
               .getRecords()
               .forEach(record -> {
                   try {
                        ads.add(AutovitMapper.fromMap(record.toMap()));
                   } catch (MalformedCSVException | DateTimeParseException e) {
                       log.error("[CSV Parser] Exception when ingesting autovit ad -> {}", e.getMessage());
                       errors.add("Error on ad with id: " + record.get("id_anunt") + " , details: " + e.getMessage());
                   }
               });

           autovitUploadDTO.setAds(ads);
           autovitUploadDTO.setErrors(errors);

           MarketDataIngestHistory ingestHistory = marketDataIngestHistoryRepository.save(new MarketDataIngestHistory(Arrays.toString(errors.toArray()), multipartFile.getOriginalFilename(), multipartFile.getSize()));
           autovitUploadDTO.setIngestHistoryId(ingestHistory.getId());

           return autovitUploadDTO;
       } catch (IOException e) {
           log.error("[CSV Parser] Error parsing file {} -> {}", multipartFile.getName(), e.getMessage());
           errors.add("Error parsing file: " + multipartFile.getName() + " , details: " + e.getMessage());
       }

        return autovitUploadDTO;
    }

    private TradeInUploadDTO parseFileTradeIn(MultipartFile multipartFile) {
        List<MarketData> ads = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        TradeInUploadDTO tradeInUploadDTO = new TradeInUploadDTO();

        try (final Reader reader = new InputStreamReader(multipartFile.getInputStream())) {
            final CSVParser parser = new CSVParser(
                reader,
                CSVFormat.RFC4180
                    .withFirstRecordAsHeader()
                    .withDelimiter(';')
                    .withIgnoreEmptyLines(true)
                    .withTrim());

            parser
                .getRecords()
                .forEach(record -> {
                    try {
                        ads.add(TradeInMapper.fromMap(record.toMap()));
                    } catch (MalformedCSVException | DateTimeParseException e) {
                        log.error("[CSV Parser] Exception when ingesting tradeIn ad -> {}", e.getMessage());
                        errors.add("Error on tradeIn item with id: " + record.getRecordNumber() + " , details: " + e.getMessage());
                    }
                });

            tradeInUploadDTO.setAds(ads);
            tradeInUploadDTO.setErrors(errors);

            MarketDataIngestHistory ingestHistory = marketDataIngestHistoryRepository.save(new MarketDataIngestHistory(Arrays.toString(errors.toArray()), multipartFile.getOriginalFilename(), multipartFile.getSize()));
            tradeInUploadDTO.setIngestHistoryId(ingestHistory.getId());

            return tradeInUploadDTO;
        } catch (IOException e) {
            log.error("[CSV Parser] Error parsing file {} -> {}", multipartFile.getName(), e.getMessage());
            errors.add("Error parsing file: " + multipartFile.getName() + " , details: " + e.getMessage());
        }

        return tradeInUploadDTO;
    }

    private AutovitUploadDTO parseFileAutovitExcel(MultipartFile multipartFile) {
        List<MarketData> ads = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        AutovitUploadDTO uploadDTO = new AutovitUploadDTO();

        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("[dd-MM-yy][yyyy-MM-dd][yyyy-MM-d]");

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                // skip header
                try {
                    if (row.getCell(0).getStringCellValue().equals("id")) {
                        continue;
                    }
                } catch (Exception ignored) {}

                // skip last line
                try {
                    if (row.getCell(0).getStringCellValue().equals("")) {
                        continue;
                    }
                } catch (Exception ignored) {}

                MarketData marketData = new MarketData();

                try {
                    String make = row.getCell(8).getStringCellValue();
                    marketData.setManufacturer(AutovitUtil.getManufacturerForJato(make).toUpperCase());
                } catch (Exception e) {
                    log.error("[EXCEL Parser] Exception when ingesting autovit ad -> {}", e.getMessage());
                    errors.add("Error on ad with row number: " + row.getRowNum() + " , make, details: " + e.getMessage());
                    continue;
                }

                try {
                    try {
                        String model = row.getCell(9).getStringCellValue();
                        marketData.setModel(AutovitUtil.getModelForJato(model).toUpperCase());
                    } catch (Exception e) {
                        double model = row.getCell(9).getNumericCellValue();
                        marketData.setModel(AutovitUtil.getModelForJato(String.valueOf(model)).toUpperCase());
                    }
                } catch (Exception e) {
                    log.error("[EXCEL Parser] Exception when ingesting autovit ad -> {}", e.getMessage());
                    errors.add("Error on ad with row number: " + row.getRowNum() + " , model, details: " + e.getMessage());
                    continue;
                }

                try {
                    String transmission = row.getCell(17).getStringCellValue();
                    marketData.setTransmissionType(AutovitUtil.getTransmissionTypeForJato(transmission).toUpperCase());
                } catch (Exception e) {
                    log.error("[EXCEL Parser] Exception when ingesting autovit ad -> {}", e.getMessage());
                    errors.add("Error on ad with row number: " + row.getRowNum() + " , transmission, details: " + e.getMessage());
                    continue;
                }

                try {
                    int horsePower = (int)row.getCell(16).getNumericCellValue();
                    marketData.setEngineHp(horsePower);
                } catch (Exception e) {
                    log.error("[EXCEL Parser] Exception when ingesting autovit ad -> {}", e.getMessage());
                    errors.add("Error on ad with row number: " + row.getRowNum() + " , horsePower, details: " + e.getMessage());
                    continue;
                }

                try {
                    String fuelType = row.getCell(14).getStringCellValue();
                    marketData.setFuelType(AutovitUtil.getFuelTypeForJato(fuelType));
                } catch (Exception e) {
                    log.error("[EXCEL Parser] Exception when ingesting autovit ad -> {}", e.getMessage());
                    errors.add("Error on ad with row number: " + row.getRowNum() + " , fuelType, details: " + e.getMessage());
                    continue;
                }

                try {
                    int manufactureYear = (int)row.getCell(12).getNumericCellValue();
                    marketData.setManufacturerYear(manufactureYear);
                } catch (Exception e) {
                    log.error("[EXCEL Parser] Exception when ingesting autovit ad -> {}", e.getMessage());
                    errors.add("Error on ad with row number: " + row.getRowNum() + " , manufacturerYear, details: " + e.getMessage());
                    continue;
                }

                try {
                    int kmNo = (int)row.getCell(13).getNumericCellValue();
                    marketData.setKmNo(kmNo);
                } catch (Exception e) {
                    log.error("[EXCEL Parser] Exception when ingesting autovit ad -> {}", e.getMessage());
                    errors.add("Error on ad with row number: " + row.getRowNum() + " , kmNo, details: " + e.getMessage());
                    continue;
                }

                try {
                    Date collectionDate = row.getCell(1).getDateCellValue();
                    marketData.setCollectionDate(LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(collectionDate)));
                } catch (Exception e) {
                    log.error("[EXCEL Parser] Exception when ingesting autovit ad -> {}", e.getMessage());
                    errors.add("Error on ad with row number: " + row.getRowNum() + " , collectionDate, details: " + e.getMessage());
                    continue;
                }

                try {
                    String currency = row.getCell(5).getStringCellValue();
                    if (currency.equals("EUR")) {
                        String priceDetails = row.getCell(6).getStringCellValue();

                        if (priceDetails.toLowerCase().contains("net")) {
                            double price = row.getCell(4).getNumericCellValue();
                            marketData.setPrice((float)price);
                        } else {
                            Double price = row.getCell(4).getNumericCellValue();
                            marketData.setPrice(CurrencyService.convertToNet(price.toString(), marketData.getCollectionDate().getYear()));
                        }
                    }
                } catch (Exception e) {
                    log.error("[EXCEL Parser] Exception when ingesting autovit ad -> {}", e.getMessage());
                    errors.add("Error on ad with row number: " + row.getRowNum() + " , price & currency, details: " + e.getMessage());
                    continue;
                }

                try {
                    if(row.getCell(24) != null) {
                        String vin = row.getCell(24).getStringCellValue();
                        marketData.setVin(vin);
                    }
                } catch (Exception e) {
                    log.error("[EXCEL Parser] Exception when ingesting autovit ad -> {}", e.getMessage());
                    errors.add("Error on ad with row number: " + row.getRowNum() + " , VIN, details: " + e.getMessage());
                    continue;
                }

                marketData.setSource("AUTOVIT");

                ads.add(marketData);
            }


            uploadDTO.setAds(ads);
            uploadDTO.setErrors(errors);

            MarketDataIngestHistory ingestHistory = marketDataIngestHistoryRepository.save(new MarketDataIngestHistory(Arrays.toString(errors.toArray()), multipartFile.getOriginalFilename(), multipartFile.getSize()));
            uploadDTO.setIngestHistoryId(ingestHistory.getId());

            return uploadDTO;
        } catch (IOException e) {
            log.error("[CSV Parser] Error parsing file {} -> {}", multipartFile.getName(), e.getMessage());
            errors.add("Error parsing file: " + multipartFile.getName() + " , details: " + e.getMessage());
        }

        return uploadDTO;
    }
}
