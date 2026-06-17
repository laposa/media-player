# SMBJ & transitive dependencies
-keep class com.hierynomus.** { *; }
-keep class com.rapid7.** { *; }
-keep class com.hierynomus.protocol.commons.EnumWithValue { *; }

# mbassy (Event Bus used by smbj)
-keep class net.engio.mbassy.** { *; }
-keepclassmembers class net.engio.mbassy.** {
    <init>(...);
}
-keepclassmembers class * {
    @net.engio.mbassy.listener.Handler <methods>;
}

# Bouncy Castle (sometimes used by smbj for crypto)
-keep class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**

# NFS client
-keep class com.emc.ecs.nfsclient.** { *; }

# JSch (SFTP)
-keep class com.jcraft.jsch.** { *; }

# Apache Commons VFS
-keep class org.apache.commons.vfs2.** { *; }

# ── dontwarn: optional/unavailable dependencies ─────────────────────────────
-dontwarn org.apache.commons.net.**
-dontwarn org.apache.commons.httpclient.**
-dontwarn org.apache.commons.collections.**
-dontwarn org.apache.commons.collections4.**
-dontwarn org.apache.commons.compress.**
-dontwarn org.apache.http.**
-dontwarn org.apache.hc.**
-dontwarn org.apache.hadoop.**
-dontwarn org.apache.tools.ant.**
-dontwarn com.jcraft.jzlib.**
-dontwarn org.ietf.jgss.**
-dontwarn net.engio.mbassy.**
-dontwarn java.rmi.**
-dontwarn javax.el.**
