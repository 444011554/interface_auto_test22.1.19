package com.test.day02;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Project: class_26_base
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: odin
 * @Create: 2021-06-24 17:37
 * @Desc：断言
 **/

public class AssertDemo {
    int memberId;
    String token;
    @Test(priority = 1)
    public void login() {
        //RestAssured全局配置
        //json小数返回类型是BigDecimal
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //BaseUrl全局配置
        RestAssured.baseURI = "http://api.lemonban.com/futureloan";
        String json = "{\"mobile_phone\":\"15029942348\",\"pwd\":\"q12345678\"}";
        Response res =
                given().
                        body(json).
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").

                when().
                        post("/member/login").
                then().
                        log().all().extract().response();
        //1、响应结果断言
        //整数类型
        int code = res.jsonPath().get("code");
        Assert.assertEquals(code,0);
        //字符串类型
        String msg = res.jsonPath().get("msg");
        Assert.assertEquals(msg,"OK");
        //小数类型
        //注意：restassured里面如果返回json小数，那么其类型是float
        //丢失精度问题解决方法：声明restassured返回json小数的其类型是BigDecimal
        BigDecimal actual = res.jsonPath().get("data.leave_amount");      //actual实际值；expected预期值；
        BigDecimal expected = BigDecimal.valueOf(112000.01);
        Assert.assertEquals(actual,expected);

        memberId =res.jsonPath().get("data.id");
        token = res.jsonPath().get("data.token_info.token");

    }
    //充值
    @Test(priority = 2)
    public void testRecharge(){
        String jsonData = "{\"member_id\":"+memberId+",\"amount\":1000.00}";
        Response res2 =
                given().
                        body(jsonData).
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v2").
                        header("Authorization","Bearer "+token).

                when().
                        post("/member/recharge").
                then().
                        log().all().extract().response();
        BigDecimal actual2 = res2.jsonPath().get("data.leave_amount");
        BigDecimal expected2 = BigDecimal.valueOf(113000.01);
        Assert.assertEquals(actual2,expected2);

    }


}
