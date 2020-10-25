package com.thftgr.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Server {

    public void loadVertx(int port) {
        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);

        vertx.createHttpServer().requestHandler(router).listen(port, http -> System.out.println("server started"));


        //로그인 인증 API
        router.route(HttpMethod.POST, "/oauth/token").handler(BodyHandler.create()).handler(req -> {
            String username = req.request().getFormAttribute("username");
            String passWdMD5 = new Encrypt().MD5(req.request().getFormAttribute("password"));




            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("https://api.debian.moe/auth/v2?id="+username+"&pwMD5="+passWdMD5)
                    .method("GET", null)
                    .build();
            String response = "";

            try {
                response= client.newCall(request).execute().body().string().toLowerCase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(response.contains("true")){
                System.out.println(response);
                JsonObject jo = new JsonObject();
                jo.addProperty("token_type", "Bearer");
                jo.addProperty("expires_in", "86400");
                jo.addProperty("access_token", "accToken");
                jo.addProperty("refresh_token", "refToken");

                System.out.println(jo.toString());

                req.response().setStatusCode(200).end(jo.toString());
            }else{
                System.out.println(response);
                req.response().setStatusCode(400).end();
            }
        });

        //http://localhost/api/v2/me/
        //https://osu.ppy.sh/api/v2/me/
        //임시사용 제거예정임
        router.route(HttpMethod.GET, "/api/v2/users/8146232").handler(req -> {

            JsonObject tmp = new JsonObject();
            try {
                tmp = (JsonObject) JsonParser.parseReader(new Gson().newJsonReader(new FileReader("api.v2.me.json")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            req.response().setStatusCode(200).putHeader("Accept", "application/json").end(tmp.toString());
        });

        router.route(HttpMethod.GET, "/api/:id").handler(req -> {
            System.out.println(req.pathParam("id"));
        });

        //이거는 헤더에 올라오는 Authorization 토큰으로 분리해야함
        //토큰 => db 조회
        router.route(HttpMethod.GET, "/api/v2/me").handler(req -> {

            JsonObject tmp = new JsonObject();
            try {
                tmp = (JsonObject) JsonParser.parseReader(new Gson().newJsonReader(new FileReader("api.v2.me.json")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            req.response().setStatusCode(200).putHeader("Accept", "application/json").end(tmp.toString());
        });
        ///api/v2/users/8146232/


    }



}
