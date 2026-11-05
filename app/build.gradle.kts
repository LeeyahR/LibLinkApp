plugins {
    alias(libs.plugins.android.application)
    id("com.google.devtools.ksp") version "2.3.10"
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.liblinkapp"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.liblinkapp"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    //Bottom Navigation
    implementation("androidx.navigation:navigation-compose:2.10.1")

    implementation("com.google.android.material:material:1.14.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0")

    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")

    // Google Credential Manager
    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")

    // Google Identity
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    // Room Database
    implementation("androidx.room:room-runtime:2.8.5")
    ksp("androidx.room:room-compiler:2.8.5")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.12.0")

    // Lifecycle and Coroutines
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")

    //Biometric authentication
    implementation("androidx.biometric:biometric:1.1.0")

    // Firebase (FCM)                                ← NEW
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

}