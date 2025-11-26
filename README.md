# 🌟 CS731 Software Testing Project: Robust DAO Persistence Layer

**Course:** CS731 (Software Testing)
**Project Title:** School Management System Persistence Layer Validation
**Project Goal:** To deeply explore advanced testing techniques, specifically **Mutation Testing**, to ensure the quality and robustness of a Data Access Object (DAO) architecture implemented using Java and JDBC.

## 🤝 Team Details

| Role | Name |
| :--- | :--- |
| Team Member 1 | **Rutul** |
| Team Member 2 | **Siddeshwar** |

## 🚀 Project Overview and Architecture

This project is a back-end Java application designed to manage core entities of a university or school using a clean, layered architecture.

### Architectural Layers
1.  **Model Layer (`.models`):** Contains Plain Old Java Objects (POJOs) like `Student`, `Teacher`, `Book`, etc., holding data state.
2.  **Data Access Object (DAO) Layer (`.dao`):** Contains the core logic for communicating with the database. This layer abstracts all SQL queries and JDBC operations (`BookDAO`, `StudentDAO`, etc.) and extends a generic `BaseDAO`.
3.  **Persistence Layer:** A MySQL database used to store all entity data.

### Core Functionality
The application provides full persistence functionality across five major domains:
* **Students:** CRUD operations, CGPA updates, retrieving the topper.
* **Teachers:** CRUD operations, address updates, salary increment, retrieving the highest-paid teacher.
* **Courses:** CRUD operations, managing course details.
* **Books & Library:** CRUD operations for books, linking books to courses, managing library metadata.

---

## 🔬 Deep Dive into Testing Strategy

The primary focus of this project is verifying the quality of the test cases using **Mutation Testing** (PIT).

### What is Mutation Testing?

Mutation testing is a fault-based testing technique used to evaluate the effectiveness of a test suite. It works by introducing small, deliberate changes (called **mutants**) into the source code to simulate potential programming errors.

1.  **Mutant Generation:** The tool (PIT) inserts mutations (e.g., changing `if (a > b)` to `if (a >= b)`, changing `return true` to `return false`, or removing a variable assignment).
2.  **Mutant Execution:** The test suite is run against each mutated version of the code.
3.  **Result:**
    * **Killed Mutant:** If a test fails due to the mutant, the test suite is strong, and the mutant is **killed**.
    * **Surviving Mutant:** If all tests pass despite the code change, the test suite is weak, and the mutant **survives**. This indicates a gap in test logic or assertions.

### Covered Test Types

Our test suite is a combination of both **Unit** and **Integration** tests to ensure comprehensive coverage:

| Test Type | Focus Area | Verification Detail |
| :--- | :--- | :--- |
| **Integration Testing (Primary Focus)** | **All DAO Methods (CRUD)** | Verifies that Java code correctly interacts with the live MySQL database: SQL syntax correctness, Foreign Key constraint handling, and actual data persistence/retrieval (`CREATE`, `UPDATE` operations). |
| **Unit Testing** | **Mapping & Utilities** | Isolated verification of non-database interaction logic, such as `mapResultSetToEntity` (POJO construction from database results) and parameter binding utilities (`createPreparedStatement`). |
| **Edge Case/Boundary Testing** | **All DAO Methods** | Tests designed specifically to kill mutants by checking null inputs, zero/negative IDs, empty result sets (`getAllTeachers` when the table is empty), and forced database errors (e.g., Foreign Key violations). |

---

## 🛠️ Tools and Technology Stack

| Category | Tool/Technology | Role in Project |
| :--- | :--- | :--- |
| **Language** | Java (JDK 17+) | Core application development. |
| **Build/Dependency** | Apache Maven | Project management and dependency handling (`pom.xml`). |
| **Database** | MySQL (JDBC Connector) | Persistent storage for all school entities. |
| **Testing Framework** | JUnit 5 Jupiter | Standard framework for writing and running Unit/Integration tests. |
| **Mutation Testing** | **PIT (Program in Tomorrow)** | Primary tool for assessing test suite quality and driving test case refinement. |

---

## ⚙️ Setup and Execution Guide

### Code Repository Link
**[INSERT GITHUB/GITLAB REPOSITORY LINK HERE]**

