package com.bid90.edusupply;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PasswordUtil {

    /**
     * Retrieves a password based on provided parameters.
     *
     * @param password     The default password to return if pathPassword is empty.
     * @param pathPassword The path to a file containing an alternative password.
     * @return The password to use, either from the file or the provided default.
     */
    public static String getPassword(String password, String pathPassword) {
        if (!pathPassword.isBlank()) {
            return getPasswordFromFile(pathPassword);
        }
        return password;
    }

    /**
     * Retrieves a password from a file specified by the path.
     *
     * @param pathPassword The path to the file containing the password.
     * @return The password read from the file.
     * @throws RuntimeException If there is an error reading the password file.
     */
    private static String getPasswordFromFile(String pathPassword) {
        try {
            return new String(Files.readAllBytes(Paths.get(pathPassword))).trim();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read password file", e);
        }
    }
}