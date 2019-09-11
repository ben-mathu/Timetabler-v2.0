package com.bernard.timetabler.dbinit.model.campus;

import com.bernard.timetabler.dbinit.Constants;
import com.google.gson.annotations.SerializedName;

public class Campus {

	@SerializedName(Constants.CAMPUS_ID)
    private String campusId;
	@SerializedName(Constants.CAMPUS_NAME)
    private String campusName;
	@SerializedName(Constants.IS_REMOVED)
	private boolean isRemoved;

    public Campus(String campusId, String campusName, boolean isRemoved) {
        this.campusId = campusId;
        this.campusName = campusName;
        this.isRemoved = this.isRemoved;
    }

    public Campus() {
    }

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }
    
    public boolean isRemoved() {
		return isRemoved;
	}
    
    public void setRemoved(boolean isRemoved) {
		this.isRemoved = isRemoved;
	}
}
