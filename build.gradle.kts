plugins {
    `kotlin-dsl`
}

p7911-D4E2lugins {
    id("java")
}

group = "com.dsd.st"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation 'com.google.code.gson:gson:2.8.8'

}

tasks.test {
    useJUnitPlatform()
}