package com.chunlangjiu.app.util;

import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.bean.UploadImageBean;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.AppUtils;
import com.pkqup.commonlibrary.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @CreatedbBy: liucun on 2018/8/12.
 * @Describe: 省市区的工具类
 */
public class CommonUtils {


    public static final String GOODS_STATUS_AUDIT_PENDING = "pending";//审核中
    public static final String GOODS_STATUS_AUDIT_REFUSE = "refuse";//审核拒绝
    public static final String GOODS_STATUS_SELL = "onsale";//出售中
    public static final String GOODS_STATUS_INSTOCK = "instock";//仓库中

    //由于后台竞拍状态和普通商品状态出现同样的值。所以给竞拍拼接上特殊字符，在发送请求的时候吧特殊字符截取掉就好
    public static final String AUCTION_STATUS_SUB = "auction_";//竞拍截取常亮
    public static final String GOODS_STATUS_AUCTION_NOT_START = AUCTION_STATUS_SUB + "pending";//竞拍进行时
    public static final String GOODS_STATUS_AUCTION_ACTIVE = AUCTION_STATUS_SUB + "active";//竞拍进行时
    public static final String GOODS_STATUS_AUCTION_STOP = AUCTION_STATUS_SUB + "stop";//竞拍结束后



    public static SpannableStringBuilder setSpecifiedTextsColor(String text, String specifiedTexts,int colorId) {
        List<Integer> sTextsStartList = new ArrayList<>();

        int sTextLength = specifiedTexts.length();
        String temp = text;
        int lengthFront = 0;//记录被找出后前面的字段的长度
        int start = -1;
        do {
            start = temp.indexOf(specifiedTexts);

            if (start != -1) {
                start = start + lengthFront;
                sTextsStartList.add(start);
                lengthFront = start + sTextLength;
                temp = text.substring(lengthFront);
            }
        } while (start != -1);

        SpannableStringBuilder styledText = new SpannableStringBuilder(text);
        for (Integer i : sTextsStartList) {
            styledText.setSpan(
                    new ForegroundColorSpan(colorId),
                    i,
                    i + sTextLength,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return styledText;
    }


    public static Observable<ResultBean<UploadImageBean>> getUploadPic(final String path,String name){
        Observable<ResultBean<UploadImageBean>> detailsGoods ;
        if (CommonUtils.isNetworkPic(path)) {
            detailsGoods = Observable.create(new ObservableOnSubscribe<ResultBean<UploadImageBean>>() {
                @Override
                public void subscribe(ObservableEmitter<ResultBean<UploadImageBean>> emitter) throws Exception {
                    ResultBean<UploadImageBean> bean = new ResultBean<>();
                    UploadImageBean uploadImageBean = new UploadImageBean();
                    uploadImageBean.setUrl(path);
                    bean.setData(uploadImageBean);
                    emitter.onNext(bean);
                }
            });
        } else {
            String base64DetailFour = FileUtils.imgToBase64(path);
            detailsGoods = ApiUtils.getInstance().shopUploadImage(base64DetailFour, name);
        }
        return detailsGoods;
    }

    public static boolean isNetworkPic(String picPath) {
        if (TextUtils.isEmpty(picPath)) {
            return false;
        } else if (picPath.startsWith("http://") || picPath.startsWith("https://")) {
            return true;
        }
        return false;
    }

    public static boolean isAuctionGoods(String status) {
        boolean isAuction = GOODS_STATUS_AUCTION_NOT_START.equals(status) || GOODS_STATUS_AUCTION_ACTIVE.equals(status) || GOODS_STATUS_AUCTION_STOP.equals(status) ? true : false;
        return isAuction;
    }

    public static String joinStr(Object... obj) {
        String joinStr = "";
        if (obj == null) {
            return "";
        }
        for (Object o : obj) {
            if (o != null) {
                joinStr += o.toString();
            }
        }
        return joinStr;
    }

    public static String getSexStr(String sexCode) {
        if (TextUtils.isEmpty(sexCode)) {
            return "保密";
        }
        switch (sexCode) {
            case "0":
                return "女";
            case "1":
                return "男";
            case "2":
                return "保密";
            default:
                return "保密";
        }
    }

    public static void callPhone(String phone) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));//跳转到拨号界面，同时传递电话号码
        AppUtils.getContext().startActivity(dialIntent);
    }


}
