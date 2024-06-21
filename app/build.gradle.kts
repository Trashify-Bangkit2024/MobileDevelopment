plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.trashify"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.trashify"
        minSdk = 30
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
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    /*Core Ktx Library*/
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.core:core-splashscreen:1.0.1")

    /* Retrofit for Network Requests */
    implementation("com.squareup.retrofit2:retrofit:2.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:converter-gson:2.10.0")

    /* DataStore Preferences for User Preferences */
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    /*CameraX*/
    implementation("androidx.camera:camera-camera2:1.3.4")
    implementation("androidx.camera:camera-lifecycle:1.3.4")
    implementation("androidx.camera:camera-view:1.3.4")

    /*Util*/
    implementation("androidx.exifinterface:exifinterface:1.3.7")

    /*LoginView*/
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")

    /*ViewModel*/
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")

    /*Activity*/
    implementation("androidx.activity:activity-ktx:1.9.0")

    /*Coil buat load photo*/
    implementation("io.coil-kt:coil:2.6.0")

    /*Circle image*/
    implementation("de.hdodenhof:circleimageview:3.1.0")

    /*Jetbrains*/
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    /* AppCompat for Backward Compatibility */
    implementation("androidx.appcompat:appcompat:1.7.0")

    /* Material Design Components */
    implementation("com.google.android.material:material:1.12.0")

    /* ConstraintLayout for Advanced Layouts */
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    /* Room Database Components */
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.media3:media3-common:1.3.1")
    implementation("androidx.compose.ui:ui-desktop:1.6.8")
    ksp("androidx.room:room-compiler:2.6.1")

    /* JUnit for Unit Testing */
    testImplementation("junit:junit:4.13.2")

    /* AndroidX Testing Libraries */
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}