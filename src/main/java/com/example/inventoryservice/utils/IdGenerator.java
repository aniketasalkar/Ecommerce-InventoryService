package com.example.inventoryservice.utils;

import java.util.Random;

public class IdGenerator {

    public static String generateId(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        // StringBuilder to hold the random string
        StringBuilder result = new StringBuilder(length);

        // Create Random object
        Random random = new Random();

        // Loop through the length and generate random characters
        for (int i = 0; i < length; i++) {
            // Randomly pick a character from the characters string
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }

        return result.toString();
    }
}
