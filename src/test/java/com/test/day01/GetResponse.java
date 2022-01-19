package com.test.day01;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * @Project: class_26_base
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: odin
 * @Create: 2021-06-21 15:05
 * @Desc：
 **/

public class GetResponse {
    @Test
    public void getResponseHeader(){
        Response response =
        given().

        when().
                post("http://www.httpbin.org/post").
        then().
                log().all().extract().response();
        System.out.println("接口响应时间："+response.time());
    }

    @Test
    public void getResponseJson(){
        String json = "{\"mobile_phone\":\"15029889121\",\"pwd\":\"q12345678\"}";
        Response res =
                given().
                        body(json).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v1").

                when().
                        post("http://api.lemonban.com/futureloan/member/login").
                then().
                        log().all().extract().response();
        System.out.println(res.jsonPath().get("data.id")+"");

    }

    @Test
    public void getResponseJson02(){
        //提取json格式响应体内容 ：res.jsonPath().get("xxx.xxx.xxx");
        Response res =
                given().

                when().
                        get("http://www.httpbin.org/json").
                then().
                        log().all().extract().response();
        System.out.println(res.jsonPath().get("slideshow.slides.title"));
        System.out.println(res.jsonPath().get("slideshow.slides.title[1]"));

    }

    @Test
    public void getResponseXml(){
        //提取xml格式响应体内容 ：res.xmlPath().get("xxx.xxx.xxx");
        Response res =
                given().

                        when().
                        get("http://www.httpbin.org/xml").
                        then().
                        log().all().extract().response();
        System.out.println(res.xmlPath().get("slideshow.slide[1].title"));
        System.out.println(res.xmlPath().get("slideshow.slide[1].@type"));
    }

    @Test
    public void getResponseHtml(){
        //提取html格式响应体内容 ：res.htmlPath().get("xxx.xxx.xxx");
        Response res =
                given().
                when().
                        get("http://www.baidu.com").
                then().
                        log().all().extract().response();
        System.out.println(res.htmlPath().get("html.head.meta[0].@http-equiv"));
        System.out.println(res.htmlPath().get("html.head.meta[0].@content"));
        System.out.println(res.htmlPath().getList("html.head.meta"));
    }


    @Test
    //接口之间的参数依赖的操作方法
    public void LoginRecharge(){
        String json = "{\"mobile_phone\":\"15029889121\",\"pwd\":\"q12345678\"}";
        Response res =
                given().
                        body(json).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                when().
                        post("http://api.lemonban.com/futureloan/member/login").
                then().
                        log().all().extract().response();
        //1.先获取id
        int memberId = res.jsonPath().get("data.id");
        System.out.println(memberId);
        //2.获取token
        String token = res.jsonPath().get("data.token_info.token");
        System.out.println(token);

        //发起充值接口请求
        String jsonData = "{\"member_id\":"+memberId+",\"amount\":10000.00}";
        Response res2 =
                given().
                        body(jsonData).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                        header("Authorization","Bearer "+token).
                when().
                        post("http://api.lemonban.com/futureloan/member/recharge").
                then().
                        log().all().extract().response();
                System.out.println("当前可用余额："+res2.jsonPath().get("data.leave_amount"));

    }

}
