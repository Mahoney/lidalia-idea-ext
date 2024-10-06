@file:Suppress("PackageDirectoryMismatch")

package uk.org.lidalia.gradle.plugin.ideaext.projectextensions

import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.tasks.SourceSetContainer

internal val Project.sourceSets: SourceSetContainer? get() =
  try {
    getExtensionByType()
  } catch (e: UnknownDomainObjectException) {
    null
  }

internal inline fun <reified T> ExtensionContainer.getByType(): T = getByType(T::class.java)

internal inline fun <reified T> Any.getExtensionByType(): T =
  (this as ExtensionAware).extensions.getByType()
