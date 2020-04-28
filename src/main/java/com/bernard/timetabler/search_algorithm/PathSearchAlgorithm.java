package com.bernard.timetabler.search_algorithm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import com.bernard.timetabler.dbinit.CreateSchemaTimeTabler;
import com.bernard.timetabler.dbinit.model.course.Unit;
import com.bernard.timetabler.dbinit.model.relationships.ClassUnit;
import com.bernard.timetabler.dbinit.model.room.Class;
import com.bernard.timetabler.dbinit.model.timetable.DayTimeUnit;
import com.bernard.timetabler.search_algorithm.entities.Timeslot;
import com.bernard.timetabler.search_algorithm.entities.TimeslotEdge;
import com.bernard.timetabler.search_algorithm.entities.TimeslotNode;
import com.bernard.timetabler.utils.Log;
import com.bernard.timetabler.utils.UtilCommonFunctions;

import static com.bernard.timetabler.dbinit.Constants.*;

public class PathSearchAlgorithm {
	private static final String TAG = PathSearchAlgorithm.class.getSimpleName();
	
	private HashMap<String, DayTimeUnit> dayTime_ClassUnitsTimetable =  new HashMap();
	
	private Statement statement;
	private ThreadLocalRandom rand;
	
	// list of acceptable nodes
	private List<TimeslotNode> nodeList = new ArrayList();
	private TimeslotNode currentNode;
	private TimeslotNode child1;
	
	// stores timeslots, eg. Mon, 6am
	private HashMap<String, String> timeslotMap;
	
	// store a list of classes
	private List<Class> classList;
	// store class unit relationship
	private HashMap<String, String> classUnit;
	
	// store mapping of student to unit
	private HashMap<String, List<String>> classStudentsMapping;
	private HashMap<String, String> classLecturersMapping;
	private List<String> studentIdList;
	private List<String> lecturerIdList;
	
	private List<Unit> unitsNotInTimetable = new ArrayList();
	
	private CreateSchemaTimeTabler ct;
	
	// store constraint value - which is the value obtained from satisfying constraint
	// highest value shows that less of the constraints were satisfied
	private double constraintValue = 0;
	private double gn = 0;
	
	// class allocated for each iteration
	private String classId = "";
	private String day, time;
	private String lecturerId;
	
	private String[] dayList;

