About
--

Application has been written using the following:
- Dropwizard

- JUnit 5
- Mockito
- MockServer
- Maven

Java version required: 17

Time Taken: ~6h

Running the Application test
--
> mvn test

com.pm.AppTest - Integration tests that runs against actual server.

The rest of the tests are component tests, where dependencies are mocked.

Running the Application
--
Application Entry point is com.pm.App

Launch the application  using the following command:

Package the application with command:
> mvn package

Run the created jar:
> java -jar target/commissions-ws-1.0-SNAPSHOT.jar server target/classes/application.yaml
 
You will see the following after it starts up successfully
```
org.eclipse.jetty.server.AbstractConnector: Started application@ab20d7c{HTTP/1.1, (http/1.1)}{0.0.0.0:8080}
org.eclipse.jetty.server.AbstractConnector: Started admin@474fae39{HTTP/1.1, (http/1.1)}{0.0.0.0:8081}
```


