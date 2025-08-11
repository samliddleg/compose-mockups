package uk.co.lidbit.compose.mockups.plugin

import org.gradle.api.provider.Property

abstract class ComposeMockupsExtension {
    abstract val mockupsDir: Property<String>
    abstract val sourceSetName: Property<String>
    abstract val composeVersion: Property<String>
}