# 1. Google AI SDK classes aur interfaces ko preserve karein
-keep class com.google.ai.client.generativeai.** { *; }
-keep interface com.google.ai.client.generativeai.** { *; }

# 2. Server-side error response classes (Fixes MissingFieldException: Field 'details' is required)
-keep class com.google.ai.client.generativeai.common.server.** { *; }
-keepclassmembers class com.google.ai.client.generativeai.common.server.** {
    <fields>;
    <methods>;
}

# 3. Kotlin Serialization metadata aur annotations ko bachayein
-keepattributes *Annotation*, InnerClasses, Signature, Exceptions, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}

# 4. Internal serialization logic ko keep karein
-keep class kotlinx.serialization.json.** { *; }
-keep class kotlinx.serialization.internal.** { *; }

# 5. Ignore non-critical warnings
-dontwarn com.gemalto.jp2.JP2Decoder
-dontwarn groovy.lang.**
-dontwarn org.codehaus.groovy.**
-dontwarn javax.management.**
-dontwarn javax.naming.**
-dontwarn javax.servlet.**
-dontwarn sun.reflect.**
