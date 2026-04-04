rootProject.name = "SimpleViewModelExample"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://us-central1-varabyte-repos.cloudfunctions.net/maven/kobweb")
        maven("https://us-central1-varabyte-repos.cloudfunctions.net/maven/kobwebx")
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven("https://us-central1-varabyte-repos.cloudfunctions.net/maven/kobweb")
        maven("https://us-central1-varabyte-repos.cloudfunctions.net/maven/kobwebx")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":shared")
include(":composeApp")
include(":kobwebApp")
include(":androidapp")
