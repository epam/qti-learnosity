version = "0.2.0"

plugins {
    // Apply the groovy plugin to also add support for Groovy (needed for Spock)
    groovy
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

val lombokVersion = "1.18.30"
val jaxbVersion = "4.0.4"
val jakartaBindApiVersion = "4.0.1"
val gsonVersion = "2.10.1"
val guavaVersion = "33.4.0-jre"
val logbackVersion = "1.5.15"
// tests
val groovyVersion = "3.0.20"
val spockVersion = "2.3-groovy-3.0"
val jacksonVersion = "2.16.0"

dependencies {
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    compileOnly("ch.qos.logback:logback-classic:$logbackVersion")

    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:$jakartaBindApiVersion")
    implementation("com.sun.xml.bind:jaxb-impl:$jaxbVersion")
    implementation("com.sun.xml.bind:jaxb-core:$jaxbVersion")
    implementation("com.google.guava:guava:$guavaVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // TESTS
    testImplementation("org.codehaus.groovy:groovy:$groovyVersion")
    testImplementation("org.codehaus.groovy:groovy-json:$groovyVersion")
    testImplementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")
    testImplementation("org.spockframework:spock-core:$spockVersion")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

val mainClassName = "com.epam.learnosity.converter.qti.App"

tasks.jar {
    // Define the base name for the JAR file
    archiveBaseName.set(rootProject.name)
    manifest {
        attributes(
            "Main-Class" to mainClassName,
            "Implementation-Version" to version
        )
    }
}

tasks.register<Jar>("fatJar") {
    group = "Distribution"
    description = "Creates a fat Jar including all runtime dependencies."
    archiveBaseName.set("${rootProject.name}-fat")
    manifest {
        attributes("Main-Class" to mainClassName, "Implementation-Version" to version)
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
