package com.example.chat_application.Model;

/**
 * Created by BANDINI on 29-04-2017.
 */

public class EncryptionConfiguration {
    public static final String HMAC_ALGORITHM_LOGIN = "SHA-512";
    public static final String CHARACTER_ENCODING = "UTF-8";
    public static final String PUBLIC_KEY_GENERATION_ALGORITHM = "RSA";
    public static final String MODE_OF_OPERATION = "NONE";
    public static final String PADDING = "OAEPWithSHA-256AndMGF1Padding";
    public static final int RSA_KEY_SIZE = 2048;
    public static final String ENCRYPTION_ALGORITHM = "AES";
    public static final int AES_KEY_SIZE = 256;
    public static final String ENCRYPTION_MODE = "CTR";
    public static final String ENCRYPTION_PADDING = "NoPadding";
    public static final String HASH_ALGORITHM = "HmacSHA256";
    public static final String QR_CODE_SEPERATOR = "###";
}
