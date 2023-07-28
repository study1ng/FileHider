package App.Background;

import java.nio.file.Path;
import java.nio.file.Paths;


// For supporting user-defined settings.
final class Setting {
    static final Path FILE_COLLECTION = Paths.get("./data/.files");
    static final String ENCODING = "UTF-8";
    static final String CIPHER = "AES/CBC/PKCS5Padding";
    static final Path PASSWORD_FILE_PATH = Paths.get("./data/.password");
    static final String ALGORITHM = "SHA-256";
}
