import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

val mapsApiKey: String = localProperties.getProperty("MAPS_API_KEY", "")

android {
    namespace = "com.dlancers.api_data"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.dlancers.api_data"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
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

    flavorDimensions += "env"
    productFlavors {
        create ("dev") {
            dimension = "env"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "BASE_URL", "\"https://api.escuelajs.co/api/v1/\"")

            buildConfigField("String", "FAKESTORE_BASE_URL", "\"https://fakestoreapi.com/\"")
        }

        create ("qa") {
            dimension = "env"
            applicationIdSuffix = ".qa"
            versionNameSuffix ="-qa"
            // FIX: qa.api.escuelajs.co does not exist (DNS fails); use the live API for QA builds
            buildConfigField("String", "BASE_URL", "\"https://api.escuelajs.co/api/v1/\"")

            buildConfigField("String", "FAKESTORE_BASE_URL", "\"https://fakestoreapi.com/\"")

        }

        create ("prod") {
            dimension = "env"
            buildConfigField("String", "BASE_URL", "\"https://api.escuelajs.co/api/v1/\"")

            buildConfigField("String", "FAKESTORE_BASE_URL", "\"https://fakestoreapi.com/\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}


dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-extended")
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)



    implementation(libs.kotlinx.coroutines.android)
    // Adding the Coil for image fetching
    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    implementation(libs.maps.compose)
    implementation(libs.kotlinx.coroutines.play.services)

    // Hilt Compose Navigation
    implementation(libs.androidx.hilt.navigation.compose)

    // OkHttp Logging Interceptor
    // OkHttp Logging Interceptor
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

}