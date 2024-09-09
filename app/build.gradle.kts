plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
	id("kotlinx-serialization") // must include this to avoid run-time error https://stackoverflow.com/questions/71988144/serializer-for-class-is-not-found-mark-the-class-as-serializable-or-prov 
}

android {
    namespace = "com.quoinsight.tv"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.quoinsight.tv"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    //def exop_ver = "2.18.0"
    val media3_ver = "1.2.1" // 1.4.1

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.leanback)
    implementation(libs.glide)
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2") // ver 1.3.2 is too old, 1.7.1 is too new!!
	// .. -json:1.3.2 not supporting Json.decodeFromString<Type>("{...}") !!

    //implementation("com.google.android.exoplayer:exoplayer-core:$exop_ver")
    //implementation("com.google.android.exoplayer:exoplayer-dash:$exop_ver")
    //implementation("com.google.android.exoplayer:exoplayer-hls:$exop_ver")
    //implementation("com.google.android.exoplayer:exoplayer-ui:$exop_ver")

    implementation("androidx.media3:media3-exoplayer:$media3_ver")
    implementation("androidx.media3:media3-ui:$media3_ver")
    implementation("androidx.media3:media3-common:$media3_ver")
	implementation("androidx.media3:media3-session:$media3_ver")
    implementation("androidx.media3:media3-exoplayer-hls:$media3_ver")
}