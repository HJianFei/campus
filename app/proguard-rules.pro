# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in d:\Users\jiangfangsheng\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keepattributes Exceptions,InnerClasses

-keepattributes Signature

-keepattributes *Annotation*

-keep class com.google.gson.examples.android.model.** { *; }

-keep class **$Properties
-dontwarn org.eclipse.jdt.annotation.**

-libraryjars libs/agora-rtc-sdk.jar
-keep class io.agora.rtc.** {*;}

-keep class io.rong.** {*;}
-keep class io.agora.rtc.** {*; }
-keep class * implements io.rong.imlib.model.MessageContent{*;}

-dontwarn io.rong.push.** 
-dontnote com.xiaomi.** 
-dontnote com.huawei.android.pushagent.** 
-dontnote com.google.android.gms.gcm.** 
-dontnote io.rong.**
 -ignorewarnings
