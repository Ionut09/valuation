package co.arbelos.gtm.modules.dataingestor.tools.csv;

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
public class CSVServiceTest {

    @Autowired
    private CSVService csvService;

    private File csv_2016_8;
    private File csv_2016_10;
    private File csv_2016_11;
    private File csv_2016_12;

    @Before
    public void setup() {
        ClassLoader classLoader = getClass().getClassLoader();

        this.csv_2016_8 = new File(classLoader.getResource("templates/autovit/autovit_2016-08-22_unmatched.csv").getFile());
        this.csv_2016_10 = new File(classLoader.getResource("templates/autovit/autovit_2016-10-31_unmatched.csv").getFile());
        this.csv_2016_11 = new File(classLoader.getResource("templates/autovit/autovit_2016-11-28_unmatched.csv").getFile());
        this.csv_2016_12 = new File(classLoader.getResource("templates/autovit/autovit_2016-12-28_unmatched.csv").getFile());
    }

    @Test
    public void parseCSV() {
        try {
            final InputStream csv_2016_8Stream =
                new DataInputStream(new FileInputStream(this.csv_2016_8));
            MultipartFile csv_2016_8File = new MockMultipartFile("autovit_2016-08-22_unmatched.csv", csv_2016_8Stream);
            //AutovitUploadDTO csv_2016_8ads = csvService.parseFile(csv_2016_8File);

            final InputStream csv_2016_10Stream =
                new DataInputStream(new FileInputStream(this.csv_2016_10));
            MultipartFile csv_2016_10File = new MockMultipartFile("autovit_2016-10-31_unmatched.csv", csv_2016_10Stream);
            //AutovitUploadDTO csv_2016_10ads = csvService.parseFile(csv_2016_10File);

            final InputStream csv_2016_11Stream =
                new DataInputStream(new FileInputStream(this.csv_2016_11));
            MultipartFile csv_2016_11File = new MockMultipartFile("autovit_2016-11-28_unmatched.csv", csv_2016_11Stream);
            //AutovitUploadDTO csv_2016_11ads = csvService.parseFile(csv_2016_11File);

            final InputStream csv_2016_12Stream =
                new DataInputStream(new FileInputStream(this.csv_2016_12));
            MultipartFile csv_2016_12File = new MockMultipartFile("autovit_2016-12-28_unmatched.csv", csv_2016_12Stream);
            //AutovitUploadDTO csv_2016_12ads = csvService.parseFile(csv_2016_12File);


//            adRepository.saveAll(csv_2016_8ads);
//            adRepository.saveAll(csv_2016_10ads);
//            adRepository.saveAll(csv_2016_11ads);
//            adRepository.saveAll(csv_2016_12ads);

        } catch (IOException e) { }
    }
}
