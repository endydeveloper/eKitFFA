plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

gradlePlugin {
    plugins {
        register("ffa.common") {
            id = "ffa.common"
            implementationClass = "com.zelicraft.script.ZelicraftCommonPlugin"
        }
    }
}

kotlin {
    jvmToolchain(8)
}