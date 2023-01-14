@file:Suppress("PackageDirectoryMismatch")

package uk.org.lidalia.gradle.plugin.ideaext.ideamodelextensions

import org.gradle.api.Project
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.gradle.plugins.ide.idea.model.IdeaModule
import org.jetbrains.gradle.ext.ModuleSettings
import org.jetbrains.gradle.ext.PackagePrefixContainer
import uk.org.lidalia.gradle.plugin.ideaext.projectextensions.getByType
import uk.org.lidalia.gradle.plugin.ideaext.projectextensions.getExtensionByType

internal val Project.ideaModel: IdeaModel get() = extensions.getByType()

internal val IdeaModule.moduleSettings: ModuleSettings get() = getExtensionByType()

internal val ModuleSettings.packagePrefixContainer: PackagePrefixContainer get() = getExtensionByType()
