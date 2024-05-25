rootProject.name = "eKitFFA"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

include("api", "plugin")
arrayOf("common", "v1_20_R1").forEach {
    include("versionsupport:$it")
}
