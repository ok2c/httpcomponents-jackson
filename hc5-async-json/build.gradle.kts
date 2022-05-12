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

val versions: Map<String, String> by project.extra
val releaseVersion: Boolean by project.extra

dependencies {
    api("org.apache.httpcomponents.core5:httpcore5:${versions["httpcore"]}")
    api("org.apache.httpcomponents.core5:httpcore5-h2:${versions["httpcore"]}")
    api("com.fasterxml.jackson.core:jackson-databind:${versions["jackson"]}")
    implementation("org.slf4j:slf4j-api:${versions["slf4j"]}")
    testImplementation("org.apache.httpcomponents.client5:httpclient5:${versions["httpclient"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${versions["junit-jupiter"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${versions["junit-jupiter"]}")
    testImplementation("org.assertj:assertj-core:${versions["assertj"]}")
    testImplementation("org.slf4j:slf4j-simple:${versions["slf4j"]}")
    testImplementation("org.mockito:mockito-inline:${versions["mockito"]}")
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
