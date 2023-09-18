plugins {
    id ("mcenter.android.feature")
    id ("mcenter.android.hilt")
    id ("mcenter.android.compose")
}

android {
    namespace = "com.astune.feature.device"
}

dependencies {

    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(project(":core:sync"))

    implementation(libs.androidx.work)

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}