// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies{
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.47")
        classpath ("com.android.tools.build:gradle:8.0.0")
    }
}


plugins {
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    id("com.android.application") version "8.0.0" apply false
    id("com.android.library") version "8.0.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}