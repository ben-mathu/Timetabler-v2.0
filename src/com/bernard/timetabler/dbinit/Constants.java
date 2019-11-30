package com.bernard.timetabler.dbinit;

public class Constants {
    public static final String DATABASE_NAME = "timetabler";

    // Campus
    public static final String TABLE_CAMPUS = "campuses";
    public static final String CAMPUS_ID = "campus_id";
    public static final String CAMPUS_NAME = "campus_name";

    // Faculties
    public static final String TABLE_FACULTIES = "faculties";
    public static final String FACULTY_ID = "faculty_id";
    public static final String FACULTY_NAME = "faculty_name";

    // Halls
    public static final String TABLE_HALLS = "halls";
    public static final String HALL_ID = "hall_id";
    public static final String HALL_NAME = "hall_name";

    // Classes
    public static final String TABLE_CLASSES = "classes";
    public static final String CLASS_ID = "class_id";
    public static final String AVAILABILITY = "availability";
    public static final String VOLUME = "volume";

    // Departments
    public static final String TABLE_DEPARTMENTS = "departments";
    public static final String DEPARTMENT_ID = "department_id";
    public static final String DEPARTMENT_NAME = "department_name";

    // Lecturers
    public static final String TABLE_LECTURERS = "lecturers";
    public static final String LECTURER_ID = "lecturer_id";
    public static final String ROLE = "role";

    // Programmes
    public static final String TABLE_PROGRAMMES = "programmes";
    public static final String PROGRAMME_ID = "programme_id";
    public static final String PROGRAMME_NAME = "programme_name";

    // Students
    public static final String TABLE_STUDENTS = "students";
    public static final String STUDENT_ID = "student_id";
    public static final String F_NAME = "f_name";
    public static final String L_NAME = "l_name";
    public static final String M_NAME = "m_name";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String IN_SESSION = "in_session";
    public static final String YEAR_OF_STUDY = "year_of_study";
    public static final String ADMISSION_DATE = "admission_date";

    // Units
    public static final String TABLE_UNITS = "units";
    public static final String UNIT_ID = "unit_id";
    public static final String UNIT_NAME = "unit_name";
	public static final String IS_COMMON = "is_common";
	public static final String IS_LAB = "is_lab";
    public static final String IS_PRACTICAL = "is_practical";

    // Lecturers Programmes
    static final String TABLE_LECTURER_PROGRAMMES = "lecturer_programmes";

    // Class units
    public static final String TABLE_CLASS_UNITS = "class_units";

    // Lecturer units
    public static final String TABLE_LECTURER_UNITS = "lecturer_units";

    // Student units
    public static final String TABLE_STUDENT_UNITS = "student_units";
    
    // other parameters
    public static final String MESSAGE = "message";

	public static final String SALTROLE = "salt_role";

	public static final String SALT = "salt";

	public static final String APPLICATION_JSON = "application/json";

	// Admin
	public static final String ADMIN_ID = "admin_id";

	public static final String TABLE_ADMIN = "admin";

	public static final String TIMETABLES = "timetables";

	public static final String TABLE_TIMTABLE = "timetables";
	public static final String PERIOD = "period";
	
	public static final String TIME = "time";

	public static final String DAY = "day";

	public static final String TIMESLOT = "timeslot";

	public static final String UNIT = "unit";

    public static final String TABLE_SCHEDULE = "schedule_unit_registration";
    public static final String SCHEDULE_ID = "schedule_id";
    public static final String STARTDATE = "start_date";
    public static final String DEADLINE = "deadline";
    public static final String ACTIVITY = "activity";


	public static final String TABLE_SCHEDULE_LEC = "schedule_lec";

	public static final String IS_REMOVED = "is_removed";

	public static final String EMAIL = "email";

    public static final String USER_ID = "user_id";
    public static final String MESSAGE_SUCCESS = "Successfully updated.";
    public static final String DELETED_ACCOUNT_MESSAGE = "Account deleted successfully";
    public static final String OTHER_ISSUE = "Please contact the administrator to resolve the issue.\n";
    public static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss.SSS";

    public static class ENV {
        public static final String DEV = "dev";
    }
}