	public PathSearchAlgorithm() {
		// initialize db
		statement = UtilCommonFunctions.initialize("ben", "");

		// populate list daylist
		dayList = new String[] {"Mon", "Tue", "Wen", "Thur", "Fri"};

		rand = ThreadLocalRandom.current();

		try {
			classList = getListOfClasses();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get a list of registered courses
	 *
	 * @return registered units
	 * @throws SQLException throws exception.
	 */
	public List<Unit> getRegisteredUnits() throws SQLException {
		List<Unit> registeredUnits = new ArrayList();
		
		// query data from the db
		String query = "SELECT DISTINCT(tu." + UNIT_ID + ")," + UNIT_NAME
				+ "," + PROGRAMME_ID + "," + DEPARTMENT_ID + "," + FACULTY_ID
				+ "," + IS_PRACTICAL + "," + IS_COMMON
				+ " FROM " + TABLE_UNITS + " tu"
				+ " INNER JOIN " + TABLE_STUDENT_UNITS + " su"
				+ " ON tu." + UNIT_ID + "=su." + UNIT_ID; 
		
		ResultSet result = statement.executeQuery(query);
		while (result.next()) {
			Unit unit = new Unit();
			unit.setId(result.getString(UNIT_ID));
			unit.setUnitName(result.getString(UNIT_NAME));
			unit.setProgrammeId(result.getString(PROGRAMME_ID));
			unit.setDepartmentId(DEPARTMENT_ID);
			unit.setFacultyId(FACULTY_ID);
			unit.setPractical(result.getBoolean(IS_PRACTICAL));
			unit.setCommon(result.getBoolean(IS_COMMON));

			registeredUnits.add(unit);
		}
		
		Log.d(TAG, registeredUnits.size() + " Units");
		return registeredUnits;
	}

	/**
	 * get list  of classes/rooms
	 * @return list of rooms
	 * @throws SQLException throws exception.
	 */
	public List<Class> getListOfClasses() throws SQLException {
		List<Class> classList = new ArrayList();
		
		String query = "SELECT * FROM " + TABLE_CLASSES;
		
		ResultSet result = statement.executeQuery(query);
		
		while (result.next()) {
			Class room = new Class();
			room.setId(result.getString(CLASS_ID));
			room.setHall_id(result.getString(HALL_ID));
			room.setLab(result.getBoolean(IS_LAB));
			room.setFacultyId(result.getString(FACULTY_ID));
			room.setVolume(result.getString(VOLUME));
			room.setAvailability(result.getBoolean(AVAILABILITY));
			
			classList.add(room);
		}
		
		Log.d(TAG, classList.size() + " units");
		return  classList;
	}

	/**
	 * generates a timetable.
	 * @param period semester whose timetable is being created.
	 * @return hashmap containing a list of time slots generated.
	 */
	public HashMap<String, DayTimeUnit> pathFindingAlgorithm(String period) {
		try {
			List<Unit> unitList = getRegisteredUnits();
			classList = getListOfClasses();
			int classCount = 0;
			
			for (int i = 0;;i++) {

				unitsNotInTimetable.clear();
				
				for (Unit unit : unitList) {
					classUnit = new HashMap();
					classLecturersMapping = new HashMap();
					classStudentsMapping = new HashMap();
					
					getNumberOfStudentsForClass(unit);
					
					classCount = rand.nextInt(0, classList.size());
					
					classId = classList.get(classCount).getId();
					
					constraintValue = 15.6;
					lecturerIdList = getLecturerByUnitId(unit.getId());
					
					
					Log.d(TAG, "Creating node " + unitList.indexOf(unit));
					
					Timeslot timeslot = new Timeslot();
					timeslotMap = new HashMap();
					timeslot.setUnit(unit);
					child1 = new TimeslotNode(timeslot);
					
					TimeslotEdge edge = new TimeslotEdge();
					edge.setSource(currentNode);
					edge.setDestination(child1);
					edge.setWeight(gn);
					child1.addEdge(edge);
					child1.setClassKey(classId);
					
					int bound = 0;
					if(lecturerIdList.size() > 1) {
						bound = lecturerIdList.size() - 1;
					} else {
						bound = 1;
					}

					classLecturersMapping.put(unit.getId(), lecturerIdList.get(rand.nextInt(0, bound)));
					child1.setClassLecturersMapping(classLecturersMapping);
					classStudentsMapping.put(unit.getId(), studentIdList);
					child1.setClassStudentsMapping(classStudentsMapping);
					classUnit.put(unit.getId(), classId);
					child1.setClassUnitMapping(classUnit);
					
					// set heuristics of child
					int duration = 0;
					if (unit.isCommon())
						duration = 2;
					else if (unit.isPractical())
						duration = 5;
					else duration = 3;
					
					if (child1.isRoot() && currentNode == null) {
						String daykey = dayList[rand.nextInt(0, dayList.length - 1)];
						timeslotMap.put(daykey, "8");
						child1.getItem().setTimeslot(timeslotMap);
						currentNode = child1;
					} else {
						child1.setParent(currentNode);
						String daykey = dayList[rand.nextInt(0, dayList.length - 1)];
						String timeValue = currentNode.getItem().getTimeslot().get(daykey);

						if (timeValue == null) {
							timeValue = "6";
						} else {
							timeValue = duration + Double.parseDouble(timeValue) + "";
						}
						timeslotMap.put(daykey, timeValue);
						child1.getItem().setTimeslot(timeslotMap);
						nodeList.add(child1);
					}
					
					// determine its constraint value
					calculateHeuristics(unit);
					
					// set the h
					child1.setH(constraintValue);
					
					// set heuristics of child
					if (currentNode != null) {
						gn = 0;
						if (currentNode.getItem().getUnit().isCommon())
							gn = 2;
						else if (currentNode.getItem().getUnit().isPractical())
							gn = 5;
						else gn = 3;
						double fn = child1.getH() + gn;
						
						child1.setFn(fn);
					}
					
					// nodes can only have 2 nodes
					if (nodeList.size() == 2 && !child1.isRoot()) {
						if (nodeList.get(0).getFn() > nodeList.get(1).getFn()) {
							currentNode = nodeList.get(1);
							
							unitsNotInTimetable.add(nodeList.get(0).getItem().getUnit());
							
							nodeList.removeAll(nodeList);
						} else {
							currentNode = nodeList.get(0);
							
							unitsNotInTimetable.add(nodeList.get(1).getItem().getUnit());
							
							nodeList.removeAll(nodeList);
						}
					} else if (nodeList.size() < 2 && !child1.isRoot() && unitList.indexOf(unit) == unitList.size() - 1) {
						currentNode = nodeList.get(0);

						nodeList.removeAll(nodeList);
					}
				}

				if (!unitsNotInTimetable.isEmpty()) {
					unitList.clear();
					unitList.addAll(unitsNotInTimetable);
				} else {
					break;
				}
			}
			
			// get the next node
			// assign it an edge
			// weight of the edge is the duration of the lecture
			
			// print timetable
			if (currentNode != null) {
				currentNode.printToConsole();
			}

			List<TimeslotNode> nodes =  currentNode.getNodeList();
			
			for (TimeslotNode n : nodes) {
				DayTimeUnit dayTimeUnit = new DayTimeUnit();
				for (String key : n.getItem().getTimeslot().keySet()) {
					dayTimeUnit.setDayOfWeek(key);
					dayTimeUnit.setTimeOfDay(n.getItem().getTimeslot().get(key));
					dayTimeUnit.setPeriod(period);
				}
				
				List<ClassUnit> classUnitList = new ArrayList();
				ClassUnit classUnit = new ClassUnit();
				for (String key : n.getClassUnitMapping().keySet()) {
					classUnit.setClassId(n.getClassUnitMapping().get(key));
					classUnit.setDay(dayTimeUnit.getDayOfWeek());
					classUnit.setHallId(getHallId(n.getClassUnitMapping().get(key)));
					classUnit.setPeriod(period);
					classUnit.setTime(dayTimeUnit.getTimeOfDay());
					classUnit.setUnitId(n.getClassUnitMapping().get(key));
					classUnitList.add(classUnit);
				}
				
				dayTimeUnit.setRoomUnit(classUnitList.get(0));
				dayTimeUnit.setFinal(true);
				dayTime_ClassUnitsTimetable.put(n.getItem().getUnit().getId(), dayTimeUnit);
			}
			
			saveGeneratedTimetable(dayTime_ClassUnitsTimetable);
			saveClassUnits(dayTime_ClassUnitsTimetable);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dayTime_ClassUnitsTimetable;
	}

	/**
	 * get period the timetabler is created for.
	 *
	 * @return string for a period in the year.
	 */
	public String getPeriod() {
		SimpleDateFormat sf = new SimpleDateFormat("MMM yyyy");
		return sf.format(new Date());
	}

	/**
	 * gets the hall id from the db.
	 * @param classId to identify the hall.
	 * @return string containing hall id.
	 * @throws SQLException throws exception when something is wrong.
	 */
	private String getHallId(String classId) throws SQLException {
		String hallId = "";
		String query = "SELECT " + HALL_ID + " FROM " + TABLE_CLASSES
				+ " WHERE " + CLASS_ID + "='" + classId + "'";
		
		ResultSet result = statement.executeQuery(query);
		if (result.next()) {
			hallId = result.getString(HALL_ID);
		}
		return hallId;
	}

	/**
	 * saves the classes for the time slots allocated.
	 * @param dayTime_ClassUnitsTimetable hashmap containing time slots.
	 * @throws SQLException throws exception when something goes wrong.
	 */
	private void saveClassUnits(HashMap<String, DayTimeUnit> dayTime_ClassUnitsTimetable) throws SQLException {
		String del = "DELETE FROM " + TABLE_CLASS_UNITS;
		statement.executeUpdate(del);
		
		for (Map.Entry<String, DayTimeUnit> map : dayTime_ClassUnitsTimetable.entrySet()) {
			String insertQuery = "INSERT INTO " + TABLE_CLASS_UNITS
					+ " VALUES ('" + map.getValue().getRoomUnit().getClassId() + "','"
					+ map.getKey() + "','"
					+ map.getValue().getRoomUnit().getHallId() + "')";
			 statement.executeUpdate(insertQuery);
		}
	}

	/**
	 * save time slots into table timetable.
	 * @param dayTime_ClassUnitsTimetable
	 * @throws SQLException throws exception when something goes wrong.
	 */
	private void saveGeneratedTimetable(HashMap<String, DayTimeUnit> dayTime_ClassUnitsTimetable) throws SQLException {
    	int count = 0;
    	String del = "DELETE FROM " + TABLE_TIMTABLE;
    	statement.executeUpdate(del);
		for (Map.Entry<String, DayTimeUnit> map : dayTime_ClassUnitsTimetable.entrySet()) {
			String addQuery = "INSERT INTO " + TABLE_TIMTABLE +
					" VALUES ('"+ map.getValue().getPeriod() +"','" +
					map.getValue().getDayOfWeek() + "','" +
					map.getValue().getTimeOfDay() + "','" +
					map.getKey() + "')";

			count++;
			Log.d(TAG, "query: " + count + " " + addQuery);
			statement.executeUpdate(addQuery);
		}
	}

	/**
	 * Check constraints, and calculate the heuristic of each state.
	 * 	1. room availability -
	 *  2. room capacity does not exceed number of student
	 * 	3. student conflict -
	 *	4. lecture conflict -
	 *  5. lecturer conflict -
	 *  6. Full time
	 *  7. Part Time
	 *  8. timetable satisfies all constraints.
	 *  
	 *  0.1 priority class assigned to their faculty hall -
	 *  0.2 practical class -
	 *  0.3 common class -
	 *
	 *  subtract constraint to get
	 */
	private void calculateHeuristics(Unit unit) {
		int count = 0;
		for (int i = 0; i < (currentNode != null ? currentNode.getCount() : 0); i++) {
			try {
				TimeslotNode n = (TimeslotNode) currentNode.getNodeAt(i);
				Class cl = getRoom();
				if (!classUnit.containsValue(n.getClassUnitMapping().get(unit.getId())) &&
						Integer.parseInt(cl.getVolume()) >= studentIdList.size()) {
					if (cl.getFacultyId().equals(unit.getFacultyId())) {
						constraintValue -= 0.1;
					}
					
					break;
				} else {
					count++;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		if (count < 1) {
			// combines both the room capacity and availability
			constraintValue -= 3;
		}
		
		// check that student have no clashing courses
		// unit, and student is not allocated another node
		count = 0;
		for (int i = 0; i < (currentNode != null ? currentNode.getCount() : 0); i++) {
			TimeslotNode prevNode = (TimeslotNode) currentNode.getNodeAt(i);
			if (!prevNode.studentsAreNotHere(classId, studentIdList, day, time)) {
				count++;
			}
		}
		
		if (count < 1) {
			constraintValue -= 3;
		}
		
		// check that lecturer have no clashing courses
		// unit, and lecturer is not allocated another node
		count = 0;
		for (int i = 0; i < (currentNode != null ? currentNode.getCount() : 0); i++) {
			TimeslotNode prevNode = (TimeslotNode) currentNode.getNodeAt(i);
			if (!prevNode.lecturersreNotHere(classId, lecturerIdList, day, time)) {
				count++;
			}
		}
		
		if (count < 1) {
			constraintValue -= 5;
		}
		
		// check that lectures don't conflict
		for (int i = 0; i < (currentNode != null ? currentNode.getCount() : 0); i++) {
			TimeslotNode prevNode = (TimeslotNode) currentNode.getNodeAt(i);
			if (!prevNode.courseConflict(classId, unit.getId(), day, time)) {
				count++;
			}
		}
		
		if (count < 1) {
			constraintValue -= 4;
		}
		
		// check common units
		if (unit.isCommon()) {
			constraintValue -= 0.2;
		}
		
		// check practical units
		if (unit.isPractical()) {
			constraintValue -= 0.3;
		}
		
		Log.d(TAG, "Constraint Value " + constraintValue);
	}

	private Class getRoom() {
		Class c = new Class();
		for (Class cl : classList) {
			if (cl.getId().equals(classId)) {
				c = cl;
			}
		}
		return c;
	}

	/**
	 * get Lecturer from db from table lecturer_units.
	 *
	 * @param id unit id
	 * @return
	 * @throws SQLException
	 */
	public List<String> getLecturerByUnitId(String id) throws SQLException {
		lecturerIdList = new ArrayList();
		
		String query = "SELECT * FROM " + TABLE_LECTURER_UNITS
				+ " WHERE " + UNIT_ID + "='" + id + "'";
		
		ResultSet result = statement.executeQuery(query);
		while (result.next()) {
			lecturerIdList.add(result.getString(LECTURER_ID));
		}

		if (lecturerIdList.isEmpty())
			throw new IllegalStateException("Lecturer list is empty.");

		return lecturerIdList;
	}

	/**
	 * get number of student doing a particular unit.
	 *
	 * @param unit unit registered by the student.
	 * @throws SQLException throws exception if an sql error occurs.
	 */
	private void getNumberOfStudentsForClass(Unit unit) throws SQLException {
		int count = 0;
		studentIdList = new ArrayList();
		
		String query = "SELECT DISTINCT * FROM " + TABLE_STUDENT_UNITS
				+ " WHERE " + UNIT_ID + "='" + unit.getId() + "'"
				+ " AND " + IS_REMOVED + "=" + 0;
		ResultSet result = statement.executeQuery(query);
		
		while (result.next()) {
			studentIdList.add(result.getString(STUDENT_ID));
			count++;
		}
		
		Log.d(TAG, count + " Students");
	}
	
	public static void main(String[] args) {
		PathSearchAlgorithm pa = new PathSearchAlgorithm();
		pa.pathFindingAlgorithm(pa.getPeriod());
	}
}
