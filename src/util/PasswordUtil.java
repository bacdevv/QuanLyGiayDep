package util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {
    private PasswordUtil() {
    }

    public static String hash(String plainText) {
        if (plainText == null || plainText.isBlank()) {
            throw new IllegalArgumentException("Mật khẩu không được trống");
        }
        return BCrypt.hashpw(plainText, BCrypt.gensalt(12));
    }

    public static boolean matches(String plainText, String hash) {
        if (plainText == null || hash == null || hash.isBlank()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainText, hash);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
