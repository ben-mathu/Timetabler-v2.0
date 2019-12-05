package com.bernard.timetabler.search_algorithm.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bernard.timetabler.dbinit.model.relationships.ClassUnit;
import com.bernard.timetabler.dbinit.model.timetable.DayTimeUnit;
import com.bernard.timetabler.search_algorithm.Edge;
import com.bernard.timetabler.search_algorithm.Node;

public class TimeslotNode extends Node<Timeslot> {
	private static final String TAG = TimeslotNode.class.getSimpleName();
	
	private HashMap<String, String> classUnitMapping;
	private String unitKey;
	
	private HashMap<String, List<String>> classStudentsMapping;
	private HashMap<String, String> classLecturersMapping;
	
	private List<TimeslotNode> nodeList = new ArrayList<>();
	
	private double fn = 0;

	public TimeslotNode(Timeslot item) {
		super(item);
		
		classUnitMapping = new HashMap<>();
		classLecturersMapping = new HashMap<>();
		classStudentsMapping = new HashMap<>();
	}
	
	@Override
	public void addEdge(Edge<Timeslot> edge) {
		super.addEdge(edge);
	}
	
	public void setClassStudentsMapping(HashMap<String, List<String>> classStudentsMapping) {
		this.classStudentsMapping = classStudentsMapping;
	}
	
	public HashMap<String, List<String>> getClassStudentsMapping() {
		return classStudentsMapping;
	}
	
	public void setClassLecturersMapping(HashMap<String, String> classLecturersMapping) {
		this.classLecturersMapping = classLecturersMapping;
	}
	
	public HashMap<String, String> getClassLecturersMapping() {
		return classLecturersMapping;
	}
	
	public void setClassUnitMapping(HashMap<String, String> classUnitMapping) {
		this.classUnitMapping = classUnitMapping;
	}
	
	public HashMap<String, String> getClassUnitMapping() {
		return classUnitMapping;
	}
	
	public void setClassKey(String unitKey) {
		this.unitKey = unitKey;
	}
	
	public String getClassKey() {
		return unitKey;
	}
	
	public boolean studentsAreNotHere(String classId, List<String> studentIdList, String day, String time) {
		List<String> ids = new ArrayList<>();
//		classStudentsMapping.put(classId, studentIdList);
		if (!classStudentsMapping.isEmpty() && studentIdList != null) {
			ids = classStudentsMapping.get(unitKey);
			
			if (ids != null) {
				for (String id : studentIdList) {
					if (this.getItem().getTimeslot().get(day) == time) {
						if (ids.contains(id)) {
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}
	
	public boolean lecturersreNotHere(String classId, List<String> lecturerIdList, String day, String time) {
//		classLecturersMapping.put(classId, lecturerId);
		if (!classLecturersMapping.isEmpty()) {
			String id = classLecturersMapping.get(unitKey);
			if (this.getItem().getTimeslot().get(day) == time) {
				
			}
		}
		return true;
	}
	
	public boolean courseConflict(String classId, String unitId, String day, String time) {
		String nodeUnitId = "";
//		classUnitMapping.put(classId, unitId);
		if (!classUnitMapping.isEmpty()) {
			for (String key : classUnitMapping.keySet()) {
				nodeUnitId = key;
			}
			
			if (this.getItem().getTimeslot().get(day) == time) {
				if (nodeUnitId.equals(unitId)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void setFn(double fn) {
		this.fn = fn;
	}
	
	public double getFn() {
		return fn;
	}

	public String printToConsole() {
		int count = this.getCount();
		StringBuilder str = new StringBuilder();

		str.append("Number of nodes: " + getCount() + "\n");
		str.append("Time\n");
		for (int i = 0; i < count; i++) {
			TimeslotNode node = (TimeslotNode) this.getNodeAt(i);
			nodeList.add(node);
			for (String key : node.getItem().getTimeslot().keySet()) {
				str.append(key + " " + node.getItem().getTimeslot().get(key));
			}
			
			str.append(" " + node.getItem().getUnit().getId() + ": " + node.getItem().getUnit().getUnitName() + "\n");
		}
		
		System.out.print(str.toString());
		
		return str.toString();
	}
	
	public List<TimeslotNode> getNodeList() {
		
//		int count = this.getCount();
//		
//		for (int i = 0; i < count; i++) {
//			TimeslotNode node = (TimeslotNode) this.getNodeAt(i);
//			nodeList.add(node);
//		}
		return nodeList;
	}
	
	public HashMap<String, DayTimeUnit> getNodeList(HashMap<String, DayTimeUnit> tt) {
		int count = this.getCount();
		for (int i = 0; i < count; i++) {
			TimeslotNode n = (TimeslotNode) this.getNodeAt(i);
			
			DayTimeUnit dayTimeUnit = new DayTimeUnit();
			for (String key : n.getItem().getTimeslot().keySet()) {
				dayTimeUnit.setDayOfWeek(key);
				dayTimeUnit.setTimeOfDay(n.getItem().getTimeslot().get(key));
			}
			
			List<ClassUnit> classUnitList = new ArrayList<>();
			ClassUnit classUnit = new ClassUnit();
			for (String key : n.getClassUnitMapping().keySet()) {
				classUnit.setClassId(n.getClassUnitMapping().get(key));
				classUnit.setDay(dayTimeUnit.getDayOfWeek());
				classUnit.setHallId(null);
				classUnit.setPeriod(null);
				classUnit.setTime(dayTimeUnit.getTimeOfDay());
				classUnit.setUnitId(n.getClassUnitMapping().get(key));
				classUnitList.add(classUnit);
			}
			
			dayTimeUnit.setRoomUnit(classUnitList.get(0));
			dayTimeUnit.setFinal(true);
			tt.put(dayTimeUnit.getRoomUnit().getUnitId(), dayTimeUnit);
		}
		
		return tt;
	}
}
