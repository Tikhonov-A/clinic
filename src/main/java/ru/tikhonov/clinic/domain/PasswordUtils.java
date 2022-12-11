package ru.tikhonov.clinic.domain;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class PasswordUtils {

    public static final int ITERATIONS = 1000;
    public static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final SecureRandom RAND = new SecureRandom();

    public static Optional<String> generateSalt(final int length) {

        byte[] salt = new byte[length];
        RAND.nextBytes(salt);

        return Optional.of(Base64.getEncoder().encodeToString(salt));
    }

    public static Optional<String> hashThePlainTextPassword(String plainTextPassword, String salt) {
        char[] chars = plainTextPassword.toCharArray();
        byte[] bytes = salt.getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);
        Arrays.fill(chars, Character.MIN_VALUE);

        try {

            SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] securePassword = fac.generateSecret(spec).getEncoded();
            return Optional.of(Base64.getEncoder().encodeToString(securePassword));

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            //Handle the exception
            return Optional.empty();
        } finally {
            spec.clearPassword();
        }
    }

    public static boolean verifyThePlainTextPassword(
            String plainTextPassword,
            String hashedPassword,
            String salt) {

        Optional<String> optEncrypted = hashThePlainTextPassword(plainTextPassword, salt);
        return optEncrypted.map(s -> s.equals(hashedPassword)).orElse(false);

    }

}
