package com.example.chat_application.CommonUtility;

import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.example.chat_application.Model.EncryptionConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static android.R.attr.data;

/**
 * Created by BANDINI on 29-04-2017.
 */

public class EncryptionUtility {

    //Variable Declaration
    private static Cipher msgEncryptCipher;
    private static int intIVBlockSize;
    private static Cipher keyEncryptCipher;

    //Constructor
    public EncryptionUtility() throws NoSuchAlgorithmException, NoSuchPaddingException {
        if (msgEncryptCipher == null) {
            msgEncryptCipher = Cipher.getInstance(EncryptionConfiguration.ENCRYPTION_ALGORITHM + "/" +
                    EncryptionConfiguration.ENCRYPTION_MODE + "/" +
                    EncryptionConfiguration.ENCRYPTION_PADDING);
            intIVBlockSize = msgEncryptCipher.getBlockSize();
        }

        if (keyEncryptCipher == null) {
            keyEncryptCipher = Cipher.getInstance(EncryptionConfiguration.PUBLIC_KEY_GENERATION_ALGORITHM + "/" +
                    EncryptionConfiguration.MODE_OF_OPERATION + "/" +
                    EncryptionConfiguration.PADDING);
        }
    }


    //Generates private and public key which will be shared with recipient
    public static List<String> fnGeneratePublicPrivateKey() {
        List<String> lstKeys = new ArrayList<String>();
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(EncryptionConfiguration.PUBLIC_KEY_GENERATION_ALGORITHM); //RSA
            kpg.initialize(EncryptionConfiguration.RSA_KEY_SIZE);//2048
            KeyPair keyPair = kpg.genKeyPair();
            byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            lstKeys.add(0,Base64.encodeToString(publicKeyBytes, Base64.DEFAULT));
            lstKeys.add(1,Base64.encodeToString(privateKeyBytes, Base64.DEFAULT));
        } catch (NoSuchAlgorithmException exAlgo) {
            exAlgo.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lstKeys;
    }

    private static SecretKey fnGenerateKey() throws NoSuchAlgorithmException {
        KeyGenerator kpg = KeyGenerator.getInstance(EncryptionConfiguration.ENCRYPTION_ALGORITHM);//AES
        kpg.init(EncryptionConfiguration.AES_KEY_SIZE, new SecureRandom());
        return kpg.generateKey();
    }

    private static IvParameterSpec fnGenerateIV(int intBlockSize) {
        SecureRandom randomSecureRandom = new SecureRandom();
        byte[] iv = new byte[intBlockSize];
        randomSecureRandom.nextBytes(iv);

        return new IvParameterSpec(iv);
    }

    private static int fnGetAESKeySizeInBytes() {
        return EncryptionConfiguration.AES_KEY_SIZE / Byte.SIZE;
    }

    private static int fnGetRSAKeySizeInBytes() {
        return EncryptionConfiguration.RSA_KEY_SIZE / Byte.SIZE;
    }

    public static String fnEncryptMessage(String strPlainMessage, String strRecipientPublicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        String strEncryptedMessage = null;

        //------ Generate Encryption and Integrity Key --------------
        SecretKey encryption_key, integrity_key;
        encryption_key = fnGenerateKey();
        integrity_key = fnGenerateKey();
        //--------------------- End --------------------------------

        //-------------------------- Encrypt Plain Message------------------------
        IvParameterSpec iv = fnGenerateIV(intIVBlockSize);
        msgEncryptCipher.init(Cipher.ENCRYPT_MODE, encryption_key, iv);
        byte[] byteCipherText = msgEncryptCipher.doFinal(strPlainMessage.getBytes()); //-------------> c : cipher text
        //----------------------- END -------------------------

        //------------------- Hash Cipher text obtained from AES with integrity_key--------------
        byte[] byteTag = HashFunctions.fnGetHmacSHA256(byteCipherText, integrity_key); //------------> t : tag
        //------------------------------ END ----------------------------------------

        //------------------- Concatenate Encryption and Integrity Key ------------------
        byte[] keys = new byte[fnGetAESKeySizeInBytes() + fnGetAESKeySizeInBytes()];
        System.arraycopy(encryption_key.getEncoded(), 0, keys, 0, fnGetAESKeySizeInBytes());
        System.arraycopy(integrity_key.getEncoded(), 0, keys, fnGetAESKeySizeInBytes(), fnGetAESKeySizeInBytes());
        //------------------------------------ END --------------------------------------

        //-------------Using Receiver's public key we will encrypt the encryption and integrity keys using RSA -----
        byte[] decodeKey = Base64.decode(strRecipientPublicKey, Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodeKey);
        KeyFactory keyFactory = KeyFactory.getInstance(EncryptionConfiguration.PUBLIC_KEY_GENERATION_ALGORITHM);
        PublicKey receiverPublicKey = keyFactory.generatePublic(keySpec);

        keyEncryptCipher.init(Cipher.ENCRYPT_MODE, receiverPublicKey);
        byte[] encryptedKeys = keyEncryptCipher.doFinal(keys);
        //--------------------------------------------------- END --------------------------------------------------

        //------------------ Create the message to be send -----------------------------------
        //We should send IV, Encrypted Message, Tag and Encrypted Keys to receiver
        byte[] message = new byte[intIVBlockSize + byteCipherText.length + byteTag.length + encryptedKeys.length];
        //First copy the Generated IV to message array
        System.arraycopy(iv.getIV(), 0, message, 0, intIVBlockSize);
        //Then Append cipher text to message array
        System.arraycopy(byteCipherText, 0, message, intIVBlockSize, byteCipherText.length);
        //Append the tag
        System.arraycopy(byteTag, 0, message, intIVBlockSize + byteCipherText.length, byteTag.length);
        //At last append Encrypted Keys
        System.arraycopy(encryptedKeys, 0, message, intIVBlockSize + byteCipherText.length + byteTag.length, encryptedKeys.length);
        //------------------------------------ END -------------------------------------------

        //--------Encode the encrypted message to String -------------------------------------
        strEncryptedMessage = Base64.encodeToString(message, Base64.DEFAULT);
        //-------------------------------- END ----------------------------------------------

        return strEncryptedMessage;
    }

