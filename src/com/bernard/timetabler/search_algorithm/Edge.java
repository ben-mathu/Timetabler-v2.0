package com.bernard.timetabler.search_algorithm;

public class Edge<T> {
	Node<T> destination;
	Node<T> source;
	double weight;
	
	public void setDestination(Node<T> destination) {
		this.destination = destination;
	}
	
	public Node<T> getDestination() {
		return destination;
	}
	
	public void setSource(Node<T> source) {
		this.source = source;
	}
	
	public Node<T> getSource() {
		return source;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public double getWeight() {
		return weight;
	}
}
