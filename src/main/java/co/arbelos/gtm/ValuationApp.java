package co.arbelos.gtm;

import co.arbelos.gtm.valuation.config.ApplicationProperties;
import co.arbelos.gtm.valuation.config.DefaultProfileUtil;
import io.github.jhipster.config.JHipsterConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication
@ComponentScan(basePackages = {"co.arbelos.gtm.core", "co.arbelos.gtm.modules", "co.arbelos.gtm.valuation"})
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
public class ValuationApp {

    private static final Logger log = LoggerFactory.getLogger(ValuationApp.class);

    private final Environment env;

    private final ApplicationContext appContext;

    @Autowired
    public ValuationApp(Environment env, ApplicationContext appContext) {
        this.env = env;
        this.appContext = appContext;
    }

    /**
     * Initializes valuation.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not " +
                "run with both the 'dev' and 'cloud' profiles at the same time.");
        }

        final String os = System.getProperty("os.name").toLowerCase();

        if (os.startsWith("mac")) {
            log.info("[INIT - MACOS] Installing necessary dependencies ...");
            // installDependencies(os, "unrar");
            log.info("[INIT - MACOS] Installations successfully made!");
        } else if (os.startsWith("linux")) {
            log.info("[INIT - LINUX] Installing necessary dependencies ...");
            // installDependencies(os, "unrar");
            log.info("[INIT - LINUX] Installations successfully made!");
        } else {
            log.error("[INIT] Operating system not supported! Only LINUX and MACOS is supported!");
            SpringApplication.exit(appContext, () -> 5);
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ValuationApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}{}\n\t" +
                "External: \t{}://{}:{}{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles());
    }

    private void installDependencies(String operatingSystem, String depedencyName) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (operatingSystem.startsWith("mac")) {
            processBuilder.command("bash", "-c", "brew install " + depedencyName);
        } else {
            processBuilder.command("bash", "-c", "apt install " + depedencyName);
        }

        try {
            log.info("[INIT - {}] Trying to install {}...", operatingSystem.toUpperCase(), depedencyName);
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                log.info("[INIT - {}] {} installed successfully!", operatingSystem.toUpperCase(), depedencyName);
                log.info("[INIT - {}] : {} ", operatingSystem.toUpperCase(), output);

            } else {
                log.error("[INIT - {}] : There was an error during installation of UnRAR", operatingSystem.toUpperCase());
                log.error("[INIT - {}] : {} ", operatingSystem.toUpperCase(), output);
                SpringApplication.exit(appContext, () -> 5);
            }

        } catch (InterruptedException | IOException e) {
            log.error("[INIT - {}] Error: {}", operatingSystem.toUpperCase(), e.getMessage());
        }
    }
}
