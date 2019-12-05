package com.bernard.timetabler.dbinit;

import com.bernard.timetabler.dbinit.model.timetable.DayTimeUnit;
import com.bernard.timetabler.search_algorithm.PathSearchAlgorithm;

import java.util.HashMap;
import java.util.Scanner;

public class Init {
    private static HashMap<String, DayTimeUnit> timetable = new HashMap<>();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String env = "";
        if (args.length > 0) {
            env = args[0];
        }

        if (env.matches("migrate|dev|update")) {
            CreateSchemaTimeTabler.createSchema(env);
        } else {
//            TimeTableGenerator ttg = new TimeTableGenerator();
//
//            timetable = ttg.generateTimeTable(5, "Jan 2018/2019");
        	
        	PathSearchAlgorithm ttg = new PathSearchAlgorithm();
        	timetable = ttg.pathFindingAlgorithm("Jan 2018/2019");

            for (HashMap.Entry<String, DayTimeUnit> tt : timetable.entrySet()) {
                System.out.println("Time: " + tt.getValue().getDayOfWeek() + " " + tt.getValue().getTimeOfDay() + ": " + tt.getKey());
            }
        }

        scanner.close();
    }
}
