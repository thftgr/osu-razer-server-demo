package com.thftgr.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;

public class Auth {
    public String login(String username, String password) {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.debian.moe/auth/v2?id=" + username + "&pwMD5=" + new Encrypt().MD5(password))
                .method("GET", null)
                .build();
        JsonObject body;

        try {
            String data = client.newCall(request).execute().body().string();
            body = (JsonObject) JsonParser.parseString(data.toLowerCase());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
        //{
        //    "status": {
        //        "code": "200",
        //        "message": "٩(๑`^´๑)۶"
        //    },
        //    "success": true
        //    "userid" : 1000
        //}
        if (!body.get("success").getAsBoolean()) return null;
        return buildAuth(body.get("userid").getAsString());
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
