package co.arbelos.gtm.modules.dataingestor.tools.unrar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UnRARService {
    private static final Logger log = LoggerFactory.getLogger(UnRARService.class);

    public void extractExeFile(String filePath, String destinationPath) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", "unrar x -y " + filePath + " " + destinationPath);


        try {
            log.info("[UnRAR Client] Trying to unrar .exe file: {} to {}", filePath, destinationPath);

            Process process = processBuilder.start();
            int exitVal = process.waitFor();

            if (exitVal == 0) {
                log.info("[UnRAR Client] File {} extracted successfully to: {}", filePath, destinationPath);
            } else {
                log.error("[UnRAR Client] Error while extraction {} to: {}", filePath, destinationPath);
            }

        } catch (InterruptedException | IOException e) {
            log.error("[UnRAR Client] Error while extraction {} to: {} with message: ", filePath, destinationPath, e.getMessage());
        }
    }
}
