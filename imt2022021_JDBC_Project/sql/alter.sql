ALTER table Studies
 add CONSTRAINT study_fk FOREIGN KEY (studAdmo) REFERENCES Student(admno) ON DELETE CASCADE;

ALTER table Studies
 add CONSTRAINT course_fk FOREIGN KEY (course_code) REFERENCES Course(course_code) ON DELETE CASCADE;

 ALTER Table Course
    add CONSTRAINT prof_fk FOREIGN KEY (instructor) REFERENCES Professor(id) ON DELETE CASCADE;

ALTER TABLE CourseWork
    ADD CONSTRAINT book_fk FOREIGN KEY (book_id) REFERENCES Book(id) ON DELETE CASCADE;

ALTER TABLE CourseWork
      ADD CONSTRAINT course_fk_work FOREIGN KEY (course_code) REFERENCES Course(course_code) ON DELETE CASCADE;