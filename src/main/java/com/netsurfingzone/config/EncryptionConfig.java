package com.netsurfingzone.config;

import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class EncryptionConfig {

    public String encrypt(String plainText)
            throws Exception {
        Key aesKey = new SecretKeySpec(Constant.SYMMETRIC_SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        byte[] plainTextByte = plainText.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encryptedByte = cipher.doFinal(plainTextByte);
        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedText = encoder.encodeToString(encryptedByte);
        return encryptedText;
    }

    public String decrypt(String encryptedText)
            throws Exception {
        Key aesKey = new SecretKeySpec(Constant.SYMMETRIC_SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        return decryptedText;
    }
}

