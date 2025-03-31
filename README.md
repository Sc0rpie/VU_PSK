# University Management System

A Java EE application for managing university students, clubs, and courses using WildFly.

## Prerequisites

- Java 11+
- WildFly 26+
- Maven 3.8+
- MySQL 8.0+ or compatible database

## Setup Database

1. Create a database named `university_db`
2. Create a database user or use an existing one
3. Configure the datasource in WildFly:

Add the following to `standalone.xml` in the datasources section:

```xml
<datasource jndi-name="java:jboss/datasources/UniversityDS" pool-name="UniversityDS">
    <connection-url>jdbc:mysql://localhost:3306/university_db</connection-url>
    <driver>mysql</driver>
    <security>
        <user-name>username</user-name>
        <password>password</password>
    </security>
</datasource>
```

Make sure to add the MySQL driver to WildFly if not already included.

## Build and Deploy

1. Build the project:
   ```
   mvn clean package
   ```

2. Deploy to WildFly:
   - Copy the generated WAR file from `target/university-app.war` to `$WILDFLY_HOME/standalone/deployments/`
   - Or use the WildFly management console to deploy the application

3. Access the application:
   ```
   http://localhost:8080/university-app/
   ```

## Features

- Student management
- Club management
- Course enrollment
- Demonstrates both JPA/ORM and MyBatis data access methods

## Database Structure

The application uses three main entities with relationships:

1. **Student**: Represents university students
2. **Club**: Represents student clubs/organizations
3. **Course**: Represents academic courses

### Relationships:
- Student-Club: Many-to-Many (a student can join multiple clubs, and a club can have multiple students)
- Student-Course: Many-to-Many (a student can enroll in multiple courses, and a course can have multiple students)

## Technical Notes

### ORM/JPA vs MyBatis

The application demonstrates both approaches:

- **JPA/Hibernate**: Object-Relational Mapping that handles relationships and SQL generation automatically
- **MyBatis**: SQL Mapper that gives more control over SQL queries with less overhead

#### When to use JPA:
- Complex object graphs and relationships
- When you want the framework to manage relationships
- When you prefer working with objects over SQL

#### When to use MyBatis:
- Performance-critical operations
- Complex queries that are difficult to express in JPQL/HQL
- When you need more control over SQL execution
