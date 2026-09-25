# MediLabo Solutions

MediLabo Solutions is an application developed as part of a training project to help doctors identify patients who may be at risk of developing type 2 diabetes.

The application is based on a microservices architecture developed with Java and Spring Boot. It manages patient information, medical notes, and diabetes risk assessment based on the patient's age, gender, and trigger terms found in medical notes.

## Architecture

The application is composed of five microservices:

| Service | Port | Role |
|---|---:|---|
| `front-service` | 8082 | User interface using Spring MVC and Thymeleaf |
| `gateway-service` | 8080 | API entry point and JWT validation |
| `patient-service` | 8081 | Patient information management |
| `notes-service` | 8083 | Medical notes management |
| `risk-service` | 8084 | Diabetes risk assessment |

Two databases are also used:

| Database | Host Port | Usage |
|---|---:|---|
| MySQL | 3307 | Patient administrative information |
| MongoDB | 27018 | Medical notes |

Simplified architecture:

```text
User
 |
 v
front-service : 8082
 |
 | JWT
 v
gateway-service : 8080
 |
 +------> patient-service : 8081
 |             |
 |             v
 |           MySQL
 |
 +------> notes-service : 8083
 |             |
 |             v
 |           MongoDB
 |
 +------> risk-service : 8084
               |
               +------> patient-service
               |
               +------> notes-service
```

The `risk-service` does not have its own database. It retrieves the required patient information and medical notes from the `patient-service` and `notes-service` through their REST APIs.

## Technologies

- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Data MongoDB
- Spring Security
- Spring Cloud Gateway
- JWT
- Thymeleaf
- MySQL 8
- MongoDB 8
- Maven
- JUnit
- Mockito
- Docker
- Docker Compose

## Features

The application allows users to:

- view the patient list;
- view a patient's record;
- add a patient;
- update patient information;
- view a patient's medical note history;
- add a new medical note;
- automatically assess a patient's diabetes risk level.

The user interface separates patient consultation from patient modification.

The **View** page displays the patient's information, medical notes, and diabetes risk level.

The **Edit** page is dedicated to updating the patient's administrative information.

## REST API

### Patient Service

Direct base URL:

```text
http://localhost:8081
```

Endpoints:

| Method | Endpoint | Description |
|---|---|---|
| GET | `/patients` | Get all patients |
| GET | `/patients/{id}` | Get a patient by ID |
| POST | `/patients` | Create a patient |
| PUT | `/patients/{id}` | Update a patient |

### Notes Service

Direct base URL:

```text
http://localhost:8083
```

Endpoints:

| Method | Endpoint | Description |
|---|---|---|
| GET | `/notes/patient/{patId}` | Get all notes for a patient |
| POST | `/notes` | Add a medical note |

### Risk Service

Direct base URL:

```text
http://localhost:8084
```

Endpoint:

| Method | Endpoint | Description |
|---|---|---|
| GET | `/risk/{id}` | Get the diabetes risk level for a patient |

The APIs are also accessible through the Gateway on port `8080`.

The Gateway routes `/patients/**`, `/notes/**`, and `/risk/**` require JWT authentication.

## Risk Assessment

The `risk-service` analyzes medical notes for the following trigger terms:

```text
Hémoglobine A1C
Microalbumine
Taille
Poids
Fumeur
Fumeuse
Anormal
Cholestérol
Vertiges
Rechute
Réaction
Anticorps
```

The assessment also takes the patient's age and gender into account.

The application returns four possible risk levels:

- `None`
- `Borderline`
- `InDanger`
- `EarlyOnset`

### Patients aged 30 or older

| Number of triggers | Risk level |
|---:|---|
| 0 to 1 | None |
| 2 to 5 | Borderline |
| 6 to 7 | InDanger |
| 8 or more | EarlyOnset |

### Male patients under 30

| Number of triggers | Risk level |
|---:|---|
| 0 to 2 | None |
| 3 to 4 | InDanger |
| 5 or more | EarlyOnset |

### Female patients under 30

| Number of triggers | Risk level |
|---:|---|
| 0 to 3 | None |
| 4 to 6 | InDanger |
| 7 or more | EarlyOnset |

## Data Storage

### MySQL

Patient administrative information is stored in the following database:

```text
medilabo_patient
```

The `patient` table contains the following main fields:

```text
id
first_name
last_name
birth_date
gender
address
phone
```

The `id` field is the primary key and uniquely identifies each patient.

The relational model is organized to respect Third Normal Form (3NF). The patient attributes depend directly on the primary key, and the model does not contain a transitive dependency between non-key attributes.

The four test patients required by the project are initialized using:

```text
docker/mysql/init.sql
```

### MongoDB

Medical notes are stored in:

```text
medilabo_notes
```

MongoDB is used because medical notes are text-based data with variable content and length that can naturally be represented as documents.

Each note contains the logical patient identifier (`patId`), which is used to retrieve the patient's medical note history.

The databases remain independent. There is no database foreign key between MySQL and MongoDB.

## Security

The user interface uses Spring Security with form-based authentication.

The application credentials are provided through environment variables.

The password used by Spring Security is encoded with BCrypt when the in-memory user is created.

For communication between the front-end and the Gateway, the front-end generates a JWT.

