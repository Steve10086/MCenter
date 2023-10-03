pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MCenter"
include (":app")
include (":core:ui")
include (":core:database")
include (":core:model")
include (":core:data")
include (":core:common")
include("core:datastore")
include("core:network")
include("core:sync")

include (":feature:device")
include("feature:setting")
include("feature:link")

