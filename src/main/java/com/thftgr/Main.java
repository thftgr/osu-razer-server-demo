package com.thftgr;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thftgr.server.Database;
import com.thftgr.server.Server;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
    public static JsonObject settingValue;
    public static void main(String[] args) throws FileNotFoundException {
        settingValue = (JsonObject) JsonParser.parseReader(new Gson().newJsonReader(new FileReader("Setting.json")));


        new Server().loadVertx(8080);
//        Database db = new Database();
//        db.connectDatabase(Main.settingValue.getAsJsonObject("database"));

    }
}

