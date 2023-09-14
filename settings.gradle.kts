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

include (":feature:device")
include("core:datastore")
findProject(":core:datastore")?.name = "datastore"
include("feature:setting")
findProject(":feature:setting")?.name = "setting"
include("feature:link")
findProject(":feature:link")?.name = "link"
include("core:network")
findProject(":core:network")?.name = "network"
