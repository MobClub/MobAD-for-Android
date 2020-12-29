-optimizationpasses 5       # 指定代码的压缩级别
-dontusemixedcaseclassnames     # 是否使用大小写混合
-dontskipnonpubliclibraryclasses        # 指定不去忽略非公共的库类
-dontskipnonpubliclibraryclassmembers       # 指定不去忽略包可见的库类的成员
-dontpreverify      # 混淆时是否做预校验
-verbose        # 混淆时是否记录日志
-ignorewarnings
#-printmapping proguardMapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes SourceFile,LineNumberTable,Exceptions,InnerClasses,Signature,Deprecated,EnclosingMethod,*Annotation*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.View
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class **.R {
    public *;
}

-keep class **.R$* {
    *;
}

-keep interface * {
    *;
}

-keep class * extends java.lang.Throwable {
	*;
}

-keep class * implements com.mob.tools.proguard.ClassKeeper

-keepclassmembers class * implements com.mob.tools.proguard.PublicMemberKeeper {
	public *;
}

-keepclassmembers class * implements com.mob.tools.proguard.ProtectedMemberKeeper {
	protected *;
}

-keepclassmembers class * implements com.mob.tools.proguard.PrivateMemberKeeper {
	private *;
}

-keep class * implements com.mob.tools.proguard.EverythingKeeper {
	*;
}

-keep public class * extends androidx.versionedparcelable.VersionedParcelable {
  <init>();
}
-keep class android.support.v4.**{
    public *;
}
-keep class android.support.v7.**{
    public *;
}

-keep class com.qq.e.** {
    public protected *;
}

-keep class pl.droidsonroids.gif.** { *; }
-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep class com.androidquery.callback.** {*;}
-keep class com.pgl.sys.ces.* {*;}
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
-keep class com.ss.** {*;}

-keep class com.kwad.sdk.** { *;}
-keep class com.ksad.download.** { *;}
-keep class com.kwai.filedownloader.** { *;}

-keepclassmembers class * extends android.app.Activity {    public void *(android.view.View); }

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class com.baidu.mobads.** { *; }
-keep class com.baidu.mobad.** { *; }


-keep class com.mob.**{*;}
-keep class cn.fcommon.**{*;}