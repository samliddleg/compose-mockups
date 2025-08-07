package uk.co.lidbit.compose.mockups.plugin

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import uk.co.lidbit.compose.mockups.annotations.MockupInfo
import uk.co.lidbit.compose.mockups.annotations.Orientation
import java.net.URLClassLoader
import kotlin.collections.forEach
import kotlin.reflect.full.declaredMemberProperties

abstract class GenerateComposeMockupsTask : DefaultTask() {

    @get:InputFiles
    @get:Classpath
    abstract val runtimeClasspath: ConfigurableFileCollection

    @get:Input
    abstract var mockupsDir: String

    @TaskAction
    fun action(): Unit = runBlocking {
        val classLoader = URLClassLoader(
            runtimeClasspath.files.map { it.toURI().toURL() }.toTypedArray(),
            javaClass.classLoader,
        )

        val registryClassName = "uk.co.lidbit.compose.mockups.generated.ComposeMockupRegistry"
        val kClass = try {
            classLoader.loadClass(registryClassName).kotlin
        } catch (_: ClassNotFoundException) {
            println(
                "⚠\uFE0F Could not locate ComposeMockupRegistry. " +
                        "Make sure you’ve annotated at least one @Composable function with @ComposeMockup. " +
                        "Each function should have either no parameters or a single PaddingValues parameter."
            )
            return@runBlocking
        }
        val instance = kClass.objectInstance ?: error("Could not get object instance")

        @Suppress("UNCHECKED_CAST")
        val functionsProp = kClass
            .declaredMemberProperties
            .first { it.name == "functions" } as kotlin.reflect.KProperty1<Any, Any?>

        @Suppress("UNCHECKED_CAST") val functions =
            functionsProp.get(instance) as? List<Triple<String, MockupInfo, @Composable () -> Unit>>

        @Suppress("UNCHECKED_CAST")
        val paddingFunctionsProp = kClass
            .declaredMemberProperties
            .first { it.name == "paddingFunctions" } as kotlin.reflect.KProperty1<Any, Any?>

        @Suppress("UNCHECKED_CAST") val paddingFunctions =
            paddingFunctionsProp.get(instance) as? List<Triple<String, MockupInfo, @Composable (PaddingValues) -> Unit>>

        val num = (functions?.sumOf { it.second.devices.size } ?: 0) + (paddingFunctions?.sumOf { it.second.devices.size } ?: 0)
        var count = 1

        functions?.forEach { (name, mockupInfo, content) ->
            mockupInfo.devices.forEach { deviceId ->
                print("Generating mockup ${count++}/$num\r")
                System.out.flush()
                val device = Device.resolve(deviceId)
                val orientations = device.allowedOrientations.intersect(mockupInfo.orientations)
                orientations.forEach { orientation ->
                    val orientationText = when (orientation) {
                        Orientation.PORTRAIT -> "portrait"
                        Orientation.LANDSCAPE -> "landscape"
                    }
                    takeScreenshot(
                        path = "$mockupsDir/${name}_${device.frame?.name}_$orientationText.png",
                        device = Device.resolve(deviceId),
                        orientation = orientation,
                        content = { content() },
                    )
                }
            }
        }

        paddingFunctions?.forEach { (name, mockupInfo, content) ->
            mockupInfo.devices.forEach { deviceId ->
                print("Generating mockup ${count++}/$num\r")
                System.out.flush()
                val device = Device.resolve(deviceId)
                val orientations = device.allowedOrientations.intersect(mockupInfo.orientations)
                orientations.forEach { orientation ->
                    val orientationText = when (orientation) {
                        Orientation.PORTRAIT -> "portrait"
                        Orientation.LANDSCAPE -> "landscape"
                    }
                    takeScreenshot(
                        path = "$mockupsDir/${name}_${device.frame?.name}_$orientationText.png",
                        device = Device.resolve(deviceId),
                        orientation = orientation,
                        content = content,
                    )
                }
            }
        }
    }
}

class ComposeMockupsPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        plugins.apply("org.jetbrains.compose")
        plugins.apply("org.jetbrains.kotlin.plugin.compose")

        val extension = extensions.create(
            "composeMockups",
            ComposeMockupsExtension::class.java
        )

        gradle.projectsEvaluated {
            val composeVersion = extension.composeVersion.orNull
            if (composeVersion.isNullOrBlank()) {
                error(
                    """
                    Provide a compose version using:
                    
                    composeMockups {
                        composeVersion.set("VERSION")
                    }
                    """.trimIndent()
                )
            }
            dependencies.apply {
                add("implementation", "org.jetbrains.compose.foundation:foundation:$composeVersion")
            }

            // Now safe to use composeVersion here
            configurations.all {
                resolutionStrategy.eachDependency {
                    if (requested.group.startsWith("org.jetbrains.compose")) {
                        useVersion(composeVersion)
                    }
                }
            }
        }

        tasks.register(
            "generateComposeMockups",
            GenerateComposeMockupsTask::class.java
        ) {
            group = "compose mockups"
            description = "Generates device mockup images from Composable functions"

            val runtimeClasspath = project.extensions.getByType<JavaPluginExtension>()
                .sourceSets.getByName("main").runtimeClasspath
            this.runtimeClasspath.from(runtimeClasspath)

            mockupsDir = extension.mockupsDir.getOrElse("mockups")

            dependsOn("kspKotlin")
            outputs.upToDateWhen { false }
        }

        dependencies.add("implementation", "uk.co.lidbit:compose-mockups:0.1.0")
    }
}