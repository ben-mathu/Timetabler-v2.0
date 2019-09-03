package com.bernard.timetabler.dbinit.utils;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

public class GenerateRandomString {
	private static final String upperAlphas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String lowerAlphas = upperAlphas.toLowerCase();
	private static final String digits = "1234567890";
	private static final String alphaNumeric = upperAlphas + lowerAlphas + digits;
	
	private final char[] buffer;
	private final char[] symbols;
	private final Random random;
	
	public GenerateRandomString(int length, Random random, String symbols) {
		if (length < 1) throw new IllegalArgumentException();
		if (symbols.length() < 2) throw new IllegalArgumentException();
		this.random = Objects.requireNonNull(random);
		this.symbols = symbols.toCharArray();
		this.buffer = new char[length];
	}
	
	public GenerateRandomString(int length, Random random) {
		this(length, random, alphaNumeric);
	}
	
	public GenerateRandomString(int length) {
		this(length, new SecureRandom());
	}
	
	public GenerateRandomString() {
		this(21);
	}
	
	public String nextString() {
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = symbols[random.nextInt(symbols.length)];
		}
		
		return new String(buffer);
	}

}
