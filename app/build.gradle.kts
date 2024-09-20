plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.fpoly.shoes_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fpoly.shoes_app"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {   // Firebase dependencies
    implementation ("com.google.zxing:core:3.4.1")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation ("androidx.datastore:datastore-preferences:1.1.1")

    // Shimmer effect
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // Service location
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.gms:play-services-maps:19.0.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Maps and places
    implementation("com.google.maps.android:places-ktx:3.2.0")
    implementation("com.google.maps.android:android-maps-utils:3.8.2")
    implementation("com.google.android.libraries.places:places:3.5.0")

    // JSON parsing
    implementation("com.beust:klaxon:5.6")
    implementation("com.google.maps:google-maps-services:2.2.0")

    // Kotlin standard library
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")

    // Customizable Toast
    implementation("io.github.muddz:styleabletoast:2.4.0")

    // AndroidX and Jetpack dependencies
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.5")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.5")
    implementation("androidx.fragment:fragment-ktx:1.8.3")
    implementation("com.google.android.libraries.places:places:3.5.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("com.google.firebase:firebase-messaging")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.google.firebase:firebase-inappmessaging-display:21.0.0")

    // Hilt
    val hiltVersion = "2.51.1"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    // Dots Indicator
    implementation("com.tbuonomo:dotsindicator:5.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Coil
    implementation("io.coil-kt:coil:2.6.0")

    // Retrofit HTTP
    val retrofit = "2.11.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit")
    implementation("com.google.code.gson:gson:$retrofit")

    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // implement ZaloPay
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation(files("/libs/zalo/zpdk-release-v3.1.aar"))
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}