
import java.util.*;

/**
 * This Teacher class represents a substitute teacher to be used by the corresponding Driver program.
 * Documentation for basic accessor and mutator methods is omitted.
 * @author Theodore Campbell, 3461781, Abdur Rahman Chowdhury, 3690692
 * */
public class Teacher {
	/**This is the substitute teacher's first name.*/
	private String first;
	/**This is the substitute teacher's last name.*/
	private String last;
	/**This is the substitute teacher's email address.*/
	private String eMail;
	/**This is the list of schools/locations the substitute will not fill in at.*/
	private String blacklist;
	/**This is the set of absences being covered by this substitute.*/
	private ArrayList<Shift> shifts;
	/**These are the names of teachers who have this substitute as a preferred substitute.*/
	private ArrayList<String> favourited;
	/**These are the teachables associated with this substitute.*/
	private String teachables;
	
	/**
	 * This constructor parameterizes the Teacher object, and creates the shifts ArrayList.
	 * @param first - The teacher's first name.
	 * @param last - The teacher's last name.
	 * @param eMail - The teacher's email address.
	 * @param blacklist - The teacher's blacklist: a string with entries separated by "\n".
	 * */
	public Teacher(String first, String last, String eMail, String blacklist) {
		this.first = first;
		this.last = last;
		this.eMail = eMail;
		this.blacklist = blacklist;
		shifts = new ArrayList<Shift>();
		favourited = new ArrayList<String>();
	}//end constructor
	
	public String getFirstName() { return first; };
	public String getLastName() { return last; };
	public String getEMail() { return eMail; };
	public String getBlacklist() { return blacklist; };
	public ArrayList<Shift> getShifts() { return shifts; };
	public ArrayList<String> getPreferred() { return favourited; };
	public String getTeachables() { return teachables; };
	public void setTeachables(String teachables) { this.teachables = teachables; };
	
	public void setFirstName(String first) { this.first = first; };
	public void setLastName(String last) { this.last = last; };
	
	/**
	 * a: This method is used to make assign a shift to this teacher.
	 * @param shift - the Shift object to be assigned to this teacher.
	 * */
	public void assign(Shift shift) {
		shifts.add(shift);
	}//end assign
	
	/**
	 * c: This method creates and assigns a special shift object with no location or absent teacher
	 * to represent only a date and time in which this substitute is unavailable. Methods trying to assign
	 * this substitute for the specified time won't be able to.
	 * @param date - the date of the unavailability.
	 * @param time - the time of the unavailability.*/
	public void makeUnavailable(String date, char time) {
		shifts.add(new Shift(date, time));
	}//end makeUnavailable
	
	/**
	 * d: This method adds a name of a teacher who has this substitute as their choice for filling absences
	 * @param pickyTeacherName - The name of the teacher who prefers this substitute.
	 * */
	public void addAsPreffered(String pickyTeacherName) {
		favourited.add(pickyTeacherName);
	}//end addAsPreferred
	
	/**This method uses a specialized binary search algorithm on the sorted set of Shift
	 * objects within the shifts array list.
	 * @param shift - the Shift object search term.
	 * */
	public boolean binaryShiftSearch(Shift shift) {
		boolean found = false;
		int mid = 0;
		int lowBound = 0;
		int highBound = shifts.size();
		if(shifts.size()==0) {
			return found;
		} else {
			while(!found && lowBound<=highBound) {
				mid = (lowBound+highBound)/2;
				//System.out.println("Midpoint value in binary search: " + mid);
				if(mid>=shifts.size()) {
					break;
				}//end if
				if(shifts.get(mid).compareTo(shift)==0) {
					found = true;
				} else {
					if(shifts.get(mid).compareTo(shift) < 0) {
						highBound = mid - 1;
					} else {
						lowBound = mid + 1;
					}//end if
				}//end if
			}// end while
		}//end else
		return found;
	}
	
	/**This overridden toString method prints the fields of this Teacher object
	 * as well as the fields of all Shift objects the substitute teacher has.
	 * */
	public String toString() {
		String result = last + ", " + first;
		for(int i=0; i<shifts.size(); i++) {
			result += "\t" + shifts.get(i).toString() + "\n";
		}//end for
		return result;
	}
}//end Teacher
