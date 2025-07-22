package org.demo.util;

public class AgeUtils {

    public static String getAgeCategory(int age) {
        if (age < 0) {
            return "Unknown";
        } else if (age < 18) {
            return "Under 18";
        } else if (age <= 25) {
            return "18–25";
        } else if (age <= 35) {
            return "26–35";
        } else if (age <= 45) {
            return "36–45";
        } else if (age <= 55) {
            return "46–55";
        } else {
            return "56+";
        }
    }
}
