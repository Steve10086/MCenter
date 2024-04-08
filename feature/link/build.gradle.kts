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

    implementation(project(":feature:sshclient"))
    implementation(project(":feature:browser"))
}