package com.home_manager.utility;

import com.home_manager.model.enums.Months;

public class MonthsUtility {
    public static String getMonthName(int month) {
        return switch (month) {
            case 1 -> Months.JANUARY.getValue();
            case 2 -> Months.FEBRUARY.getValue();
            case 3 -> Months.MARCH.getValue();
            case 4 -> Months.APRIL.getValue();
            case 5 -> Months.MAY.getValue();
            case 6 -> Months.JUNE.getValue();
            case 7 -> Months.JULY.getValue();
            case 8 -> Months.AUGUST.getValue();
            case 9 -> Months.SEPTEMBER.getValue();
            case 10 -> Months.OCTOBER.getValue();
            case 11 -> Months.NOVEMBER.getValue();
            case 12 -> Months.DECEMBER.getValue();
            default -> "Wrong month number!";
        };
    }
}
