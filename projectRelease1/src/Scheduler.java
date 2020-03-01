
import java.util.*;

/**
 * This Scheduler class will be used by the Driver program to perform actions on the pool of Teacher objects.
 * Documentation for basic accessor methods and mutator method is omitted.
 * @author Theodore Campbell, 3461781, Abdur Rahman Chowdhury, 3690692
 * */
public class Scheduler {
	/**This is where Teacher objects are stored for processing.*/
	private ArrayList<Teacher> teacherPool;
	/**This is where Shift objects are stored for processing.*/
	private ArrayList<Shift> absencesPool;
	/**This is where the completed assignments are stored.*/
	private ArrayList<Assignment> assignments;
	
	public Scheduler() {
		teacherPool = new ArrayList<Teacher>();
		absencesPool = new ArrayList<Shift>();
		assignments = new ArrayList<Assignment>();
	}//end constructor
	
	//CHANGE getTeacherPool added.
	public ArrayList<Teacher> getTeacherPool() { return teacherPool; };
	public ArrayList<Shift> getAbsencesPool() { return absencesPool; };
	public ArrayList<Assignment> getAssignments() { return assignments; };
	
	/**
	 * This method is used to add a teacher to the teacherPool field.
	 * @param teach - the Teacher object to be added to the pool.*/
	public void addTeacher(Teacher teach) {
		teacherPool.add(teach);
	}//end addTeacher
	
	/**
	 * This method is used to add a shift to the absencesPool field.
	 * @param shift - the Shift object to be added to the pool.
	 * */
	public void addShift(Shift shift) {
		absencesPool.add(shift);
	}//end addShift
	
	/**
	 * This method is used to add an assignment to the set of legitimate assignments.
	 * @param assignment - the Assignment object to be added.
	 * */
	public void addAssignment(Assignment assignment) {
		assignments.add(assignment);
	}//end addAssignment
	
	/**
	 * This method finds a substitute in the pool by first and last name.
	 * @param first - the substitute's first name.
	 * @param first - the substitute's last name.
	 * */
	public Teacher findSub(String first, String last) {
		Teacher teacherResult = new Teacher("Default", "Name", "fakeaddress@gmail.com", "");
		for(Teacher curTeacher : teacherPool) {
			if(curTeacher.getFirstName().equals(first)) {
				if(curTeacher.getLastName().equals(last)) {
					teacherResult = curTeacher;
				}//end if
			}//end if
		}//end for each
		return teacherResult;
	}//end findSub
	
	/**
	 * a: This method selects a random teacher from the teacher pool.
	 * @return randTeacher - a Teacher object randomly selected from the teacherPool.
	 * */
	public Teacher selectRandomTeacher() {
		Random indexRNG = new Random();
		int index = indexRNG.nextInt(teacherPool.size());
		Teacher randTeacher = teacherPool.get(index);
		return randTeacher;
	}//end selectRandomTeacher
	
	/**
	 * d: This method selects a teacher that is preferred by the teacher referenced by name as the input.
	 * @param pickyTeacherName - the name of the teacher choosing by their preference.
	 * */
	public Teacher selectPreferredTeacher(String pickyTeacherName) {
		Teacher teacherResult = selectRandomTeacher();
		for(Teacher curTeacher : teacherPool) {
			for(String curPref : curTeacher.getPreferred()) {
				if(curPref.equals(pickyTeacherName)) {
					teacherResult = curTeacher;
				}//end if
			}//end for each
		}//end for each
		return teacherResult;
	}//end selectPreferredTeacher
	
	/**
	 * This method generates a string from the fields of the object, overwriting the default toString method.
	 * @return the fields of the teacher object, including all Shift objects in the shifts ArrayList.
	 * */
	public String toString() {
		String result = "";
		for(int i=0;i<teacherPool.size();i++) {
			result += teacherPool.get(i).toString() + "\n";
		}//end for
		return result;
	}
}
