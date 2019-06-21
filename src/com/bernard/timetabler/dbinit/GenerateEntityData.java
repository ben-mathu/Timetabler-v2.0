package com.bernard.timetabler.dbinit;

import com.bernard.timetabler.dbinit.model.*;
import com.bernard.timetabler.dbinit.model.Class;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generate data to emulate real world environment testing
 * Ultimately these data will be input by the user
 */
public class GenerateEntityData {
    private CreateSchemaTimeTabler ct;
    private PopulateDatabase populateDatabase;

    private List<Student> students;
    private List<Programme> programmes;
    private List<Lecturer> lecturers;
    private List<Faculty> faculties;
    private List<Unit> units;
    private List<Department> departments;
    private List<Class> classes;
    private List<Campus> campuses;
    //relationship class - unit
    private List<ClassUnit> classUnits;
    private List<Hall> halls;
    // relationship lecturer - programmes
    private List<LecturerProgramme> lecturerProgrammes;
    // relationship lecturer - units
    private List<LecturerUnit> lecturerUnits;
    // relationship student - units
    private List<StudentUnit> studentUnits;
    private Statement statement;

    public GenerateEntityData() {
        CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
        populateDatabase = new PopulateDatabase();

        ct = new CreateSchemaTimeTabler();
        statement = ct.getStatement();
    }

    /**
     * Method: Populate Campuses
     */
    public boolean populateCampuses() throws SQLException {
        campuses = populateDatabase.populateCampus();
        int result = 0;

        System.out.println("Populating db table: " + Constants.TABLE_CAMPUS);
        for (Campus campus : campuses) {
            String strReplace = "";
            if (campus.getCampusName().contains("'")) {
                Pattern pattern = Pattern.compile("(\\w+)'(\\w+)");
                Matcher matcher = pattern.matcher(campus.getCampusName());

                if (matcher.find()) {
                    strReplace = matcher.group(1) + "''" + matcher.group(2);
                } else {
                    strReplace = campus.getCampusName();
                }
            } else {
                strReplace = campus.getCampusName();
            }
            String strQuery = "INSERT INTO " + Constants.TABLE_CAMPUS +
                    " VALUES ('" + campus.getCampusId() + "', '" + strReplace + "')";

            result += statement.executeUpdate(strQuery);
        }

        if (result != 0) {
            System.out.println("Successfully update table " + Constants.TABLE_CAMPUS + ": " + result);
            return true;
        }

        return false;
    }

    /**
     * Method: Populate Faculty
     */
    public boolean populateFaculties() throws SQLException {
        faculties = populateDatabase.populateFaculty();
        int result = 0;

        System.out.println("Populating db table: " + Constants.TABLE_FACULTIES);
        for (Faculty faculty : faculties) {
            String strQuery = "INSERT INTO " + Constants.TABLE_FACULTIES +
                    " VALUES ('" + faculty.getFacultyId() +
                    "', '" + faculty.getFacultyName() +
                    "', '" + faculty.getCampusId() +
                    "')";

            result += statement.executeUpdate(strQuery);
        }

        if (result != 0) {
            System.out.println("Successfully update table " + Constants.TABLE_FACULTIES + ": " + result);
            return true;
        }

        return false;
    }

    /**
     * Method: Populate Halls
     */
    public boolean populateHalls() throws SQLException {
        halls = populateDatabase.populateHalls();
        int result = 0;

        System.out.println("Populating db table: " + Constants.TABLE_HALLS);
        for (Hall hall : halls) {
            String strQuery = "INSERT INTO " + Constants.TABLE_HALLS +
                    " VALUES ('" + hall.getHallId() +
                    "', '" + hall.getHallName() +
                    "', '" + hall.getFacultyId()+
                    "')";

            result += statement.executeUpdate(strQuery);
        }

        if (result != 0) {
            System.out.println("Successfully update table " + Constants.TABLE_HALLS + ": " + result);
            return true;
        }

        return false;
    }

