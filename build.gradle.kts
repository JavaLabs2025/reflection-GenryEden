plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

sourceSets {
    create("processor") {
        java.setSrcDirs(listOf("src/main/java"))
        resources.setSrcDirs(listOf("src/main/resources"))
    }
}

tasks.named<JavaCompile>("compileProcessorJava").configure {
    source = fileTree("src/main/java") {
        include("org/example/generator/GeneratableProcessor.java")
    }
}

tasks.named<JavaCompile>("compileJava").configure {
    dependsOn("compileProcessorJava", "processProcessorResources")
    options.annotationProcessorPath = files(
        sourceSets.getByName("processor").output,
        configurations.getByName("processorCompileClasspath")
    )
}