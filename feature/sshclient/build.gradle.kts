plugins {
    id("mcenter.android.feature")
    id("mcenter.android.hilt")
    id("mcenter.android.compose")
}

android{
    namespace = "com.astune.feature.sshclient"
}

dependencies {
    implementation(project(":core:network"))
    implementation(libs.google.accompanist.webview)
    implementation(libs.hierynomus.sshj)
}