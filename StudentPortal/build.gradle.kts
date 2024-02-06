plugins {
	java
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.4"
	id("dev.hilla") version "2.5.5"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
	id("com.vaadin") version "24.3.3"
}

group = "com.sesc"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")
extra["hillaVersion"] = "2.5.5"
extra["vaadinVersion"] = "24.3.3"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.vaadin:vaadin-spring-boot-starter")
	implementation("dev.hilla:hilla-react-spring-boot-starter")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
}

dependencyManagement {
	imports {
		mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
		mavenBom("dev.hilla:hilla-bom:${property("hillaVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	project.property("snippetsDir")!!.let { outputs.dir(it) }
}

tasks.asciidoctor {
	project.property("snippetsDir")!!.let { inputs.dir(it) }
	dependsOn(tasks.test)
}
