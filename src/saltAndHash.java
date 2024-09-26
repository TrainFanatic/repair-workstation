
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

/**
 * @author pwxcoo
 * @package com.pwxcoo.github.utils
 * @email pwxcoo@gmail.com
 * @time 2018/10/01 13:24
 * @description password util
 * @from https://stackoverflow.com/questions/18142745/how-do-i-generate-a-salt-in-java-for-salted-hash
 */
public class saltAndHash {
    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    /**
     * static utility class
     */
    private saltAndHash() {
    }

    /**
     * Returns a random salt to be used to hash a password.
     *
     * @return a 16 bytes random salt
     */
    public static String getNextSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        // return salt;
        return Base64.getEncoder().encodeToString(salt);

    }

    /**
     * Returns a salted and hashed password using the provided hash.<br>
     * Note - side effect: the password is destroyed (the char[] is filled with
     * zeros)
     *
     * @param password the password to be hashed
     * @param salt     a 16 bytes salt, ideally obtained with the getNextSalt method
     *
     * @return the hashed password with a pinch of salt
     */
    public static String hash(char[] password, String salt) {
        PBEKeySpec spec = new PBEKeySpec(password, Base64.getDecoder().decode(salt), ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); // select key derivation function
            // return skf.generateSecret(spec).getEncoded(); // wrong data type... must
            // return String
            return Base64.getEncoder().encodeToString(skf.generateSecret(spec).getEncoded()); // encoder --> String
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e); // error message for
                                                                                              // debugging
        } finally {
            spec.clearPassword();
        }
    }

    /**
     * Returns true if the given password and salt match the hashed value, false
     * otherwise.<br>
     * Note - side effect: the password is destroyed (the char[] is filled with
     * zeros)
     *
     * @param password     the password to check
     * @param salt         the salt used to hash the password
     * @param expectedHash the expected hashed value of the password
     *
     * @return true if the given password and salt match the hashed value, false
     *         otherwise
     */
    public static boolean isExpectedPassword(char[] password, String salt, char[] expectedHash) {
        char[] pwdHash = hash(password, salt).toCharArray(); // generate hash locally
        Arrays.fill(password, Character.MIN_VALUE);
        if (pwdHash.length != expectedHash.length) { // this is very bad. only happens when hashing alg is wrong or
                                                     // database is tampered
            System.err.println("Something is very wrong: The retrieved hash from SQL is the wrong length");
            return false;
        }

        for (int i = 0; i < pwdHash.length; i++) { // iterate to compare hash char by char
            if (pwdHash[i] != expectedHash[i])
                return false;
        }
        return true;
    }

    /**
     * Generates a random password of a given length, using letters and digits.
     *
     * @param length the length of the password
     *
     * @return a random password
     */
    public static String generateRandomPassword(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int c = RANDOM.nextInt(62);
            if (c <= 9) {
                sb.append(String.valueOf(c));
            } else if (c < 36) {
                sb.append((char) ('a' + c - 10));
            } else {
                sb.append((char) ('A' + c - 36));
            }
        }
        return sb.toString();
    }
}