package com.example.chat_application.CommonUtility;

import android.util.Base64;

import com.example.chat_application.Model.EncryptionConfiguration;
import com.example.chat_application.Model.Message;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BANDINI on 05-05-2017.
 */

public class UserToUserAuth {

    public static List<String> fnGenerateDSKeys() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        List<String> lstKeys = new ArrayList<String>();
        KeyPairGenerator pairgen = KeyPairGenerator.getInstance(EncryptionConfiguration.DIGITAL_SIGNATURE_ALGORITHM);
        SecureRandom random = new SecureRandom();
        pairgen.initialize(EncryptionConfiguration.DSA_KEY_SIZE, random);//512
        KeyPair keyPair = pairgen.generateKeyPair();
        byte[] byteDSPublicKey = keyPair.getPublic().getEncoded();
        byte[] byteDSPrivateKey = keyPair.getPrivate().getEncoded();
        lstKeys.add(0, Base64.encodeToString(byteDSPublicKey, Base64.DEFAULT));
        lstKeys.add(1, Base64.encodeToString(byteDSPrivateKey, Base64.DEFAULT));
        return lstKeys;
    }

    public static Message fnSign(String strPrivateKey, Message objMessage) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException {
        byte[] decodePrivateKey = Base64.decode(strPrivateKey, Base64.DEFAULT);
        PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(decodePrivateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(EncryptionConfiguration.DIGITAL_SIGNATURE_ALGORITHM);
        PrivateKey DSPrivateKey = keyFactory.generatePrivate(pkcs8);

        Signature signalg = Signature.getInstance(EncryptionConfiguration.DIGITAL_SIGNATURE_ALGORITHM);
        signalg.initSign(DSPrivateKey);

        byte[] byteMessage = Base64.decode(objMessage.getMessage(), Base64.DEFAULT);

        signalg.update(byteMessage);
        byte[] signature = signalg.sign();
        objMessage.setUserSignature(Base64.encodeToString(signature, Base64.DEFAULT));
        return objMessage;
    }

    public static boolean fnVerify(String strPublicKey, String strSignature, String strMessage) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        boolean IsVerified = false;
        byte[] decodeKey = Base64.decode(strPublicKey, Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodeKey);
        KeyFactory keyFactory = KeyFactory.getInstance(EncryptionConfiguration.DIGITAL_SIGNATURE_ALGORITHM);
        PublicKey DSPublicKey = keyFactory.generatePublic(keySpec);

        Signature verifyalg = Signature.getInstance(EncryptionConfiguration.DIGITAL_SIGNATURE_ALGORITHM);
        verifyalg.initVerify(DSPublicKey);

        byte[] byteMessage = Base64.decode(strMessage, Base64.DEFAULT);
        byte[] byteSignature = Base64.decode(strSignature, Base64.DEFAULT);

        verifyalg.update(byteMessage);
        if (verifyalg.verify(byteSignature))
            IsVerified = true;

        return IsVerified;
    }
}
