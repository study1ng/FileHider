package App.Background;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.Arrays;

import App.Background.Exceptions.InvalidPasswordException;

public class RegistrationManager {

    static boolean canLogin(String password) throws IOException, NoSuchAlgorithmException {
        final byte[] DigestedPassword = Files.readAllBytes(Setting.PASSWORD_FILE_PATH);
        MessageDigest md = MessageDigest.getInstance(Setting.ALGORITHM);
        var InputtedPassword = md.digest(password.getBytes());
        return Arrays.equals(DigestedPassword, InputtedPassword);
    }

    public static void makeNewRegistration(String password)
            throws IOException, NoSuchAlgorithmException, InvalidPasswordException {

        if (password.length() < 1) {
            throw new InvalidPasswordException("Password must at least have 1 character.");
        }
        MessageDigest md = MessageDigest.getInstance(Setting.ALGORITHM);
        final byte[] DigestedPassword = md.digest(password.getBytes());

        try {
            Files.createFile(Setting.PASSWORD_FILE_PATH);
        } catch (FileAlreadyExistsException e) {
        }
        Files.setAttribute(Setting.PASSWORD_FILE_PATH, "dos:hidden", true);

        try {
            Files.createFile(Setting.FILE_COLLECTION);
        } catch (FileAlreadyExistsException e) {
        }
        Files.setAttribute(Setting.FILE_COLLECTION, "dos:hidden", true);

        // TODO: In the future, we may support multiple users.
        Files.write(Setting.PASSWORD_FILE_PATH, DigestedPassword);
    }
}