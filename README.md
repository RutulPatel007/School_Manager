# SchoolManager

## Overview
**SchoolManager** is a database-driven application designed to implement a School Management System using **Java** and **JDBC**. It follows a **layered architecture**, ensuring separation of concerns and maintainability. The system includes modules for managing students, teachers, courses, and libraries, alongside database operations.

---

## Features
- **Database Integration**: Uses **JDBC** for seamless database operations.
- **Layered Architecture**: Includes DAO, Service, and Entity layers for modularity.
- **Scalability**: Designed to handle growing data and traffic.
- **Test-Driven**: Includes unit and integration tests to ensure quality.
- **Extensible Design**: Easily adaptable to add new features.

---

## Installation

### Prerequisites
- Install **Java Development Kit (JDK)** version 8 or later.
- Set up a relational database like **MySQL** or **PostgreSQL**.

### Installation Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/varnit-mittal/SchoolManager
   cd SchoolManager/imt2022025_JDBC_Project/
    ```
### Configure the Database:

1. Configure SQL Server:
```bash
sudo mysql -u root
SELECT User,Host FROM mysql.user;
DROP USER 'root'@'localhost'; 
INSTALL PLUGIN validate_password SONAME 'validate_password.so';
SET GLOBAL validate_password_length = 5; 
SET GLOBAL validate_password_number_count = 0; 
SET GLOBAL validate_password_mixed_case_count = 0; 
SET GLOBAL validate_password_special_char_count = 0; 
SET GLOBAL validate_password_policy = LOW;
CREATE USER 'root'@'%' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION; 
```

2. Execute the SQL scripts in the `sql/` folder:
   ```bash
   mysql -u root -p
   admin
   SOURCE /path to schem.sql in sql/ folder of the project 
   ```
3. Run the application locally
   ```bash
   export CLASSPATH=$CLASSPATH:./mysql-connector-j-8.3.0.jar:./junit-jupiter-engine-5.9.3.jar:./junit-jupiter-api-5.9.3.jar
   javac -classpath "./apiguardian-api-1.1.2.jar:./junit-jupiter-api-5.9.3.jar:./junit-jupiter-engine-5.9.3.jar:./junit-platform-console-standalone-1.11.3.jar:./mysql-connector-j-8.3.0.jar" -d . src/com/schoolmanagement/**/*.java
   java com.schoolmanagement.main.Main
   ```
4. To test the application using junit
```bash
export CLASSPATH=$CLASSPATH:./mysql-connector-j-8.3.0.jar:./junit-jupiter-engine-5.9.3.jar:./junit-jupiter-api-5.9.3.jar
javac -classpath "./apiguardian-api-1.1.2.jar:./junit-jupiter-api-5.9.3.jar:./junit-jupiter-engine-5.9.3.jar:./junit-platform-console-standalone-1.11.3.jar:./mysql-connector-j-8.3.0.jar" -d . src/com/schoolmanagement/**/*.java
java -jar ./junit-platform-console-standalone-1.11.3.jar -cp .:./mysql-connector-j-8.3.0.jar --scan-class-path
```

## Contributors

We would like to thank the following contributors for their valuable input and contributions to this project:

- **[Varnit Mittal](https://github.com/varnit-mittal)** 
- **[Aditya Priyadarshi](https://github.com/ap5967ap)** 
- **[Hemang Seth](https://github.com/Hemang-2004)** 
- **[Rutul Patel](https://github.com/RutulPatel007)** 


If you would like to contribute, feel free to fork the repository and submit a pull request!






   
## License

[MIT](https://github.com/varnit-mittal/JDBC-MINI-Proj/blob/main/LICENSE)
