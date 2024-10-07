@file:Suppress("PackageDirectoryMismatch")

package uk.org.lidalia.gradle.plugin.ideaext

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.SupportsKotlinAssignmentOverloading
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.gradle.ext.IdeaExtPlugin
import uk.org.lidalia.gradle.plugin.ideaext.ideamodelextensions.extensions
import uk.org.lidalia.gradle.plugin.ideaext.ideamodelextensions.ideaModel
import uk.org.lidalia.gradle.plugin.ideaext.ideamodelextensions.moduleSettings
import uk.org.lidalia.gradle.plugin.ideaext.ideamodelextensions.packagePrefixContainer
import uk.org.lidalia.gradle.plugin.ideaext.projectextensions.sourceSets

@Suppress("unused")
class LidaliaIdeaExtPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    project.plugins.apply(IdeaExtPlugin::class.java)
    val ideaModel = project.ideaModel

    val defaultPackagePrefix = "${project.group}.${project.name}".normalise()

    ideaModel.setPackagePrefix(defaultPackagePrefix)

    ideaModel
      .extensions
      .add(
        "packagePrefix",
        ActOnSetProperty(defaultPackagePrefix) { prefix ->
          ideaModel.setPackagePrefix(prefix)
        },
      )
  }
}

private val unwantedPackageChars = "[^a-z0-9.]".toRegex()

private fun String.normalise() = this.lowercase().remove(unwantedPackageChars)

private fun String.remove(regex: Regex) = replace(regex, "")

private fun IdeaModel.setPackagePrefix(prefix: String) {
  val gradleProject = module?.project ?: project?.project

  val srcDirs = gradleProject?.sourceSets
    ?.flatMap { it.allSource.srcDirs }
    ?.map { it.relativeTo(gradleProject.projectDir).path }

  val packagePrefixContainer = module.moduleSettings.packagePrefixContainer

  srcDirs?.forEach {
    packagePrefixContainer[it] = prefix
  }
}

@SupportsKotlinAssignmentOverloading
class ActOnSetProperty<T>(
  private var value: T,
  private val onSet: (T) -> Unit,
) {
  fun get(): T = value

  fun set(value: T) {
    this.value = value
    onSet(value)
  }
}
