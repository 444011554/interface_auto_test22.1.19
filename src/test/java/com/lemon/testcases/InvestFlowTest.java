package com.lemon.testcases;

import com.lemon.common.BaseTest;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @Project: class_26_base
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: odin
 * @Create: 2021-08-10 11:04
 * @Desc：
 **/

public class InvestFlowTest extends BaseTest {
    @BeforeClass
    public void setup(){
        //生成三个角色的随机手机号码（投资人+借款人+管理员）
        String borrowserPhone = PhoneRandomUtil.getUnregisterPhone();
        String adminPhone = PhoneRandomUtil.getUnregisterPhone();
        String investPhone = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("borrower_phone",borrowserPhone);
        Environment.envData.put("admin_phone",adminPhone);
        Environment.envData.put("invest_phone",investPhone);
        //读取用例数据从第一条至第九条
        List<ExcelPojo> list = readSpecifyExcelData(4,0,9);
        for (int i=0; i<list.size(); i++){
            //发送请求
            ExcelPojo excelPojo = list.get(i);
            excelPojo = casesReplace(excelPojo);
            Response res = request(excelPojo,"业务流模块");
            //判断是否要提取响应数据
            if (excelPojo.getExtract() != null){
                extractToEnvironment(excelPojo,res);
            }
        }

    }

    @Test
    public void testInvest(){
        List<ExcelPojo> list = readSpecifyExcelData(4,9);
        ExcelPojo excelPojo = list.get(0);
        //替换
        excelPojo = casesReplace(excelPojo);
        //发送投资请求
        Response res = request(excelPojo,"业务流模块");
        //响应断言
        assertResponse(excelPojo,res);
        //数据库断言
        assertSQL(excelPojo);

    }

    @AfterTest
    public void teardown(){

    }
}
