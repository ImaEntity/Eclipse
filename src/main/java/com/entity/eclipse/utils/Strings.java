package com.entity.eclipse.utils;

public class Strings {
    public static String format(boolean value, String trueValue, String falseValue) {
        if(value) return "§a§l" + trueValue + "§r";
        else return "§c§l" + falseValue + "§r";
    }

    public static String format(boolean value) {
        return Strings.format(value, "✔", "❌");
    }
    public static String format(byte value) {
        return "§d§l" + value + "§r";
    }
    public static String format(short value) {
        return "§d§l" + value + "§r";
    }
    public static String format(int value) {
        return "§d§l" + value + "§r";
    }
    public static String format(long value) {
        return "§d§l" + value + "§r";
    }
    public static String format(float value) {
        return "§b§l" + value + "§r";
    }
    public static String format(double value) {
        return "§b§l" + value + "§r";
    }
    public static String format(char value) {
        return "§7§l" + value + "§r";
    }
    public static String format(String value) {
        return "§7" + value + "§r";
    }

    public static String camelToReadable(String camel) {
        StringBuilder readableString = new StringBuilder();
        String lowerCamel = camel.toLowerCase();

        StringBuilder temp = new StringBuilder();

        for(int i = 0; i < camel.length(); i++) {
            if(lowerCamel.charAt(i) == camel.charAt(i)) {
                temp.append(camel.charAt(i));
                continue;
            }

            readableString.append(temp);
            readableString.append(' ');
            readableString.append(camel.charAt(i));

            temp = new StringBuilder();
        }

        readableString.append(temp);

        if(readableString.charAt(0) == ' ')
            readableString.deleteCharAt(0);

        return readableString.toString();
    }
}
