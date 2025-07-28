plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.verbose"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.verbose"
        minSdk = 24
        targetSdk = 34
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.gridlayout:gridlayout:1.0.0")

    val navVersion = "2.7.5"

    //Calendar
    //implementation("com.google.apis:google-api-services-calendar:v3-rev411-1.25.0")
    //implementation("com.google.code.gson:gson:2.10")
    //implementation("com.google.api-client:google-api-client-android:1.23.0")
    //implementation("com.google.api-client:google-api-client:2.0.0")
    //implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    //implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    implementation ("com.google.android.gms:play-services-auth:20.7.0")

    //Credentials manager
    implementation("androidx.credentials:credentials:1.2.0")

    //Google ID
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")

    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-analytics")
    //Google logo
    implementation("com.google.android.gms:play-services-base:18.3.0")

    //Navigation
    implementation ("androidx.navigation:navigation-fragment:$navVersion")
    implementation ("androidx.navigation:navigation-ui:$navVersion")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //email validator
    implementation("commons-validator:commons-validator:1.7")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Room
    val roomVersion = "2.6.1"

    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    //SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    //AndroidSVG
    implementation("com.caverock:androidsvg-aar:1.4")
}