package co.arbelos.gtm.modules.dataingestor.tools.sftp;

import co.arbelos.gtm.ValuationApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ValuationApp.class)
public class SFTPServiceTest {

    @Autowired
    private SFTPService sftpService;


    @Test
    public void testDataFetchingFromFTP() {
        sftpService.retrieveData();
    }
}
