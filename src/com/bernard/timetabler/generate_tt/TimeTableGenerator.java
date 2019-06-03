package com.bernard.timetabler.generate_tt;

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.GenerateEntityData;
import com.bernard.timetabler.dbinit.model.*;
import com.bernard.timetabler.dbinit.model.Class;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class TimeTableGenerator {
    private ThreadLocalRandom rand;

    private List<Class> classes;
    private List<ClassUnit> classUnits;
    private List<StudentUnit> studentUnits;
    private List<LecturerUnit> lecturerUnits;
    
    private List<String> hallId = new ArrayList<String>();

    private Statement statement;

    public TimeTableGenerator() {
        CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);

        CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler("ben", "");
        statement = ct.getStatement();
    }

    public void getClasses() throws SQLException {

        System.out.println("Querying " + Constants.TABLE_CLASSES);
        String strClassQuery = "SELECT * FROM " + Constants.TABLE_CLASSES;
        ResultSet resultSet = statement.executeQuery(strClassQuery);

        classes = new ArrayList<Class>();
        while (resultSet.next()) {
            Class classroom = new Class();
            classroom.setId(resultSet.getString(Constants.CLASS_ID));
            classroom.setHall_id(resultSet.getString(Constants.HALL_ID));
            classroom.setVolume(resultSet.getString(Constants.VOLUME));
            classroom.setAvailability(resultSet.getBoolean(Constants.AVAILABILITY));
            classes.add(classroom);
        }
    }

    public void getStudentUnits() throws SQLException {

        System.out.println("Querying " + Constants.TABLE_STUDENT_UNITS);
        String strStudentUnitQuery = "SELECT * FROM " + Constants.TABLE_STUDENT_UNITS;

        ResultSet resultSet = statement.executeQuery(strStudentUnitQuery);

        studentUnits = new ArrayList<StudentUnit>();
        while (resultSet.next()) {
            StudentUnit su = new StudentUnit();
            su.setStudentId(resultSet.getString(Constants.STUDENT_ID));
            su.setUnitId(resultSet.getString(Constants.UNIT_ID));
            studentUnits.add(su);
        }
    }

    public void getLecturerUnits() throws SQLException {

        System.out.println("Querying " + Constants.TABLE_LECTURER_UNITS);
        String strLecturerUnitsQuery = "SELECT * FROM " + Constants.TABLE_LECTURER_UNITS;

        ResultSet resultSet = statement.executeQuery(strLecturerUnitsQuery);

        lecturerUnits = new ArrayList<LecturerUnit>();
        while(resultSet.next()) {
            LecturerUnit lu = new LecturerUnit();
            lu.setLecturerId(resultSet.getString(Constants.LECTURER_ID));
            lu.setUnitId(resultSet.getString(Constants.UNIT_ID));
            lecturerUnits.add(lu);
        }
    }

    /**
     * Generate time-table for five days of the week
     * DayTime_ClassUnitTimetable HashMap is the final timetable
     *
     * @param n describe the number of days to set for the timetable
     */
    public HashMap<DayTimeUnit, String> generateTimeTable(int n, String semester) {
        System.out.println("Initializing...");

        rand = ThreadLocalRandom.current();

        try {
            System.out.println("Getting classes...");
            getClasses();

            System.out.println("Getting studentUnits...");
            getStudentUnits();

            System.out.println("Getting lecturerUnits...");
            getLecturerUnits();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<DayTimeUnit> dayTimeUnits = getTimeUnits();
        List<String> registeredUnits = getRegisteredUnits();

        // get faculties to check classes ar of the same faculty
        List<Faculty> faculties = getAllFaculties();
        // get a list of classes to act as clash data structure
        List<Class> classClash = getAllClasses();
        classUnits = new ArrayList<ClassUnit>();
        int unitCount = 0;

        /*
         * get units registered by student from studentUnits
         * for (Units...unit)
         *      get classes
         *      check count is greater than 0
         *      lbl2:if count > 0
         *          assign unit to class
         *          assign class a timeslot
         *          add class to hashtable clash
         *          add a timeslot to hashtable timeslot_clash
         *      else
         *          check that class is not clashing
         *          if clashing
         *              choose another class
         *              lbl2
         */

        HashMap<DayTimeUnit, String> dayTime_ClassUnitsTimetable =  new HashMap<>();
        List<ClassUnit> classUnits = new ArrayList<ClassUnit>();

        int count = 0;

        // beginning of timetable generator
        System.out.println("Starting timetable creation...");
        int timeSlotCount = 0; // counts the slots per day
        next:for (;;) {
            String unitId;
//            int numberOfStudents = getNumberOfStudents(unitId);

            ClassUnit classUnit = new ClassUnit();

            // daycount should not exceed number of days specified
            if (unitCount == registeredUnits.size()) {
                break;
            } else {
                for (int i = 0; ; i++) {
                    //get unit for the timeslot
                    if (unitCount > registeredUnits.size() - 1)
                        break;

                    unitId = registeredUnits.get(unitCount);

                    // create a class-unit object for each unit
                    List<String> classIdList = getClassId(unitId);
                    if (!classIdList.isEmpty()) {
                        classUnit.setClassId(classIdList.get(0));
                        classUnit.setUnitId(unitId);
                        classUnit.setHallId(hallId.get(0));
                        classUnits.add(classUnit);
                    }

                    if (i != dayTimeUnits.size() - 1) {
                        if (!dayTime_ClassUnitsTimetable.containsValue(unitId)) {
                            // get lecturer who teaches the unit
                            List<Lecturer> lecturers = getLecturer(unitId);
                            if (!lecturers.isEmpty()) {
                                dayTime_ClassUnitsTimetable.put(dayTimeUnits.get(i), unitId);
//                                dayTimeUnits.remove(dayTimeUnit);
                                timeSlotCount++;
                            }
                        }
                        
                        unitCount++;
                    }

                    if (timeSlotCount > dayTimeUnits.size()) {
                        timeSlotCount = 0;
                        continue next;
                    }
                }
            }
        }
        
        try {
        // 	Save timetable to db
        	saveGeneratedTimetable(dayTime_ClassUnitsTimetable, semester);
        
        // 	Save class-unit relationship
			saveClassUnitRelationship(classUnits);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return dayTime_ClassUnitsTimetable;
    }

    /**
     * get lecturer that teaches unitId
     * @param unitId
     * @return
     */
    private List<Lecturer> getLecturer(String unitId) {
        List<Lecturer> lecturers = new ArrayList<Lecturer>();
        String query = "SELECT lu." + Constants.LECTURER_ID + ", " +
                "lec." + Constants.F_NAME + ", " +
                "lec." + Constants.L_NAME + ", " +
                "lec." + Constants.M_NAME + ", " +
                "lec." + Constants.DEPARTMENT_ID + ", " +
                "lec." + Constants.IN_SESSION +
                " FROM " + Constants.TABLE_LECTURER_UNITS + " lu " +
                " INNER JOIN " + Constants.TABLE_LECTURERS + " lec" +
                " ON lu." + Constants.LECTURER_ID + "=lec." + Constants.LECTURER_ID +
                " WHERE lu." + Constants.UNIT_ID + "='" + unitId + "'";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getBoolean(Constants.IN_SESSION)) {
                    Lecturer lecturer = new Lecturer();
                    lecturer.setId(resultSet.getString(Constants.LECTURER_ID));
                    lecturer.setFirstName(resultSet.getString(Constants.F_NAME));
                    lecturer.setLastName(resultSet.getString(Constants.L_NAME));
                    lecturer.setMiddleName(resultSet.getString(Constants.M_NAME));
                    lecturer.setDepartmentId(resultSet.getString(Constants.DEPARTMENT_ID));
                    lecturer.setInSesson(resultSet.getBoolean(Constants.IN_SESSION));
                    lecturers.add(lecturer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecturers;
    }

    private List<Faculty> getAllFaculties() {
        List<Faculty> faculties = new ArrayList<Faculty>();
        String query = "SELECT * FROM " + Constants.TABLE_FACULTIES;
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Faculty faculty = new Faculty();
                faculty.setCampusId(resultSet.getString(Constants.CAMPUS_ID));
                faculty.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
                faculty.setFacultyName(resultSet.getString(Constants.FACULTY_NAME));
                faculties.add(faculty);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return faculties;
    }

    /**
     * Get the volume of class with class_id id
     * @param id class id also used as the class name
     * @return vol volume of the class as string value
     */
    private String getClassVolume(String id) {
        String query = "SELECT * FROM " + Constants.TABLE_CLASSES + " WHERE " + Constants.CLASS_ID + "=" + id;
        String vol = "";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                vol = resultSet.getString(Constants.VOLUME);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vol;
    }

    private int getNumberOfStudents(String unitId) {
        String query = "SELECT * FROM " + Constants.TABLE_STUDENT_UNITS + " WHERE " + Constants.UNIT_ID + "='" + unitId + "'";
        int count = 0;
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private List<Class> getAllClasses() {
        List<Class> classes = new ArrayList<Class>();
        String query = "SELECT * FROM " + Constants.TABLE_CLASSES;
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Class classItem = new Class();
                classItem.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
                classItem.setLab(resultSet.getBoolean(Constants.IS_LAB));
                classItem.setAvailability(resultSet.getBoolean(Constants.AVAILABILITY));
                classItem.setVolume(resultSet.getString(Constants.VOLUME));
                classItem.setHall_id(resultSet.getString(Constants.HALL_ID));
                classItem.setId(resultSet.getString(Constants.CLASS_ID));
                classes.add(classItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    private List<String> getClassId(String unitId) {
        List<String> classId = new ArrayList<String>();

        // select class id from a hall of
        String strClassIdQuery = "SELECT un." + Constants.FACULTY_ID +
                ", un." + Constants.UNIT_ID +
                ", cl." + Constants.CLASS_ID +
                ", cl." + Constants.HALL_ID +
                ", fac." + Constants.FACULTY_ID +
                " FROM " + Constants.TABLE_UNITS + " un " +
                "INNER JOIN " + Constants.TABLE_FACULTIES + " fac " +
                "ON un." + Constants.FACULTY_ID + "=fac." + Constants.FACULTY_ID +
                " INNER JOIN " + Constants.TABLE_CLASSES + " cl " +
                "ON cl." + Constants.FACULTY_ID + "=fac." + Constants.FACULTY_ID +
                " WHERE un." + Constants.UNIT_ID + "='" + unitId + "'";

        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(strClassIdQuery);
            while (resultSet.next()) {
                classId.add(resultSet.getString(Constants.CLASS_ID));
                hallId.add(resultSet.getString(Constants.HALL_ID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classId;
    }

    private List<String> getRegisteredUnits() {
        List<String> units = new ArrayList<String>();
        for (StudentUnit studentUnit : studentUnits) {
            if (!units.contains(studentUnit.getUnitId())) {
                units.add(studentUnit.getUnitId());
            }
        }
        return units;
    }

    private List<DayTimeUnit> getTimeUnits() {
        String[] day = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String[] time = new String[]{"6 am", "8 am", "11 am", "2 pm", "5 pm"};
        List<DayTimeUnit> dayTimeUnits = new ArrayList<DayTimeUnit>();

        int count = 0;
        for (int i = 0; i < day.length; i++) {
            for (String item : time) {
                DayTimeUnit dayTimeUnit = new DayTimeUnit();
                dayTimeUnit.setDayOfWeek(day[i]);
                dayTimeUnit.setTimeOfDay(item);
                dayTimeUnits.add(dayTimeUnit);
            }

        }
        return dayTimeUnits;
    }
    
    /**
     * Save the timetable in the db
     * 
     * @param timetable a hash table to keep key value pairs for timeslot and unitId
     * @param period the semester the application is created for
     * @throws SQLException 
     */
    private void saveGeneratedTimetable(HashMap<DayTimeUnit, String> timetable, String period) throws SQLException {
    	int count = 0;
    	for (HashMap.Entry<DayTimeUnit, String> timetableItem : timetable.entrySet()) {
    		String updateTimetable = "INSERT INTO " + Constants.TIMETABLES +
        			" VALUES ('" + period + "','" + timetableItem.getKey().getDayOfWeek() + 
        			" " + timetableItem.getKey().getTimeOfDay() +
        			"','" + timetableItem.getValue() + "')";
    		count = statement.executeUpdate(updateTimetable);
		}
    }
    
    private void saveClassUnitRelationship(List<ClassUnit> classUnitList) throws SQLException {
    	GenerateEntityData gen = new GenerateEntityData();
    	gen.populateClassUnits(classUnitList);
    }
}
