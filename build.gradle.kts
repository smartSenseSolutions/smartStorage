// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.nexusPublish) apply true
}

apply {
    from("${rootDir}/scripts/publish-root.gradle")
}

tasks {
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
}