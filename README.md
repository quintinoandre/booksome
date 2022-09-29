![image](https://user-images.githubusercontent.com/109354042/192764373-1b3b929b-8423-4af3-853c-59d3b507106f.png)
# BookSome 
The *-awesome-* restful API created by André Quintino, Ivan Moreira and Lygia Garrido as a project of the MindSwap Bootcamp.

___
### What is it? 

The API was created in Java and Spring Boot, connects to MongoDB and accesses an external API (Google Books API) to retrieve information to present the user. You can perform CRUD requests internally and GET externally.

- By using BookSome, users can:

  - find information of books by searching by title, author, category , and/or ISBN; 
  - save a favorite book;
  - save a book as read;
  - manage users and books (if ADMIN);
  - manage their personalized lists
___

### What is in it?
In the API you will find

- Spring Security features (authentication - *JWT token and refresh token* - and authorization)
- Docker-compose file (containing the necessary set up for the app to run)
- Connection to Google Books API
- Documentation with Swagger (localhost:8080/docs)
- Insomnia collection (which can be found in the path: resources/insomnia_collection)
- Cache using Redis

___

### Prerequisites

To run this project, you will need: 


 - a development tool (we used Intellij IDEA)
 
 - docker
 
 - docker-compose
 
 - git clone

___
 
 ### How to use it
 
```
-> fill environment variables in application properties file

-> docker-compose up -d

-> run the application

-> import the insomnia collection

-> perform the desired requests

```
___

### This is what you will find in Swagger
![Screenshot 2022-09-28 at 11 46 44](https://user-images.githubusercontent.com/109354042/192772049-905fc6b3-161b-421c-a0f4-474e1c549f65.png)
![Screenshot 2022-09-28 at 14 58 43](https://user-images.githubusercontent.com/109354042/192799316-4d469d68-0bf2-4d02-9ff8-a810972b07ec.png)
![Screenshot 2022-09-28 at 11 48 07](https://user-images.githubusercontent.com/109354042/192772041-c888ca2e-3c59-4f6d-a2a6-a9a72c58509c.png)








