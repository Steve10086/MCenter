plugins {
    id("mcenter.android.library")
    id("mcenter.android.hilt")
}

android {
    namespace = "com.astune.core.network"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.android)

}