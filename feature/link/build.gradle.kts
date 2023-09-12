plugins {
    id("mcenter.android.feature")
    id("mcenter.android.hilt")
    id("mcenter.android.compose")
}

android{
    namespace = "com.astune.feature.link"
}

dependencies {
    implementation(project(":core:database"))
    implementation(libs.google.accompanist.webview)
}