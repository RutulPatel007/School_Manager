//STEP 1. Import required packages

import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.io.IOException;

public class JdbcDemo {

   // Set JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://localhost:3306/School";

   // Database credentials
   static final String USER = "root";// add your user
   static final String PASSWORD = "admin";// add password

   public static void main(String[] args) {

      String fileName="sql/schema.sql";
      String fileName2="sql/alter.sql";
      ArrayList<String> Schema = new ArrayList<>();

      try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
               String line;
               String temp="";
               while ((line = br.readLine()) != null) {
                   {
                    temp+=line;
                    if(line.contains(";"))
                    {
                        Schema.add(temp);
                        temp="";
                    }
                   }// Print each line of the file
               }
           } catch (IOException e) {
               System.err.println("Error reading the file: " + e.getMessage());
           } 

        try (BufferedReader br = new BufferedReader(new FileReader(fileName2))) {
        String line;
        String temp="";
        while ((line = br.readLine()) != null) {
            {
                temp+=line;
                if(line.contains(";"))
                {
                    Schema.add(temp);
                    temp="";
                }
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading the file: " + e.getMessage());
    } 

      Connection conn = null;
      Statement stmt = null;
      // STEP 2. Connecting to the Database

      try {
         // STEP 2a: Register JDBC driver
         Class.forName(JDBC_DRIVER);
         // STEP 2b: Open a connection
         System.out.println("Connecting to database...");
         conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
         // STEP 2c: Execute a query
         System.out.println("Creating statement...");
         stmt = conn.createStatement();

         // INSERT, UPDATE, DELETE
         for(String str: Schema)
         stmt.executeUpdate(str);

         // STEP 3: Query to database
        //  String query = "SELECT fname, lname, dno, bdate from employee";
        //  ResultSet rs = stmt.executeQuery(query);

        //  // STEP 4: Extract data from result set
        //  while (rs.next()) {

        //  // Retrieve by column name
        //  String fname = rs.getString("fname");
        //  String lname = rs.getString("lname");
        //  Date birthDate = rs.getDate("bdate", null);
        //  Integer dno = rs.getInt("dno");

        //  // Display values
        //  System.out.print("fname: " + fname);
        //  System.out.print(", lname: " + lname);
        //  System.out.print(", birth date: " + birthDate.toString());
        //  System.out.println(", department number: " + dno);
        //  }

         
         // STEP 5: Clean-up environment
        //  rs.close();
         stmt.close();
         conn.close();
      } catch (SQLException se) { // Handle errors for JDBC
         se.printStackTrace();
      } catch (Exception e) { // Handle errors for Class.forName
         e.printStackTrace();
      } finally { // finally block used to close resources regardless of whether an exception was
                  // thrown or not
         try {
            if (stmt != null)
               stmt.close();
         } catch (SQLException se2) {
         }
         try {
            if (conn != null)
               conn.close();
         } catch (SQLException se) {
            se.printStackTrace();
         } // end finally try
      } // end try
      System.out.println("End of Code");
   } // end main
} // end class

// Note : By default autocommit is on. you can set to false using
// con.setAutoCommit(false)