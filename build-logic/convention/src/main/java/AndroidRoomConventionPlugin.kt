import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            pluginManager.apply("com.google.devtools.ksp")

            dependencies {
                add("ksp", MClibs.findLibrary("room.compiler").get())
                add("implementation", MClibs.findLibrary("room.runtime").get())
                add("implementation", MClibs.findLibrary("room.ktx").get())

            }
        }
    }
}