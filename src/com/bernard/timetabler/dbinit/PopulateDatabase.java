package com.bernard.timetabler.dbinit;

import com.bernard.timetabler.dbinit.model.*;
import com.bernard.timetabler.dbinit.model.Class;
import com.bernard.timetabler.dbinit.utils.GenerateAlphanumeric;
import com.bernard.timetabler.utils.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PopulateDatabase {
	private static final String TAG = PopulateDatabase.class.getSimpleName();
	
    private static ThreadLocalRandom rand;
    private Statement statement;

    private static String[] campusNames = new String[]{"Lang'ata Campus", "Town Campus", "Gaba Campus"};
    private static String[] hallNames = new String[]{"Tzadjua Hall", "Otunga Hall", "Missio Hall", "Jubilee Hall"};
    private static String[] facultyNames = new String[]{"Theology", "Arts and Social Sciences", "School of Business", "Education", "Law", "Science", "Center for Social Justice and ethics", "Institute of Canon Law", "Institute of Regional Integration and Development", "Library and Information Science", "School of Graduate Studies"};

    public PopulateDatabase() {
        CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
        
        CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler();
        statement = ct.getStatement();
    }

    public List<Campus> populateCampus() {
        List<Campus> campuses = new ArrayList<>();

        for (int i = 0; i < campusNames.length; i++) {
            Campus campus = new Campus();
            campus.setCampusId(GenerateAlphanumeric.generateIdAlphanumeric(5));
            campus.setCampusName(campusNames[i]);
            campuses.add(campus);
        }

        return campuses;
    }

    public List<Faculty> populateFaculty() throws SQLException {
        List<Faculty> facultyList = new ArrayList<>();
        List<String> campusId = new ArrayList<>();

        System.out.println("Querying db for " + Constants.FACULTY_ID);
        String strFacultyId = "SELECT " + Constants.CAMPUS_ID + " FROM " + Constants.TABLE_CAMPUS;
        ResultSet resultSet = statement.executeQuery(strFacultyId);

        while (resultSet.next()) {
            campusId.add(resultSet.getString(Constants.CAMPUS_ID));
        }

        rand = ThreadLocalRandom.current();
        for (int i = 0; i < facultyNames.length; i++) {
            int randInt = 0;
            if (!campusId.isEmpty())
                randInt = rand.nextInt(0, campusId.size());
            Faculty faculty = new Faculty();
            faculty.setFacultyId(GenerateAlphanumeric.generateIdAlphanumeric(5));
            faculty.setFacultyName(facultyNames[i]);
            faculty.setCampusId(campusId.get(randInt));
            facultyList.add(faculty);
        }

        return facultyList;
    }

    public List<Hall>  populateHalls() throws SQLException {
        List<Hall> halls = new ArrayList<>();
        List<String> faculty_id = new ArrayList<>();

        System.out.println("Querying " + Constants.TABLE_FACULTIES);
        String strFacultyIdQuery = "SELECT " + Constants.FACULTY_ID + " FROM " + Constants.TABLE_FACULTIES;
        System.out.println("SQL query statement " + strFacultyIdQuery);

        ResultSet resultSet = statement.executeQuery(strFacultyIdQuery);

        while (resultSet.next()) {
            faculty_id.add(resultSet.getString(Constants.FACULTY_ID));
        }

        rand = ThreadLocalRandom.current();

        for (int i = 0; i < hallNames.length; i++) {
            int randomFacultyId = 0;
            if (!faculty_id.isEmpty())
                randomFacultyId = rand.nextInt(0, faculty_id.size());

            Hall hall = new Hall();
            hall.setHallId(GenerateAlphanumeric.generateIdAlphanumeric(5));
            hall.setHallName(hallNames[i]);
            hall.setFacultyId(faculty_id.get(randomFacultyId));
            halls.add(hall);
        }

        return halls;
    }

    public List<Class> populateClasses() throws SQLException {
        List<Class> classes = new ArrayList<>();
        List<String> hallId = new ArrayList<>();
        List<String> facultyId = new ArrayList<>();

        rand = ThreadLocalRandom.current();
        int randomItem, randomfacId = 0;

        String strClassItem = "Select " + Constants.HALL_ID + " FROM " + Constants.TABLE_HALLS;
        System.out.println("The SQL query is: " + strClassItem);

        ResultSet resultSet = statement.executeQuery(strClassItem);
        while (resultSet.next()) {
            hallId.add(resultSet.getString("hall_id"));
        }

        String strFacultyId = "SELECT " + Constants.FACULTY_ID + " FROM " + Constants.TABLE_FACULTIES;

        ResultSet resultSetFacId = statement.executeQuery(strFacultyId);
        while (resultSetFacId.next()) {
            facultyId.add(resultSetFacId.getString(Constants.FACULTY_ID));
        }

        for (int i = 0; i < 14 * hallNames.length * 0.25; i++) {
            randomItem = rand.nextInt(0, hallNames.length);

            if (!facultyId.isEmpty()) {
                randomfacId = rand.nextInt(0, facultyId.size());
            }

            Class item = new Class();
            item.setId(GenerateAlphanumeric.generateIdAlphanumeric(5));
            item.setHall_id(hallId.get(randomItem));
            item.setVolume(String.valueOf(rand.nextInt(45) + 15));
            item.setAvailability(rand.nextBoolean());
            item.setLab(rand.nextBoolean());
            item.setFacultyId(facultyId.get(randomfacId));
            classes.add(item);
        }

        return classes;
    }

    public List<Department> populateDepartments() throws SQLException {
        List<Department> departments = new ArrayList<>();
        List<String> facultyId = new ArrayList<>();

        rand = ThreadLocalRandom.current();
        int randomItem = 0;

        String strFacultyId = "SELECT " + Constants.FACULTY_ID + " FROM " + Constants.TABLE_FACULTIES;
        System.out.println("The SQL query is: " + strFacultyId);
        ResultSet resultSet = statement.executeQuery(strFacultyId);

        while (resultSet.next()) {
            facultyId.add(resultSet.getString("faculty_id"));
        }

        int length = rand.nextInt(10);

        for (int i = 0; i < length * facultyNames.length * 0.25; i++) {
            randomItem = rand.nextInt(0, facultyNames.length);
            Department item = new Department();
            item.setDepartmentId(GenerateAlphanumeric.generateIdAlphanumeric(5));
            item.setDepartmentName(GenerateAlphanumeric.generateRandomLongAlpha(45));
            item.setFacultyId(facultyId.get(randomItem));
            departments.add(item);
        }
        return departments;
    }

    public List<Lecturer> populateLecturers() throws SQLException {
        List<Lecturer> lecturers = new ArrayList<>();
        List<String> departmentId = new ArrayList<>();
        List<String> facultyId = new ArrayList<>();

        rand = ThreadLocalRandom.current();

        String strDepartmentId = "SELECT " + Constants.DEPARTMENT_ID + " FROM " + Constants.TABLE_DEPARTMENTS;
        System.out.println("SQL statement: " + strDepartmentId);
        ResultSet resultSet = statement.executeQuery(strDepartmentId);

        while (resultSet.next()) {
            departmentId.add(resultSet.getString("department_id"));
        }
        
        String queryFacultyId = "SELECT " + Constants.FACULTY_ID + " FROM " + Constants.TABLE_FACULTIES;
        Log.d(TAG, "SQL Statement: " + queryFacultyId);
        ResultSet resultProgId = statement.executeQuery(queryFacultyId);
        
        while (resultProgId.next()) {
        	facultyId.add(resultProgId.getString(Constants.FACULTY_ID));
        }

        int length = rand.nextInt(10);

        int randomItem = 0, randomFacultyId = 0;
        for (int i = 0; i < length * departmentId.size(); i++) {
            if (!departmentId.isEmpty())
                randomItem = rand.nextInt(0, departmentId.size());

            if (!facultyId.isEmpty())
            	randomFacultyId = rand.nextInt(0, facultyId.size() - 1);
            Lecturer lecturer = new Lecturer();
            lecturer.setId(GenerateAlphanumeric.generateIdAlphanumeric(5));
            lecturer.setFirstName(GenerateAlphanumeric.generateSingleShortAlpha(12));
            lecturer.setLastName(GenerateAlphanumeric.generateSingleShortAlpha(12));
            lecturer.setMiddleName(GenerateAlphanumeric.generateSingleShortAlpha(12));
            lecturer.setUsername(GenerateAlphanumeric.generateIdAlphanumeric(5));
            lecturer.setPassword(GenerateAlphanumeric.generateIdAlphanumeric(45));
            lecturer.setFacultyId(facultyId.get(randomFacultyId));
            lecturer.setDepartmentId(departmentId.get(randomItem));
            lecturer.setInSesson(rand.nextBoolean());
            lecturers.add(lecturer);
        }

        return lecturers;
    }

    public List<Programme> populateProgrammes() throws SQLException {
        List<Programme> programmes = new ArrayList<>();
        List<String> departmentId = new ArrayList<>();
        List<String> facultyId = new ArrayList<>();

        rand = ThreadLocalRandom.current();
        int randomItem = 0;

        String strDepartmentId = "SELECT " + Constants.DEPARTMENT_ID + " FROM " + Constants.TABLE_DEPARTMENTS;
        System.out.println("SQL statement: " + strDepartmentId);

        ResultSet resultSet = statement.executeQuery(strDepartmentId);
        while (resultSet.next()) {
            departmentId.add(resultSet.getString("department_id"));
        }

        for (String id : departmentId) {
            String strFacultyId = "SELECT " + Constants.FACULTY_ID + " FROM " + Constants.TABLE_DEPARTMENTS + " WHERE " + Constants.DEPARTMENT_ID + "='" + id + "'";
            ResultSet resultSetFacId = statement.executeQuery(strFacultyId);

            // move cursor to point to the next row
            resultSetFacId.next();
            String fId = resultSetFacId.getString(Constants.FACULTY_ID);
            if (!facultyId.contains(fId)) {
                facultyId.add(fId);
            }
        }

        int length = rand.nextInt(30);
        int randomfacId = 0;

        for (int i = 0; i < length * departmentId.size() * 0.25; i++) {
            if (!departmentId.isEmpty())
                randomItem = rand.nextInt(0, departmentId.size());
            if (!facultyId.isEmpty())
                randomfacId = rand.nextInt(0, facultyId.size());

            Programme programme = new Programme();
            programme.setProgrammeId(GenerateAlphanumeric.generateIdAlphanumeric(5));
            programme.setProgrammeName(GenerateAlphanumeric.generateRandomLongAlpha(45));
            programme.setDepartmentId(departmentId.get(randomItem));
            programme.setFacultyId(facultyId.get(randomfacId));
            programmes.add(programme);
        }
        return programmes;
    }

    public List<Student> populateStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        List<String> campusId = new ArrayList<>();
        List<String> programmeId = new ArrayList<>();
        List<String> departmentId = new ArrayList<>();
        List<String> facultyId = new ArrayList<>();

        int randomCampusId = 0, randomProgrammeId = 0, randomDepartmentId = 0, randomIndexFacultyId = 0;

        String strCampusId = "SELECT " + Constants.CAMPUS_ID + " FROM " + Constants.TABLE_CAMPUS;
        String strProgrammeId = "SELECT " + Constants.PROGRAMME_ID + " FROM " + Constants.TABLE_PROGRAMMES;
        System.out.println("SQL statement:\n" + strCampusId +
                "\n" + strProgrammeId);

        ResultSet resultSetCampus = statement.executeQuery(strCampusId);
        while (resultSetCampus.next()) {
            campusId.add(resultSetCampus.getString(Constants.CAMPUS_ID));
        }

        ResultSet resultSetProgramme = statement.executeQuery(strProgrammeId);
        while (resultSetProgramme.next()) {
            programmeId.add(resultSetProgramme.getString(Constants.PROGRAMME_ID));
        }


        for (String id : programmeId) {
            String strFacultyId = "SELECT " + Constants.FACULTY_ID + " FROM " + Constants.TABLE_PROGRAMMES +
                    " WHERE " + Constants.PROGRAMME_ID + "='" + id + "'";
            ResultSet resultSetFaculty = statement.executeQuery(strFacultyId);
            if (resultSetFaculty.next()) {
                facultyId.add(resultSetFaculty.getString(Constants.FACULTY_ID));
            }

            String strDepartmentId = "SELECT " + Constants.DEPARTMENT_ID + " FROM " + Constants.TABLE_PROGRAMMES +
                    " WHERE " + Constants.PROGRAMME_ID + "='" + id + "'";
            ResultSet resultSetDepartment = statement.executeQuery(strDepartmentId);
            while (resultSetDepartment.next()) {
                departmentId.add(resultSetDepartment.getString(Constants.DEPARTMENT_ID));
            }
        }

        int length = rand.nextInt(1500);

        for (int i = 0; i < length; i++) {
            if (!campusId.isEmpty())
                randomCampusId = rand.nextInt(0, campusId.size() - 1);

            if (!departmentId.isEmpty())
                randomDepartmentId = rand.nextInt(0, departmentId.size() - 1);

            if (!programmeId.isEmpty())
                randomProgrammeId = rand.nextInt(0, programmeId.size() - 1);

            if (!facultyId.isEmpty())
                randomIndexFacultyId = rand.nextInt(0, facultyId.size() - 1);

            Student student = new Student();
            student.setStudentId(GenerateAlphanumeric.generateIdAlphanumeric(5));
            student.setFname(GenerateAlphanumeric.generateSingleShortAlpha(12));
            student.setLname(GenerateAlphanumeric.generateSingleShortAlpha(12));
            student.setMname(GenerateAlphanumeric.generateSingleShortAlpha(12));
            student.setUsername(GenerateAlphanumeric.generateIdAlphanumeric(5));
            student.setPassword(GenerateAlphanumeric.generateIdAlphanumeric(45));
            student.setInSession(rand.nextBoolean());
            student.setYearOfStudy(String.valueOf(rand.nextInt(5)));
            student.setDepartmentId(departmentId.get(randomDepartmentId));
            student.setProgrammeId(programmeId.get(randomProgrammeId));
            student.setCampusId(campusId.get(randomCampusId));
            student.setFacultyId(facultyId.get(randomIndexFacultyId));
            student.setAdmissionDate(GenerateAlphanumeric.setAdmissionDate(student.getYearOfStudy()));
            students.add(student);
        }
        return students;
    }

    public List<Unit> populateUnits() throws SQLException {
        List<Unit> units = new ArrayList<>();
        List<String> programmeId = new ArrayList<>();
        List<String> facultyId = new ArrayList<>();

        int randomItem = 0;

        String strProgrammeId = "SELECT " + Constants.PROGRAMME_ID + " FROM " + Constants.TABLE_PROGRAMMES;
        System.out.println("SQL statement: " + strProgrammeId);
        ResultSet resultSet = statement.executeQuery(strProgrammeId);

        while (resultSet.next()) {
            programmeId.add(resultSet.getString(Constants.PROGRAMME_ID));
        }

        String strFacultyId = "SELECT " + Constants.FACULTY_ID + " FROM " + Constants.TABLE_PROGRAMMES;
        System.out.println("Query statement " + strFacultyId);
        ResultSet resultSetFaculty = statement.executeQuery(strFacultyId);

        while (resultSetFaculty.next()) {
            facultyId.add(resultSetFaculty.getString(Constants.FACULTY_ID));
        }

        int length = rand.nextInt(40);

        for (int i = 0; i < length * programmeId.size() * 0.25; i++) {
            int randomIndex = 0;
            if (!programmeId.isEmpty() || !facultyId.isEmpty()) {
                randomItem = rand.nextInt(0, programmeId.size());
                randomIndex = rand.nextInt(0, facultyId.size());
            }

            Unit unit = new Unit();
            unit.setId(GenerateAlphanumeric.generateIdAlphanumeric(5));
            unit.setUnitName(GenerateAlphanumeric.generateRandomLongAlpha(45));
            unit.setProgrammeId(programmeId.get(randomItem));
            unit.setFacultyId(facultyId.get(randomIndex));
            unit.setPractical(rand.nextBoolean());
            units.add(unit);
        }
        return units;
    }

    public List<LecturerProgramme> populateLecturerProgrammes() throws SQLException {
        List<LecturerProgramme> lecturerProgrammes = new ArrayList<>();
        List<String> lectureId = new ArrayList<>();
        List<String> programmeId = new ArrayList<>();

        int randomProgrammeId = 0;

        String strQueryLec = "SELECT " + Constants.LECTURER_ID + " FROM " + Constants.TABLE_LECTURERS;
        String strQueryProg = "SELECT " + Constants.PROGRAMME_ID  + " FROM " + Constants.TABLE_PROGRAMMES;
        System.out.println("SQL statement:\n" + strQueryLec + "\n" + strQueryProg);

        ResultSet resultSetLec = statement.executeQuery(strQueryLec);
        while (resultSetLec.next()) {
            lectureId.add(resultSetLec.getString(Constants.LECTURER_ID));
        }

        ResultSet resultSetProg = statement.executeQuery(strQueryProg);
        while (resultSetProg.next()) {
            programmeId.add(resultSetProg.getString(Constants.PROGRAMME_ID));
        }

        for (String s : lectureId) {
            if (!programmeId.isEmpty())
                randomProgrammeId = rand.nextInt(0, programmeId.size());

            LecturerProgramme lecturerProgramme = new LecturerProgramme();
            lecturerProgramme.setLecturerId(s);
            lecturerProgramme.setProgrammeId(programmeId.get(randomProgrammeId));

            lecturerProgrammes.add(lecturerProgramme);
        }
        return lecturerProgrammes;
    }

    public List<ClassUnit> populateClassUnits() throws SQLException {
        // TODO: requires that classes - unit relationship be generated by an algorithm
        List<ClassUnit> classUnits = new ArrayList<>();
        List<String> classId = new ArrayList<>();
        List<String> unitId = new ArrayList<>();

        int randomUnitId;

        String strQuery = "SELECT " + Constants.CLASS_ID  + ", " + Constants.UNIT_ID + " FROM " + Constants.TABLE_CLASSES + ", " + Constants.TABLE_UNITS;
        System.out.println("SQL statement: " + strQuery);
        ResultSet resultSet = statement.executeQuery(strQuery);

        while (resultSet.next()) {
            classId.add(resultSet.getString(Constants.CLASS_ID));
            unitId.add(resultSet.getString(Constants.UNIT_ID));
        }

        // TODO: Add time attribute
        int length = rand.nextInt();
        return null;
    }

    public List<LecturerUnit> populateLecturerUnits() throws SQLException {
        List<LecturerUnit> lecturerUnits = new ArrayList<>();
        List<String> lecturerId = new ArrayList<>();
        List<String> unitId = new ArrayList<>();

        String strQueryLec = "SELECT " + Constants.LECTURER_ID + " FROM " + Constants.TABLE_LECTURERS;
        String strQueryUnit = "SELECT " + Constants.UNIT_ID + " FROM " + Constants.TABLE_UNITS;
        System.out.println("SQL  statement:\n" + strQueryLec + "\n" + strQueryUnit);

        ResultSet resultSetLec = statement.executeQuery(strQueryLec);
        while (resultSetLec.next()) {
            lecturerId.add(resultSetLec.getString(Constants.LECTURER_ID));
        }

        ResultSet resultSetUnits = statement.executeQuery(strQueryUnit);
        while (resultSetUnits.next()) {
            unitId.add(resultSetUnits.getString(Constants.UNIT_ID));
        }
//        int length = rand.nextInt(unitId.size());

        for (int i = 0; i < unitId.size(); i++) {
            int randLecturer = 0, randUnit = 0;
            if (!lecturerId.isEmpty())
                randLecturer = rand.nextInt(0, lecturerId.size());
            if (!unitId.isEmpty())
                randUnit = rand.nextInt(0, unitId.size());

            LecturerUnit lecturerUnit = new LecturerUnit();
            lecturerUnit.setLecturerId(lecturerId.get(randLecturer));
            lecturerUnit.setUnitId(unitId.get(randUnit));
            lecturerUnits.add(lecturerUnit);
        }
        return lecturerUnits;
    }

    public List<StudentUnit> populateStudentUnits() throws SQLException {
        List<StudentUnit> studentUnits = new ArrayList<>();
        List<String> studentId = new ArrayList<>();
        List<String> unitId = new ArrayList<>();

        String strQueryStu = "SELECT " + Constants.STUDENT_ID + " FROM " + Constants.TABLE_STUDENTS + " WHERE " + Constants.IN_SESSION + " = 1";
        String strQueryUnits = "SELECT distinct(" + Constants.UNIT_ID + ") FROM " + Constants.TABLE_UNITS + " INNER JOIN " + Constants.TABLE_LECTURERS + " lec on " + Constants.LECTURER_ID + "=lec." + Constants.LECTURER_ID + " WHERE lec." +Constants.IN_SESSION + "=1 order by " + Constants.UNIT_ID;
        System.out.println("SQL  statement:\n" + strQueryStu + "\n" + strQueryUnits);

        ResultSet resultSetStu = statement.executeQuery(strQueryStu);
        while (resultSetStu.next()) {
            studentId.add(resultSetStu.getString(Constants.STUDENT_ID));
        }

        ResultSet resultSetUnits = statement.executeQuery(strQueryUnits);
        while (resultSetUnits.next()) {
            unitId.add(resultSetUnits.getString(Constants.UNIT_ID));
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int length = rand.nextInt(studentId.size());

        for (int i = 0; i < studentId.size(); i++) {
            int numOfUnitsPerStudent = rand.nextInt(7) + 2;

            for (int j = 0; j < numOfUnitsPerStudent; j++) {
                int randUnit = rand.nextInt(unitId.size());
                StudentUnit studentUnit = new StudentUnit();
                studentUnit.setStudentId(studentId.get(i));
                studentUnit.setUnitId(unitId.get(randUnit));
                studentUnits.add(studentUnit);
            }

        }
        return studentUnits;
    }
}
