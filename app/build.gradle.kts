

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("androidx.navigation.safeargs")
    id ("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.example.medsreminder"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.medsreminder"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.medsreminder.testAplication.MyTestRunner"

        // The following argument makes the Android Test Orchestrator run its
        // "pm clear" command after each test invocation. This command ensures
        // that the app's state is completely cleared between tests.
        testInstrumentationRunnerArguments += mapOf(
            "clearPackageData" to "true",
        )

    }
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        unitTests.isReturnDefaultValues = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.21")

    //RecyclerView
    implementation ("androidx.recyclerview:recyclerview:1.3.1")


    implementation ("com.google.dagger:hilt-android:2.47")
    kapt ("com.google.dagger:hilt-compiler:2.47")
    implementation ("androidx.hilt:hilt-navigation-fragment:1.0.0")

    // For instrumentation tests
    androidTestImplementation  ("com.google.dagger:hilt-android-testing:2.47")
    kaptAndroidTest ("com.google.dagger:hilt-compiler:2.47")

    // fragment test
    debugImplementation("androidx.fragment:fragment-testing:1.6.1")

    //navigation test
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.1")

    // For local unit tests
    testImplementation ("com.google.dagger:hilt-android-testing:2.47")
    kaptTest ("com.google.dagger:hilt-compiler:2.47")

    // Core library
    androidTestImplementation ("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")

    // AndroidJUnitRunner and JUnit Rules
    androidTestImplementation ("androidx.test:runner:1.5.2")
    androidTestImplementation ("androidx.test:rules:1.5.0")
    androidTestUtil ("androidx.test:orchestrator:1.4.2")

    // Assertions
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.ext:truth:1.5.0")

    //coroutines test
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")


    // Espresso dependencies
    implementation ("androidx.test.espresso:espresso-idling-resource:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-accessibility:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-web:3.5.1")
    androidTestImplementation ("androidx.test.espresso.idling:idling-concurrent:3.5.1")

    //Mockito
    testImplementation ("org.mockito:mockito-core:5.5.0")

    //Turbine
    testImplementation ("app.cash.turbine:turbine:1.0.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.2.2"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Leak canary
    debugImplementation ("com.squareup.leakcanary:leakcanary-android:2.12")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

