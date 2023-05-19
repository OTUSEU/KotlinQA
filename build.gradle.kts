//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
}
// для котлина 1.8.* рекомендуемая сборка >8
//distributionUrl=https\://services.gradle.org/distributions/gradle-7.5.1-bin.zip --> 8.1.1
group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
//библиотека kotlin-reflect выпускается jetbrains синхронно с kotlin
// желательно чтобы их версии совпадали
dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.21")

}

tasks.test {
    useJUnitPlatform()
}

//tasks.withType<KotlinCompile> {
//    kotlinOptions.jvmTarget = "1.8"
//}
