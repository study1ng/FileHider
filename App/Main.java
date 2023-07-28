package App;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import App.Background.FileManager;
import App.Background.RegistrationManager;
import App.Background.Exceptions.InvalidPasswordException;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        final String PASSWORD = "Daisuke937182264";
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            try {
                RegistrationManager.makeNewRegistration(PASSWORD);
                isLoggedIn = true;
            } catch (InvalidPasswordException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                isLoggedIn = false;
            }
        }
        try {
            final FileManager fm;
            fm = new FileManager(PASSWORD);
            fm.load();
            fm.addFile(Paths.get("C:\\Users\\icant\\Documents\\FileHider\\Main.java"));
            fm.addFile(Paths.get("C:\\Users\\icant\\Documents\\FileHider\\InvalidPasswordException.java"));
            fm.save();

            System.out.println("Initialized.\n");
            for (Path p : fm.getFiles()) {
                System.out.println(p);
            }
            System.out.println();
            fm.addFile(Paths.get("C:\\Users\\icant\\Documents\\Main.java"));
            fm.save();
            System.out.println("Added.\n");
            for (Path p : fm.getFiles()) {
                System.out.println(p);
            }
            System.out.println();
            fm.removeFile(Paths.get("C:\\Users\\icant\\Documents\\FileHider\\Main.java"));
            fm.save();
            System.out.println("Removed.\n");
            for (Path p : fm.getFiles()) {
                System.out.println(p);
            }
            System.out.println();
            fm.removeFile(1);
            fm.save();
            System.out.println("Removed.\n");
            for (Path p : fm.getFiles()) {
                System.out.println(p);
            }
            System.out.println();
        } catch (InvalidKeyException | NoSuchPaddingException | InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }

    }

}
