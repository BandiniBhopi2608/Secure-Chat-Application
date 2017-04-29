package com.example.chat_application.CommonUtility;

import android.support.annotation.NonNull;
import android.util.Base64;

import org.mindrot.jbcrypt.BCrypt;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

/**
 * Created by BANDINI on 23-04-2017.
 */

public class HashFunctions {
    public static String getHashValue(String strData, String strSalt) {
        String strHashedData = null;
        try {
            strHashedData = BCrypt.hashpw(strData, strSalt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return strHashedData;
    }

    //Reference : http://stackoverflow.com/questions/33085493/hash-a-password-with-sha-512-in-java
    public static String getSHA512SecurePWD(String strPassword, String strChallenge) {
        String strHashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(strChallenge.getBytes("UTF-8"));
            byte[] bytes = md.digest(strPassword.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            strHashedPassword = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return strHashedPassword;
    }

    //Generates public key which will be shared with recipient
    public static String fnGeneratePublicKey() {
        String strPublicKey = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair keyPair = kpg.genKeyPair();
            //byte[] privateKey = keyPair.getPrivate().getEncoded();
            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            strPublicKey = Base64.encodeToString(publicKeyBytes, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException exAlgo) {
            exAlgo.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return strPublicKey;
    }

    public static String fnEncryptMessage(String strMessage, String strRecipientPublicKey) {
        String strEncryptedMessage = null;
        String strCipher1, strCipher2;
        try {
            Key encryption_key, integrity_key;
            //------------Generate Encryption & Integrity Keys------------
            KeyGenerator kpg = KeyGenerator.getInstance("AES");
            kpg.init(256, new SecureRandom());
            encryption_key = kpg.generateKey();
            integrity_key = kpg.generateKey();
            //--------------------- End ----------------------

            //-------------- Encrypt Plain Message with AES--------
            // Encrypt cipher
            Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, encryption_key);
            byte[] byteCipherText = encryptCipher.doFinal(strMessage.getBytes());
            strCipher1 = Base64.encodeToString(byteCipherText, Base64.DEFAULT);
            //----------------------- END -------------------------

            //------------------- Hash Cipher text obtained from AES with Key2--------------
            strCipher2 = getSHA512SecurePWD(strCipher1, integrity_key.toString());
            //------------------------------ END ----------------------------------------

            //


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return strEncryptedMessage;
    }
}
