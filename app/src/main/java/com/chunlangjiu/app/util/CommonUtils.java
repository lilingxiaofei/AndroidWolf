package com.chunlangjiu.app.util;

import android.content.Intent;
import android.net.Uri;

import com.pkqup.commonlibrary.util.AppUtils;

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

    public static void callPhone(String phone){
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +phone));//跳转到拨号界面，同时传递电话号码
        AppUtils.getContext().startActivity(dialIntent);
    }


}
