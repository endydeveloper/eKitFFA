package com.zelicraft.script

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType

class FFACommonPlugin : Plugin<Project> {
    override fun apply(target: Project) = target.configure()

    private fun Project.configure() {
        project.plugins.apply("java")

        project.tasks.apply {
            withType<JavaCompile>().configureEach {
                options.encoding = "UTF-8"
                options.compilerArgs.add("-parameters")
            }

            configure<JavaPluginExtension> {
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(8))
                }
            }
        }

        project.repositories {
            mavenLocal()
            mavenCentral()
        }
    }
}