rootProject.name = "rocket"

plugins {
    // add toolchain resolver, for class hotswapping
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}