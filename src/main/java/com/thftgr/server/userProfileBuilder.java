package com.thftgr.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thftgr.Main;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class userProfileBuilder {



    public JsonObject getProfile(String userid) throws FileNotFoundException {
        JsonObject user = (JsonObject) JsonParser.parseReader(new Gson().newJsonReader(new FileReader(Main.settingValue.getAsJsonObject("filePath").get("api/v2/me").getAsString())));
        Database db = new Database();
        db.connectDatabase(Main.settingValue.getAsJsonObject("database"));

        //유저 기본데이터
        JsonObject data = db.query(Main.settingValue.getAsJsonObject("sql").get("userdata").getAsString().replace("%id%",userid)).get(0).getAsJsonObject();
        user.addProperty("username",data.get("username").getAsString());
        user.addProperty("id",data.get("id").getAsInt());
        user.addProperty("last_visit",data.get("last_visit").getAsString());
        user.addProperty("join_date",data.get("join_date").getAsString());
        user.addProperty("is_active",data.get("is_active").getAsBoolean());

        //유저 메달
        user.add("user_achievements",
                db.query(Main.settingValue.getAsJsonObject("sql").get("user_achievements").getAsString().replace("%id%",userid))
                );

//      grade_counts s ss 등등 몃개나 있는지
        user.getAsJsonObject("statistics").add("grade_counts",
                db.query(Main.settingValue.getAsJsonObject("sql").get("grade_counts").getAsString().replace("%id%",userid)).get(0).getAsJsonObject()
        );

        //팔로워 카운트
        user.addProperty("follower_count",
                db.query(
                        Main.settingValue.getAsJsonObject("sql")
                                .get("follower_count")
                                .getAsString()
                                .replace("%id%",userid))
                        .get(0)
                        .getAsJsonObject()
                        .get("follower_count").getAsInt()
        );


        //레벨 플카 등등
        data = db.query(Main.settingValue.getAsJsonObject("sql").get("statistics").getAsString().replace("%id%",userid)).get(0).getAsJsonObject();
        System.out.println(data.toString());
        user.getAsJsonObject("statistics").addProperty("ranked_score",data.get("ranked_score_std").getAsString());
        user.getAsJsonObject("statistics").addProperty("play_count",data.get("playcount_std").getAsString());
        user.getAsJsonObject("statistics").addProperty("replays_watched_by_others",data.get("replays_watched_std").getAsString());
        user.getAsJsonObject("statistics").addProperty("total_hits",data.get("total_hits_std").getAsString());
        user.getAsJsonObject("statistics").addProperty("total_score",data.get("total_score_std").getAsString());
        user.getAsJsonObject("statistics").addProperty("play_time",data.get("playtime_std").getAsString());
        user.getAsJsonObject("statistics").addProperty("hit_accuracy",data.get("avg_accuracy_std").getAsString());
        user.getAsJsonObject("statistics").addProperty("pp",data.get("pp_std").getAsString());
        user.addProperty("is_online",data.get("is_online").getAsBoolean());
        user.addProperty("country_code",data.get("country").getAsString());
//        user.getAsJsonObject("page").addProperty("raw",data.get("userpage_content").getAsString());
        user.getAsJsonObject("statistics").getAsJsonObject("level").addProperty("current",data.get("level_std").getAsString());






        return user;
    }





}
