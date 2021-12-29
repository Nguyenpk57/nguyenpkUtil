package com.util.func.encryption;

import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import org.apache.commons.codec.binary.Base64;
import java.util.HashMap;
import java.util.UUID;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.StringUtils;


/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public class Cipher {

    private static final Object LOCK = new Object();
    private static Cipher instance;
    private static HashMap<String, Cipher> instances;
    private HashMap<String, Key> keys;
    private String algorithm;
    private Charset charset;
    private Key defKey;
    protected ILogger logger;
    private javax.crypto.Cipher cipher;

    private Cipher() {
        this.logger = LoggerImpl.getInstance(this.getClass());
        initialization();
    }

    private Cipher(String algorithm) {
        this.logger = LoggerImpl.getInstance(this.getClass());
        this.algorithm = algorithm;
        initialization();
    }

    public static Cipher getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new Cipher();
            }
            return instance;
        }
    }

    public static Cipher getInstance(String algorithm) {
        synchronized (LOCK) {
            if (instances == null) {
                instances = new HashMap<String, Cipher>();
            }
            if (!instances.containsKey(algorithm)) {
                instances.put(algorithm, new Cipher(algorithm));
            }
            return instances.get(algorithm);
        }
    }

    private void initialization() {
        try {
            if (algorithm == null) {
                algorithm = "AES";
            }
            charset = Charset.forName("UTF-8");
            keys = new HashMap<String, Key>();
            cipher = javax.crypto.Cipher.getInstance(algorithm);
            defKey = new SecretKeySpec(new byte[]{-95, -29, -62, 25, 25, -83, -18, -85}, algorithm);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private Key getKey(String key) {
        if (key == null) {
            return defKey;
        }
        key = key.trim();
        if (key.isEmpty()) {
            return defKey;
        }
        if (!keys.containsKey(key)) {
            keys.put(key, new SecretKeySpec(key.getBytes(charset), algorithm));
        }
        return keys.get(key);
    }

    public String encrypt64(String value) {
        return encrypt64(StringUtils.defaultString(value).getBytes());
    }

    public String encrypt64(byte[] values) {
        return new String(Base64.encodeBase64(values));
    }

    public String decrypt64(String value) {
        return new String(decrypt64Bytes(value));
    }

    public byte[] decrypt64Bytes(String value) {
        return Base64.decodeBase64(StringUtils.defaultString(value).getBytes());
    }

    /**
     * Ma hoa value voi key mac dinh
     *
     * @param value Chuoi can ma hoa
     * @return Chuoi da duoc ma hoa
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String encrypt(String value) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return new String(Base64.encodeBase64(encrypt(StringUtils.defaultString(value).getBytes(charset), defKey)));
    }

    /**
     * Ma hoa value voi key bat ky
     *
     * @param value Chuoi can ma hoa
     * @param key Chuoi key bat ky
     * @return Chuoi da duoc ma hoa
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String encrypt(String value, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return new String(Base64.encodeBase64(encrypt(StringUtils.defaultString(value).getBytes(charset), getKey(key))));
    }

    public String encrypt(String value, Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return new String(Base64.encodeBase64(encrypt(StringUtils.defaultString(value).getBytes(charset), key)));
    }

    private byte[] encrypt(byte[] value, Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(value);
    }

    /**
     * Giai ma value voi key mac dinh
     *
     * @param value Chuoi can giai ma
     * @return Chuoi da duoc giai ma
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String decrypt(String value) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return new String(decrypt(Base64.decodeBase64(StringUtils.defaultString(value).getBytes()), defKey), charset);
    }

    /**
     * Giai ma value voi key bat ky
     *
     * @param value Chuoi can giai ma
     * @param key Chuoi key bat ky
     * @return Chuoi da duoc giai ma
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String decrypt(String value, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return new String(decrypt(Base64.decodeBase64(StringUtils.defaultString(value).getBytes()), getKey(key)), charset);
    }

    private byte[] decrypt(byte[] value, Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(value);
    }

    //<editor-fold defaultstate="collapsed" desc="randomKey">
    public String randomAESKey() {
        return new String(new SecretKeySpec(Arrays.copyOf((UUID.randomUUID().toString().replaceAll("-", "")).getBytes(), 16), "AES").getEncoded(), charset);
    }
    //</editor-fold>
}
