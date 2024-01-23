The project is a springboot project developed in Intellij ultimate with Java 17 and uses a local MySQL 8 database which needs to be set up. 
Simply create a local database and then make a copy of the application-template.properties file, name it application.properties and follow the instructions from the template.

Run the project once to create the tables and releationships between them. Then insert a user with admin acces in the user table. 
Set the username to whatever you want but use this password:`$2a$10$3svT6hNcgJ8wxKuD6FJd1uT0CsLAItE/ufaQLAchQbKI1glpug9Ua`

Reload the project and you can now login with your choosen username and the password __admin__. It is highly recommended to create a new user with admin access and a stronger password straight away and then delete this user.

Bellow is a diagram over the database and its reletionships.

![db_releationships](https://github.com/adrian-jonsson-sjoedin/ID1212-Labb-and-Project/assets/51127381/ac1285b6-6b23-4dad-aa3c-03c45a476d2c)
