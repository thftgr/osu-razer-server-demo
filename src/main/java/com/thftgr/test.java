package com.thftgr;

import com.thftgr.server.Encrypt;

public class test {
    public static void main(String[] args) {

        System.out.println(new Encrypt().MD5("passwd"));
    }
}
