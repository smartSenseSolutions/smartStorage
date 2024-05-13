import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("maven-publish")
}

val getVersionName = { ->
    "1.0.0"  // Replace with version Name
}

val getArtifactId = { ->
    "com.ss.smart-storage" // Replace with library name ID
}

android {
    namespace = "com.ss.smart-storage"
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

publishing {
    val githubProperties = Properties()
    githubProperties.load(FileInputStream(rootProject.file("secret.properties")))

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/smartSenseSolutions/smartStorage")
            credentials {
                username = githubProperties.getProperty("gpr.usr")
                password = githubProperties.getProperty("gpr.key")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.smartsensesolutions"
            artifactId = getArtifactId()
            version = getVersionName()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}