package com.bernard.timetabler.dbinit.model.timetable;

import java.util.List;

import com.bernard.timetabler.dbinit.model.relationships.ClassUnit;

public class DayTimeUnit {
    private String dayOfWeek;
    private String timeOfDay;
    private boolean isFinal = false;
    private List<ClassUnit> classUnits;
    private ClassUnit roomUnit;
    private String period;

    public DayTimeUnit(String dayOfWeek, String timeOfDay, boolean isFinal, String period) {
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
        this.isFinal = isFinal;
        this.period = period;
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
    
    public void setRoomUnit(ClassUnit classUnits) {
		this.roomUnit = classUnits;
	}
    
    public ClassUnit getRoomUnit() {
		return roomUnit;
	}

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }
}
