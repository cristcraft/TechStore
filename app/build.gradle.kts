plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    id ("com.google.dagger.hilt.android")
    id ("com.google.devtools.ksp")
    id ("kotlin-parcelize")

}

android {
    namespace = "com.techstore.techstore"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.techstore.techstore"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // AndroidX Core
    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    // Navigation Component
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.6")

    // Lifecycle (ViewModel + LiveData)
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // Room (Base de datos local / caché)
    implementation ("androidx.room:room-runtime:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
    //ksp ("androidx.room:room-compiler:2.6.1")

    // Hilt (Inyección de Dependencias)
    implementation ("com.google.dagger:hilt-android:2.50")
    ksp ("com.google.dagger:hilt-compiler:2.50")

    // Firebase BOM (gestiona versiones automáticamente)
//    implementation(platform("com.google.firebase:firebase-bom:34.12.0"))
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.firebase:firebase-firestore-ktx")
    implementation ("com.google.firebase:firebase-storage-ktx")
    implementation ("com.google.firebase:firebase-messaging-ktx")

    implementation (platform("com.google.firebase:firebase-bom:32.7.2"))


    // Google Sign-In
    implementation ("com.google.android.gms:play-services-auth:21.0.0")

    // CameraX
    implementation ("androidx.camera:camera-core:1.3.1")
    implementation ("androidx.camera:camera-camera2:1.3.1")
    implementation ("androidx.camera:camera-lifecycle:1.3.1")
    implementation ("androidx.camera:camera-view:1.3.1")

    // Glide (imágenes)
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    ksp ("com.github.bumptech.glide:ksp:4.16.0")

    // Biometría
    implementation ("androidx.biometric:biometric:1.1.0")

    // Maps y Localización
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.1.0")

    // Seguridad
    implementation ("androidx.security:security-crypto:1.1.0-alpha06")

    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // RecyclerView y SwipeRefresh
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Testing
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")


}