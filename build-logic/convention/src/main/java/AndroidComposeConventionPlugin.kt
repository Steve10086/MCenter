
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){

            extensions.configure<LibraryExtension> {
                buildFeatures {
                    compose = true
                }
                composeOptions {
                    kotlinCompilerExtensionVersion = MClibs.findVersion("androidxCompose").get().toString()
                }
            }

            dependencies {
                add("implementation", MClibs.findLibrary("androidx.compose.foundation").get())
                add("implementation", MClibs.findLibrary("androidx.compose.ui").get())
                add("implementation", MClibs.findLibrary("androidx.compose.ui.tooling").get())
                add("implementation", MClibs.findLibrary("androidx.compose.material3").get())
                add("implementation", MClibs.findLibrary("androidx.compose.constraintlayout").get())

                add("androidTestImplementation", MClibs.findLibrary("androidx.compose.ui.test").get())
                add("testImplementation", MClibs.findLibrary("androidx.compose.ui.test").get())
                add("debugImplementation", MClibs.findLibrary("androidx.compose.ui.manifest").get())
            }
        }
    }
}