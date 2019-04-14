package com.chunlangjiu.app.abase;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.awen.photo.FrescoImageLoader;
import com.chunlangjiu.app.net.ApiUtils;
import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.pinyinhelper.PinyinMapDict;
import com.pkqup.commonlibrary.util.AppUtils;
import com.pkqup.commonlibrary.util.SPUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends MultiDexApplication {

    //   http://mall.chunlangjiu.com/appdownload/index.html

    //是否隐藏竞拍专区
    public static boolean HIDE_AUCTION = false;

    private static String token = "";

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.init(this);
        ApiUtils.getInstance().init();
        initToken();
        initRealm();
        initUM();
        initPinyinCity();
        FrescoImageLoader.init(this);
        //CrashHandler.getInstance().init(this);
        KLog.init(AppUtils.isDebug());
    }


    public static void initToken() {
        //个人:  e99a73de0d110f00fb7f9924e689315218c7f4532875d76012ac3e8e4238abcf
        //商家： e99a73de0d110f00fb7f9924e689315218c7f4532875d76012ac3e8e4238abcf
//        SPUtils.put("token", "e99a73de0d110f00fb7f9924e689315218c7f4532875d76012ac3e8e4238abcf");
        token = (String) SPUtils.get("token", "");
        KLog.e("-----token-----", token);
    }


    /**
     * 友盟初始化
     */
    private void initUM() {
//        UMConfigure.init(this, "5b3b3744f43e4879b8000236", "chunlang", UMConfigure.DEVICE_TYPE_PHONE, "");
//        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
////        MobclickAgent.openActivityDurationTrack(false);//禁止默认的页面统计功能
//        PlatformConfig.setWeixin("wx0e1869b241d7234f", "13a3d322c7055d7c33e3de912a4fad2a");
//        PlatformConfig.setQQZone("1106941413", "9qybO7qdrpQIYPiq");
//        PlatformConfig.setSinaWeibo("1325843831", "bccdf04dac982831efd444a71588daea", "http://sns.whalecloud.com");


        UMConfigure.init(this, "5b3b3744f43e4879b8000236", "chunlang", UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
//        MobclickAgent.openActivityDurationTrack(false);//禁止默认的页面统计功能

        PlatformConfig.setWeixin("wx0e1869b241d7234f", "76be7f506fd8a4d51e002dffbbb30f14");
//        PlatformConfig.setWeixin("wx0e1869b241d7234f", "13a3d322c7055d7c33e3de912a4fad2a");
        PlatformConfig.setQQZone("1106941413", "9qybO7qdrpQIYPiq");
//        PlatformConfig.setSinaWeibo("1325843831", "bccdf04dac982831efd444a71588daea", "http://sns.whalecloud.com");
        PlatformConfig.setSinaWeibo("1325843831", "bccdf04dac982831efd444a71588daea", "http://sns.whalecloud.com/sina2/callback");

    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().
                name("MyRealm.realm").deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }

    private void initPinyinCity() {
        // 添加自定义词典
        Pinyin.init(Pinyin.newConfig()
                .with(new PinyinMapDict() {
                    @Override
                    public Map<String, String[]> mapping() {
                        HashMap<String, String[]> map = new HashMap<>();
                        map.put("重庆", new String[]{"CHONG", "QING"});
                        return map;
                    }
                }));
    }


    public static boolean isLogin() {
        return !TextUtils.isEmpty(token);
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        BaseApplication.token = token;
    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                ClassicsHeader classicsHeader = new ClassicsHeader(context);
                classicsHeader.setEnableLastTime(false);//隐藏更新时间
                return classicsHeader;
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                ClassicsFooter classicsFooter = new ClassicsFooter(context);
                return classicsFooter;
            }
        });
    }

}
