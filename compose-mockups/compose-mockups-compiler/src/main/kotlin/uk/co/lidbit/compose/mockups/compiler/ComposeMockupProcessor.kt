package uk.co.lidbit.compose.mockups.compiler

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import uk.co.lidbit.compose.mockups.annotations.DeviceId
import uk.co.lidbit.compose.mockups.annotations.Orientation
import java.io.BufferedWriter

class ComposeMockupProcessor(
    private val codeGenerator: CodeGenerator,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val mockupAnnotations =
            resolver.getSymbolsWithAnnotation("uk.co.lidbit.compose.mockups.annotations.ComposeMockup")
                .filterIsInstance<KSClassDeclaration>()
                .toList()
        val mockupFunctions =
            resolver.getSymbolsWithAnnotation("uk.co.lidbit.compose.mockups.annotations.ComposeMockup")
                .filterIsInstance<KSFunctionDeclaration>()
                .toList()
        val mockupInfos =
            getMockupInfoForCustomAnnotations(
                resolver,
                mockupAnnotations
            ) + getMockupInfoForFunctions(
                mockupFunctions
            )

        if (mockupInfos.isEmpty()) {
            return emptyList()
        }

        val file = try {
            codeGenerator.createNewFile(
                Dependencies(aggregating = true, *mockupFunctions.mapNotNull { it.containingFile }.toTypedArray()),
                "uk.co.lidbit.compose.mockups.generated",
                "ComposeMockupRegistry",
            )
        } catch (_: FileAlreadyExistsException) {
            return emptyList()
        }

        val paddingFuncs = mockupInfos.filter { info ->
            val func = info.function
            if (func.parameters.size != 1) return@filter false
            val param = func.parameters.first()
            param.type.resolve().declaration.qualifiedName?.asString() == "androidx.compose.foundation.layout.PaddingValues"
        }
        val noParamFuncs = mockupInfos.filter { info ->
            val func = info.function
            func.parameters.isEmpty()
        }

        file.bufferedWriter().use { writer ->
            writer.appendLine("package uk.co.lidbit.compose.mockups.generated")
            writer.appendLine()
            writer.appendLine("import androidx.compose.foundation.layout.PaddingValues")
            writer.appendLine("import uk.co.lidbit.compose.mockups.annotations.DeviceId")
            writer.appendLine("import uk.co.lidbit.compose.mockups.annotations.Orientation")
            writer.appendLine("import androidx.compose.runtime.Composable")
            writer.appendLine("import uk.co.lidbit.compose.mockups.annotations.MockupInfo")
            writer.appendLine()
            writer.appendLine("object ComposeMockupRegistry {")
            writeFunctions(writer, "functions", noParamFuncs, false)
            writeFunctions(writer, "paddingFunctions", paddingFuncs, true)
            writer.appendLine("}")
        }

        return emptyList()
    }

    private fun writeFunctions(
        writer: BufferedWriter,
        name: String,
        functions: List<MockupInfo>,
        includePadding: Boolean
    ) {
        writer.appendLine("    val $name: List<Triple<String, MockupInfo, @Composable (${if (includePadding) "androidx.compose.foundation.layout.PaddingValues" else ""}) -> Unit>> = listOf(")
        for ((name, deviceIds, orientations, func) in functions) {
            val funcName = func.simpleName.asString()
            val pkg = func.packageName.asString()
            val deviceString = "setOf(${
                deviceIds.joinToString(
                    ", "
                ) { "DeviceId.${it.name}" }
            })"
            val orientationString = "setOf(${
                orientations.joinToString(
                    ", "
                ) { "Orientation.${it.name}" }
            })"
            val composableString =
                "{ ${if (includePadding) "padding -> " else ""}$pkg.$funcName(${if (includePadding) "padding" else ""}) }"
            writer.appendLine("        Triple(\"$name\", MockupInfo($deviceString, $orientationString), $composableString),")
        }
        writer.appendLine("    )")
    }

    private fun getMockupInfoForCustomAnnotations(
        resolver: Resolver,
        annotations: List<KSClassDeclaration>
    ): List<MockupInfo> {
        return annotations.flatMap { customAnnotation ->
            resolver.getSymbolsWithAnnotation(customAnnotation.qualifiedName!!.asString())
                .filterIsInstance<KSFunctionDeclaration>()
                .map { function ->
                    // Get the ComposeMockup annotation on the annotation class
                    val (devices, orientations) = getDevicesAndOrientationForAnnotation(
                        customAnnotation
                    )

                    MockupInfo(
                        name = function.simpleName.asString(),
                        deviceIds = devices,
                        orientations = orientations,
                        function = function
                    )
                }
        }
    }

    private fun getMockupInfoForFunctions(functions: List<KSFunctionDeclaration>): List<MockupInfo> {
        return functions.map { function ->
            val (devices, orientations) = getDevicesAndOrientationForAnnotation(function)

            MockupInfo(
                name = function.simpleName.asString(),
                deviceIds = devices,
                orientations = orientations,
                function = function
            )
        }
    }

    private fun getDevicesAndOrientationForAnnotation(annotated: KSAnnotated): Pair<Set<DeviceId>, Set<Orientation>> {
        val composeMockupAnnotation = annotated.annotations
            .firstOrNull { it.shortName.asString() == "ComposeMockup" }

        @Suppress("UNCHECKED_CAST") val deviceIds = composeMockupAnnotation
            ?.arguments
            ?.firstOrNull { it.name?.asString() == "devices" }
            ?.value as? List<KSClassDeclaration>
            ?: emptyList()

        @Suppress("UNCHECKED_CAST") val orientations = composeMockupAnnotation
            ?.arguments
            ?.firstOrNull { it.name?.asString() == "orientations" }
            ?.value as? List<KSClassDeclaration>
            ?: emptyList()

        val orientationEnums = orientations.mapNotNull { type ->
            type.simpleName.asString()
                .let { name ->
                    Orientation.entries.firstOrNull { it.name == name }
                }
        }.toSet()

        val deviceEnums = deviceIds.mapNotNull { type ->
            type.simpleName.asString()
                .let { name ->
                    DeviceId.entries.firstOrNull { it.name == name }
                }
        }.toSet()
        return Pair(deviceEnums, orientationEnums)
    }

    data class MockupInfo(
        val name: String,
        val deviceIds: Set<DeviceId>,
        val orientations: Set<Orientation>,
        val function: KSFunctionDeclaration,
    )
}