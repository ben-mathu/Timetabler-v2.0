package com.bernard.timetabler.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import com.bernard.timetabler.dbinit.Constants;
import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;

public class UtilCommonFunctions {
	public static Statement initialize(String username, String password) {
		CreateSchemaTimeTabler.setDatabase(Constants.DATABASE_NAME);
		
		CreateSchemaTimeTabler ct = new CreateSchemaTimeTabler(username, password);
		
		return ct.getStatement();
	}
}
