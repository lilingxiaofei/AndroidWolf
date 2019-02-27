package com.chunlangjiu.app.util;

import android.app.Activity;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * @CreatedbBy: liucun on 2018/7/24
 * @Describe:
 */
public class ShareUtils {

    public static void shareTxt(Activity activity, String txt, UMShareListener umShareListener) {
        new ShareAction(activity)
                .withText(txt)
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener).open();
    }

    public static void shareTxtAndImage(Activity activity, String txt, UMImage image, UMShareListener umShareListener) {
        new ShareAction(activity)
                .withText(txt)
                .withMedia(image)
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener).open();
    }

    public static void shareLink(Activity activity, UMWeb umWeb, UMShareListener umShareListener) {
        new ShareAction(activity)
                .withMedia(umWeb)
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE)
                .setCallback(umShareListener).open();
    }
}
