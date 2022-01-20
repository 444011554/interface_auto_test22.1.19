package com.test.authorization;

//import com.lemon.encryption.RSAManager;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * @Project: class_26_base
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: odin
 * @Create: 2021-09-08 14:45
 * @Desc：
 **/

public class V3author {
    //全局变量
    int memberId;
    String token;

    @Test
    //登录
    public void login() {
        String json = "{\"mobile_phone\":\"15029942351\",\"pwd\":\"q12345678\"}";
        Response res =
                given().
                        log().all().
                        body(json).
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v3").

                when().
                        post("http://api.lemonban.com/futureloan/member/login").
                then().
                        log().all().
                        extract().response();
        //1.先来获取id
        memberId = res.jsonPath().get("data.id");
        System.out.println(memberId);
        //2.获取token
        token = res.jsonPath().get("data.token_info.token");
        System.out.println(token);

    }

    @Test
    //充值
    public void recharge() throws Exception {
        //timestamp参数
        long timestamp = System.currentTimeMillis()/1000;     //原本是毫秒除以1000变秒
        //sign参数
        //1、取token前50位
        String preStr = token.substring(0,50);     //含头不含尾
        //2、取到的结果拼接上timestamp
        String str = preStr+timestamp;
        //3、通过RSA加密算法对拼接的结果进行加密，得到sign签名
//        String sign = RSAManager.encryptWithBase64(str);

//        String jsonData = "{\"member_id\":"+memberId+",\"amount\":10000.01,\"timestamp\":\""+timestamp+"\",\"sign\":\""+sign+"\"}";
        Response res2 =
                given().
//                        body(jsonData).
                        header("Content-Type", "application/json").
                        header("X-Lemonban-Media-Type", "lemonban.v3").
                        header("Authorization","Bearer "+token).

                when().
                        post("http://api.lemonban.com/futureloan/member/recharge").
                then().
                        log().all().extract().response();
        System.out.println("当前可用余额："+res2.jsonPath().get("data.leave_amount")+"");

    }
}
