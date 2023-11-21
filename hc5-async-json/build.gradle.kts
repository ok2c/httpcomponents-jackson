plugins {
    `java-library`
    `maven-publish`
    signing
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    withJavadocJar()
    withSourcesJar()
}

val releaseVersion: Boolean by project.extra

dependencies {
    api(libs.httpcore)
    api(libs.httpcore.h2)
    api(libs.jackson.databind)
    testImplementation(libs.httpclient)
    testImplementation(libs.slf4j.simple)
    testImplementation(libs.junit.engine)
    testImplementation(libs.junit.params)
    testImplementation(libs.mockito)
    testImplementation(libs.assertj.core)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenArtifacts") {
            from(components["java"])
            pom {
                name.set("JSON for Apache HC 5.0")
                description.set("Asynchronous JSON message processors for Apache HttpComponents 5.0")
                url.set("https://github.com/ok2c/httpcomponents-jackson")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("ok2c")
                        name.set("Oleg Kalnichevski")
                        email.set("olegk@apache.org")
                    }
                }
                scm {
                    connection.set("scm:git:git@github.com:ok2c/httpcomponents-jackson.git")
                    developerConnection.set("scm:git:ssh://github.com:ok2c/httpcomponents-jackson.git")
                    url.set("https://github.com/ok2c/httpcomponents-jackson")
                }
            }
        }
    }
}

if (releaseVersion) {
    signing {
        useGpgCmd()
        sign(publishing.publications["mavenArtifacts"])
    }
}
