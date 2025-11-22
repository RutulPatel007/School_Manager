create table Student(
    admno char(10),
    name varchar(40) NOT NULL,
    batch varchar(4) NOT NULL,
    stream varchar(12),
    rollno SMALLINT,
    email varchar(60) NOT NULL,
    cgpa FLOAT,
    CONSTRAINT admno_pk PRIMARY KEY(admno)
);

create table Studies(
    studAdmo char(10),
    course_code char(5),
    CONSTRAINT study_pk PRIMARY KEY(studAdmo, course_code)
);

create table Course(
    course_code char(5),
    name VARCHAR(60) NOT NULL,
    instructor int NOT NULL,
    CONSTRAINT course_pk PRIMARY KEY(course_code)
);

create table CourseWork(
    course_code char(5),
    book_id char(5),
    CONSTRAINT coursework_pk PRIMARY KEY(course_code, book_id)
);

create table Professor(
    id int,
    name VARCHAR(40) NOT NULL,
    email VARCHAR(60) NOT NULL,
    salary FLOAT NOT NULL,
    CONSTRAINT prof_pk PRIMARY KEY(id)
);

create table Book(
    id char(5),
    name VARCHAR(60) NOT NULL,
    author VARCHAR(40) NOT NULL,
    price FLOAT NOT NULL,
    CONSTRAINT book_pk PRIMARY KEY(id)
);