package com.example.chat_application.CommonUtility;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.example.chat_application.Model.EncryptionConfiguration;

import org.mindrot.jbcrypt.BCrypt;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by BANDINI on 23-04-2017.
 */

//This class performs the hash operations.
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
            MessageDigest md = MessageDigest.getInstance(EncryptionConfiguration.HMAC_ALGORITHM_LOGIN); //SHA-512
            md.update(strChallenge.getBytes(EncryptionConfiguration.CHARACTER_ENCODING));//UTF-8
            byte[] bytes = md.digest(strPassword.getBytes(EncryptionConfiguration.CHARACTER_ENCODING));//UTF-8
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

    public static byte[] fnGetHmacSHA256(byte[] byteCipherText, SecretKey hashKey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] byteHashKey = hashKey.getEncoded();
        Mac sha256_HMAC = Mac.getInstance(EncryptionConfiguration.HASH_ALGORITHM);
        SecretKeySpec sk = new SecretKeySpec(byteHashKey, EncryptionConfiguration.HASH_ALGORITHM);
        sha256_HMAC.init(sk);
        return sha256_HMAC.doFinal(byteCipherText);
    }
}
