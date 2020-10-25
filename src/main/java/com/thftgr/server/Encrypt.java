package com.thftgr.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt {
    public String MD5(String str) {
        String MD5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteData = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte byteDatum : byteData) {
                sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
            }
            MD5 = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            MD5 = null;
        }
        return MD5;
    }

}
