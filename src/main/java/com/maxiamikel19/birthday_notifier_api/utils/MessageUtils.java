package com.maxiamikel19.birthday_notifier_api.utils;

public class MessageUtils {

    private MessageUtils() {
    }

    public static String generateUpcomingMessage(String recipientName, String birthdayNames, int dayInterval) {

        return """
                Hi %s,
                In %d days it will be the birthday of %s.
                Don't forget to congratulate them.
                """
                .formatted(recipientName, dayInterval, birthdayNames);
    }

    public static String generateCurrentBirthdayMessage(String recipientName, String birthdayNames) {

        return """
                Hi %s,
                Today is the birthday of %s.
                Don't forget to congratulate them.
                """
                .formatted(recipientName, birthdayNames);
    }

    public static String generateCongratulationsMessage(String fullName) {

        return """
                Hello %s,
                Today the whole team wishes you a very happy birthday.
                Enjoy your special day!
                """
                .formatted(fullName);
    }

}
