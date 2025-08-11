plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.mavenPublish)
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
    google()
}

gradlePlugin {
    plugins {
        create("composeMockupsPlugin") {
            id = "uk.co.lidbit.compose.mockups"
            implementationClass = "uk.co.lidbit.compose.mockups.plugin.ComposeMockupsPlugin"
        }
    }
}

group = "uk.co.lidbit"
version = "0.1.1"

dependencies {
    api(projects.composeMockups)

    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.google.devtools.ksp:symbol-processing-api:2.2.0-2.0.2")
}

kotlin {
    jvmToolchain(21)
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "compose-mockups-plugin", version.toString())

    pom {
        name = "Compose Mockups Plugin"
        description = "Plugin for Compose Mockups"
        inceptionYear = "2025"
        url = "https://github.com/samliddleg/compose-mockups/"
        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/licenses/MIT"
                distribution = "https://opensource.org/licenses/MIT"
            }
        }
        developers {
            developer {
                id = "samliddleg"
                name = "Sam Liddle"
                url = "https://github.com/samliddleg/"
            }
        }
        scm {
            url = "https://github.com/samliddleg/compose-mockups/"
            connection = "scm:git:git://github.com/samliddleg/compose-mockups.git"
            developerConnection = "scm:git:ssh://git@github.com/samliddleg/compose-mockups.git"
        }
    }
}
