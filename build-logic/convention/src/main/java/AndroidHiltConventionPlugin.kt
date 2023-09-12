import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            pluginManager.apply("dagger.hilt.android.plugin")
            pluginManager.apply("org.jetbrains.kotlin.kapt")

            dependencies {
                "kapt"(MClibs.findLibrary("hilt.compiler").get())
                "kaptAndroidTest"(MClibs.findLibrary("hilt.compiler").get())
                "implementation"(MClibs.findLibrary("hilt.android").get())
            }
        }
    }
}