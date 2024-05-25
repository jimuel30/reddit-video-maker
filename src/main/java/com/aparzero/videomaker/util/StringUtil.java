package com.aparzero.videomaker.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public static String generateUniqueKey(final String inputString) {
        try {
            // Create SHA-256 Hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(inputString.getBytes());

            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }




}


