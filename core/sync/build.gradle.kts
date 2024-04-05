plugins {
    id("mcenter.android.library")
    id("mcenter.android.hilt")
}

android{
    namespace = "com.astune.core.sync"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:data"))
    kapt(libs.hilt.ext.compiler)

    implementation(libs.androidx.work.ktx)
    implementation(libs.hilt.ext.work)

    androidTestImplementation(libs.androidx.work.testing)

}