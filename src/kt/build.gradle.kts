import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    kotlin("jvm") version "2.0.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
sourceSets {
    val main by getting {
        java.setSrcDirs(listOf("com")).exclude("**/*Test.kt")
    }
    val test by getting {
        java.setSrcDirs(listOf("com")).include("**/*Test.kt")
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()

   testLogging {
    showStandardStreams = true
    exceptionFormat = TestExceptionFormat.FULL
    events = setOf(
        TestLogEvent.FAILED,
        TestLogEvent.PASSED,
        TestLogEvent.SKIPPED,
        TestLogEvent.STANDARD_OUT
    )
   }
}