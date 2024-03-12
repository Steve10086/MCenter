plugins {
    id("mcenter.android.library")
    id("mcenter.android.compose")
}

android {
    namespace = "com.astune.core.ui"

}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:database"))
    implementation(project(":core:common"))

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.constraintlayout)
    implementation(libs.androidx.compose.material)

    testImplementation(libs.androidx.compose.ui.test)

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}