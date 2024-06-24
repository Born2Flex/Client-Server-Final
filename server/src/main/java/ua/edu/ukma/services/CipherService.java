package ua.edu.ukma.services;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class CipherService {
    private static final String CIPHER_MODE = "AES/CBC/PKCS5Padding";
    private static final SecretKey key;
    private static final IvParameterSpec initVector;

    static {
        try {
            byte[] keyBytes = new byte[32];
            byte[] ivBytes = new byte[16];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.setSeed(12345L); // Fixed seed

            secureRandom.nextBytes(keyBytes);
            secureRandom.nextBytes(ivBytes);

            key = new SecretKeySpec(keyBytes, "AES");
            initVector = new IvParameterSpec(ivBytes);
//            System.out.println("Generated Key: " + Base64.getEncoder().encodeToString(key.getEncoded()));
//            System.out.println("Generated IV: " + Base64.getEncoder().encodeToString(initVector.getIV()));
        } catch (Exception e) {
            System.out.printf("Error occurred while generating key %s%n", e);
            throw new RuntimeException(e);
        }
    }

    public byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, key, initVector);
        byte[] encryptedData = cipher.doFinal(data);
        return base64Encode(encryptedData);
    }

    public byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        cipher.init(Cipher.DECRYPT_MODE, key, initVector);
        byte[] decodedData = base64Decode(data);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return decryptedData;
    }

    public static byte[] base64Encode(byte[] data) {
        return Base64.getEncoder().encode(data);
    }

    public static byte[] base64Decode(byte[] data) {
        return Base64.getDecoder().decode(data);
    }
}