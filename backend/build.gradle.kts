import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0" // Kotlin 버전 확인
    kotlin("plugin.jpa") version "1.8.0" // Kotlin-JPA 플러그인 추가
    kotlin("plugin.spring") version "1.8.0"
    id("org.springframework.boot") version "3.4.2" // Spring Boot 플러그인
    id("io.spring.dependency-management") version "1.1.7" // Spring Dependency Management 플러그인
}

group = "com.ll"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21)) // Java 21 버전 설정
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get()) // annotationProcessor 의존성 추가
    }
}

repositories {
    mavenCentral() // Maven 중앙 저장소 사용
}

dependencies {
    // Spring Boot Starter 의존성
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // JPA
    implementation("org.springframework.boot:spring-boot-starter-security") // Spring Security
    implementation("org.springframework.boot:spring-boot-starter-web") // 웹 애플리케이션
    implementation("org.springframework.boot:spring-boot-starter-webflux") // WebFlux
    implementation("org.springframework.boot:spring-boot-starter-validation") // Validation

    // JWT 관련 의존성
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Lombok 관련 의존성
    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    // 개발 관련 의존성
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // DB 관련 의존성
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")

    // 테스트 관련 의존성
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // 추가적인 라이브러리
    implementation("org.apache.commons:commons-text:1.10.0")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client") // OAuth2 클라이언트
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0") // Swagger
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.0") // Jackson JSR310
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")

    // Kotlin 관련 의존성
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.0")
}

tasks.named<Test>("test") {
    useJUnitPlatform() // JUnit 플랫폼을 사용하여 테스트 실행
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}
