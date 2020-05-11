# Payment system
Graduate project for programming course "Java developer" (Programming school TeachMeSkills)<br/>
Date of defence: 31.03.2020

### Functionality:
##### 1. User can:
* register in the system
* create, remove wallets and replenish the balance
* transfer money to another personal wallet with a currency conversion function
* make a deposit and get it back
* get and repay a loan
* get fresh currency exchange rates

##### 2. Admin can:
* CRUD operations with users, type of deposits and credits
* search deposits, credits, transfer details by date, id, user etc.
* send messages to email (fresh currency exchange rates, notification about blocking)

##### 3. Additionally:
* authentication and authorization (JWT)
* automatic update status of deposits and credits (Scheduler task)
* exchange rates from website https://myfin.by/converter (Java HTML parser - Jsoup)
* sending messages – JavaMailSender
* simple unit tests

### Tools and Technologies:
* **Technology:** Java, Spring Boot, Spring Data JPA, Spring Security, JWT, Maven, JUnit, Slf4j, Apache Tomcat
* **Database:** MySQL

