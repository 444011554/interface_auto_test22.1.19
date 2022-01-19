package com.test.day01;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * @Project: class_26_base
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: odin
 * @Create: 2021-06-23 16:42
 * @Desc：
 **/
//作业2、通过RestAssured完成注册、登录、充值、新增项目、审核项目、投资

public class homework01 {
    //全局变量
    String mobilephone = "15029942348";
    String pwd = "q12345678";
    int type = 0;
    int memberId;
    String token;

    @Test(priority = 1)
    //注册
    public void register() {
        String json = "{\"mobile_phone\":\""+mobilephone+"\",\"pwd\":\""+pwd+"\",\"type\":"+type+",\"reg_name\":\"ludong\"}";
        Response res =
                given().
                        body(json).
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").

                when().
                        post("http://api.lemonban.com/futureloan/member/register").
                then().
                        log().all().extract().response();
    }

    @Test(priority = 2)
    //登录
    public void login() {
        String json = "{\"mobile_phone\":\"15029942348\",\"pwd\":\"q12345678\"}";
        Response res =
                given().
                        body(json).
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").

                when().
                        post("http://api.lemonban.com/futureloan/member/login").
                then().
                        extract().response();
        //1.先来获取id
        memberId = res.jsonPath().get("data.id");
        System.out.println(memberId);
        //2.获取token
        token = res.jsonPath().get("data.token_info.token");
        System.out.println(token);

    }

    @Test(priority = 3)
    //充值
    public void recharge() {
        String jsonData = "{\"member_id\":"+memberId+",\"amount\":10000.01}";
        Response res2 =
                given().
                        body(jsonData).
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        header("Authorization","Bearer "+token).

                when().
                        post("http://api.lemonban.com/futureloan/member/recharge").
                then().
                        log().all().extract().response();
        System.out.println("当前可用余额："+res2.jsonPath().get("data.leave_amount")+"");

    }

    @Test(priority = 4)
    //新增项目
    public void loginAdd() {
        String jsonData = "{\"member_id\":"+memberId+",\"title\":\"有钱花借贷金融\",\"amount\":1000.00,\"loan_rate\":10.0,\"loan_term\":12,\"loan_date_type\":1,\"bidding_days\":5}";
        Response res2 =
                given().
                        body(jsonData).
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        header("Authorization","Bearer "+token).

                when().
                        post("http://api.lemonban.com/futureloan/loan/add").
                then().
                        log().all().extract().response();
        System.out.println("新增项目名称："+res2.jsonPath().get("data.title")+"");

    }


}
