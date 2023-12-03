package com.base.authproject.configurations;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Sha256 {
    public static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Converting to bytes
            byte[] encodedhash = digest.digest(
                    input.getBytes(StandardCharsets.UTF_8));

            // Converting from bytes to Hexadecimal
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
            return null;
        }
    }
}
