import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kobwebApplication)
}

kobweb {
    app {
        index {
            description.set("Music Discovery - No ViewModel, just UIModel")
        }
        globals.put("qualifiedPackage", "kobwebApp")
    }
}

kotlin {
    configAsKobwebApplication("kobwebApp", includeServer = false)

    sourceSets {
        jsMain.dependencies {
            implementation(projects.shared)
            implementation(libs.compose.runtime)
            implementation(libs.compose.html.core)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.koin.core)
        }
    }
}
