import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.io.IOException;

public class imt2022025_school {
    
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/Schooldb";
 
    // Database credentials
    static final String USER = "root";// add your user
    static final String PASSWORD = "admin";// add password


    public static void FileReading(String fileName, ArrayList<String> Schema)
    {
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
    }

    public static void main(String args[])
    {
        String[] inp= {"sql/schema.sql","sql/alter.sql","sql/insert.sql"};

        ArrayList<String> Schema = new ArrayList<>();

        for(int i=0;i<3;i++)
        {
            String fileName=inp[i];
            FileReading(fileName, Schema);
        }

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        Scanner sc = new Scanner(System.in);
        try{
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Creating statement...");
            stmt = conn.createStatement();

            String sql = "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = '" + DB_URL.substring(DB_URL.lastIndexOf('/') + 1) + "' AND TABLE_NAME = '" + "Student" + "'";
            rs = stmt.executeQuery(sql);

            if(!rs.next())
            {
                for(String str: Schema)
                    stmt.executeUpdate(str);
            }
            
            System.out.println("""
                    
                    ***************************MENU***************************
                    1. Add a new student to the school 
                    2. Add a new teacher to the school 
                    3. Add a new course to the school
                    4. Enroll a student in a course 
                    5. Remove a Student from School
                    6. Remove teacher from school
                    7. Remove course from school
                    8. Remove student from course
                    9. Update CGPA of student
                    10. Increment salary of teacher
                    11. Get details of student
                    12. Get details of teacher
                    13. Get details of course
                    14. Topper of batch
                    15. Highest paid teacher
                    16. Courses taken by student
                    17. Courses taught by teacher
                    18. List of students in a school
                    19. List of teachers in a school
                    20. List of courses in a school
                    21. Add a new book to the library
                    22. Mark a book with a course
                    23. Books in course work of a student
                    24. Remove a book from library
                    25. Exit
                    **********************************************************
                    **********************************************************
                    """
            );
            while(true)
            {
                System.out.print("Enter your choice: ");
                int choice =  sc.nextInt();

                switch(choice)
                {
                    case 1:
                        System.out.println("Enter the details of the student");
                        System.out.println("Enter Admissions number of the student");
                        String admno=sc.next();
                        System.out.println("Enter the name of the student");
                        String name = sc.next();
                        System.out.println("Enter the roll number of the student");
                        int roll = sc.nextInt();
                        System.out.println("Enter the batch of the student");
                        String batch = sc.next();
                        System.out.println("Enter the stream of the student");
                        String stream = sc.next();
                        System.out.println("Enter Student's Email Address");
                        String email = sc.next();
                        System.out.println("Enter the CGPA of the student");
                        float cgpa = sc.nextFloat();
                        String query = "INSERT INTO Student (admno, name, batch, stream, rollno, email, cgpa) VALUES ("+admno+",'"+name+"','"+batch+"','"+stream+"',"+roll+",'"+email+"',"+cgpa+");";
                        int output=stmt.executeUpdate(query);

                        if(output==1)
                        {
                            System.out.println("Student Admitted Successfully");
                        }
                        else
                        {
                            System.out.println("Student wasn't admitted");
                        }
                        break;
                    case 2:
                        System.out.println("Enter the details of the teacher");
                        System.out.println("Enter the name of the teacher");
                        String tname = sc.next();
                        System.out.println("Enter the teacher id");
                        int tid = sc.nextInt();
                        System.out.println("Enter Teacher's Email Address");
                        email = sc.next();
                        System.out.println("Enter the salary of the teacher");
                        float salary = sc.nextFloat();
                        query = "INSERT INTO Professor (id, name, email, salary) VALUES ("+tid+",'"+tname+"','"+email+"',"+salary+");";
                        output=stmt.executeUpdate(query);
                        if(output==1)
                        {
                            System.out.println("Teacher Added Successfully");
                        }
                        else
                        {
                            System.out.println("Teacher wasn't added");
                        }
                        break;
                    case 3:
                        System.out.println("Enter the details of the course");
                        System.out.println("Enter the course code");
                        String code= sc.next();
                        System.out.println("Enter the course name");
                        String cname = sc.next();
                        System.out.println("Enter the teacher id");
                        int teacher_id = sc.nextInt();
                        query = "INSERT INTO Course (course_code, name, instructor) VALUES ('"+code+"','"+cname+"',"+teacher_id+");";
                        output=stmt.executeUpdate(query);
                        if(output==1)
                        {
                            System.out.println("Course Added Successfully");
                        }
                        else
                        {
                            System.out.println("Course wasn't added");
                        }
                        break;
                    case 4:
                        System.out.println("Enter Student Course Erollment Details");
                        System.out.println("Enter the student admission number");
                        admno = sc.next();
                        System.out.println("Enter the course code");
                        code = sc.next();
                        query = "INSERT INTO Studies (studAdmo, course_code) VALUES ('"+admno+"','"+code+"');";
                        output=stmt.executeUpdate(query);
                        if(output==1)
                        {
                            System.out.println("Student Enrolled Successfully");
                        }
                        else
                        {
                            System.out.println("Student wasn't enrolled");
                        }
                        break;
                    case 5:
                        System.out.println("Enter details of student to be removed from School");
                        System.out.println("Enter the student admission number");
                        admno = sc.next();
                        query = "DELETE FROM Student WHERE admno='"+admno+"';";
                        output=stmt.executeUpdate(query);
                        if(output==1)
                        {
                            System.out.println("Student Removed Successfully");
                        }
                        else
                        {
                            System.out.println("Couldn't find Student with given Admission Number");
                        }
                        break;
                    case 6:
                        System.out.println("Enter details of teacher to be removed from School");
                        System.out.println("Enter the teacher id");
                        tid = sc.nextInt();
                        query = "DELETE FROM Professor WHERE id="+tid+";";
                        output=stmt.executeUpdate(query);
                        if(output==1)
                        {
                            System.out.println("Teacher Removed Successfully");
                        }
                        else
                        {
                            System.out.println("Couldn't find Teacher with given ID");
                        }
                        break;
                    case 7:
                        System.out.println("Enter details of course to be removed from School");
                        System.out.println("Enter the course code");
                        code = sc.next();
                        query = "DELETE FROM Course WHERE course_code='"+code+"';";
                        output=stmt.executeUpdate(query);
                        if(output==1)
                        {
                            System.out.println("Course Removed Successfully");
                        }
                        else
                        {
                            System.out.println("Couldn't find Course with given Course Code");
                        }
                        break;
                    case 8:
                        System.out.println("Enter details of student to be removed from Course");
                        System.out.println("Enter the student admission number");
                        admno = sc.next();
                        System.out.println("Enter the course code");
                        code = sc.next();
                        query = "DELETE FROM Studies WHERE studAdmo='"+admno+"' AND course_code='"+code+"';";
                        output=stmt.executeUpdate(query);
                        if(output==1)
                        {
                            System.out.println("Student Removed Successfully");
                        }
                        else
                        {
                            System.out.println("Couldn't find Student with given Admission Number or Course with given Course Code");
                        }
                        break;
                    case 9:
                        System.out.println("Enter details to update CGPA of student");
                        System.out.println("Enter the student admission number");
                        admno = sc.next();
                        System.out.println("Enter the new CGPA of the student");
                        cgpa = sc.nextFloat();
                        query = "UPDATE Student SET cgpa="+cgpa+" WHERE admno='"+admno+"';";
                        output=stmt.executeUpdate(query);
                        if(output==1)
                        {
                            System.out.println("CGPA Updated Successfully");
                        }
                        else
                        {
                            System.out.println("Couldn't find Student with given Admission Number");
                        }
                    case 10:
                        System.out.println("Enter details to increment salary of teacher");
                        System.out.println("Enter the teacher id");
                        tid = sc.nextInt();
                        System.out.println("Enter the increment in salary");
                        float increment = sc.nextFloat();
                        query = "UPDATE Professor SET salary=salary+"+increment+" WHERE id="+tid+";";
                        output=stmt.executeUpdate(query);
                        if(output==1)
                        {
                            System.out.println("Salary Updated Successfully");
                        }
                        else
                        {
                            System.out.println("Couldn't find Teacher with given ID");
                        }
                        break;
                    case 11:
                        System.out.println("Enter the student admission number");
                        admno = sc.next();
                        query = "SELECT * FROM Student WHERE admno='"+admno+"';";
                        rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            System.out.println("Admission Number: "+rs.getString("admno"));
                            System.out.println("Name: "+rs.getString("name"));
                            System.out.println("Roll Number: "+rs.getInt("rollno"));
                            System.out.println("Batch: "+rs.getString("batch"));
                            System.out.println("Stream: "+rs.getString("stream"));
                            System.out.println("Email: "+rs.getString("email"));
                            System.out.println("CGPA: "+rs.getFloat("cgpa"));
                        }
                        else
                        {
                            System.out.println("Couldn't find Student with given Admission Number");
                        }
                        break;
                    case 12:
                        System.out.println("Enter the teacher id");
                        tid = sc.nextInt();
                        query = "SELECT * FROM Professor WHERE id="+tid+";";
                        rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            System.out.println("Teacher ID: "+rs.getInt("id"));
                            System.out.println("Name: "+rs.getString("name"));
                            System.out.println("Email: "+rs.getString("email"));
                            System.out.println("Salary: "+rs.getFloat("salary"));
                        }
                        else
                        {
                            System.out.println("Couldn't find Teacher with given ID");
                        }
                        break;
                    case 13:
                        System.out.println("Enter the course code");
                        code = sc.next();
                        query = "SELECT * FROM Course WHERE course_code='"+code+"';";
                        rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            System.out.println("Course Code: "+rs.getString("course_code"));
                            System.out.println("Name: "+rs.getString("name"));
                            System.out.println("Instructor: "+rs.getInt("instructor"));
                        }
                        else
                        {
                            System.out.println("Couldn't find Course with given Course Code");
                        }
                        break;
                    case 14:
                        System.out.println("Enter the batch");
                        batch = sc.next();
                        query = "SELECT * FROM Student WHERE batch='"+batch+"' ORDER BY cgpa DESC LIMIT 1;";
                        rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            System.out.println("Topper of the batch "+batch+" is "+rs.getString("name")+" with CGPA "+rs.getFloat("cgpa"));
                        }
                        else
                        {
                            System.out.println("Couldn't find any student in the batch");
                        }
                        break;
                    case 15:
                        query = "SELECT * FROM Professor ORDER BY salary DESC LIMIT 1;";
                        rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            System.out.println("Highest paid teacher is "+rs.getString("name")+" with salary "+rs.getFloat("salary"));
                        }
                        else
                        {
                            System.out.println("Couldn't find any teacher");
                        }
                        break;
                    case 16:
                        System.out.println("Enter the student admission number");
                        admno = sc.next();
                        query = "select c.name from Student s INNER JOIN Studies st on s.admno=st.studAdmo INNER JOIN Course c on st.course_code=c.course_code WHERE s.admno='"+admno+"';";
                        rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            System.out.println("Courses taken by student with admission number "+admno+" are:");
                            do
                            {
                                System.out.println(rs.getString("name"));
                            }while(rs.next());
                        }
                        else
                        {
                            System.out.println("Couldn't find any course taken by student with given Admission Number");
                        }
                        break;
                    case 17:
                        System.out.println("Enter the teacher id");
                        tid = sc.nextInt();
                        query = "select c.name from Professor p INNER JOIN Course c on p.id=c.instructor WHERE p.id="+tid+";";
                        rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            System.out.println("Courses taught by teacher with id "+tid+" are:");
                            do
                            {
                                System.out.println(rs.getString("name"));
                            }while(rs.next());
                        }
                        else
                        {
                            System.out.println("Couldn't find any course taught by teacher with given ID");
                        }
                        break;
                    case 18:
                        query = "SELECT * FROM Student;";
                        rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            System.out.println("List of students in the school:");
                            do
                            {
                                System.out.println("Admission Number: "+rs.getString("admno"));
                                System.out.println("Name: "+rs.getString("name"));
                                System.out.println("Roll Number: "+rs.getInt("rollno"));
                                System.out.println("Batch: "+rs.getString("batch"));
                                System.out.println("Stream: "+rs.getString("stream"));
                                System.out.println("Email: "+rs.getString("email"));
                                System.out.println("CGPA: "+rs.getFloat("cgpa"));
                                System.out.println();
                            }while(rs.next());
                        }
                        else
                        {
                            System.out.println("No students in the school");
                        }
                        break;
                    case 19:
                        query = "SELECT * FROM Professor;";
                        rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            System.out.println("List of teachers in the school:");
                            do
                            {
                                System.out.println("Teacher ID: "+rs.getInt("id"));
                                System.out.println("Name: "+rs.getString("name"));
                                System.out.println("Email: "+rs.getString("email"));
                                System.out.println("Salary: "+rs.getFloat("salary"));
                                System.out.println();
                            }while(rs.next());
                        }
                        else
                        {
                            System.out.println("No teachers in the school");
                        }
                        break;
                    case 20:
                        query = "SELECT * FROM Course;";
                        rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            System.out.println("List of courses in the school:");
                            do
                            {
                                System.out.println("Course Code: "+rs.getString("course_code"));
                                System.out.println("Name: "+rs.getString("name"));
                                System.out.println("Instructor: "+rs.getInt("instructor"));
                                System.out.println();
                            }while(rs.next());
                        }
                        else
                        {
                            System.out.println("No courses in the school");
                        }
                        break;
                    case 21:
                        System.out.println("Enter the details of the book");
                        System.out.println("Enter the book id");
                        String book_id = sc.next();
                        System.out.println("Enter the book name");
                        String bname = sc.next();
                        System.out.println("Enter the author of the book");
                        String author = sc.next();
                        System.out.println("Enter price of the book");
                        float price = sc.nextFloat();
                        query="INSERT INTO Book (id, name, author, price) VALUES ('"+book_id+"','"+bname+"','"+author+"',"+price+");";
                        output=stmt.executeUpdate(query);
                        if(output==1)
                        {
                            System.out.println("Book Added Successfully");
                        }
                        else
                        {
                            System.out.println("Book wasn't added");
                        }
                        break;
                    case 22:
                        System.out.println("Enter the details of the book and course");
                        System.out.println("Enter the course code");
                        code = sc.next();
                        System.out.println("Enter the book id");
                        book_id = sc.next();
                        query="INSERT INTO CourseWork (course_code,book_id) VALUES ('"+code+"','"+book_id+"');";
                        output=stmt.executeUpdate(query);
                        if(output==1)
                        {
                            System.out.println("Book Marked Successfully");
                        }
                        else
                        {
                            System.out.println("Book wasn't marked");
                        }
                        break;
                    case 23:
                        System.out.println("Enter the student admission number");
                        admno = sc.next();
                        query = "select b.name from Student s INNER JOIN Studies ss on s.admno = ss.studAdmo INNER JOIN Course c on ss.course_code = c.course_code INNER JOIN CourseWork cw on c.course_code = cw.course_code INNER JOIN Book b on cw.book_id=b.id WHERE s.admno='"+admno+"';";
                        rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            System.out.println("Books in course work of student with admission number "+admno+" are:");
                            do
                            {
                                System.out.println(rs.getString("name"));
                            }while(rs.next());
                        }
                        else
                        {
                            System.out.println("Couldn't find any book in course work of student with given Admission Number");
                        }
                        break;
                    case 24:
                        System.out.println("Enter the book id");
                        book_id = sc.next();
                        query = "DELETE FROM Book WHERE id='"+book_id+"';";
                        output=stmt.executeUpdate(query);
                        if(output==1)
                        {
                            System.out.println("Book Removed Successfully");
                        }
                        else
                        {
                            System.out.println("Couldn't find Book with given Book ID");
                        }
                        break;
                    case 25:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid Choice");
                }
                Thread.sleep(1500);
                if(choice==25)
                    break;
            }
        sc.close();
        rs.close();
        stmt.close();
        conn.close();
        } catch(SQLException se){
            se.printStackTrace();
        } catch( Exception e){
            e.printStackTrace();
        } finally { 
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
            } 
        } 
        System.out.println("End of Code");
    }
}
