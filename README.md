## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Featues](#features)
## Gift Certificates system 
This training project was build in [EPAM Java Lab](https://github.com/mjc-school/MJC-School/tree/old/stage%20%233/java). 
The purpose was to learn building REST API using all aspects of Spring framework, host it on Docker and integrate with CI/CD pipeline.
## Technologies
* Java 17
* Spring boot 3
* MySQL 8.0
## Setup
Just clone this repository and run main file [AppLauncher.java](https://github.com/danPr0/epam_lab_module_4/blob/master/src/main/java/com/epam/esm/AppLauncher.java)
## Features
* Authentication and authorization is done with JWT. Users can log in with their Google and Github accounts through OAuth2 and OpenId Connect.
* User get email letters to confirm registration and reset password.
* Gift Certificates query with multiple search, sort and pagination
* Support HATEOAS on REST endpoints.
* Properties are encrypted using Jasypt
* AWS X-Ray tracing and AWS CodeGuru profiling