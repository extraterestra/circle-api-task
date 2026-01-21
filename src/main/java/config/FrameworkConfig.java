package config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Getter
public class FrameworkConfig {
    private static final String DEFAULT_CONFIG_FILE = "framework.properties";
    private static final String DEFAULT_BASE_URL = "https://restful-booker.herokuapp.com";
    private static final int DEFAULT_CONNECT_TIMEOUT_MS = 10_000;
    private static final int DEFAULT_READ_TIMEOUT_MS = 10_000;

    private final String baseUrl;
    private final int connectTimeoutMs;
    private final int readTimeoutMs;

    public FrameworkConfig() {
        Properties props = new Properties();

        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE)) {
            if (is != null) {
                props.load(is);
            } else {
                log.warn("Config file '{}' not found on classpath; falling back to defaults/system properties", DEFAULT_CONFIG_FILE);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + DEFAULT_CONFIG_FILE, e);
        }

        this.baseUrl = value(props, "baseUrl", DEFAULT_BASE_URL);
        this.connectTimeoutMs = intValue(props, "connectTimeoutMs", DEFAULT_CONNECT_TIMEOUT_MS);
        this.readTimeoutMs = intValue(props, "readTimeoutMs", DEFAULT_READ_TIMEOUT_MS);
    }

    private static String value(Properties props, String key, String defaultValue) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isBlank()) {
            return sys.trim();
        }
        return props.getProperty(key, defaultValue).trim();
    }

    private static int intValue(Properties props, String key, int defaultValue) {
        String raw = value(props, key, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer for '" + key + "': " + raw, e);
        }
    }

}
