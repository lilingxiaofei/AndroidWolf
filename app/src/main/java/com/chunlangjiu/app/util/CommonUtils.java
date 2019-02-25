package com.chunlangjiu.app.util;

/**
 * @CreatedbBy: liucun on 2018/8/12.
 * @Describe: 省市区的工具类
 */
public class CommonUtils {

    public static String joinStr(Object... obj) {
        String joinStr = "";
        if(obj == null){
            return "";
        }
        for (Object o :obj) {
            if(o!=null){
                joinStr += o.toString();
            }
        }
        return joinStr;
    }
}
