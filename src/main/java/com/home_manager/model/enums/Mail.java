package com.home_manager.model.enums;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public enum Mail {
    HOME_MANAGER_FORGOTTEN_PASSWORD,
    HOME_MANAGER_WELCOME;

    private Properties properties;

    public void init() {

        if (properties == null) {
            properties = new Properties();

            String path = "src/main/resources/i18n/mail.properties";

            try {
                FileInputStream input = new FileInputStream(path);
                properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            } catch (Exception exception) {
                System.out.printf("Unable to load %s file from classpath%n", path);
            }
        }
    }

    public String getValue() {
        if (properties == null) {
            init();
        }

        return (String) properties.get(this.toString());
    }
}
