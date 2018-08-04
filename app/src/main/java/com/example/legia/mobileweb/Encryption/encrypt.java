package com.example.legia.mobileweb.Encryption;

import android.os.Build;
import android.support.annotation.RequiresApi;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class encrypt {

    // SHA 256
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String hashWith256(String input) {
        String encode = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            encode = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return encode;
    }

    // RSA Encryption
    public static byte[] encrypt(PrivateKey privateKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(message.getBytes());
    }

    // RSA Decryption
    public static byte[] decrypt(PublicKey publicKey, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(encrypted);
    }
}
