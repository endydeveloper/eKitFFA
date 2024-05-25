plugins {
    `java-library`
}

repositories {
    mavenCentral()

    maven("https://repo.triumphteam.dev/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.unnamed.team/repository/unnamed-public/")
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    compileOnly(libs.spigot)
    compileOnly(libs.placeholderapi)

    compileOnly(libs.gson)

    compileOnly(libs.bundles.nmessage)
    compileOnly(libs.bundles.retrofit)
    compileOnly(libs.coreapi)

    api(libs.xseries)

    api(libs.triumphgui) {
        exclude(group = "com.google.code.gson", module = "gson")
    }
}


/*
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "es.endydev.core"
            artifactId = "core-spigot-api"
            version = rootProject.version.toString()
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}
 */