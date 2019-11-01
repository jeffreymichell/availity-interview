# Purpose

This Spring Boot service exposes 2 REST endpoints, both at `http://localhost:8080`.  
**There are also unit tests for the business logic exposed by each of these REST endpoints.**

### LISP Code Validation
`/validateLispCode` - a POST endpoint that expects a JSON body similar to:  
`{"lispCode":"(a*(b-c)*{d+e}+[r++1])"}`  _substitute your own LISP code for testing_

**Example using cURL:**  
`curl -k --request POST --header 'Content-Type: application/json' --data '{"lispCode":"(a*(b-c)*{d+e}+[r++1])"}' 'http://localhost:8080/validateLispCode'`

This endpoint returns "The LISP code passed validation" if all parentheses are opened and closed correctly, or "The LISP code failed validation" if not.

**NOTE:** The passed "lispCode" cannot be null, but _can_ be an empty string.

----
### Insurance Enrollment File Processing
`/processEnrollmentFile` - a POST endpoint that expects a JSON body similar to:  
`{"enrollmentFilePath":"C:/temp/enrollmentIn/insurance_enrollment_file.txt", "outputfilePath":"C:/temp/enrollmentOut/"}`  _Substitute your own input file path and output file directory path_

**Example using cURL:**  
`curl -k --request POST --header 'Content-Type: application/json' --data '{"enrollmentFilePath":"C:/temp/enrollmentIn/insurance_enrollment_file.txt", "outputfilePath":"C:/temp/enrollmentOut/"}' 'http://localhost:8080/processEnrollmentFile'`

This endpoint returns "File successfully processed" if successful, and writes individual output files to the directory indicated by "outputfilePath". 

## Project Run configuration
This project can be run from an IDE as a Maven project, or as a Spring Boot project.  
**It also has unit tests for the domain API methods.**

The included (bare-bones) Dockerfile can be used to `>docker build` the project (or this can be accomplished via Jenkins), and the result can be run in Docker Desktop.

##Spring Actuator
The Spring Actuator endpoints (with the exception of /shutdown and /beans) are available under /manage. For example:

http://localhost:8080/manage/health  - provides useful health check information  
http://localhost:8080/manage/loggers  - will show the configured logging level of all packages  

The below cURL command can be used to change the logging level of the running application (set to DEBUG in the application.properties file) to INFO, if you'd like less noise in the logs:  
`curl -i -X POST -H 'Content-Type: application/json' -d '{"configuredLevel": "INFO"}'  http://localhost:8080/manage/loggers/com.jeffreymichell.availityinterview`

## Design
This project was designed according to the principles of Hexagonal/Onion architecture, rather than the more common "layered" architecture that has become somewhat standard with Spring Web applications and services.

Each of the functional components (enrollment file processing and LISP code validation) are self-contained within their own packages. Each component has its own REST controller and its own business logic, with the business logic encapsulated behind a public interface (the FileProcessorFacade and LispCheckerFacade). The other classes and methods within each domain are package-private, "ensuring" that later developers don't create dependencies between separate domains.

The fact that each domain's functionality is encapsulated, and that each is contained within its own package structure means that, for instance, the LISP code validation functionality could be removed from the service by simply deleting the lispchecker package (and the associated test package), without **_any_** impact to the Enrollment File Processing functionality. 

With a little additional time (some refinement, some additional unit and integration tests), this application could be deployed into Kubernetes or some other container management framework as a stateless Microservice. Or, the `<packaging>` could be changed to "war", and the project could be deployed to Tomcat (or some other Java Application Server) as a simple, headless application. _My intention was to show more than just the implementation of a couple algorithms._

**NOTE:** This project started from an existing "Spring Boot headless service" template project, and I stripped out structure and dependencies that were not needed for this project.