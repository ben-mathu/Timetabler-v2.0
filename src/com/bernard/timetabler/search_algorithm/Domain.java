package com.bernard.timetabler.search_algorithm;

public interface Domain<T> {
	public T initial();
	public int h(T state);
	public boolean isGoal(T state);
	public int numActions(T state);
	public int nthAction(T state, int nth);
	public Edge<T> apply(T state, int op);
	public T copy(T state);
}
