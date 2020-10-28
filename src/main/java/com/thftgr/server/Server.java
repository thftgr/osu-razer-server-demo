package com.thftgr.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Server {

    public void loadVertx(int port) {
        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);


        vertx.createHttpServer().requestHandler(router).listen(port, http -> System.out.println("server started"));


        //로그인 인증 API
        router.route(HttpMethod.POST, "/oauth/token").handler(BodyHandler.create()).handler(req -> {

//            false 만 내려옴 인증 api 확인필요
            String username = req.request().getFormAttribute("username");
            String password = req.request().getFormAttribute("password");

            String auth = new Auth().login(username, password);
            if (auth == null) {
                System.out.println("login FAIL user : " + username);
                req.response().setStatusCode(400).end();
                return;
            }


            System.out.println("login PASS user : " + username);
            req.response().setStatusCode(200).end(auth);


            //임시 강제셋
//            req.response().setStatusCode(200).end(new Auth().buildAuth(username));
//            req.response().setStatusCode(200).end(new Auth().buildAuth("8226"));


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


        //http://localhost:8080/api/v2/users/8226/
        router.route(HttpMethod.GET, "/api/v2/users/:id/").handler(req -> {
            System.out.println(req.pathParam("id"));
            userProfileBuilder userProfileBuilder = new userProfileBuilder();
            try {
                req.response().setStatusCode(200).putHeader("Accept", "application/json").end(userProfileBuilder.getProfile(req.pathParam("id")).toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        //이거는 헤더에 올라오는 Authorization 토큰으로 분리해야함
        //토큰 => db 조회
        router.route(HttpMethod.GET, "/api/v2/me").handler(req -> {

            String userid = req.request().getHeader("Authorization");
            userid = userid.substring(userid.indexOf(" ") + 1, userid.indexOf(":"));
            System.out.println(userid);

            userProfileBuilder userProfileBuilder = new userProfileBuilder();
            try {
                req.response().setStatusCode(200).putHeader("Accept", "application/json").end(userProfileBuilder.getProfile(userid).toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        ///api/v2/users/8146232/


    }


}