### 1. Environment Preparation
* Ensure **Java (JDK)** and **Maven** are installed and configured.
* Start your **MySQL Server**.

### 2. Database Setup
1.  **Create Database:** Execute SQL to create the database:
    ```bash
    mysql -u root -padmin -e "CREATE DATABASE IF NOT EXISTS school_db;"
    ```
    *(Adjust credentials if necessary)*
2.  **Initialize Schema:** Apply the necessary table structures:
    ```bash
    mysql -u root -padmin school_db < sql/schem.sql
   
    ```

### 3. Running Tests and PIT Analysis

1.  **Run Standard Tests (for Coverage):**
    ```bash
    mvn clean test
    ```

2.  **Run Mutation Testing (for Strength):**
    ```bash
    mvn org.pitest:pitest-maven:mutationCoverage
    ```

3.  **View Results:** Open the detailed report in your browser:
    ```bash
    # Use the appropriate path for your OS
    open target/pit-reports/index.html 
    ```

### 📈 Final Quality Metrics

| Metric | Score | Comment |
| :--- | :--- | :--- |
| **Line Coverage** | **99%** | High line coverage achieved across all DAOs. |
| **Mutation Coverage** | **82%** | Strong mutation score, demonstrating effectiveness of integration tests. |
| **Test Strength** | **95%** | Indicates that 95% of covered lines are asserted by tests robust enough to detect code changes. |


---

## 📜 Team Contribution Breakdown

The project successfully required collaborative effort, with each team member taking ownership of specific components and testing layers.
### Rutul's Individual Contribution (50%):

* **Core Entity Development:** Implemented the data access logic (DAOs) and corresponding Model classes for **Library** and **Book** entities.

* **Foundational Development:** Wrote the essential **JUnit Test Cases** for `LibraryDAO` and `BookDAO`, ensuring basic CRUD operations and list retrieval functions were correct and stable.

* **Advanced Testing (Refinement I):** Performed the initial **PIT Mutation Analysis** across all DAOs (executing PIT and interpreting the first mutation report). Designed and implemented the **targeted mutation-killing tests** specifically for `BookDAO` and `LibraryDAO`, focusing on edge cases and logical branch coverage.

* **Tooling and Infrastructure Setup:** Led the full initial configuration of the **Maven project (`pom.xml`)**, including dependency setup (JUnit 5, PIT), project structure organization, and generating the initial test execution and mutation testing reports.


### Siddeshwar's Individual Contribution (50%):

* **Core Entity Development:** Implemented the data access logic (DAOs) and corresponding Model classes for **Student**, **Teacher**, and **Course** entities, and developed the abstract **BaseDAO** structure used across the project.

* **Foundational Development:** Wrote the essential **JUnit Test Cases** for `StudentDAO`, `TeacherDAO`, and `CourseDAO`, covering basic CRUD operations, list retrieval, and special queries such as `getTopper`.

* **Advanced Testing (Refinement II):** Conducted the iterative **PIT Mutation Analysis** rounds following Rutul’s initial pass and authored the complete **Mutation Testing Strategy** document. Designed and implemented the **crucial targeted tests** to kill surviving mutants in `StudentDAO`, `TeacherDAO`, `CourseDAO`, and `BaseDAO`, especially focusing on complex condition boundaries, empty-table behavior, and forced foreign-key/exception scenarios.

* **Final Quality Assurance & Documentation:** Authored the comprehensive **README**, performed the **final verification test runs**, and compiled the entire submission package for delivery.


---

## 🤖 AI Tool Usage Acknowledgment

We affirm that all critical test case design, mutation analysis, and structural modifications (e.g., refining logic based on surviving mutants) were executed **manually** by the team members.

However, we acknowledge the use of an **AI/LLM tool (Google Gemini)** for the following auxiliary tasks to improve code quality and documentation efficiency:

* **Source Code Scaffolding:** Initial generation of standard boilerplate code (constructors, getters, setters) in Model classes and the initial JDBC query structure in some DAO methods.
* **Code Documentation:** Insertion of comprehensive comments and Javadoc-style annotations in the source code.
* **Documentation Formatting:** Assistance in formatting and structuring this project README file according to the detailed project guidelines.


