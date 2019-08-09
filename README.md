Library System
=====================

Task #16:
-----------------------------------

Library System
 
Create a catalogue where user can search by:

    • The author (one of the group).
    • The name of the book or its fragment.
    • One of the book’s keywords (book attribute).
The catalog of books is filled in by the Administrator, adding and modifying / deleting them. Each book should have an address (shelf place) or reader. The reader to take the book have to register, leaving an e-mail and a phone number. The book can be taken from the Administrator in the library for a period of not more than a month,
only if the book is available in the library. The administrator must have a page where the books taken and the readers who use the book are displayed.
### Requirements
    • JDK 1.8 or higher
    • MySQL 8.0.15 or higher
### Installation
* Clone project from GitHub (git clone https://github.com/)
* Go to /src/main/resources/DBConnectionConfig.properties and fill in connection parameters to your database (url/login/password). 
Please, be sure that mysql user filled in property file has enough rights for creating and changing databases.
* Execute script /src/main/resources/schema.sql to create database schema
* Execute script /src/main/resources/data.sql to populate database with demo data
    
### Running
* cd to root project folder and execute command *mvnw clean tomcat7:run*
* After server start, application will be available by URL [http://localhost:8080/](http://localhost:8080/) 
* Use login "**admin@gmail.com**" and password "**officer**" to log in with administrator rights.
* Use login "**borrower@gmail.com**" and password "**borrower**" to log in as user.