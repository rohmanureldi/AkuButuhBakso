import org.gradle.kotlin.dsl.kotlin

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.diffplug.spotless") version "6.19.0" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

apply(from = "spotless.gradle")

buildscript {
    dependencies {
        classpath(libs.secrets.gradle.plugin)
    }
}