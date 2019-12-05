package com.bernard.timetabler.search_algorithm;

import java.util.LinkedList;
import java.util.List;


/*
 * Generic class - defines a node's attributes
 */
public class Node<T> {
	private static final String TAG = "Node";
	
	Node<T> parent = null;
	
	// List of associated edges - there can only be a max of 2 edges
	List<Edge<T>> edgeList = new LinkedList<>();
	// List to keep node children
	List<Node<T>> nodeChildrenList = new LinkedList<>();
	
	T item;
	// difference of total constraints and satisfied constraints
	double h = 0;
	
	public Node(T item) {
		this.item = item;
	}
	
	public void addEdge(Edge<T> edge) {
		edgeList.add(edge);
	}
	
	public void removeEdge(Edge<T> edge) {
		edgeList.remove(edge);
	}
	
	public void addChild(Node<T> node) {
		nodeChildrenList.add(node);
	}
	
	public void removeChild(Node<T> node) {
		nodeChildrenList.remove(node);
	}
	
	public boolean isRoot() {
		return parent != null ? false : true;
	}
	
	public void setParent(Node<T> parent) {
		this.parent = parent;
	}
	
	public Node<T> getParent() {
		return parent;
	}
	
	public void setH(double h) {
		this.h = h;
	}
	
	public double getH() {
		return h;
	}
	
	public int getCount() {
		int count = 1;
		Node<T> currNode = this.getParent();
		
		if (currNode == null) {
			return 1;
		} else {
			for (int i = 0; ; i++) {
				if (currNode.getParent() == null) {
					count++;
					break;
				} else {
					count++;
					currNode = currNode.getParent();
				}
			}
			
			return count;
		}
	}
	
	public Node<T> getNodeAt(int pos) {
		int count = this.getCount();
		
		Node<T> node = this;
		
		for (int i = count; i > count - pos; i--) {
			node = node.getParent();
		}
		
		return node;
	}
	
	public T getItem() {
		return item;
	}
	
	public void setItem(T item) {
		this.item = item;
	}
	
	public String toString() {
		String str = "";
		
		str = getCount() + "";
		
		return str;
	}
	
	public List<Edge<T>> getEdgeList() {
		return edgeList;
	}
}
