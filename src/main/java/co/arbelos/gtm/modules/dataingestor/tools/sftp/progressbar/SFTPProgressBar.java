package co.arbelos.gtm.modules.dataingestor.tools.sftp.progressbar;

import com.jcraft.jsch.SftpProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SFTPProgressBar implements SftpProgressMonitor {

    private static final Logger log = LoggerFactory.getLogger(SFTPProgressBar.class);

    @Override
    public void init(int op, String src, String dest, long max) {
        log.info("[SFTP CLIENT] Starting downloading : {} -> {}  [size : {} kb]", src, dest, max);
    }

    @Override
    public boolean count(long count) {
        return(true);
    }

    @Override
    public void end() {
        log.info("[SFTP CLIENT] Download finished!");
    }
}
