package com.bernard.timetabler.search_algorithm;

import com.bernard.timetabler.dbinit.model.course.Unit;
import com.bernard.timetabler.dbinit.model.room.Class;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author bernard
 */
public class PathSearchAlgorithmTest {
    private PathSearchAlgorithm ps;

    @Before
    public void setUp() {
        ps = new PathSearchAlgorithm();
    }

    @Test
    public void getRegisteredUnits_IsNotEmpty_ReturnRegisteredUnits() throws Exception {
        List<Unit> unitList = ps.getRegisteredUnits();
        assertFalse(unitList.isEmpty());
    }

    @Test
    public void getListOfClasses_IsNotEmpty_ReturnRegisteredUnits() throws Exception {
        List<Class> classList = ps.getListOfClasses();
        assertFalse(classList.isEmpty());
    }

    @Test
    public void pathFindingAlgorithm() {
    }
}