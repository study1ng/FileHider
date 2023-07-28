package App.Background;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FileManager {

    private final static String ENCODING = "UTF-8";
    private final static String CIPHER = "AES/CBC/PKCS5Padding";
    private final Cipher DECRYPTOR;
    private final Cipher ENCRYPTOR;

    private ArrayList<Path> fileContent = new ArrayList<Path>();

    public FileManager(String password) throws NoSuchAlgorithmException, IOException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException {
        final String ALGORITHM = "SHA3-256";
        if (!Files.exists(Setting.FILE_COLLECTION)) {
            Files.createFile(Setting.FILE_COLLECTION);
            Files.setAttribute(Setting.FILE_COLLECTION, "dos:hidden", true);
        }
        final byte[] IV = new byte[16];
        final byte[] BYTE_KEY = MessageDigest.getInstance(ALGORITHM).digest(password.getBytes(ENCODING));

        SecretKeySpec key = new SecretKeySpec(BYTE_KEY, "AES");
        int tmp = "FileManager.java".hashCode();
        for (int i = 0; i < 16; i++) {
            IV[i] = (byte) ((byte) (tmp >> (i)) % 10);
        }
        IvParameterSpec iv = new IvParameterSpec(IV);
        DECRYPTOR = Cipher.getInstance(CIPHER);
        DECRYPTOR.init(Cipher.DECRYPT_MODE, key, iv);
        ENCRYPTOR = Cipher.getInstance(CIPHER);
        ENCRYPTOR.init(Cipher.ENCRYPT_MODE, key, iv);
    }

    // This method will return false if Setting.FILE_COLLECTION not fit the format
    // or any
    // Exception occurs;
    // Otherwise, true.
    public boolean load() {
        // We'll compile each file's path to 128-byte using CIPHER.
        try {
            LinkedList<byte[]> encrypted = new LinkedList<byte[]>();
            byte[] buffer = new byte[128];
            int len;
            var stream = new BufferedInputStream(Files.newInputStream(Setting.FILE_COLLECTION));
            while ((len = stream.read(buffer)) > 0) {
                if (len != 128) {
                    return false;
                }
                encrypted.add(buffer);
            }
            loadFileContent(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void loadFileContent(List<byte[]> encrypted) {
        encrypted.forEach((byte[] encry) -> {
            try {
                byte[] decrypted = DECRYPTOR.doFinal(encry);
                fileContent.add(Paths.get(new String(decrypted, ENCODING)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    };

    public void addFile(Path path) {
        fileContent.add(path);
    }

    public void removeFile(Path path) {
        fileContent.remove(path);
    }

    public void removeFile(int index) {
        fileContent.remove(index);
    }

    public ArrayList<Path> getFiles() {
        return fileContent;
    }

    private void saveFile(Path path) throws IllegalBlockSizeException, BadPaddingException, IOException {
        byte[] encrypted = ENCRYPTOR.doFinal(path.toString().getBytes(ENCODING));
        Files.write(Setting.FILE_COLLECTION, encrypted, StandardOpenOption.APPEND);
    }

    public void save() {
        try {
            // Clear Setting.FILE_COLLECTION
            FileChannel.open(Setting.FILE_COLLECTION, StandardOpenOption.WRITE)
                    .truncate(0).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Path p : fileContent) {
            try {
                saveFile(p);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }
}
