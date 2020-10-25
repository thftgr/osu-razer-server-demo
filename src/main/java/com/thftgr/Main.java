package com.thftgr;

import com.thftgr.server.Server;

public class Main {
    public static void main(String[] args) {
        new Server().loadVertx(8080);
    }
}

