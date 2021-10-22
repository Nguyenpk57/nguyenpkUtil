package com.util.func;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public class MD5 {

    private static final Object LOCK = new Object();
    private static MD5 instance;
    private String algorithm;
    private String hash;

    private MD5() {
        initialization();
    }

    public static MD5 getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new MD5();
            }
            return instance;
        }
    }

    private void initialization() {
        if (algorithm == null) {
            algorithm = "MD5";
        }
        if (hash == null) {
            hash = "0";
        }
    }

    public String getMd5(Object value) throws NoSuchAlgorithmException {
        return getMd5(String.valueOf(value));
    }

    public String getMd5(String value) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] digest = md.digest(value.getBytes());
        BigInteger number = new BigInteger(1, digest);
        String hashText = number.toString(16);
        while (hashText.length() < 32) {
            hashText = hash + hashText;
        }
        return hashText;
    }
}
