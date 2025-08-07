plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.mavenPublish)
}

group = "uk.co.lidbit"
version = "0.1.0"
kotlin {
    jvmToolchain(21)
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "compose-mockups", version.toString())

    pom {
        name = "Compose Mockups Annotations"
        description = "Home of all annotations for Compose Mockups"
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
