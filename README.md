# Marcura Technical Task

*Remaining Tasks:*
* Admin UI Pages - admin endpoint can be seen here http://localhost:8080/admin/audit?page=0 (admin/password)
* Ensure URLs older than 6 months are not returned
* Add angular tests
* Improve error handling in UI, `replace console.warn(...)`
* Dockerize with real dbms

## Service

From inside `./service` directory.  

Spring boot app running on port 8080.
Uses embedded postgres database which starts from blank when app starts.

Build and run:
```
./mvnw clean package
./mvnw spring-boot:run
```

## UI

From inside `./ui` directory. 

Angular app running on port 4200.

Build and run:
```
yarn install
yarn start
```

Start generating URLs here: http://localhost:4200/


