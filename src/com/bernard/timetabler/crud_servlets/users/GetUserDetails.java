package com.bernard.timetabler.crud_servlets.users;

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.model.admin.Admin;
import com.bernard.timetabler.dbinit.model.admin.AdminResponse;
import com.bernard.timetabler.dbinit.model.campus.Campus;
import com.bernard.timetabler.dbinit.model.department.Department;
import com.bernard.timetabler.dbinit.model.faculty.Faculty;
import com.bernard.timetabler.dbinit.model.lecturer.Lecturer;
import com.bernard.timetabler.dbinit.model.lecturer.LecturerPackageRequest;
import com.bernard.timetabler.dbinit.model.programme.Programme;
import com.bernard.timetabler.dbinit.model.student.Student;
import com.bernard.timetabler.dbinit.model.student.StudentPackageResponse;
import com.bernard.timetabler.utils.Log;
import com.bernard.timetabler.utils.UtilCommonFunctions;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author bernard
 */
@WebServlet("/user-details/*")
public class GetUserDetails extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TAG = GetUserDetails.class.getSimpleName();
    private final Statement statement;
    private Gson gson;
    private String jsonResponse = "";
    private StudentPackageResponse response;

    public GetUserDetails() {
        super();

        statement = UtilCommonFunctions.initialize("ben", "");
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter(Constants.USER_ID);
        String role = req.getParameter(Constants.ROLE);

        resp.setContentType(Constants.APPLICATION_JSON);
        PrintWriter writer = resp.getWriter();

        try {
            if (role.equalsIgnoreCase("admin")) {
                // Send successful report if true
                Admin admin = getAdmin(userId);

                AdminResponse adminResp = new AdminResponse();
                adminResp.setAdmin(admin);

                // prepare response
                jsonResponse = gson.toJson(adminResp);

                writer.write(jsonResponse);
            } else if (role.equalsIgnoreCase("lecturer")) {
                LecturerPackageRequest response = getLecturer(userId);

                jsonResponse = gson.toJson(response);

                writer.write(jsonResponse);

            } else if (role.equalsIgnoreCase("student")) {
                StudentPackageResponse response = getStudent(userId);

                jsonResponse = gson.toJson(response);

                writer.write(jsonResponse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    private StudentPackageResponse getStudent(String userId) throws SQLException {
        Student student = new Student();

        String query = "SELECT * FROM " + Constants.TABLE_STUDENTS +
                " WHERE " + Constants.LECTURER_ID + "='" + userId + "'" +
                " AND " + Constants.IS_REMOVED + "=" + false;

        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            student.setAdmissionDate(resultSet.getString(Constants.ADMISSION_DATE));
            student.setCampusId(resultSet.getString(Constants.CAMPUS_ID));
            student.setDepartmentId(resultSet.getString(Constants.DEPARTMENT_ID));
            student.setEmail(resultSet.getString(Constants.EMAIL));
            student.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
            student.setFname(resultSet.getString(Constants.F_NAME));
            student.setInSession(resultSet.getBoolean(Constants.IN_SESSION));
            student.setLname(resultSet.getString(Constants.L_NAME));
            student.setMname(resultSet.getString(Constants.M_NAME));
            student.setPassword(resultSet.getString(Constants.PASSWORD));
            student.setProgrammeId(resultSet.getString(Constants.PROGRAMME_ID));
            student.setRemoved(resultSet.getBoolean(Constants.IS_REMOVED));
            student.setStudentId(resultSet.getString(Constants.STUDENT_ID));
            student.setUsername(resultSet.getString(Constants.USERNAME));
            student.setYearOfStudy(resultSet.getString(Constants.YEAR_OF_STUDY));
        }

        StudentPackageResponse studentResponse = new StudentPackageResponse();
        studentResponse.setCampus(getCampus(student.getCampusId()));
        studentResponse.setDepartment(getDepartment(student.getDepartmentId()));
        studentResponse.setFaculty(getFaculty(student.getFacultyId()));
        studentResponse.setProgramme(getProgramme(student.getProgrammeId()));
        studentResponse.setStudent(student);
        return response;
    }

    private Programme getProgramme(String programmeId) throws SQLException {
        Programme programme = new Programme();

        String query = "SELECT * FROM " + Constants.TABLE_PROGRAMMES +
                " WHERE " + Constants.LECTURER_ID + "='" + programmeId + "'" +
                " AND " + Constants.IS_REMOVED + "=" + false;

        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            programme.setDepartmentId(resultSet.getString(Constants.DEPARTMENT_ID));
            programme.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
            programme.setProgrammeId(resultSet.getString(Constants.PROGRAMME_ID));
            programme.setProgrammeName(resultSet.getString(Constants.PROGRAMME_NAME));
            programme.setRemoved(resultSet.getBoolean(Constants.IS_REMOVED));
        }
        return programme;
    }

    private Campus getCampus(String campusId) throws SQLException {
        Campus campus = new Campus();

        String query = "SELECT * FROM " + Constants.TABLE_CAMPUS +
                " WHERE " + Constants.LECTURER_ID + "='" + campusId + "'" +
                " AND " + Constants.IS_REMOVED + "=" + false;

        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            campus.setCampusId(resultSet.getString(Constants.CAMPUS_ID));
            campus.setCampusName(resultSet.getString(Constants.CAMPUS_NAME));
            campus.setRemoved(resultSet.getBoolean(Constants.IS_REMOVED));
        }
        return campus;
    }

    private LecturerPackageRequest getLecturer(String userId) throws SQLException {
        Lecturer lecturer = new Lecturer();

        String query = "SELECT * FROM " + Constants.TABLE_LECTURERS +
                " WHERE " + Constants.LECTURER_ID + "='" + userId + "'" +
                " AND " + Constants.IS_REMOVED + "=" + false;

        ResultSet result = statement.executeQuery(query);

        if (result.next()) {
            lecturer.setId(userId);
            lecturer.setFacultyId(result.getString(Constants.FACULTY_ID));
            lecturer.setDepartmentId(result.getString(Constants.DEPARTMENT_ID));
            lecturer.setEmail(result.getString(Constants.EMAIL));
            lecturer.setFirstName(result.getString(Constants.F_NAME));
            lecturer.setMiddleName(result.getString(Constants.M_NAME));
            lecturer.setInSession(result.getBoolean(Constants.IN_SESSION));
            lecturer.setLastName(result.getString(Constants.L_NAME));
            lecturer.setRemoved(result.getBoolean(Constants.IS_REMOVED));
            lecturer.setUsername(result.getString(Constants.USERNAME));
        }

        LecturerPackageRequest response = new LecturerPackageRequest();
        response.setFaculty(getFaculty(lecturer.getFacultyId()));
        response.setDepartment(getDepartment(lecturer.getDepartmentId()));

        return response;
    }

    private Faculty getFaculty(String facultyId) throws SQLException {
        Faculty faculty = new Faculty();

        String query = "SELECT * FROM " + Constants.TABLE_FACULTIES +
                " WHERE " + Constants.FACULTY_ID + "='" + facultyId + "'" +
                " AND " + Constants.IS_REMOVED + "=" + false;

        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            faculty.setCampusId(resultSet.getString(Constants.CAMPUS_ID));
            faculty.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
            faculty.setFacultyName(resultSet.getString(Constants.FACULTY_NAME));
            faculty.setRemoved(resultSet.getBoolean(Constants.IS_REMOVED));
        }
        return faculty;
    }

    private Department getDepartment(String departmentId) throws SQLException {
        Department dep = new Department();

        String query = "SELECT * FROM " + Constants.TABLE_DEPARTMENTS +
                " WHERE " + Constants.LECTURER_ID + "='" + departmentId + "'" +
                " AND " + Constants.IS_REMOVED + "=" + false;

        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            dep.setDepartmentId(departmentId);
            dep.setDepartmentName(resultSet.getString(Constants.DEPARTMENT_NAME));
            dep.setFacultyId(resultSet.getString(Constants.FACULTY_ID));
            dep.setRemoved(resultSet.getBoolean(Constants.IS_REMOVED));
        }

        return dep;
    }

    private Admin getAdmin(String userId) throws SQLException {
        String query = "SELECT * FROM " + Constants.TABLE_ADMIN +
                " WHERE " + Constants.ADMIN_ID + "='" + userId + "'";

        ResultSet result = statement.executeQuery(query);

        Admin admin = new Admin();
        if (result.next()) {
            admin.setAdminId(userId);
            admin.setEmail(result.getString(Constants.EMAIL));
            admin.setfName(result.getString(Constants.F_NAME));
            admin.setmName(result.getString(Constants.M_NAME));
            admin.setlName(result.getString(Constants.L_NAME));
            admin.setUsername(result.getString(Constants.USERNAME));
            admin.setPassword(result.getString(Constants.PASSWORD));
        }
        return admin;
    }
}