package uk.org.lidalia.gradle.plugin.ideaext

import io.kotest.core.spec.style.StringSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.collections.shouldBeEmpty
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome.FAILED
import org.intellij.lang.annotations.Language

/**
 * A simple functional test for the 'uk.org.lidalia.ideaext' plugin.
 */
class LidaliaIdeaExtPluginFunctionalTest : StringSpec({

  val tempFolder = tempdir()

  fun getProjectDir() = tempFolder

  fun getBuildFile() = getProjectDir().resolve("build.gradle.kts")

  fun getSettingsFile() = getProjectDir().resolve("settings.gradle.kts")

  "can apply plugin" {
    // Setup the test build
    getSettingsFile().writeText("")
    @Language("kotlin")
    val buildFile = """
      plugins {
//        kotlin("jvm") version "1.7.22"
        id("uk.org.lidalia.ideaext")
      }
      
      idea {
        setPackagePrefix("com.example.foo")
      }
    """.trimIndent()
    getBuildFile().writeText(
      buildFile,
    )

    // Run the build
    val result = GradleRunner.create()
      .forwardOutput()
      .withPluginClasspath()
      .withArguments("--info", "build")
      .withProjectDir(getProjectDir())
      .build()

    val failedTasks = result.tasks.filter { it.outcome == FAILED }
    failedTasks.shouldBeEmpty()
  }
})
