package com.bernard.timetabler.utils;

import java.sql.Statement;


import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;

public class UtilCommonFunctions {
	public static Statement initialize(String username, String password) {
		CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
		
		CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler(username, password);
		
		return ct.getStatement();
	}
}
