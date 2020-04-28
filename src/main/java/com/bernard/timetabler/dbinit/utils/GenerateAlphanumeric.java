package com.bernard.timetabler.dbinit.utils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateAlphanumeric {
    private static String alphaNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    private static String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String num = "1234567890";
    private static final Random rand = ThreadLocalRandom.current();
    private static final Set<String> identifiers = new HashSet();

    public static String generateRandomLongAlpha(int bound) {
        StringBuilder builder = new StringBuilder();
        int len = 0;

        for (int k = 0; k < bound; k++) {
            int length = rand.nextInt(bound);
            int count = 0;

            len = builder.toString().length();

            if (count != 0) {
                builder.append(Character.toLowerCase(alpha.charAt(rand.nextInt(alpha.length()))));
            } else {
                builder.append(alpha.charAt(rand.nextInt(alpha.length())));
            }

            count++;

            if (k == rand.nextInt(5) + 5) {
                builder.append(" ");
                count = 0;
            }

            if (identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
                k = 0;
            }
        }

        identifiers.add(builder.toString());

        return builder.toString();
    }

    public static String generateIdAlphanumeric(int bound) {
        StringBuilder builder = new StringBuilder();
        int len = 0;

        for (int k = 0; k < bound; k++) {
            int length = rand.nextInt(bound);

            len = builder.toString().length();

            builder.append(alphaNum.charAt(rand.nextInt(alphaNum.length())));


            if (identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
                k = 0;
            }
        }

        identifiers.add(builder.toString());

        return builder.toString();
    }

    public static String generateSingleShortAlpha(int bound) {
        StringBuilder builder = new StringBuilder();
        int len = 0;

        for (int k = 0; k < bound; k++) {
            int length = rand.nextInt(bound);

            len = builder.toString().length();

            builder.append(alpha.charAt(rand.nextInt(alpha.length())));

            if (identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
                k = 0;
            }
        }

        identifiers.add(builder.toString());

        return builder.toString();
    }

    public static String setAdmissionDate(String yearOfStudy) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        int year = calendar.get(Calendar.YEAR) - Integer.parseInt(yearOfStudy.trim());

        // randomly generate month and day of registration
        Random rand = ThreadLocalRandom.current();

        List<Integer> months = new ArrayList();
        months.add(0);
        months.add(5);
        months.add(9);

        int month1 = months.get(rand.nextInt(2));

        int day = rand.nextInt(14);

        Calendar cal = Calendar.getInstance();
        if (month1 == 0)
            cal.set(year, Calendar.JANUARY, day);
        if (month1 == 5)
            cal.set(year, Calendar.MAY, day);
        if (month1 == 9)
            cal.set(year, Calendar.SEPTEMBER, day);

        return cal.getTime().toString();
    }
}
