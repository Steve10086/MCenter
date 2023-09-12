import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.astune.mcenter.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}
dependencies {
    compileOnly(mclibs.android.gradlePlugin)
    compileOnly(mclibs.firebase.crashlytics.gradlePlugin)
    compileOnly(mclibs.firebase.performance.gradlePlugin)
    compileOnly(mclibs.kotlin.gradlePlugin)
    compileOnly(mclibs.ksp.gradlePlugin)
}
gradlePlugin{
    plugins{
        register("androidRoom"){
            id = "mcenter.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
    }
    plugins{
        register("androidHilt"){
            id = "mcenter.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
    }
    plugins{
        register("androidLibrary"){
            id = "mcenter.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
    }
    plugins{
        register("androidFeature"){
            id = "mcenter.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
    }
    plugins{
        register("androidCompose"){
            id = "mcenter.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
    }
}

