package com.aparzero.videomaker.util;

public class StringUtil {


    public static String processText(final String text) {
        // Remove slashes from the text using regex
      return text.replace("/", "");

    }



    public static String removeSpecialChar(final String text) {
        if (text == null) {
            return "output";
        }
        // Use a regular expression to replace all non-alphanumeric characters with an empty string
        return text.replaceAll("[^a-zA-Z0-9]", "");
    }




}


