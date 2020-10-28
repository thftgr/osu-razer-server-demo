package com.thftgr;

import com.thftgr.server.BCrypt;
import com.thftgr.server.Encrypt;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class test {
    public static void main(String[] args) {


        System.out.println(BCrypt.checkpw(new Encrypt().MD5("passwd"),"hash"));



    }
}
