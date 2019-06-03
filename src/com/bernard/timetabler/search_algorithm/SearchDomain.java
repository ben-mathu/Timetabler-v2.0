package com.bernard.timetabler.search_algorithm;

public interface SearchDomain<T> {
	
	public T initialState();
	public int numOfActions(T item);
}
