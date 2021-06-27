package co.arbelos.gtm.valuation.service.vin.soap;

import gtestimate.wsdl.GTIWS;
import gtestimate.wsdl.GTIWSResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;

import java.util.UUID;


@Service
public class SoapClient extends WebServiceGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(SoapClient.class);

    public GTIWSResponse getCarInformation(String vin) {
        final String uuid = RandomStringUtils.randomAlphabetic(14);

        String payload  = "<request>\n" +
            "\t\t  <autenticationData>\n" +
            "\t\t    <gsId value=\"SGS018100B\" />\n" +
            "\t\t    <gsPwd value=\"F33204D22V\" />\n" +
            "\t\t    <customerId value=\"GTM_RO\" />\n" +
            "\t\t    <userId value=\"GTMRO_Valtest\" />\n" +
            "\t\t  </autenticationData>\n" +
            "\t\t   <requestInfo>\n" +
            "\t\t    <estimate>\n" +
            "\t\t      <operation value=\"create\" />\n" +
            "\t\t      <showGui value=\"false\" />\n" +
            "\t\t      <includeEstimate value=\"true\" />\n" +
            "\t\t      <estimateInfo>\n" +
            "\t\t        <estimateId value=\"[VAL]"+uuid+"\" />\n" +
            "\t\t        <vehicleInfo>\n" +
            "\t\t          <registrationNumber value=\"TEST\" />\n" +
            "\t\t          <vin value=\""+vin+"\" />\n" +
            "\t\t        </vehicleInfo>\n" +
            "\t\t      </estimateInfo>\n" +
            "\t\t      <behaviour>\n" +
            "\t\t        <useIdCar value=\"true\" />\n" +
            "\t\t      </behaviour>\n" +
            "\t\t    </estimate>\n" +
            "\t\t  </requestInfo>\n" +
            "</request>";

        GTIWS request = new GTIWS();
        request.setXml(payload);

        log.info("Requesting estimation");

        return (GTIWSResponse) getWebServiceTemplate()
            .marshalSendAndReceive(
                "https://estimate.mygtmotive.com/webservice/GtInterfaceWS.asmx?WSDL",
                request,
                webServiceMessage -> ((SoapMessage)webServiceMessage).setSoapAction("http://gtmotive.com/GTIWS")
            );
    }
}
