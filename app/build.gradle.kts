import java.io.FileInputStream
import java.util.Locale
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlinx-serialization")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
    jacoco
}

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

jacoco {
    toolVersion = "0.8.4"
}

val exclusions = listOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "**/di/**",
    "**/ui/**",
)

android {
    namespace = "com.eldi.akubutuhbakso"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.eldi.akubutuhbakso"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        val realtimeDbUrl = localProperties.getProperty("FIREBASE_REALTIME_URL")
        buildConfigField("String", "FIREBASE_REALTIME_URL", "\"$realtimeDbUrl\"")

        val realtimeDbProjectId = localProperties.getProperty("FIREBASE_REALTIME_PROJECT_ID")
        buildConfigField("String", "FIREBASE_REALTIME_PROJECT_ID", "\"$realtimeDbProjectId\"")

        val realtimeDbHost = localProperties.getProperty("FIREBASE_REALTIME_HOST")
        buildConfigField("String", "FIREBASE_REALTIME_HOST", "\"$realtimeDbHost\"")
    }

    buildTypes {
        debug {
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    lint {
        baseline = file("lint-baseline.xml")
        abortOnError = false
    }
    secrets {
        propertiesFileName = "secrets.properties"
        defaultPropertiesFileName = "local.defaults.properties"
        ignoreList.add("sdk.*")
    }

    applicationVariants.all { variant ->
        // Extract variant name and capitalize the first letter
        val variantName = variant.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }

        // Define task names for unit tests and Android tests
        val unitTests = "test${variantName}UnitTest"

        // Register a JacocoReport task for code coverage analysis
        tasks.register<JacocoReport>("Jacoco${variantName}CodeCoverage") {

        }

        false
    }
}

tasks.withType(Test::class) {
//    useJUnitPlatform()
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
//        useJUnitPlatform()
    }
}

tasks.register("jacocoTestReport", JacocoReport::class) {
    // Depend on unit tests and Android tests tasks
    dependsOn("testDebugUnitTest")
    // Set task grouping and description
    group = "Reporting"
    description = "Execute UI and unit tests, generate and combine Jacoco coverage report"
    // Configure reports to generate both XML and HTML formats
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    // Set source directories to the main source directory
    sourceDirectories.setFrom(layout.projectDirectory.dir("src/main"))
    // Set class directories to compiled Java and Kotlin classes, excluding specified exclusions
    classDirectories.setFrom(files(
        fileTree(layout.buildDirectory.dir("intermediates/javac/")) {
            exclude(exclusions)
        },
        fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/")) {
            exclude(exclusions)
        }
    ))
    // Collect execution data from .exec and .ec files generated during test execution
    executionData.setFrom(files(
        fileTree(layout.buildDirectory) { include(listOf("**/*.exec", "**/*.ec")) }
    ))
}

tasks.register("jacocoTestCoverageVerification", JacocoCoverageVerification::class) {
    dependsOn("jacocoTestReport") // Ensure report is generated before verification
    group = "Verification"
    description = "Verify code coverage against defined thresholds"

    violationRules {
        rule {
            element = "CLASS" // Check coverage for each class
            limit {
                counter = "LINE" // Check line coverage
                value = "COVEREDRATIO" // Use coverage ratio (0.0 to 1.0)
                minimum = BigDecimal(0.80) // Set the threshold to 80%
            }
        }
    }

    classDirectories.setFrom(files(
        fileTree(layout.buildDirectory.dir("intermediates/javac/")) {
            exclude(exclusions)
        },
        fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/")) {
            exclude(exclusions)
        }
    ))
    executionData.setFrom(files(
        fileTree(layout.buildDirectory) { include(listOf("**/*.exec", "**/*.ec")) }
    ))
}

tasks.named("check") {
    dependsOn("jacocoTestCoverageVerification")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.navigation.compose)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.database)

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    // Maps
    implementation(libs.maps.compose)
    implementation(libs.play.services.location)

    // OkHttp
    implementation(libs.okhttp)

    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit.jupiter)

    lintChecks(libs.compose.lint.checks)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.bundles.mockk)
    testImplementation(libs.turbine)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
