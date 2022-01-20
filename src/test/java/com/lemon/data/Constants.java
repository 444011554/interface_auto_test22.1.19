package com.lemon.data;

/**
 * @Project: class_26_base
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: odin
 * @Create: 2021-07-27 16:23
 * @Desc：constants:常量
 **/

public class Constants {
    //日志输出配置：控制台显示（false）/报表和文件中显示（true）
    public static final boolean LOG_TO_FILE = true;
    //Excel文件的路径
    public static final String EXCEL_FILE_PATH="src/test/resources/api_testcases_futureloan_v2.xls";
    //接口BaseUrl地址
    public static final String BASE_URL="http://api.lemonban.com/futureloan";      //若是ip需写成：类似127.0.0.1:8080格式
    //数据库baseuri
    public static final String DB_BASE_URI = "api.lemonban.com";
    //数据库名
    public static final String DB_NAME = "futureloan";
    public static final String DB_USERNAME = "future";
    public static final String DB_PWD = "123456";
}
