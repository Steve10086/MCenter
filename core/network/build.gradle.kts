plugins {
    id("mcenter.android.library")
    id("mcenter.android.hilt")
}

android {
    namespace = "com.astune.core.network"

    packagingOptions{
        pickFirst("META-INF/DEPENDENCIES")
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.hierynomus.sshj)
}