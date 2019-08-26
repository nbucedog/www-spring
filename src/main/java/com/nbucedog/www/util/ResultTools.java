package com.nbucedog.www.util;

import java.util.HashMap;
import java.util.Map;

public class ResultTools {
    /*
     * 错误码记录：
     * 0--------成功
     * 1001-----请求传参错误
     * 1002-----没有对应内容
     * 1003-----此用户已存在
     * 1004-----上传文件为空
     * 404------异常抛出错误
     *
     */

    /**
     * @param errCode--错误码
     * @param object--数据源
     * @return map
     */
    public static Map<String,Object> dataResult(int errCode,Object object) {
        Map<String,Object> map = new HashMap<>();
        return putMap(map,errCode,object);
    }

    public static Map<String,Object> dataResult(int errCode,long total,Object object) {
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        return putMap(map,errCode,object);
    }

    private static Map<String,Object> putMap(Map<String,Object> map, int errCode,Object object){
        map.put("errcode",errCode);
        String errmsg = getErrmsg(errCode);
        map.put("errmsg",errmsg);
        map.put("data",object);
        return map;
    }

    public static Map<String,Object> result(int Errcode) {
        Map<String,Object> map = new HashMap<>();
        String Errmsg = getErrmsg(Errcode);
        map.put("errcode",Errcode);
        map.put("errmsg",Errmsg);
        return map;
    }

    private static String getErrmsg(int Errcode){
        switch (Errcode) {
            /*
             * 成功返回码
             */
            case 0:
                return "请求成功";
            case 100:
                return "登录成功";
            case 101:
                return "注销成功";
            case 200:
                return "上传成功";
            /*
             * 失败返回码
             */
            case 401:
                return "账号不存在";
            case 402:
                return "用户名或密码错误";
            case 403:
                return "没有权限";
            case 404:
                return "页面搞丢了";

            case 500:
                return "内部错误";
            case 700:
                return "参数错误";
            case 710:
                return "用户名已存在";
            /*
             * 上传错误
             */
            case 1000:
                return "系统错误";
            case 1001:
                return "文件不存在";
            case 1002:
                return "文件名格式不规范";
            default:
                return "未知错误";
        }
    }
}
