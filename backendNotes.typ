= scala config API (play)
*Dependency Injection*
- design pattern
- use of \@inject 
  - guice simplifies this by reducing the need to write java factories
    - creating objects without specifying their exact classes
- generates a router
- builtin components describe dependencies for an instance of application

*configuration api*
- used to set settings for the runtime 


*actions*
- takes in a request
- returns a result

*controllers*
- action generators
- an object that generates action values 
- two main parent types:
  - BasicController
    - lower level
  - AbstractController
    - abstracted BasicController
    - easier to use

*routes*
- describe relationships between url and controllers (which generate actions)

_basic backend model_
- the frontend makes calls to different routes
  - the backend server returns different responses via actions

*HTTP routing + the builtin one*
- HTTP request (seen as event by the MCV framework)
  - request path
  - HTTP method
    - GET
    - POST
    - PUT
    - DELETE
    - PATCH
    - HEAD
    - OPTIONS
    - CONNECT
    - TRACE


_fast forward a few days_

*Accessing the database from PLAY*
- options
  - JDBC
    - lowest level of abstraction
    - base library for db access in java + derivative languages
    - raw SQL
  - slick
    - functional relational mapping library 
    - strongly typed
    - has play integration 
  - doobie
    - pure functional layer
    - low level esque 

- im going to use  *JDBC* because its raw sql and i should learn how to query sql and stuff
