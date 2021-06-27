package co.arbelos.gtm.modules.dataingestor.tools.sftp;

import co.arbelos.gtm.core.config.ApplicationProperties;
import co.arbelos.gtm.modules.dataingestor.tools.sftp.progressbar.SFTPProgressBar;
import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Component
public class SFTPService {

    private static final Logger log = LoggerFactory.getLogger(SFTPService.class);

    private final JSch jsch =  new JSch();
    private Session session;

    private final ApplicationProperties applicationProperties;

    public SFTPService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    /**
     * @param from Remote path
     * @param to   Local path
     * */
    public void retrieveData(String from, String to) {
        try {
            session.connect();

            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            sftpChannel.get(from, to, new SFTPProgressBar());
            sftpChannel.disconnect();

        } catch (JSchException | SftpException e) {
            log.error("[SFTP Client] Error while doing JSch connection: {}", e.getMessage());
        } finally {
            session.disconnect();
        }

    }

    public void retrieveData() {
        retrieveData(
            applicationProperties.getFtpFrom(),
            applicationProperties.getFtpTo());
    }

    @PostConstruct
    public void init() {
        log.info("[SFTP Client] Initializing JSch client...");

        try {
            session = jsch.getSession(
                applicationProperties.getFtpUser(),
                applicationProperties.getFtpUrl());
            session.setPassword(applicationProperties.getFtpPasswd());

            // Disable RSA key fingerprint checking (HostKey)
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
        } catch (JSchException e) {
            log.error("[SFTP Client] Error initializing JSch session: {}", e.getMessage());
        }
    }
}
