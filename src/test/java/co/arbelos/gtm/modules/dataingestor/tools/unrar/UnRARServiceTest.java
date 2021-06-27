package co.arbelos.gtm.modules.dataingestor.tools.unrar;

import co.arbelos.gtm.ValuationApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ValuationApp.class)
public class UnRARServiceTest {

    @Autowired
    private UnRARService unRARService;

    @Test
    public void testExtractionFromExeFile() {
        unRARService.extractExeFile("/data/jatodb/SSCRO_CS2002_TextFiles.exe", "/data/jatodb/");
    }
}
