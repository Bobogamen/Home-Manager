package com.home_manager.utility;

import net.bytebuddy.utility.RandomString;

import javax.servlet.http.HttpServletRequest;

public class MailUtility {
    public static String appUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        return url.replace(request.getServletPath(), "");
    }

    public static String passwordResetMailSubject() {
        return "Домоуправител забравена парола";
    }

    public static String registrationMailSubject() {
        return "Добре дошли в Домоуправител";
    }

    public static String resetPasswordUrl(HttpServletRequest request, String token) {
        return MailUtility.appUrl(request) + "/reset-password?token=" + token;
    }

    public static String getToken() {
        return RandomString.make(30);
    }
}
