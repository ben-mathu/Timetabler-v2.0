package com.bernard.timetabler.search_algorithm;

public class Node<T> {
	final int f, g, pop;
	final Node<?> parent;
	final T state;
	
	private Node(T state, Node<?> parent, int cost, int pop) {
		this.g = (parent != null) ? parent.g+cost : cost;
//		this.f = g + domain.h(state);
		this.f = 0;
		this.pop = pop;
		this.parent = parent;
		this.state = state;
	}
}
