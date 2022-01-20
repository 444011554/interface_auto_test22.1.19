package com.lemon.testcases;

import com.lemon.common.BaseTest;
import com.lemon.data.Environment;
//import com.lemon.encryption.RSAManager;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @Project: class_26_base
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: odin
 * @Create: 2022-01-20 11:55
 * @Desc： v2
 **/

public class RechargeTestV2 extends BaseTest {
    int memberId;
    String token;

    @BeforeClass
    public void setup(){
        //生成一个没有被注册过的手机号码
        String phone = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("phone",phone);
        //前置条件
        //读取Excel里面前两条数据
        List<ExcelPojo> listDatas = readSpecifyExcelData(3,0,2);
        ExcelPojo excelPojo = listDatas.get(0);
        //注册请求
        excelPojo = casesReplace(excelPojo);
        Response resRegister = request(excelPojo,"充值模块");
        //获取【提取返回数据(extract)】
        //提取接口返回对应的字段保存到环境变量中
        extractToEnvironment(excelPojo,resRegister);
        //参数替换，替换{{phone}}
        casesReplace(listDatas.get(1));
        //登录请求
        Response resLogin = request(listDatas.get(1),"充值模块");
        //得到【提取返回数据】这列
        extractToEnvironment(listDatas.get(1),resLogin);

    }

    @Test(dataProvider = "getRechargeDatas")
    public void testRecharge(ExcelPojo excelPojo) {
        excelPojo = casesReplace(excelPojo);
        Response res = request(excelPojo,"充值模块");
        //断言
        assertResponse(excelPojo,res);
        //数据库断言
        assertSQL(excelPojo);

    }

    @DataProvider
    public Object[] getRechargeDatas(){
        List<ExcelPojo> listDatas = readSpecifyExcelData(3,2);
        return listDatas.toArray();
    }



}
