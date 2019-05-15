package com.bernard.timetabler.dbinit.test_package;

import com.bernard.timetabler.dbinit.GenerateEntityData;

import java.sql.SQLException;

/**
 * Purely for tests and mocks
 */
public class PopulateEntitiesForTests {
    public static void populateEntities() throws SQLException {
        // Initialize the statement
        GenerateEntityData generateEntityData = new GenerateEntityData();

        int countSuccess = 0;
        // insert/populate tables
        if (generateEntityData.populateCampuses()) {
            countSuccess++;
            System.out.println();
        }
        if (generateEntityData.populateFaculties()) {
            countSuccess++;
            System.out.println();
        }
        if (generateEntityData.populateHalls()) {
            countSuccess++;
            System.out.println();
        }
        if (generateEntityData.populateClasses()) {
            countSuccess++;
            System.out.println();
        }
        if (generateEntityData.populateDepartments()) {
            countSuccess++;
            System.out.println();
        }
        if (generateEntityData.populateLecturers()) {
            countSuccess++;
            System.out.println();
        }
        if (generateEntityData.populateProgrammes()) {
            countSuccess++;
            System.out.println();
        }
        if (generateEntityData.populateStudents()) {
            countSuccess++;
            System.out.println();
        }
        if (generateEntityData.populateUnits()) {
            countSuccess++;
            System.out.println();
        }
        if (generateEntityData.populateLecturerProgrammes()) {
            countSuccess++;
            System.out.println();
        }
        if (generateEntityData.populateClassUnits()) {
            countSuccess++;
            System.out.println();
        }
        if (generateEntityData.populateLecturerUnits()) {
            countSuccess++;
            System.out.println();
        }
        if (generateEntityData.populateStudentUnits()) {
            countSuccess++;
            System.out.println();
        }

        System.out.println("Number of tables populated: " + countSuccess + "\n");
        System.out.println("Completed populating the database ");
    }
}
