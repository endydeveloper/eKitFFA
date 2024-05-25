import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    alias(libs.plugins.bukkitPlugin)
    alias(libs.plugins.shadow)
}

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://repo.bg-software.com/repository/api/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.unnamed.team/repository/unnamed-public/")
    maven("https://repo.triumphteam.dev/snapshots/")
    maven("https://jitpack.io")
    maven("https://repo.triumphteam.dev/snapshots/")
    maven("https://repo.glaremasters.me/repository/concuncan/")
    maven("https://repo.andrei1058.dev/releases/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.viaversion.com")
    maven("https://repo.alessiodp.com/releases/")
}
dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.lombok)
    compileOnly(libs.vault)
    implementation("ch.qos.logback:logback-classic:1.4.12")
    compileOnly("net.luckperms:api:5.4")

    implementation("net.kyori:adventure-text-minimessage:4.16.0")

    compileOnly("net.leonardo_dgs:InteractiveBooks:1.7.2")

    compileOnly(libs.placeholderapi)
    compileOnly(libs.viaversion)
    compileOnly(libs.protocollib)

    implementation(project(":api"))
    implementation(libs.inject)
    compileOnly(libs.fastboard)
    implementation(libs.triumphcmd)
    compileOnly(libs.anvilgui)
    compileOnly(libs.coreapi)

    implementation("net.kyori:adventure-text-serializer-legacy:4.16.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.2")
    compileOnly("com.squareup.okhttp3:okhttp:5.0.0-alpha.12")
    compileOnly(libs.gson)
    implementation(libs.hikaricp)
    compileOnly(libs.bundles.nmessage)
    implementation(libs.jackson)

    compileOnly("org.apache.commons:commons-lang3:3.14.0")

    // bukkit is already in the server. we don't need to shade it.
    compileOnly(libs.bossbarapi) {
        exclude(group = "org.bukkit", module = "bukkit")
    }

    arrayOf("common","v1_20_R1").forEach {
        runtimeOnly(project(":versionsupport:$it"))
    }

    testImplementation(libs.bundles.junit)

    annotationProcessor(libs.lombok)
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<Test> {
        useJUnitPlatform()
    }

    named("build") {
        dependsOn(named("shadowJar"))
        //dependsOn(named("copyJar"))
    }

    withType<ShadowJar> {
        val pkg = "me.endydev.ffa.internal"
        val version = project.version.toString()
        archiveFileName.set("eKitFFA-$version.jar")
    }
}

bukkit {
    main = "me.endydev.ffa.FFAPlugin"
    apiVersion = "1.13"
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    version = "${rootProject.version}"
    authors = listOf("endydev")
    description = "FFA for Zelicraft Network"
    name = "eKitFFA"
    depend = listOf("ZC-Core")
    softDepend = listOf("ProtocolLib", "LuckPerms", "PlaceholderAPI")
}