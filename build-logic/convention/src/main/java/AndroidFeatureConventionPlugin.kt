
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner =
                        "com.astune.core.common.test.MCTestRunner"
                }
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
            }

            dependencies {
                add("implementation", project(":core:model"))
                add("implementation", project(":core:ui"))
                add("implementation", project(":core:data"))
                add("implementation", project(":core:common"))

                add("androidTestImplementation", kotlin("test"))
                add("testImplementation", kotlin("test"))

                add("implementation", MClibs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", MClibs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                add("implementation", MClibs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                add("implementation", MClibs.findLibrary("androidx.navigation.compose").get())

                add("implementation", MClibs.findLibrary("kotlinx.coroutines.android").get())
            }
        }
    }
}