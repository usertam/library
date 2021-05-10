# Awesome Library
Awesome Library is a simple mockup of a library management system, with user control and database functions, written in Java and developed with sqlite-jdbc driver and other miscellaneous libraries.

## Features
The program provides search functions for library materials, and allows individuals to borrow and return books. Individuals may also check their own status to see currently borrowed books. \
As for librarians and administrators, the program handles the book transactions and provides control functions over the database, such as adding and deleting books and users, and even impersonating a user.

## Build
Note: To update from Gradle 6.6 and remove the deprecation warning, cherry pick the commit `1fc6f53`.
```
gradle build
```
