/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.func.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public class SHA1 {

    private static final Object LOCK = new Object();
    private static SHA1 instance;
    private String algorithm;

    private SHA1() {
        initialization();
    }

    public static SHA1 getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new SHA1();
            }
            return instance;
        }
    }

    private void initialization() {
        if (algorithm == null) {
            algorithm = "SHA1";
        }
    }

    public String sha1(Object value) throws NoSuchAlgorithmException {
        return sha1(String.valueOf(value));
    }

    public String sha1(String value) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] digest = mDigest.digest(value.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
