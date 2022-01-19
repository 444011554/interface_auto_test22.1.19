package com.test.day01;

import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

/**
 * @Project: class_26_base
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: odin
 * @Create: 2021-06-21 14:04
 * @Desc：
 **/

public class RestAssuredDemo {
    @Test
    public void fistGetRequest(){
        given().
                //设置请求头、请求体等...
        when().
                get("https://www.baidu.com").
        then().
                log().all();
    }

    @Test
    public void getDemo01(){
        given().
                queryParam("mobilephone","15029954122").
                queryParam("pwd","123456").
        when().
                get("http://www.httpbin.org/get").
        then().
                log().body();
    }

    @Test
    public void postDemo01(){
        //form表单参数类型
        given().
                contentType("application/x-www-form-urlencoded").     //contentType项可以不加，因为框架比较智能，但在postman里面必须加
                formParam("mobilephone","15029954122").
                formParam("pwd","123456").
        when().
                post("http://www.httpbin.org/post").
        then().
                log().body();
    }

    @Test
    public void postDemo02(){
        //json参数类型
        String jsonData="{\"mobilephone\":\"15055265694521\",\"pwd\":\"3132136\"}";
        given().
                contentType("application/json").
                body(jsonData).
        when().
                post("http://www.httpbin.org/post").
        then().
                log().body();
    }

    @Test
    public void postDemo03(){
        //xml参数类型
        String xmlData="<Appenders>\n" +
                "\n" +
                "    <File name=\"jmeter-log\" fileName=\"${sys:jmeter.logfile:-jmeter.log}\" append=\"false\">\n" +
                "      <PatternLayout>\n" +
                "        <pattern>%d %p %c{1.}: %m%n</pattern>\n" +
                "      </PatternLayout>\n" +
                "    </File>";
        given().
                contentType("application/xml").
                body(xmlData).
        when().
                post("http://www.httpbin.org/post").
        then().
                log().body();
    }

    @Test
    public void postDemo04(){
        //上传文件
        given().
                multiPart(new File("D:\\Users\\test.txt")).
        when().
                post("http://www.httpbin.org/post").
        then().
                log().body();
    }

}
