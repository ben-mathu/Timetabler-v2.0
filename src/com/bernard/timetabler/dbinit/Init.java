package com.bernard.timetabler.dbinit;

import com.bernard.timetabler.dbinit.model.DayTimeUnit;
import com.bernard.timetabler.generate_tt.TimeTableGenerator;

import java.util.HashMap;
import java.util.Scanner;

public class Init {
    private static HashMap<DayTimeUnit, String> timetable = new HashMap<>();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String env = "";
        if (args.length > 0) {
            env = args[0];
        }

        if (env.matches("migrate|dev|update")) {
            CreateSchemaTimeTabler.createSchema(env);
        } else {
            TimeTableGenerator ttg = new TimeTableGenerator();

            timetable = ttg.generateTimeTable(5);

            for (HashMap.Entry<DayTimeUnit, String> tt : timetable.entrySet()) {
                System.out.println("Time: " + tt.getKey().getDayOfWeek() + " " + tt.getKey().getTimeOfDay() + ": " + tt.getValue());
            }
        }

        scanner.close();
    }
}
