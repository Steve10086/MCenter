plugins {
    id("mcenter.android.library")
    id("mcenter.android.hilt")
}

android{
    namespace = "com.astune.core.sync"
}

dependencies {
    implementation(project(":core:network"))


    implementation(libs.androidx.work)
    implementation(libs.androidx.hilt.common)
}