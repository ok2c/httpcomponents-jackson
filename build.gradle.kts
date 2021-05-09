val versions = mapOf(
        "httpcore" to "5.0.4",
        "httpclient" to "5.0.3",
        "jackson" to "2.12.3",
        "slf4j" to "1.7.25",
        "junit-jupiter" to "5.7.1",
        "mockito" to "2.28.2",
        "assertj" to "3.15.0"
)

allprojects {
    group = "com.github.ok2c.hc5"
    version = "0.2.3-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    extra["releaseVersion"] = !version.toString().endsWith("-SNAPSHOT")
    extra["versions"] = versions
}

subprojects {
    tasks.withType<Javadoc> {
        (options as StandardJavadocDocletOptions).addBooleanOption("Xdoclint:-missing", true)
    }

    apply<MavenPublishPlugin>()

    configure<PublishingExtension> {
        val releaseVersion: Boolean by project.extra

        repositories {
            maven {
                val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"
                url = uri(if (releaseVersion) releasesRepoUrl else snapshotsRepoUrl)

                if (project.extra.has("ossrh.user")) {
                    credentials {
                        username = project.extra["ossrh.user"] as String
                        password = project.extra["ossrh.password"] as String
                    }
                }
            }
        }
    }
}