    /**
     * Method: Populate Classes
     */
    public boolean populateClasses() throws SQLException {
        classes = populateDatabase.populateClasses();
        int result = 0;

        System.out.println("Populating db table: " + Constants.TABLE_CLASSES);
        for (Class item : classes) {
            String strQuery = "INSERT INTO " + Constants.TABLE_CLASSES +
                    " VALUES ('" + item.getId() +
                    "', '" + item.getHall_id() +
                    "', '" + item.getFacultyId() +
                    "', '" + item.getVolume() +
                    "', " + item.isAvailability() +
                    ", " + item.isLab() +
                    ")";

            result += statement.executeUpdate(strQuery);
        }

        if (result != 0) {
            System.out.println("Successfully update table " + Constants.TABLE_CLASSES + ": " + result);
            return true;
        }

        return false;
    }

    /**
     * Method: Populate Departments
     */
    public boolean populateDepartments() throws SQLException {
        departments = populateDatabase.populateDepartments();
        int result = 0;

        System.out.println("Populating db table: " + Constants.TABLE_DEPARTMENTS);
        for (Department department : departments) {
            String strQuery = "INSERT INTO " + Constants.TABLE_DEPARTMENTS +
                    " VALUES ('" + department.getDepartmentId() +
                    "', '" + department.getDepartmentName() +
                    "', '" + department.getFacultyId() + "')";

            result += statement.executeUpdate(strQuery);
        }

        if (result != 0) {
            System.out.println("Successfully update table " + Constants.TABLE_DEPARTMENTS + ": " + result);
            return true;
        }

        return false;
    }

    /**
     * Method: Populate Lecturers
     */
    public boolean populateLecturers() throws SQLException {
        lecturers = populateDatabase.populateLecturers();
        int result = 0;

        System.out.println("Populating db table: " + Constants.TABLE_LECTURERS);
        for (Lecturer lecturer : lecturers) {
            String strQuery = "INSERT INTO " + Constants.TABLE_LECTURERS +
                    " VALUES ('" + lecturer.getId() +
                    "', '" + lecturer.getFirstName() +
                    "', '" + lecturer.getLastName() +
                    "', '" + lecturer.getMiddleName() +
                    "', '" + lecturer.getUsername() +
                    "', '" + lecturer.getPassword() +
                    "', '" + lecturer.getFacultyId() +
                    "', '" + lecturer.getDepartmentId() +
                    "', " + lecturer.isInSesson() +
                    ")";
            result += statement.executeUpdate(strQuery);
        }

        if (result != 0) {
            System.out.println("Successfully updated table " + Constants.TABLE_LECTURERS + ": " + result);
            return true;
        }

        return false;
    }

    /**
     * Method: Programmes
     */
    public boolean populateProgrammes() throws SQLException {
        programmes = populateDatabase.populateProgrammes();
        int result = 0;

        System.out.println("Populating db table: " + Constants.TABLE_PROGRAMMES);
        for (Programme programme : programmes) {
            String strQuery = "INSERT INTO " + Constants.TABLE_PROGRAMMES +
                    " VALUES ('" + programme.getProgrammeId() +
                    "', '" + programme.getProgrammeName() +
                    "', '" + programme.getDepartmentId() +
                    "', '" + programme.getFacultyId() +
                    "')";
            result += statement.executeUpdate(strQuery);
        }

        if (result != 0) {
            System.out.println("Successfully updated table " + Constants.TABLE_PROGRAMMES + ": " + result);
            return true;
        }

        return false;
    }

    /**
     * Method: Students
     */
    public boolean populateStudents() throws SQLException {
        students = populateDatabase.populateStudents();
        int result = 0;

        System.out.println("Populating db table: " + Constants.TABLE_STUDENTS);

        for (Student student : students) {
            String strQuery = "INSERT INTO " + Constants.TABLE_STUDENTS +
                    " VALUES ('" + student.getStudentId() +
                    "', '" + student.getFname() +
                    "', '" + student.getLname() +
                    "', '" + student.getMname() +
                    "', '" + student.getUsername() +
                    "', '" + student.getPassword() +
                    "', " + student.isInSession() +
                    ", '" + student.getDepartmentId() +
                    "', '" + student.getCampusId() +
                    "', '" + student.getFacultyId() +
                    "', '" + student.getProgrammeId() +
                    "', '" + student.getYearOfStudy() +
                    "', '" + student.getAdmissionDate() +
                    "')";
            result += statement.executeUpdate(strQuery);
        }

        if (result != 0) {
            System.out.println("Successfully updated table " + Constants.TABLE_STUDENTS + ": " + result);
            return true;
        }

        return false;
    }

