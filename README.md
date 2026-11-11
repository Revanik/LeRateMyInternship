# LeRateMyInternship

**Team:** Lebron

**Members:**
Ryan Evanik, Owen Latt, Aaron Zheng, Liana Karim, Luke Simpson


We plan to build our system using Java 25 and Spring Boot for the backend, Thymeleaf with HTML, CSS, and JavaScript for the frontend, and PostgreSQL for the database. One thing we are unsure about is how to verify that reviewers’ work email addresses are legitimate and confirm that they actually worked at the company they are reviewing.

Software Needed
1. Git: 

   a. Check with [git --version] in the command prompt

   b. Should get git version 2.4X.X.

   c. Visit https://git-scm.com/install/ if needed

3. JDK (Java):

   a. Check with [java --version]

   b. Should get java 25...

   c. Visit https://www.oracle.com/java/technologies/javase/jdk25-archive-downloads.html if needed

5. Postgres

   a. Check with [psql --version] or try to open SQL Shell (psql)

   b. Should get psql 18.X

   c. Visit https://www.postgresql.org/download/ if needed

Database Setup
1. Open SQl Shell (psql)
2. Click enter until you get to Password for user postgres:
3. Enter the superuser password
4. Run CREATE USER internships WITH PASSWORD 'internships';
5. CREATE DATABASE internships OWNER internships;

To get Postgres in VS Code (Not needed but just nice for visualization)
1. Open the extensions panel and install SQLTools and SQLTools PostgreSQL/Cockroach Driver
2. Click the SQLTools icon in the sidebar and "Add New Connection"
3. Select Postgres
4. Fill in Connection Name: LeRateMyInternship, Server: localhost, Port: 5432, Database: postgres, Username: postgres, and Password SQLTools Driver Credentials. Enter the superuser password after connecting

Skeleton: (Subsystem and folder)
Account Management -> account
Review and Rating Management -> review
Data Management -> internship
Searching and Filtering -> search
Moderation and Verification -> moderation
User Interface controllers, shared code -> common

How the project is organized. Everything lives under src/. The backend is Java in src/main/java/internshipnetwork/, split into packages that match our subsystems (account, review, internship, search, moderation, and common).

Each package holds the controllers (which handle web requests), services (the business logic) and repositories (which read and write the database) for that feature. Each package contains everything its feature needs, and not much else. 

The frontend is in src/main/resources/: Thymeleaf HTML pages go in templates/, and plain CSS and JavaScript go in static/css/ and static/js/. A controller returns a template’s name, and Spring fills it in with data and sends it to the browser. 

The database is PostgreSQL, running on your own machine. Its connection settings are in src/main/resources/application.yml, and its tables are created by numbered SQL files in src/main/resources/db/migration/ (V1__...sql, V2__...sql), which run automatically when the app starts. 

Tests go in src/test/java/, mirroring the main folder structure. 

The build is configured in pom.xml and run with the included Maven wrapper (mvnw), so nobody needs to install Maven.

Diagram:
![DeploymentDiagram](images\DeploymentDiagram.png)