The Gateway acts as a Resource Server and validates the JWT before allowing access to:

```text
/patients/**
/notes/**
/risk/**
```

Secrets and credentials are not stored directly in the source code.

## Configuration

Create a `.env` file at the root of the project.

Example:

```env
MYSQL_ROOT_PASSWORD=change-me
JWT_SECRET=replace-with-a-long-secret-key
APP_USERNAME=user
APP_PASSWORD=change-me
```

The `.env` file contains sensitive information and must not be committed to Git.

## Running the Application with Docker

### Prerequisites

The following tools are required:

- Docker Desktop
- Docker Compose

When the application is run with Docker Compose, Java, Maven, MySQL, and MongoDB do not need to be installed locally to run the complete architecture.

### Build and Start the Application

From the project root directory, run:

```bash
docker compose up -d --build
```

This command builds the application images and starts the five microservices, MySQL, and MongoDB.

Check the status of the containers with:

```bash
docker compose ps
```

Once the services are running, the application is available at:

```text
http://localhost:8082
```

### Stop the Application

Run:

```bash
docker compose down
```

The MySQL and MongoDB data are persisted using Docker volumes:

```text
mysql-data
mongo-data
```

To stop the application and also remove the database volumes:

```bash
docker compose down -v
```

> **Warning:** this command deletes the data stored in the Docker volumes.

## Docker

Each microservice has its own `Dockerfile`.

### Multi-stage Builds

The Dockerfiles use multi-stage builds.

The first stage uses Maven and the JDK to build the application from its source code:

```text
Source code + Maven + JDK
          |
          v
         JAR
```

The second stage uses a JRE and copies only the generated JAR:

```text
JRE + JAR
```

This means that Maven, the Java compiler, and the application source code are not required in the final runtime image.

The application images can therefore be built directly from the project source without requiring a previous Maven build on the host machine.

### Docker Ignore Files

Each microservice contains a `.dockerignore` file with:

```text
target/
```

The local `target` directory is excluded from the Docker build context because the JAR is rebuilt directly inside the first stage of the Dockerfile.

This avoids sending unnecessary generated files to the Docker build context.

### Communication Between Containers

Docker Compose creates a network where containers can communicate using their service names.

For example:

```text
http://patient-service:8081
http://notes-service:8083
http://risk-service:8084
http://gateway-service:8080
```

Inside a container, `localhost` refers to that container itself. It does not refer to another microservice or to the host machine.

## Tests

The microservices contain automated tests that can be executed with Maven.

From the directory of the service to test:

```bash
mvn clean test
```

The `risk-service` includes tests for the diabetes risk assessment rules and their different thresholds.

The patient management service also contains tests for its repository, service, and controller layers.

## Green Code

The project applies several principles intended to reduce unnecessary processing and resource usage.

### Implemented Measures

#### Avoiding Unnecessary Inter-service Calls

The patient Edit page retrieves only the information required to update the patient.

Medical notes and risk assessment are not loaded on this page. They are retrieved only when the user opens the patient's View page.

This avoids unnecessary HTTP requests between microservices and unnecessary processing of data that is not displayed to the user.

#### Multi-stage Docker Images

Maven and the JDK are required to build the application but are not required to run it.

The multi-stage Dockerfiles separate the build environment from the runtime environment so that the final image contains only the elements required to execute the service.

#### Reduced Docker Build Context

The `.dockerignore` files exclude local `target/` directories.

Because each service is built from its source code inside Docker, these locally generated files do not need to be transferred to the Docker build context.

#### Separation of Responsibilities

Each microservice has a specific responsibility.

The `risk-service`, for example, does not duplicate the patient and medical note databases. It retrieves the information required for the assessment from the services responsible for these data.

### Possible Improvements

If the application had to support a larger number of patients and users, additional optimizations could be considered:

- implement pagination to avoid loading very large patient or note lists in a single request;
- measure database usage before adding indexes only where they are actually useful;
- adjust container CPU and memory resources according to measured requirements;
- reduce unnecessary production logging;
- monitor stored data and avoid unnecessary duplication;
- measure network calls and expensive operations before introducing further optimizations.

Green Code is not simply about writing fewer lines of code. The objective is to avoid unnecessary computation, network transfers, storage, and resource usage while preserving the required functionality.

## Project Structure

```text
medilabo-solutions/
|
+-- docker/
|   +-- mysql/
|       +-- init.sql
|
+-- patient-service/
|   +-- Dockerfile
|   +-- .dockerignore
|   +-- pom.xml
|   +-- src/
|
+-- notes-service/
|   +-- Dockerfile
|   +-- .dockerignore
|   +-- pom.xml
|   +-- src/
|
+-- risk-service/
|   +-- Dockerfile
|   +-- .dockerignore
|   +-- pom.xml
|   +-- src/
|
+-- gateway-service/
|   +-- Dockerfile
|   +-- .dockerignore
|   +-- pom.xml
|   +-- src/
|
+-- front-service/
|   +-- Dockerfile
|   +-- .dockerignore
|   +-- pom.xml
|   +-- src/
|
+-- docker-compose.yml
+-- .env
+-- .gitignore
+-- README.md
```

## Auteur

Projet réalisé dans le cadre d'une formation en développement web Java.