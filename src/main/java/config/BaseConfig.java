package config;

import com.github.dzieciou.testing.curl.CurlRestAssuredConfigFactory;
import io.restassured.config.RestAssuredConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BaseConfig {
    private static final Logger log = LoggerFactory.getLogger(BaseConfig.class);

    protected final RestAssuredConfig curlConfig;
    protected final Properties properties = new Properties();

    public BaseConfig() {
        curlConfig = CurlRestAssuredConfigFactory.createConfig();
        String env = getEnvName();
        String configFile = "config-" + env + ".properties";
        loadProperties(configFile);

        // This is helpful when switching env via: mvn test -Denv=qa|preprod
        log.info("Running tests against env='{}', config='{}', booking.baseUrl='{}'",
                env,
                configFile,
                properties.getProperty("booking.baseUrl"));
    }

    private String getEnvName() {
        String env = System.getProperty("env");
        if (env == null || env.isBlank()) {
            env = "qa";
        }
        return env.trim().toLowerCase();
    }

    protected String getRequiredProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required property: " + key);
        }
        return value.trim();
    }

    protected int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid integer property '" + key + "': " + value, e);
        }
    }

    private void loadProperties(String classpathResourceName) {
        try (InputStream in = BaseConfig.class.getClassLoader().getResourceAsStream(classpathResourceName)) {
            if (in == null) {
                throw new IllegalStateException("Config file not found on classpath: " + classpathResourceName);
            }
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load properties from " + classpathResourceName, e);
        }
    }
}
