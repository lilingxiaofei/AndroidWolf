# ----------------------- 通用配置  start  ------------------------------
#官方混淆配置 http://proguard.sourceforge.net/index.html#manual/usage.html
#混淆时不使用大小写混合类名
-dontusemixedcaseclassnames
#不跳过library中的非public的类
-dontskipnonpubliclibraryclasses
 #不进行预校验,可加快混淆速度。
-dontpreverify
#打印混淆的详细信息
-verbose
-dontwarn
-dontskipnonpubliclibraryclassmembers
-dontshrink
#不进行优化，建议使用此选项，
-dontoptimize

#设置混淆的压缩比率 0 ~ 7
#-optimizationpasses 5
-ignorewarning
#-keep
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.IntentService
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep class * extends android.webkit.WebChromeClient { *; }
-keep class android.util.Singleton {*;}

-keep class **.R$* {
    <fields>;
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class org.apache.commons.**{*;}
-keep class com.threed.**{*;}
-keep class android.support.v7.** { *; }
-keep class * extends java.lang.annotation.Annotation {*;}
-keep class !android.support.v7.internal.view.menu.**,** {*;}
-keep class javax.crypto.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.net.SSLCertificateSocketFactory{*;}

#-keepattributes
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*.,EnclosingMethod
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes JavascriptInterface


-keepclasseswithmembers class android.support.v7.** { *; }
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keepclassmembers class ** {
    public void onEvent*(**);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#native方法不混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclasseswithmembernames class * implements java.io.Serializable {
	*;
}
#-dontwarn
-dontwarn android.support.**
-dontwarn javax.xml.**
-keep class android.support.** { public *; }
-dontwarn com.aphidmobile.**
-dontwarn org.apache.http.**
-dontwarn org.apache.http.**
-dontwarn org.jdom2.**
-dontwarn org.apache.commons.**
-dontwarn java.time.**
-dontwarn java.awt.**
-dontwarn org.springframework.**
-dontwarn javax.servlet.**
-dontwarn com.actionbarsherlock.**
-dontwarn com.google.android.**
-dontwarn org.apache.tools.ant.**
-dontwarn android.util.Singleton
-dontwarn javax.crypto.**



# ----------------------- 通用配置  end  ------------------------------


# ----------------------- Glide  start  ------------------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl
-keep public class  extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# ----------------------- Glide  end  ------------------------------
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-dontoptimize
