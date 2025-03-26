plugins {
    kotlin("js") version "1.8.10"
}

group = "com.veryphy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    js(IR) {
        browser {
            webpackTask {
                cssSupport {
                    enabled.set(true)
                }
            }
            runTask {
                cssSupport {
                    enabled.set(true)
                }
                devServer = devServer?.copy(
                    static = devServer?.static?.let { mutableListOf("$projectDir/src/main/resources") + it } as MutableList<String>?
                        ?: mutableListOf("$projectDir/src/main/resources"),
                    open = true,  // Open browser automatically
                )
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
            binaries.executable()
        }
    }

    sourceSets {
        val main by getting {
            dependencies {
                implementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:1.0.0-pre.590"))
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

                // Add CSS loader support
                implementation(npm("style-loader", "3.3.1"))
                implementation(npm("css-loader", "6.7.1"))
            }
        }
    }
}