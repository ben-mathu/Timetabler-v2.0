package com.bernard.timetabler.dbinit.model;

import java.util.List;

import com.bernard.timetabler.dbinit.Constants;
import com.google.gson.annotations.SerializedName;

public class DayTimeUnit {
    private String dayOfWeek;
    private String timeOfDay;
    private boolean isFinal = false;
    private List<ClassUnit> classUnits;

    public DayTimeUnit(String dayOfWeek, String timeOfDay, boolean isFinal) {
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
        this.isFinal = isFinal;
    }

    public DayTimeUnit() {
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }
}
