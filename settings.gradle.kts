pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven{url = uri("https://github.com/psiegman/mvn-repo/raw/master/releases")}
        maven { url = uri("https://jitpack.io") }
        flatDir { dirs("libs") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://github.com/psiegman/mvn-repo/raw/master/releases")}
        maven { url = uri("https://repo.spring.io/libs-release/") }
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "PROYECTO_FINAL"
include(":app")

