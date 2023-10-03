plugins {
    id("mcenter.android.library")
    id("mcenter.android.hilt")
    id("mcenter.android.room")

}

android {
    namespace = "com.astune.core.database"
}



dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.lifecycle.runtimeCompose)
}