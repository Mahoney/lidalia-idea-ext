package uk.org.lidalia.gradle.plugin.ideaext

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.reflect.TypeOf
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.gradle.ext.IdeaExtPlugin
import org.jetbrains.gradle.ext.ModuleSettings
import org.jetbrains.gradle.ext.PackagePrefixContainer

@Suppress("unused")
class LidaliaIdeaExtPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    project.plugins.apply(IdeaExtPlugin::class.java)
    val ideaModel = project.extensions.getByName("idea") as IdeaModel
    val ideaModelExt = ideaModel as ExtensionAware

    val defaultPackagePrefix = "${project.group}.${project.name}"

    ideaModel.setPackagePrefix(defaultPackagePrefix)

    ideaModelExt
      .extensions
      .add(
        object : TypeOf<Function1<String, Unit>>() {},
        "setPackagePrefix"
      ) { packagePrefix: String ->
        ideaModel.setPackagePrefix(packagePrefix)
      }
  }
}

private fun IdeaModel.setPackagePrefix(prefix: String) {

  val gradleProject = module?.project ?: project?.project

  val srcDirs = gradleProject?.sourceSets
    ?.flatMap { it.allSource.srcDirs }
    ?.map { it.relativeTo(gradleProject.projectDir).path }

  srcDirs?.forEach {
    packagePrefix[it] = prefix
  }
}

private val IdeaModel.packagePrefix: PackagePrefixContainer
  get() {
    val settings: ModuleSettings by (module as ExtensionAware).extensions
    val packagePrefix: PackagePrefixContainer by (settings as ExtensionAware).extensions
    return packagePrefix
  }
private val Project.sourceSets: SourceSetContainer?
  get() = try {
    (this as ExtensionAware).extensions.getByName("sourceSets") as SourceSetContainer
  } catch (e: UnknownDomainObjectException) { null }
