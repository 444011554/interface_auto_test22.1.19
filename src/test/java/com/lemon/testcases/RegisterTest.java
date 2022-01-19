package com.lemon.testcases;

import com.lemon.common.BaseTest;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * @Project: class_26_base
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: odin
 * @Create: 2021-07-23 15:36
 * @Desc：
 **/

public class RegisterTest extends BaseTest {
    @BeforeClass
    public void setup(){
        //随机生成没有注册过的手机号码
        String phone1 = PhoneRandomUtil.getUnregisterPhone();
        String phone2 = PhoneRandomUtil.getUnregisterPhone();
        String phone3 = PhoneRandomUtil.getUnregisterPhone();
        //保存到环境变量中
        Environment.envData.put("phone1",phone1);
        Environment.envData.put("phone2",phone2);
        Environment.envData.put("phone3",phone3);

    }

    @Test(dataProvider = "getRegisterDatas")
    public void testRegister(ExcelPojo excelPojo) throws FileNotFoundException {
        //替换用例数据
        excelPojo = casesReplace(excelPojo);
        //发起注册请求
        Response res = request(excelPojo,"注册模块");
        //响应断言
        assertResponse(excelPojo,res);
        //数据库断言
        assertSQL(excelPojo);

    }


    @DataProvider
    public Object [] getRegisterDatas(){
        List<ExcelPojo> listDatas = readSpecifyExcelData(1,0);
        //把集合转换为一个一维数组
        return listDatas.toArray();
    }

}
