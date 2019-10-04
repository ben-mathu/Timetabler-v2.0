package com.bernard.timetabler.dbinit;

import com.bernard.timetabler.dbinit.model.*;
import com.bernard.timetabler.dbinit.model.campus.Campus;
import com.bernard.timetabler.dbinit.model.course.Unit;
import com.bernard.timetabler.dbinit.model.department.Department;
import com.bernard.timetabler.dbinit.model.faculty.Faculty;
import com.bernard.timetabler.dbinit.model.lecturer.Lecturer;
import com.bernard.timetabler.dbinit.model.programme.Programme;
import com.bernard.timetabler.dbinit.model.relationships.ClassUnit;
import com.bernard.timetabler.dbinit.model.relationships.LecturerProgramme;
import com.bernard.timetabler.dbinit.model.relationships.LecturerUnit;
import com.bernard.timetabler.dbinit.model.relationships.StudentUnit;
import com.bernard.timetabler.dbinit.model.room.Class;
import com.bernard.timetabler.dbinit.model.student.Student;
import com.bernard.timetabler.utils.Log;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generate data to emulate real world environment testing
 * Ultimately these data will be input by the user
 */
public class PopulateDb {
	private static final String TAG = PopulateDb.class.getSimpleName();
    private CreateSchemaTimeTabler ct;
    private GenerateEntityData generateEntityData;

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

    public PopulateDb() {
        CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
        generateEntityData = new GenerateEntityData();

        ct = new CreateSchemaTimeTabler("ben", "");
        statement = ct.getStatement();
    }

