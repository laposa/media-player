# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Keep line numbers and annotations for better debugging and reflection support
-keepattributes SourceFile,LineNumberTable,RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,Signature,InnerClasses

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class androidx.compose.ui.platform.AndroidComposeView {
    <fields>;
    <methods>;
}

# libVLC — keep all JNI-called classes intact
-keep class org.videolan.libvlc.** { *; }
-keep class org.videolan.libvlc.util.** { *; }

# Gson — keep model classes used for serialisation
-keepclassmembers class com.laposa.domain.** {
    <fields>;
}

# smbj / dcerpc — reflection-heavy
-keep class com.hierynomus.** { *; }
-keepclassmembers class com.hierynomus.** { *; }
-keep class com.rapid7.** { *; }
-keepclassmembers class com.rapid7.** { *; }

# mbassy event bus (used by smbj)
# Mbassy uses extensive reflection to find handlers and instantiate invocations
-keep class net.engio.mbassy.** { *; }
-keepclassmembers class net.engio.mbassy.** { *; }
-keepclassmembers class * {
    @net.engio.mbassy.listener.Handler <methods>;
}

# NFS client
-keep class com.emc.ecs.nfsclient.** { *; }

# JSch (SFTP)
-keep class com.jcraft.jsch.** { *; }

# Apache Commons VFS
-keep class org.apache.commons.vfs2.** { *; }

# ── dontwarn: optional/unavailable dependencies ─────────────────────────────
# commons-vfs2 optional transports (FTP, HTTP3/4/5, HDFS, Ant tasks)
-dontwarn org.apache.commons.net.**
-dontwarn org.apache.commons.httpclient.**
-dontwarn org.apache.commons.collections.**
-dontwarn org.apache.commons.collections4.**
-dontwarn org.apache.commons.compress.**
-dontwarn org.apache.http.**
-dontwarn org.apache.hc.**
-dontwarn org.apache.hadoop.**
-dontwarn org.apache.tools.ant.**

# jsch optional compression (jzlib)
-dontwarn com.jcraft.jzlib.**

# smbj optional GSSAPI/Kerberos
-dontwarn org.ietf.jgss.**

# Java SE APIs not present on Android
-dontwarn java.rmi.**
-dontwarn javax.el.**
