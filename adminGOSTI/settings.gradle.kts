pluginManagement {
    repositories {
        // Required for Android Gradle Plugin & Google plugins
        google()
        // Required for Kotlin & other JVM plugins
        mavenCentral()
        // Required for community Gradle plugins
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

rootProject.name = "admin-GOSTI"
include(":app")
