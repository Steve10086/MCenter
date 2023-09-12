plugins {
    id("mcenter.android.feature")
    id("mcenter.android.hilt")
    id("mcenter.android.compose")
}

android {
    namespace = "com.astune.feature.setting"
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
}