plugins {
    id("mcenter.android.library")
    id("mcenter.android.hilt")
}

android {
    namespace = "com.astune.core.common"
}

dependencies {
    //api(libs.hilt.android.testing)


    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}