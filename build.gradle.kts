allprojects {
    group = "com.github.ok2c.hc5"
    version = "0.3.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    extra["releaseVersion"] = !version.toString().endsWith("-SNAPSHOT")
}

subprojects {
    tasks.withType<Javadoc> {
        (options as StandardJavadocDocletOptions).addBooleanOption("Xdoclint:-missing", true)
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:deprecation")
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
