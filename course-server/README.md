## Course-Server
Course Information Provider

### Structure
This is only a guard service of course information. It connects to a course sevice and a oauth service with http.

### Dependencies
- Spring MVC
- Spring Boot
- Spring Security

### Gradle
- appStart  : run server in production env
- appStop   : stop server in production env

### Packages
- exception  : spring exception handler
- interceptor: spring interceptor
- web        : spring controller
- service    : spring service
- config     : spring java config

### Resources
There are two environments for this project

- test : `application-test.yml`
- production : `application-production.yml`

You have to write your own production config. See `.example` file in resources root path


### API

This api server is protected by api token.

See https://github.com/NCU-CC/API-Documentation for further information