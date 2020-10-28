package com.thftgr.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thftgr.Main;

import java.io.IOException;

public class Auth {
    public String login(String username, String password) {
        Database db = new Database();
        db.connectDatabase(Main.settingValue.getAsJsonObject("database"));

        //유저 기본데이터
        JsonObject data = db.query(Main.settingValue.getAsJsonObject("sql").get("getUserByUsername").getAsString().replace("%username%",username)).get(0).getAsJsonObject();
        if(BCrypt.checkpw(new Encrypt().MD5(password),data.get("password_md5").getAsString()) && data.get("ban_datetime").getAsInt() == 0){
            return buildAuth(data.get("id").getAsString());
        }

        return null;



    }

    public String buildAuth(String auth) {
        JsonObject jo = new JsonObject();
        jo.addProperty("token_type", "Bearer");
        jo.addProperty("expires_in", "86400");
        jo.addProperty("access_token", auth + ":db9b1cd3262dee37756a09b9064973589847caa8e53d31a9d142ea2701b1b28abd97838bb9a27068ba305dc8d04a45a1fcf079de54d607666996b3cc54f6b67c");
        jo.addProperty("refresh_token", auth + ":a39cf4402a65d91b914551cee1218a668dd70d3d4e7fd8a06d96b25428d73ed1577e6426e4472786572fee38836b1322ca5456861288e555e3da8148725fd83c");
        return jo.toString();
    }


}