    public boolean populateUnits() throws SQLException {
        units = populateDatabase.populateUnits();
        int result = 0;

        System.out.println("Populating db table: " + Constants.TABLE_UNITS);
        for (Unit unit : units) {
            String strQuery = "INSERT INTO " + Constants.TABLE_UNITS +
                    " VALUES ('" + unit.getId() +
                    "', '" + unit.getUnitName() +
                    "', '" + unit.getProgrammeId() +
                    "', '" + unit.getFacultyId() +
                    "', '" + unit.getDepartmentId() +
                    "', " + unit.isPractical() +
                    ")";
            result += statement.executeUpdate(strQuery);
        }

        if (result != 0) {
            System.out.println("Successfully updated table " + Constants.TABLE_UNITS + ": " + result);
            return true;
        }

        return false;
    }

    /**
     * Method: Lecturer Programmes relationships
     * @throws SQLException
     */
    public boolean populateLecturerProgrammes() throws SQLException {
        lecturerProgrammes = populateDatabase.populateLecturerProgrammes();
        int result = 0;

        System.out.println("Populating db table: " + Constants.TABLE_LECTURER_PROGRAMMES);
        for (LecturerProgramme lecturerProgramme : lecturerProgrammes) {
            String strQuery = "INSERT INTO " + Constants.TABLE_LECTURER_PROGRAMMES +
                    " VALUES ('" + lecturerProgramme.getLecturerId() +
                    "', '" + lecturerProgramme.getProgrammeId() +
                    "')";
            result += statement.executeUpdate(strQuery);
        }

        if (result != 0) {
            System.out.println("Successfully updated table " + Constants.TABLE_LECTURER_PROGRAMMES + ": " + result);
            return true;
        }

        return false;
    }

    /**
     * Method: Class Units
     */
    public boolean populateClassUnits() throws SQLException {
        classUnits = populateDatabase.populateClassUnits();
        int result = 0;

        System.out.println("Populating db table: " + Constants.TABLE_CLASS_UNITS);

        if (classUnits != null) {
            for (ClassUnit classUnit : classUnits) {
                String strQuery = "INSERT INTO " + Constants.TABLE_CLASS_UNITS +
                        " VALUES ('" + classUnit.getClassId() +
                        "', '" + classUnit.getUnitId() +
                        "')";
                result += statement.executeUpdate(strQuery);
            }
        }

        if (result != 0) {
            System.out.println("Successfully updated table " + Constants.TABLE_CLASS_UNITS + ": " + result);
            return true;
        }

        return false;
    }

    public boolean populateLecturerUnits() throws SQLException {
        lecturerUnits = populateDatabase.populateLecturerUnits();
        int result = 0;

        System.out.println("Populating db table: " + Constants.TABLE_LECTURER_UNITS);
        for (LecturerUnit lecturerUnit : lecturerUnits) {
            String strQuery = "INSERT INTO " + Constants.TABLE_LECTURER_UNITS +
                    " VALUES ('" + lecturerUnit.getLecturerId() +
                    "', '" + lecturerUnit.getUnitId() +
                    "')";
            result += statement.executeUpdate(strQuery);
        }

        if (result != 0) {
            System.out.println("Successfully updated table " + Constants.TABLE_LECTURER_UNITS + ": " + result);
            return true;
        }

        return false;
    }

    public boolean populateStudentUnits() throws SQLException {
        studentUnits = populateDatabase.populateStudentUnits();
        int result = 0;

        System.out.println("Populating db table: " + Constants.TABLE_STUDENT_UNITS);
        for (StudentUnit studentUnit : studentUnits) {
            String strQuery = "INSERT INTO " + Constants.TABLE_STUDENT_UNITS +
                    " VALUES ('" + studentUnit.getStudentId() +
                    "', '" + studentUnit.getUnitId() +
                    "')";
            result += statement.executeUpdate(strQuery);
        }

        if (result != 0) {
            System.out.println("Successfully updated table " + Constants.TABLE_STUDENT_UNITS + ": " + result);
            return true;
        }

        return false;
    }
}