    public static String fnDecryptMessage(String strEncryptedMessage, String strPrivateKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, BadPaddingException,
            IllegalBlockSizeException, InvalidAlgorithmParameterException {
        String strDecryptedMessage = null;
        //Decode the received message from Base64 to byte array
        byte[] byteEncryptedMsg = Base64.decode(strEncryptedMessage, Base64.DEFAULT);

        //Extract the Concatenation of Encryption and Integrity key which is encrypted with RSA
        byte[] keys = new byte[fnGetRSAKeySizeInBytes()];
        System.arraycopy(byteEncryptedMsg, byteEncryptedMsg.length - keys.length, keys, 0, keys.length);
        //------------------------------------------- END -----------------------------------

        //Decrypt the keys with RSA to get Encryption and Integrity Key ------------------
        byte[] decodePrivateKey = Base64.decode(strPrivateKey, Base64.DEFAULT);
        PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(decodePrivateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(EncryptionConfiguration.PUBLIC_KEY_GENERATION_ALGORITHM);
        PrivateKey PrivateKey = keyFactory.generatePrivate(pkcs8);

        keyEncryptCipher.init(Cipher.DECRYPT_MODE, PrivateKey);
        byte[] decryptedKeys = keyEncryptCipher.doFinal(keys); //This contains Encrypted and Integrity Keys
        //------------------------------------------ END ---------------------------------

        //--------------- Separate the Encryption and Integrity Keys ------------------------------
        byte[] encodedEncryptionKey = new byte[fnGetAESKeySizeInBytes()];
        byte[] encodedIntegrityKey = new byte[fnGetAESKeySizeInBytes()];
        System.arraycopy(decryptedKeys, 0, encodedEncryptionKey, 0, encodedEncryptionKey.length);
        System.arraycopy(decryptedKeys, encodedEncryptionKey.length, encodedIntegrityKey, 0, encodedIntegrityKey.length);
        SecretKey encryptionKey = new SecretKeySpec(encodedEncryptionKey, EncryptionConfiguration.ENCRYPTION_ALGORITHM);
        SecretKey integrityKey = new SecretKeySpec(encodedIntegrityKey, EncryptionConfiguration.ENCRYPTION_ALGORITHM);
        //------------------------------------------ END ------------------------------------------

        //----------- Extract IV, Encrypted Message and Tag from received encrypted message ------------------
        byte[] encryptedMessage = new byte[byteEncryptedMsg.length - keys.length];
        System.arraycopy(byteEncryptedMsg, 0, encryptedMessage, 0, encryptedMessage.length);
        //----------------------------------------- END -------------------------------------------------------

        // --------------------------Extract the IV---------------------------------
        byte[] iv = new byte[intIVBlockSize];
        System.arraycopy(encryptedMessage, 0, iv, 0, iv.length);
        //------------------------------ END ---------------------------------------

        //----------------------- Extract Tag ----------------------------------------
        byte[] byteTag = new byte[fnGetAESKeySizeInBytes()]; // HMAC tag is the same size of AES key = 256 bits
        System.arraycopy(encryptedMessage, encryptedMessage.length - byteTag.length, byteTag, 0, byteTag.length);
        //---------------------------- END -------------------------------------------

        //---------------------- Extract the message ---------------------------------------
        int intMessageLength = encryptedMessage.length - intIVBlockSize - byteTag.length;
        byte[] byteDecodedMessage = new byte[intMessageLength];
        System.arraycopy(encryptedMessage, intIVBlockSize, byteDecodedMessage, 0, intMessageLength);
        //------------------------------ END ------------------------------------------------

        //------------------- Hash Cipher text with integrity_key to check integrity--------------
        byte[] byteMyTag = HashFunctions.fnGetHmacSHA256(byteDecodedMessage, integrityKey); //------------> t : tag
        //------------------------------ END ----------------------------------------

        //---------Verify the tag : if tag matches then no tampering occurred-----------------------
        if (fnVerifyHMAC(byteTag, byteMyTag)) {
            //Tag matches : Therefore decrypt the message
            msgEncryptCipher.init(Cipher.DECRYPT_MODE, encryptionKey, new IvParameterSpec(iv));
            byte[] bytePlainText = msgEncryptCipher.doFinal(byteDecodedMessage);
            strDecryptedMessage = new String(bytePlainText);//Base64.encodeToString(bytePlainText, Base64.DEFAULT);

        } else {
            return "Message is tampered";
        }
        //---------------------------------- END ---------------------------------------------------

        return strDecryptedMessage;
    }

    private static boolean fnVerifyHMAC(byte[] byteTag, byte[] byteMyTag) {
        return Arrays.equals(byteTag, byteMyTag);
    }

    //----------------------- Only for testing -----------------------------
    public static void Writefile()
    {
        String path = Environment.getExternalStorageDirectory() + File.separator  + "AppTest";
        // Create the folder.
        File folder = new File(path);
        folder.mkdirs();

        // Create the file.
        File file = new File(folder, "config.txt");

        try
        {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append("Test");

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}