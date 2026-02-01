plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

gradle.taskGraph.whenReady {
    allTasks
        .filter { it.name.startsWith("ijDownloadArtifact") }
        .forEach { it.enabled = false }
}

android {
    namespace = "com.example.pennlabsapplication"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.pennlabsapplication"
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    implementation("androidx.appcompat:appcompat:1.4.1")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.1.0")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.google.android.gms:play-services-maps:18.0.2")
    implementation("com.google.maps.android:android-maps-utils:2.2.0")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation("com.google.maps.android:maps-compose:4.3.0")
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")
}