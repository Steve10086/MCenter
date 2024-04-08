plugins {
    id("mcenter.android.feature")
    id("mcenter.android.hilt")
    id("mcenter.android.compose")
}

android {
    namespace = "com.astune.feature.browser"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:ui"))
    implementation(libs.google.accompanist.webview)
}