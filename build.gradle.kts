import com.zelicraft.script.FFACommonPlugin
import com.zelicraft.script.FFAPublishPlugin

plugins {
    id("java")
}

repositories {
    mavenCentral()
}

subprojects {
    apply<FFACommonPlugin>()
    apply<FFAPublishPlugin>()

    version = rootProject.version

    repositories {
        mavenLocal()
        mavenCentral()
    }
}
