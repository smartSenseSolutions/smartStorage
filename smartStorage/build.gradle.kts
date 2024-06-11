plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

buildscript {
    extra.apply {
        set("PUBLISH_GROUP_ID", "io.github.smartsensesolutions")
        set("PUBLISH_VERSION", "1.0.0")
        set("PUBLISH_ARTIFACT_ID", "smartStorage")
        set("PUBLISH_DESCRIPTION", "File storage management with required permissions")
        set("PUBLISH_URL", "https://github.com/smartSenseSolutions/smartStorage")
        set("PUBLISH_LICENSE_NAME", "Apache License")
        set("PUBLISH_LICENSE_URL", "https://github.com/smartSenseSolutions/smartStorage/blob/master/LICENSE")
        set("PUBLISH_DEVELOPER_ID", "sagarsmartsense")
        set("PUBLISH_DEVELOPER_NAME", "Sagar Maiyad")
        set("PUBLISH_DEVELOPER_EMAIL", "sagar.maiyad@smartsensesolutions.com")
        set("PUBLISH_SCM_CONNECTION", "scm:git:github.com/smartSenseSolutions/smartStorage.git")
        set("PUBLISH_SCM_DEVELOPER_CONNECTION", "scm:git:ssh://github.com/smartSenseSolutions/smartStorage.git")
        set("PUBLISH_SCM_URL", "https://github.com/smartSenseSolutions/smartStorage")
    }
}

apply {
    from("${rootProject.projectDir}/scripts/publish-module.gradle")
}

android {
    namespace = "com.ss.smartstorage"
    compileSdk = 34

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}