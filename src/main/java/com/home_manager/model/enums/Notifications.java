package com.home_manager.model.enums;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public enum Notifications {
    REGISTRATION_SUCCESSFULLY,
    INVALID_LOGIN,
    UPDATED_SUCCESSFULLY,
    CASHIER_NO_HOMES_GROUP,
    CASHIER_REGISTRATION_SUCCESSFULLY,
    CHOOSE_AT_LEAST_ONE_HOME,
    HOME_ADDED_SUCCESSFULLY,
    RESIDENT_ADDED_SUCCESSFULLY,
    INVALID_EMAIL,
    PASSWORD_CHANGED_SUCCESSFULLY,
    FEE_ADD_FOR_HOME,
    FEE_ADD_FOR_HOMES,
    ASSIGN_SUCCESSFULLY;
    private Properties properties;

    public void init() {
        if (properties == null) {
            properties = new Properties();

            String path = "src/main/resources/i18n/notifications.properties";

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
