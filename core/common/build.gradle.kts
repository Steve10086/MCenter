plugins {
    id("mcenter.android.library")
    id("mcenter.android.hilt")
}

android {
    namespace = "com.astune.core.common"
}


dependencies {
    api(libs.hilt.android.testing)
    api(libs.androidx.test.runner)
    api(libs.kotlinx.coroutines.test)

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}