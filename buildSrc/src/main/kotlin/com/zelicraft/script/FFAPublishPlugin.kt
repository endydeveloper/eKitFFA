package com.zelicraft.script

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.*

class FFAPublishPlugin : Plugin<Project> {

    override fun apply(target: Project) = target.afterEvaluate {
        configure()
    }

    private fun Project.configure() {
        val version = project.version.toString().toLowerCase()

        apply<JavaBasePlugin>()
        apply<MavenPublishPlugin>()

        extensions.configure<PublishingExtension> {
            repositories {

            }


            publications {
                create<MavenPublication>("maven") {
                    artifactId = project.name
                    from(components["java"])
                }
            }
        }
    }
}