    /**
     * Method: Populate Campuses
     */
    public boolean populateCampuses() throws SQLException {
        campuses = generateEntityData.populateCampus();
        int result = 0;

        // Delete records before generating data
        String del = "DELETE FROM " + Constants.TABLE_CAMPUS;
        int records = statement.executeUpdate(del);
        
        Log.d(TAG, "Records affected " + records);
        
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
            String strQuery = "INSERT INTO " + Constants.TABLE_CAMPUS
            		+ " VALUES ('" + campus.getCampusId()
                    + "', '" + strReplace
                    + "'," + campus.isRemoved() + ")";

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
        faculties = generateEntityData.populateFaculty();
        int result = 0;
        
     // Delete records before generating data
        String del = "DELETE FROM " + Constants.TABLE_FACULTIES;
        int records = statement.executeUpdate(del);
        
        Log.d(TAG, "Records affected " + records);

        System.out.println("Populating db table: " + Constants.TABLE_FACULTIES);
        for (Faculty faculty : faculties) {
            String strQuery = "INSERT INTO " + Constants.TABLE_FACULTIES +
                    " VALUES ('" + faculty.getFacultyId() +
                    "', '" + faculty.getFacultyName() +
                    "', '" + faculty.getCampusId() +
                    "'," + faculty.isRemoved() + ")";

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
        halls = generateEntityData.populateHalls();
        int result = 0;
        
     // Delete records before generating data
        String del = "DELETE FROM " + Constants.TABLE_HALLS;
        int records = statement.executeUpdate(del);
        
        Log.d(TAG, "Records affected " + records);

        System.out.println("Populating db table: " + Constants.TABLE_HALLS);
        for (Hall hall : halls) {
            String strQuery = "INSERT INTO " + Constants.TABLE_HALLS +
                    " VALUES ('" + hall.getHallId() +
                    "', '" + hall.getHallName() +
                    "', '" + hall.getFacultyId() +
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
        classes = generateEntityData.populateClasses();
        int result = 0;
        
     // Delete records before generating data
        String del = "DELETE FROM " + Constants.TABLE_CLASSES;
        int records = statement.executeUpdate(del);
        
        Log.d(TAG, "Records affected " + records);

        System.out.println("Populating db table: " + Constants.TABLE_CLASSES);
        for (Class item : classes) {
            String strQuery = "INSERT INTO " + Constants.TABLE_CLASSES +
                    " VALUES ('" + item.getId() +
                    "', '" + item.getHall_id() +
                    "', '" + item.getFacultyId() +
                    "', '" + item.getVolume() +
                    "', " + item.isAvailability() +
                    ", " + item.isLab() +
                    "'," + item.isRemoved() + ")";

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
        departments = generateEntityData.populateDepartments();
        int result = 0;
        
     // Delete records before generating data
        String del = "DELETE FROM " + Constants.TABLE_DEPARTMENTS;
        int records = statement.executeUpdate(del);
        
        Log.d(TAG, "Records affected " + records);

        System.out.println("Populating db table: " + Constants.TABLE_DEPARTMENTS);
        for (Department department : departments) {
            String strQuery = "INSERT INTO " + Constants.TABLE_DEPARTMENTS +
                    " VALUES ('" + department.getDepartmentId() +
                    "', '" + department.getDepartmentName() +
                    "', '" + department.getFacultyId() +
                    "'," + department.isRemoved() + ")";

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
        lecturers = generateEntityData.populateLecturers();
        int result = 0;
        
     // Delete records before generating data
        String del = "DELETE FROM " + Constants.TABLE_LECTURERS;
        int records = statement.executeUpdate(del);
        
        Log.d(TAG, "Records affected " + records);

        System.out.println("Populating db table: " + Constants.TABLE_LECTURERS);
        for (Lecturer lecturer : lecturers) {
            String strQuery = "INSERT INTO " + Constants.TABLE_LECTURERS +
                    " VALUES ('" + lecturer.getId() +
                    "', '" + lecturer.getFirstName() +
                    "', '" + lecturer.getLastName() +
                    "', '" + lecturer.getMiddleName() +
                    "', '" + lecturer.getUsername() +
                    "', '" + lecturer.getPassword() +
                    "', '" + lecturer.getUsername() + "@gmail.com" +
                    "', '" + lecturer.getFacultyId() +
                    "', '" + lecturer.getDepartmentId() +
                    "', " + lecturer.isInSesson() +
                    "," + lecturer.isRemoved() + ")";
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
        programmes = generateEntityData.populateProgrammes();
        int result = 0;
        
     // Delete records before generating data
        String del = "DELETE FROM " + Constants.TABLE_PROGRAMMES;
        int records = statement.executeUpdate(del);
        
        Log.d(TAG, "Records affected " + records);

        System.out.println("Populating db table: " + Constants.TABLE_PROGRAMMES);
        for (Programme programme : programmes) {
            String strQuery = "INSERT INTO " + Constants.TABLE_PROGRAMMES +
                    " VALUES ('" + programme.getProgrammeId() +
                    "', '" + programme.getProgrammeName() +
                    "', '" + programme.getDepartmentId() +
                    "', '" + programme.getFacultyId() +
                    "'," + programme.isRemoved() + ")";
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
        students = generateEntityData.populateStudents();
        int result = 0;
        
     // Delete records before generating data
        String del = "DELETE FROM " + Constants.TABLE_STUDENTS;
        int records = statement.executeUpdate(del);
        
        Log.d(TAG, "Records affected " + records);

        System.out.println("Populating db table: " + Constants.TABLE_STUDENTS);

        for (Student student : students) {
            String strQuery = "INSERT INTO " + Constants.TABLE_STUDENTS +
                    " VALUES ('" + student.getStudentId() +
                    "', '" + student.getFname() +
                    "', '" + student.getLname() +
                    "', '" + student.getMname() +
                    "', '" + student.getUsername() +
                    "', '" + student.getPassword() +
                    "', '" + student.getUsername() + "@gmail.com" +
                    "', " + student.isInSession() +
                    ", '" + student.getDepartmentId() +
                    "', '" + student.getCampusId() +
                    "', '" + student.getFacultyId() +
                    "', '" + student.getProgrammeId() +
                    "', '" + student.getYearOfStudy() +
                    "', '" + student.getAdmissionDate() +
                    "'," + student.isRemoved() + ")";
            result += statement.executeUpdate(strQuery);
        }

        if (result != 0) {
            System.out.println("Successfully updated table " + Constants.TABLE_STUDENTS + ": " + result);
            return true;
        }

        return false;
    }

    public boolean populateUnits() throws SQLException {
        units = generateEntityData.populateUnits();
        int result = 0;
        
     // Delete records before generating data
        String del = "DELETE FROM " + Constants.TABLE_UNITS;
        int records = statement.executeUpdate(del);
        
        Log.d(TAG, "Records affected " + records);

        System.out.println("Populating db table: " + Constants.TABLE_UNITS);
        for (Unit unit : units) {
            String strQuery = "INSERT INTO " + Constants.TABLE_UNITS +
                    " VALUES ('" + unit.getId() +
                    "', '" + unit.getUnitName() +
                    "', '" + unit.getProgrammeId() +
                    "', '" + unit.getFacultyId() +
                    "', '" + unit.getDepartmentId() +
                    "', " + unit.isPractical() +
                    ", " + unit.isCommon() +
                    ", " + unit.isRemoved() +
                    "," + unit.isRemoved() + ")";
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
        lecturerProgrammes = generateEntityData.populateLecturerProgrammes();
        int result = 0;
        
     // Delete records before generating data
        String del = "DELETE FROM " + Constants.TABLE_LECTURER_PROGRAMMES;
        int records = statement.executeUpdate(del);
        
        Log.d(TAG, "Records affected " + records);

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
    public boolean populateClassUnits(List<ClassUnit> classUnits) throws SQLException {
//        classUnits = populateDatabase.populateClassUnits();
        int result = 0;
        
     // Delete records before generating data
        String del = "DELETE FROM " + Constants.TABLE_CLASS_UNITS;
        int records = statement.executeUpdate(del);
        
        Log.d(TAG, "Records affected " + records);

        System.out.println("Populating db table: " + Constants.TABLE_CLASS_UNITS);

        if (classUnits != null) {
            for (ClassUnit classUnit : classUnits) {
                String strQuery = "INSERT INTO " + Constants.TABLE_CLASS_UNITS +
                        " VALUES ('" + classUnit.getClassId() +
                        "', '" + classUnit.getUnitId() +
                        "', '" + classUnit.getHallId() +
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
        lecturerUnits = generateEntityData.populateLecturerUnits();
        int result = 0;
        
     // Delete records before generating data
        String del = "DELETE FROM " + Constants.TABLE_LECTURER_UNITS;
        int records = statement.executeUpdate(del);
        
        Log.d(TAG, "Records affected " + records);

        System.out.println("Populating db table: " + Constants.TABLE_LECTURER_UNITS);
        for (LecturerUnit lecturerUnit : lecturerUnits) {
            String strQuery = "INSERT INTO " + Constants.TABLE_LECTURER_UNITS +
                    " VALUES ('" + lecturerUnit.getLecturerId() +
                    "', '" + lecturerUnit.getUnitId() +
                    "'," + lecturerUnit.isRemoved() +
                    ")";
            result += statement.executeUpdate(strQuery);
        }

        if (result != 0) {
            System.out.println("Successfully updated table " + Constants.TABLE_LECTURER_UNITS + ": " + result);
            return true;
        }

        return false;
    }

    public boolean populateStudentUnits() throws SQLException {
        studentUnits = generateEntityData.populateStudentUnits();
        int result = 0;
        
     // Delete records before generating data
        String del = "DELETE FROM " + Constants.TABLE_STUDENT_UNITS;
        int records = statement.executeUpdate(del);
        
        Log.d(TAG, "Records affected " + records);

        System.out.println("Populating db table: " + Constants.TABLE_STUDENT_UNITS);
        for (StudentUnit studentUnit : studentUnits) {
            String strQuery = "INSERT INTO " + Constants.TABLE_STUDENT_UNITS +
                    " VALUES ('" + studentUnit.getStudentId() +
                    "', '" + studentUnit.getUnitId() +
                    "', ''," + studentUnit.isRemoved() + ")";
            result += statement.executeUpdate(strQuery);
        }

        if (result != 0) {
            System.out.println("Successfully updated table " + Constants.TABLE_STUDENT_UNITS + ": " + result);
            return true;
        }

        return false;
    }
}
