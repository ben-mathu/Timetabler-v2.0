package com.bernard.timetabler.dbinit;

import com.bernard.timetabler.dbinit.test_package.PopulateEntitiesForTests;
import com.bernard.timetabler.utils.Log;
import com.bernard.timetabler.utils.UtilCommonFunctions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateSchemaTimeTabler {
	private static final String TAG = CreateSchemaTimeTabler.class.getSimpleName();
	
    private static String database = "";
    private String env = "";

    private Statement statement;

    public CreateSchemaTimeTabler(String user, String password) {
        Connection conn = null;
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + database + "?allowMultiQueries=true",
                    user, password
            );

            statement = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDatabase(String databaseName) {
       database = databaseName;
    }
 
    public void setStatement(Statement statement) throws SQLException {
        this.statement = statement;
    }

    public Statement getStatement() {
        return statement;
    }

    public static void createSchema(String env) {
        int countResult = 0;
        try {
        	// Update the db
        	if (database.isEmpty()) {
            	database = Constants.DATABASE_NAME;
            }
        	
            Statement statement = UtilCommonFunctions.initialize("ben", "");
            
            if (env.equals("update")) {
        		PopulateEntitiesForTests.populateEntities();
        		return;
        	}
            // Create a database if it is not already created
            System.out.println();

            System.out.println("Deleting database " + Constants.DATABASE_NAME + "...");
            String dropSchema = "DROP DATABASE IF EXISTS " + Constants.DATABASE_NAME;
            countResult = statement.executeUpdate(dropSchema);
            System.out.println("Database deleted. Result: " + countResult + " affected");

            System.out.println("Creating a database...");
            String createSchema = "CREATE DATABASE IF NOT EXISTS timetabler";
            countResult = statement.executeUpdate(createSchema);
            System.out.println("Database created. Result: " + countResult + " affected");

            System.out.println();

            System.out.println("Select a database");
            String dbStatement = "USE " + Constants.DATABASE_NAME;
            countResult = statement.executeUpdate(dbStatement);

            database = Constants.DATABASE_NAME;

            System.out.println();

            // Campuses
            System.out.println("Creating table campus...");
            String campusStatement = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_CAMPUS + " (" +
                    Constants.CAMPUS_ID + " VARCHAR(10)," +
                    Constants.CAMPUS_NAME + " VARCHAR(255)," +
                    Constants.IS_REMOVED + " BOOLEAN, " +
                    "PRIMARY KEY (" + Constants.CAMPUS_ID + ")" +
                    ")";
            countResult = statement.executeUpdate(campusStatement);
            System.out.println("Table campus created result: " + countResult);

            // Faculty
            System.out.println("Creating table faculty...");
            String facultiesStatement = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_FACULTIES + " (" +
                    Constants.FACULTY_ID + " VARCHAR(10)," +
                    Constants.FACULTY_NAME + " VARCHAR(255)," +
                    Constants.CAMPUS_ID + " VARCHAR(10)," +
                    Constants.IS_REMOVED + " BOOLEAN," +
                    "PRIMARY KEY (" + Constants.FACULTY_ID + ")," +
                    "FOREIGN KEY fk_campuses(" + Constants.CAMPUS_ID + ") " +
                    "REFERENCES " + Constants.TABLE_CAMPUS + "(" + Constants.CAMPUS_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE" +
                    ")";
            countResult = statement.executeUpdate(facultiesStatement);
            System.out.println("Created table faculties result: " + countResult + " affected");

            System.out.println();

            // Department
            System.out.println("Creating table Department...");
            String departmentStatement = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_DEPARTMENTS + " (" +
                    Constants.DEPARTMENT_ID + " VARCHAR(10)," +
                    Constants.DEPARTMENT_NAME + " VARCHAR(255)," +
                    Constants.FACULTY_ID + " VARCHAR(10)," +
                    Constants.IS_REMOVED + " BOOLEAN," +
                    "PRIMARY KEY (" + Constants.DEPARTMENT_ID + ")," +
                    "FOREIGN KEY fk_faculties(" + Constants.FACULTY_ID + ") " +
                    "REFERENCES " + Constants.TABLE_FACULTIES + "(" + Constants.FACULTY_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE" +
                    ")";
            countResult = statement.executeUpdate(departmentStatement);
            System.out.println("Table Department created result: " + countResult);

            System.out.println();

            // Programme
            System.out.println("Creating table programmes...");
            String programmeStatement = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_PROGRAMMES + " (" +
                    Constants.PROGRAMME_ID +" VARCHAR(10)," +
                    Constants.PROGRAMME_NAME + " VARCHAR(255)," +
                    Constants.DEPARTMENT_ID + " VARCHAR(10)," +
                    Constants.FACULTY_ID + " VARCHAR(10)," +
                    Constants.IS_REMOVED + " BOOLEAN," +
                    "PRIMARY KEY (" + Constants.PROGRAMME_ID + ")," +
                    "FOREIGN KEY fk_departments(" + Constants.DEPARTMENT_ID + ") " +
                    "REFERENCES " + Constants.TABLE_DEPARTMENTS + "(" + Constants.DEPARTMENT_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE, " +
                    "FOREIGN KEY fk_faculties(" + Constants.FACULTY_ID + ") " +
                    "REFERENCES " + Constants.TABLE_FACULTIES + "(" + Constants.FACULTY_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE" +
                    ")";
            countResult = statement.executeUpdate(programmeStatement);
            System.out.println("Created table Programmes result: " + countResult);

            System.out.println();

            // Create tables: Students, Lecturer, Classes, Hall, Units, Departments, Programmes, Faculty
            System.out.println("Create table: Students");
            String studentStatement = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_STUDENTS + " (" +
                    Constants.STUDENT_ID + " VARCHAR(10)," +
                    Constants.F_NAME + " VARCHAR(25)," +
                    Constants.L_NAME + " VARCHAR(25)," +
                    Constants.M_NAME + " VARCHAR(25)," +
                    Constants.USERNAME + " VARCHAR(25) UNIQUE," +
                    Constants.PASSWORD + " VARCHAR(255)," +
                    Constants.EMAIL + " VARCHAR(255) UNIQUE," +
                    Constants.IN_SESSION + " BOOLEAN," +
                    Constants.DEPARTMENT_ID + " VARCHAR(10)," +
                    Constants.CAMPUS_ID + " VARCHAR(10)," +
                    Constants.FACULTY_ID + " VARCHAR(10)," +
                    Constants.PROGRAMME_ID + " VARCHAR(10)," +
                    Constants.YEAR_OF_STUDY + " VARCHAR(10)," +
                    Constants.ADMISSION_DATE + " VARCHAR(255)," +
                    Constants.IS_REMOVED + " BOOLEAN," +
                    "PRIMARY KEY (" + Constants.STUDENT_ID + ")," +
                    "KEY fk_depart(" + Constants.DEPARTMENT_ID + ")," +
                    "KEY fk_campus(" + Constants.CAMPUS_ID + ")," +
                    "KEY fk_programme(" + Constants.PROGRAMME_ID + ")," +
                    "CONSTRAINT student_ibfk_3 " +
                    "FOREIGN KEY (" + Constants.PROGRAMME_ID + ") " +
                    "REFERENCES " + Constants.TABLE_PROGRAMMES + "(" + Constants.PROGRAMME_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE," +
                    "CONSTRAINT student_ibfk_2 " +
                    "FOREIGN KEY (" + Constants.CAMPUS_ID + ") " +
                    "REFERENCES " + Constants.TABLE_CAMPUS + "(" + Constants.CAMPUS_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE," +
                    "CONSTRAINT student_ibfk_1 " +
                    "FOREIGN KEY (" + Constants.FACULTY_ID + ") " +
                    "REFERENCES " + Constants.TABLE_FACULTIES + "(" + Constants.FACULTY_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE," +
                    "CONSTRAINT student_ibfk_0 " +
                    "FOREIGN KEY (" + Constants.DEPARTMENT_ID + ") " +
                    "REFERENCES " + Constants.TABLE_DEPARTMENTS + "(" + Constants.DEPARTMENT_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE" +
                    ")";
            countResult = statement.executeUpdate(studentStatement);
            System.out.println("Created table students: " + countResult);

            System.out.println();

            // Lecturers
            System.out.println("Creating table: lecturers");
            String lecturerStatement = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_LECTURERS + " (" +
                    Constants.LECTURER_ID + " VARCHAR(10)," +
                    Constants.F_NAME + " VARCHAR(25)," +
                    Constants.L_NAME + " VARCHAR(25)," +
                    Constants.M_NAME + " VARCHAR(25)," +
                    Constants.USERNAME + " VARCHAR(25) UNIQUE," +
                    Constants.PASSWORD + " VARCHAR(255)," +
                    Constants.EMAIL + " VARCHAR(255)," +
                    Constants.FACULTY_ID + " VARCHAR(10)," +
                    Constants.DEPARTMENT_ID + " VARCHAR(10)," +
                    Constants.IN_SESSION + " BOOLEAN," +
                    Constants.IS_REMOVED + " BOOLEAN," +
                    "PRIMARY KEY (" + Constants.LECTURER_ID + ")," +
                    "FOREIGN KEY fk_lecturers(" + Constants.DEPARTMENT_ID + ") " +
                    "REFERENCES " + Constants.TABLE_DEPARTMENTS + "(" + Constants.DEPARTMENT_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE," +
                    "FOREIGN KEY fk_lecturers_fac(" + Constants.FACULTY_ID + ") " +
                    "REFERENCES " + Constants.TABLE_FACULTIES + "(" + Constants.FACULTY_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE" +
                    ")";
            countResult = statement.executeUpdate(lecturerStatement);
            System.out.println("Students table created result: " + countResult);

            System.out.println();

            // Hall
            System.out.println("Creating table Hall...");
            String hallsStatement = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_HALLS + " (" +
                    Constants.HALL_ID + " VARCHAR(10)," +
                    Constants.HALL_NAME + " VARCHAR(255)," +
                    Constants.FACULTY_ID + " VARCHAR(10)," +
                    Constants.IS_REMOVED + " BOOLEAN," +
                    "PRIMARY KEY (" + Constants.HALL_ID + ")," +
                    "FOREIGN KEY fk_faculty_id(" + Constants.FACULTY_ID + ") " +
                    "REFERENCES " + Constants.TABLE_FACULTIES + "(" + Constants.FACULTY_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE" +
                    ")";
            countResult = statement.executeUpdate(hallsStatement);
            System.out.println("Created table Hall result :" + countResult);

            System.out.println();

            // Classes
            System.out.println("Creating table classes...");
            String classesStatement = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_CLASSES + " (" +
                    Constants.CLASS_ID + " VARCHAR(10)," +
                    Constants.HALL_ID + " VARCHAR(10)," +
                    Constants.FACULTY_ID + " VARCHAR(10)," +
                    Constants.VOLUME + " VARCHAR(10)," +
                    Constants.AVAILABILITY + " BOOLEAN," +
                    Constants.IS_LAB + " BOOLEAN," +
                    Constants.IS_REMOVED + " BOOLEAN," +
                    "PRIMARY KEY (" + Constants.CLASS_ID + ")," +
                    "FOREIGN KEY fk_classes(" + Constants.HALL_ID + ") " +
                    "REFERENCES " + Constants.TABLE_HALLS + "(" + Constants.HALL_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE," +
                    "FOREIGN KEY fk_faculties_2(" + Constants.HALL_ID + ") " +
                    "REFERENCES " + Constants.TABLE_HALLS + "(" + Constants.HALL_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE" +
                    ")";
            countResult = statement.executeUpdate(classesStatement);
            System.out.println("Classes table created result " + countResult);

            System.out.println();

            // Units
            System.out.println("Creating table units...");
            String unitStatement = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_UNITS + " (" +
                    Constants.UNIT_ID + " VARCHAR(10)," +
                    Constants.UNIT_NAME + " VARCHAR(255)," +
                    Constants.PROGRAMME_ID + " VARCHAR(10)," +
                    Constants.FACULTY_ID + " VARCHAR(10)," +
                    Constants.DEPARTMENT_ID + " VARCHAR(10)," +
                    Constants.IS_PRACTICAL + " BOOLEAN," +
                    Constants.IS_COMMON + " BOOLEAN," +
                    Constants.IS_REMOVED + " BOOLEAN," +
                    "PRIMARY KEY (" + Constants.UNIT_ID + ")," +
                    "FOREIGN KEY fk_units_programmes(" + Constants.PROGRAMME_ID + ") " +
                    "REFERENCES " + Constants.TABLE_PROGRAMMES + "(" + Constants.PROGRAMME_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE," +
                    "FOREIGN KEY fk_units_faculty(" + Constants.FACULTY_ID + ") " +
                    "REFERENCES " + Constants.TABLE_FACULTIES + "(" + Constants.FACULTY_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE," +
                    "FOREIGN KEY fk_units_department(" + Constants.DEPARTMENT_ID + ") " +
                    "REFERENCES " + Constants.TABLE_DEPARTMENTS + "(" + Constants.DEPARTMENT_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE" +
                    ")";
            countResult = statement.executeUpdate(unitStatement);
            System.out.println("Table Units created result: " + countResult);

            System.out.println();

            // Relationships :
            // Table of programme >-< Lecturer
            System.out.println("Relationship table: " +
                    "\nmany programmes can be taught by more than one lecturer " +
                    "\nCreating...");
            String lecturerProgrammeTableStatement = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_LECTURER_PROGRAMMES + " (" +
                    Constants.LECTURER_ID + " VARCHAR(10)," +
                    Constants.PROGRAMME_ID + " VARCHAR(10)," +
                    "KEY fk_lecturer(" + Constants.LECTURER_ID + ")," +
                    "KEY fk_programme(" + Constants.PROGRAMME_ID + ")," +
                    "CONSTRAINT lecturer_programme_ibfk_2 " +
                    "FOREIGN KEY (" + Constants.PROGRAMME_ID + ") " +
                    "REFERENCES " + Constants.TABLE_PROGRAMMES + "(" + Constants.PROGRAMME_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE," +
                    "CONSTRAINT lecturer_programme_ibfk_1 " +
                    "FOREIGN KEY (" + Constants.LECTURER_ID + ") " +
                    "REFERENCES " + Constants.TABLE_LECTURERS + "(" + Constants.LECTURER_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE" +
                    ")";
            countResult = statement.executeUpdate(lecturerProgrammeTableStatement);
            System.out.println("Created table the relationship table: Lecture-Programme result " + countResult);

            System.out.println();

            // Table of lectuer >-< units
            System.out.println("Relationship table: " +
                    "\nmany units can be taught by more than one lecturer " +
                    "\nCreating...");
            String lecturerUnitTableStatement = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_LECTURER_UNITS + " (" +
                    Constants.LECTURER_ID + " VARCHAR(10)," +
                    Constants.UNIT_ID + " VARCHAR(10)," +
                    Constants.IS_REMOVED + " BOOLEAN," +
                    "KEY fk_lecturer_id(" + Constants.LECTURER_ID + ")," +
                    "KEY fk_unit_id(" + Constants.UNIT_ID + ")," +
                    "CONSTRAINT lecturer_unit_ibfk_2 " +
                    "FOREIGN KEY (" + Constants.UNIT_ID + ") " +
                    "REFERENCES " + Constants.TABLE_UNITS + "(" + Constants.UNIT_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE," +
                    "CONSTRAINT lecturer_unit_ibfk_1 " +
                    "FOREIGN KEY (" + Constants.LECTURER_ID + ") " +
                    "REFERENCES " + Constants.TABLE_LECTURERS + "(" + Constants.LECTURER_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE" +
                    ")";
            countResult = statement.executeUpdate(lecturerUnitTableStatement);
            System.out.println("Created relationship table Lecturer - Unit result: " + countResult);

            System.out.println();

            // Table of student >-< unit
            System.out.println("Relationship table: " +
                    "\nmany units can be taken by more than one student " +
                    "\nCreating...");
            String studentUnitStatement = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_STUDENT_UNITS + " (" +
                    Constants.STUDENT_ID + " VARCHAR(10)," +
                    Constants.UNIT_ID + " VARCHAR(10)," +
                    Constants.PERIOD + " VARCHAR(15)," +
                    Constants.IS_REMOVED + " BOOLEAN," +
                    "KEY fk_student_id(" + Constants.STUDENT_ID + ")," +
                    "KEY fk_unit(" + Constants.UNIT_ID + ")," +
                    "CONSTRAINT student_unit_ibfk_2 " +
                    "FOREIGN KEY (" + Constants.UNIT_ID + ") " +
                    "REFERENCES " + Constants.TABLE_UNITS + "(" + Constants.UNIT_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE," +
                    "CONSTRAINT student_unit_ibfk_1 " +
                    "FOREIGN KEY (" + Constants.STUDENT_ID + ") " +
                    "REFERENCES students(" + Constants.STUDENT_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE" +
                    ")";
            countResult = statement.executeUpdate(studentUnitStatement);
            System.out.println("Created table Student - unit result: " + countResult);

            System.out.println();

            // Table of class >- unit
            System.out.println("Relationship table: " +
                    "\none unit can be taught in more than one class" +
                    "\nCreating...");
            String unitClassStatement = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_CLASS_UNITS + " (" +
                    Constants.CLASS_ID + " VARCHAR(10)," +
                    Constants.UNIT_ID + " VARCHAR(10)," +
                    Constants.HALL_ID + " VARCHAR(10)," +
//                    Constants.TIME + " VARCHAR(10)," +
//                    Constants.DAY + " VARCHAR(10)," +
//                    Constants.PERIOD + " VARCHAR(10)," +
                    "KEY class_id(" + Constants.CLASS_ID + ")," +
                    "KEY fk_id_unit(" + Constants.UNIT_ID + ")," +
                    "KEY fk_id_hall(" + Constants.HALL_ID + ")," +
                    "CONSTRAINT class_unit_ibfk_2 " +
                    "FOREIGN KEY (" + Constants.UNIT_ID + ") " +
                    "REFERENCES " + Constants.TABLE_UNITS + "(" + Constants.UNIT_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE," +
                    "CONSTRAINT class_unit_ibfk_1 " +
                    "FOREIGN KEY (" + Constants.CLASS_ID + ") " +
                    "REFERENCES " + Constants.TABLE_CLASSES + "(" + Constants.CLASS_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE," +
                    "CONSTRAINT class_unit_ibfk_0 " +
                    "FOREIGN KEY (" + Constants.HALL_ID + ") " +
                    "REFERENCES " + Constants.TABLE_HALLS + "(" + Constants.HALL_ID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE" +
                    ")";
            countResult = statement.executeUpdate(unitClassStatement);
            System.out.println("Created table class - unit result " + countResult);

            System.out.println();
            
            // Create a table to store salt
            Log.d(TAG, "Creating table to save salt for particular users");
            String saltRole = "CREATE TABLE IF NOT EXISTS " + Constants.SALTROLE + " (" +
            		Constants.SALT + " VARCHAR(255)," +
            		Constants.ROLE + " VARCHAR(25))";
            countResult = statement.executeUpdate(saltRole);
            Log.d(TAG, "Created table salt_role " + countResult);

            System.out.println();
            
//            Create a table to store timetables
            String ttQuery = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_TIMTABLE + " (" +
            		Constants.PERIOD + " VARCHAR(255)," +
            		Constants.TIME + " VARCHAR(25)," +
            		Constants.DAY + " VARCHAR(25)," +
            		Constants.UNIT_ID + " VARCHAR(25))";
            countResult = statement.executeUpdate(ttQuery);
            Log.d(TAG, "Created table " + Constants.TABLE_TIMTABLE + " Result: " + countResult);
            
            System.out.println();
            
            // Create a Scheduling table to keep all schedules
			String scheduleTable = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_SCHEDULE + " (" +
					Constants.SCHEDULE_ID + " INT PRIMARY KEY NOT NULL AUTO_INCREMENT," +
					Constants.STARTDATE + " VARCHAR(255)," +
					Constants.DEADLINE + " VARCHAR(25)," +
					Constants.ACTIVITY + " BOOLEAN)";
			countResult = statement.executeUpdate(scheduleTable);
			Log.d(TAG, "Created table " + Constants.TABLE_SCHEDULE + " Result: " + countResult);
			
			System.out.println();
			
			// Create a Scheduling table to keep all schedules
			String scheduleTableLec = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_SCHEDULE_LEC + " (" +
					Constants.SCHEDULE_ID + " INT PRIMARY KEY NOT NULL AUTO_INCREMENT," +
					Constants.STARTDATE + " VARCHAR(255)," +
					Constants.DEADLINE + " VARCHAR(25)," +
					Constants.ACTIVITY + " BOOLEAN)";
			countResult = statement.executeUpdate(scheduleTableLec);
			Log.d(TAG, "Created table " + Constants.TABLE_SCHEDULE_LEC + " Result: " + countResult);
			
			System.out.println();

//            DELIMITER //
//            USE `timetabler`//
//            CREATE PROCEDURE GetTimetableForLecturer()
//            BEGIN
//            SELECT period, time, day, tt.unit_id, lu.lecturer_id FROM timetables tt
//            INNER JOIN lecturer_units lu
//            ON tt.unit_id=lu.unit_id;
//            END//
//
//                    DELIMITER ;
//            String timetableForLecturer = " CREATE PROCEDURE "+Constants.TIME_TABLE_FOR_LECTURER+"(IN id CHAR(25)) " +
//                    " BEGIN " +
//                    " SELECT DISTINCT "+Constants.PERIOD+", "+Constants.TIME+", " + Constants.DAY+", " +
//                    "tt."+Constants.UNIT_ID+", " +
//                    "lu." + Constants.LECTURER_ID+", cu."+Constants.CLASS_ID+ " " +
//                    "FROM "+Constants.TABLE_TIMTABLE+" tt " +
//                    "INNER JOIN "+Constants.TABLE_LECTURER_UNITS+" lu " +
//                    "ON tt."+Constants.UNIT_ID+"=lu."+Constants.UNIT_ID+" " +
//                    "INNER JOIN "+Constants.TABLE_CLASS_UNITS+" cu " +
//                    "ON tt."+Constants.UNIT_ID+"=cu."+Constants.UNIT_ID+" " +
//                    "WHERE " + Constants.LECTURER_ID + "=id;" +
//                    "END";
//            countResult = statement.executeUpdate(timetableForLecturer);
//            Log.d(TAG, "Created stored procedure " + Constants.TIME_TABLE_FOR_LECTURER + " Result: " + countResult);
//
//            System.out.println();
//
//            String timetablerForStudents = " CREATE PROCEDURE "+Constants.TIME_TABLE_FOR_STUDENTS+"(IN id CHAR(25)) " +
//                    "BEGIN " +
//                    "SELECT DISTINCT "+Constants.PERIOD+", "+Constants.TIME+", " + Constants.DAY+ ", " +
//                    "tt."+Constants.UNIT_ID+", " +
//                    "lu." + Constants.STUDENT_ID+", cu."+Constants.CLASS_ID+ " " +
//                    "FROM "+Constants.TABLE_TIMTABLE+" tt " +
//                    "INNER JOIN "+Constants.TABLE_STUDENT_UNITS+" su " +
//                    "ON tt."+Constants.UNIT_ID+"=su."+Constants.UNIT_ID+" " +
//                    "INNER JOIN "+Constants.TABLE_CLASS_UNITS+" cu " +
//                    "ON tt."+Constants.UNIT_ID+"=cu."+Constants.UNIT_ID+" " +
//                    "WHERE " + Constants.STUDENT_ID + "=id;" +
//                    "END";
//            countResult = statement.executeUpdate(timetablerForStudents);
//            Log.d(TAG, "Created stored procedure " + Constants.TIME_TABLE_FOR_STUDENTS + " Result: " + countResult);

            if (env.equals("dev")) {
                PopulateEntitiesForTests.populateEntities();
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
