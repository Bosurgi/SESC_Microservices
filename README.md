# SESC - Microservices

This is the project for SESC.
The project contains several microservices, including Student, Library and Finance portal.
As well as a Discovery Server and the API Gateway.
The Microservices are also Dockerized in independent containers.

## Table of Contents

- [About the Project](#about-the-project)
- [Technologies Used](#technologies-used)
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Book Library](#booklibrary)

## About the Project

The project focuses on the development of Web Applications communicating with each other using RESTful APIs.
The three platforms available allow a student to Register, Enroll in different courses, borrow books and pay eventual fines or fees.

![image](https://github.com/Bosurgi/SESC_Microservices/assets/87176210/941bf2d9-eb09-4dd6-b80e-8d8b9c8a9f6c)


## Technologies Used

List the technologies/frameworks/languages used in the project.

- Java
- Spring Boot
- Spring Cloud
- Docker
- React
- PostgreSQL
- REST
- Thymeleaf
- Tailwind
- Mockito
- Maven
- Gradle

## Features

Highlight the main features of the application.

### Student Portal
- Register Student
- Register Students in the other microservices
- View and Update Student Profile
- View and Enrol in courses
- See Invoice reference numbers
- Check Graduation Status

### Library Portal
- Student Login with default password
- First time login password change
- Borrow Books
- Return Books
- Admin Functionalities
- Adding new Books
- Search Books by ISBN through Google Library API

## Getting Started

### Prerequisites

List any prerequisites or system requirements needed to run the project.

- Java 11 or higher
- Maven
- Gradle
- Docker

### Installation

Before dockerizing the application, compile the JAR file.
- Using Gradle this can be done from the right hand side menu of your IDE if using IntelliJ by clicking on Gradle -> Build
- Using Maven this can be done from the right hand side menu of your IDE if using IntelliJ by clicking on Maven -> Install

Once compiled run ```docker compose up -d``` in the same folder of each microservice.
The JAR file will be copied and used to build the image for the container.

The container's defaul network is run by the Discovery Server, hence run this first.

# Book Library
This project uses Google Library API for Admin to add books.
If you want to test such features, create a new file named ```key.properties``` in the Resource folder.
This file will contain you API key to be used and it will have the following format:

```library.key=YOUR_KEY_HERE```

