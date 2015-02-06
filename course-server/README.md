## Course-Server
Course Information Provider

### Dependencies
- spring-webmvc 4.1.1
- spring-security 4.0.0.M2
- openid-consumer 0.0.1 ( https://github.com/NCU-CC/OpenID-Consumer )
- oauth-resource 0.1.5  ( https://github.com/NCU-CC/OAuth-Service )
- oauth-data 0.1.5      ( https://github.com/NCU-CC/OAuth-Service )

### Gradle
- jettyStart : run embedded server
- jettyStop  : stop embedded server above

### Resources
resources are divided into two environments for Spring

- develope : include embedded database and mocked elements

- production : put following files into **src/main/resources/production**
    - remote.properties
    ```
        remote_course_service = https://localhost/course/api/v0/
        remote_token_service  = https://localhost/oauth/management/v1/token/string/
    ```
