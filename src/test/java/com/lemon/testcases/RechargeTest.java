package com.lemon.testcases;

import com.lemon.common.BaseTest;
import com.lemon.data.Environment;
import com.lemon.encryption.RSAManager;
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
 * @Create: 2021-07-26 15:22
 * @Desc： 充值接口测试
 **/

public class RechargeTest extends BaseTest {
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
    public void testRecharge(ExcelPojo excelPojo) throws Exception {
        //timestamp参数
        long timestamp = System.currentTimeMillis()/1000;     //原本是毫秒除以1000变秒
        //sign参数
        //1、取token前50位
        String token = (String) Environment.envData.get("token");
        String preStr = token.substring(0,50);     //含头不含尾
        //2、取到的结果拼接上timestamp
        String str = preStr+timestamp;
        //3、通过RSA加密算法对拼接的结果进行加密，得到sign签名
        String sign = RSAManager.encryptWithBase64(str);
        //保存到环境变量中
        Environment.envData.put("timestamp",timestamp);
        Environment.envData.put("sign",sign);
        //用例执行之前替换{{member_id}} 为环境变量中保存的对应的值
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
