package uk.org.lidalia.gradle.plugin.ideaext

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.testfixtures.ProjectBuilder
import uk.org.lidalia.gradle.plugin.ideaext.ideamodelextensions.ideaModel
import uk.org.lidalia.gradle.plugin.ideaext.ideamodelextensions.moduleSettings
import uk.org.lidalia.gradle.plugin.ideaext.ideamodelextensions.packagePrefixContainer

/**
 * A simple unit test for the 'uk.org.lidalia.ideaext' plugin.
 */
class LidaliaIdeaExtPluginTest : StringSpec({

  val project = ProjectBuilder.builder().withName("somename").build()
  project.group = "com.example.somegroup"
  project.plugins.apply("org.jetbrains.kotlin.jvm")
  project.plugins.apply("uk.org.lidalia.ideaext")

  val ideaModel = project.ideaModel
  val packagePrefixContainer = ideaModel.module.moduleSettings.packagePrefixContainer

  "plugin defaults package prefix to module name" {

    packagePrefixContainer shouldBe mapOf(
      "src/main/resources" to "com.example.somegroup.somename",
      "src/main/java" to "com.example.somegroup.somename",
      "src/main/kotlin" to "com.example.somegroup.somename",
      "src/test/resources" to "com.example.somegroup.somename",
      "src/test/java" to "com.example.somegroup.somename",
      "src/test/kotlin" to "com.example.somegroup.somename",
    )
  }

  "plugin can set package prefix" {
    val ideaModelExt = ideaModel as ExtensionAware

    @Suppress("UNCHECKED_CAST")
    val packagePrefix = ideaModelExt.extensions.findByName("packagePrefix") as Property<String>
    packagePrefix.set("com.example.somethingelse")

    packagePrefixContainer shouldBe mapOf(
      "src/main/resources" to "com.example.somethingelse",
      "src/main/java" to "com.example.somethingelse",
      "src/main/kotlin" to "com.example.somethingelse",
      "src/test/resources" to "com.example.somethingelse",
      "src/test/java" to "com.example.somethingelse",
      "src/test/kotlin" to "com.example.somethingelse",
    )
    packagePrefix.get() shouldBe "com.example.somethingelse"
  }
})
