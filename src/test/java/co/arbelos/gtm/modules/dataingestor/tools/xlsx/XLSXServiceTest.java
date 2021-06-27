package co.arbelos.gtm.modules.dataingestor.tools.xlsx;

import co.arbelos.gtm.ValuationApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ValuationApp.class)
public class XLSXServiceTest {

    @Autowired
    private XLSXService xlsxService;

    private File carRegistrationsDataFile;

    @Before
    public void setup() {
        ClassLoader classLoader = getClass().getClassLoader();
        this.carRegistrationsDataFile = new File(classLoader.getResource("templates/jato/car_registrations_data.xlsx").getFile());
    }

    @Test
    public void parseXLSX() {
        try {
            final InputStream inputStream =
                new DataInputStream(new FileInputStream(this.carRegistrationsDataFile));
            MultipartFile multipartFile = new MockMultipartFile("car_registrations_data.xlsx", inputStream);

            xlsxService.handleFile(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
