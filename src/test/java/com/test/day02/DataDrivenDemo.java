package com.test.day02;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Project: class_26_base
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: odin
 * @Create: 2021-06-28 15:13
 * @Desc： DataDriven:数据驱动
 **/

public class DataDrivenDemo {

    @Test(dataProvider = "getLoginDatas02")
    public void login(ExcelPojo excelPojo) {
        //RestAssured全局配置
        //json小数返回类型是BigDecimal
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //BaseUrl全局配置
        RestAssured.baseURI = "http://api.lemonban.com/futureloan";
        //接口入参
        String inputParams = excelPojo.getInputParams();
        //接口地址
        String url = excelPojo.getUrl();
        //请求头
        String requestHeader = excelPojo.getRequestHeader();
        //把请求头转成map
        Map requestHeaderMap = (Map) JSON.parse(requestHeader);
        //期望的响应结果
        String expected = excelPojo.getExpected();
        //把响应结果转成map
        Map<String,Object> expectedMap = (Map) JSON.parse(expected);
        Response res =
                given().
                        body(inputParams).
                        headers(requestHeaderMap).
                when().
                        post(url).
                then().
                        log().all().extract().response();
        //断言
        //思路：1.循环变量响应map，取到里面每一个key（实际上就是我们设计的jsonPath表达式）
        //2.通过res.jsonPath.get(key)取到实际结果，再跟期望的结果做对比（key对应的value）
        for (String key : expectedMap.keySet()){
            //获取map里面的key
            System.out.println(key);
            //获取map里面的value
            //获取期望结果
            Object expectedValue = expectedMap.get(key);
            //获取接口返回的实际结果（jsonPath表达式）
            Object actualValue = res.jsonPath().get(key);
            //用实际值与期望值进行比较，来做断言
            Assert.assertEquals(actualValue,expectedValue);
        }
    }

    @DataProvider     //数据提供者
    public Object [][] getLoginDatas(){
        Object [][] datas = {{"13325986545","123456"},{"1332598654","123456"},{"13325986545","12345678"}};
        return datas;
    }
    //上述是一个简单的数据驱动，缺点：里面数据时写死的不便数据维护；

    @DataProvider
    public Object [] getLoginDatas02(){
        File file = new File("E:\\学习\\Java自动化\\接口自动化\\api_testcases_futureloan_v1.xls");
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(1);    //此处“1”为该Excel表的第2页
        //读取Excel
        List<ExcelPojo> listDatas = ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);
        //把集合转换为一个一维数组
        return listDatas.toArray();

    }

    //使用easypoi来读取Excel表数据
    public static void main(String[] args) {
        File file = new File("E:\\学习\\Java自动化\\接口自动化\\api_testcases_futureloan_v1.xls");
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(1);    //此处“1”为该Excel表的第2页
        //读取Excel
        List<Object> listDatas = ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);
        for (Object object: listDatas){
            System.out.println(object);
        }

    }
}







