package com.lemon.testcases;

import com.lemon.common.BaseTest;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.annotations.AfterTest;
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
 * @Create: 2021-08-17 11:17
 * @Desc：
 **/

public class AddloanTest extends BaseTest {
    @BeforeClass
    public void setup(){
        //生成三个角色的随机手机号码（借款人+管理员）
        String borrowserPhone = PhoneRandomUtil.getUnregisterPhone();
        String adminPhone = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("borrower_phone",borrowserPhone);
        Environment.envData.put("admin_phone",adminPhone);
        //读取用例数据-前面4条
        List<ExcelPojo> list = readSpecifyExcelData(5,0,4);
        for (int i=0; i<list.size();i++){
            //发送请求
            ExcelPojo excelPojo = list.get(i);
            excelPojo = casesReplace(excelPojo);
            Response res = request(excelPojo,"加贷模块");
            //判断是否要提取响应数据
            if (excelPojo.getExtract() != null){
                extractToEnvironment(excelPojo,res);
            }
        }
    }

    @Test(dataProvider = "getAddLoanDatas")
    public void testAddLoan(ExcelPojo excelPojo){
        excelPojo = casesReplace(excelPojo);
        Response res = request(excelPojo,"加贷模块");
        //断言
        assertResponse(excelPojo,res);
    }

    @DataProvider
    public Object[] getAddLoanDatas(){
        List<ExcelPojo> listDatas = readSpecifyExcelData(5,4);
        //把集合转换为一个一维数组
        return listDatas.toArray();
    }

    @AfterTest
    public void teardown(){

    }
}
