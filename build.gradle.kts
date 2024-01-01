plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

val pluginGroup: String by extra
val pluginVersion: String by extra

val acfVersion = "0.5.1-SNAPSHOT"
val cfVersion = "3.42.0"
val paperVersion = "1.20.4-R0.1-SNAPSHOT"
val votifierVersion = "nuvotifier-2.7.3"
val luckpermsVersion = "5.4"

group = pluginGroup
version = pluginVersion

repositories {
    mavenCentral()
    flatDir { dirs("libraries") } // NuVotifier
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") } // Paper
    maven { url = uri("https://repo.aikar.co/content/groups/aikar/") } // ACF
}

dependencies {
    implementation("co.aikar:acf-paper:$acfVersion")
    implementation("org.checkerframework:checker-qual:$cfVersion")
    compileOnly("io.papermc.paper:paper-api:$paperVersion")
    compileOnly(":$votifierVersion") // https://github.com/NuVotifier/NuVotifier/issues/237
    compileOnly("net.luckperms:api:$luckpermsVersion")
}

tasks.shadowJar {
    relocate("co.aikar.commands", "de.stealwonders.epicsuite.acf")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(17)
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to version)
    }
}