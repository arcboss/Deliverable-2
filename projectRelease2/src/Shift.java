

/**
 * This Shift class represents an absence needed to be filled by a substitute teacher.
 * Documentation for basic accessor and mutator methods is omitted.
 * @author Theodore Campbell, 3461781
 * */
public class Shift implements Comparable<Shift>{
	/**This is the name of the teacher who is absent.*/
	private String absentTeacherName;
	/**This is the date of the absence.*/
	private String date;
	/**This is the time of the absence. Either morning (A), or evening (P).*/
	private char time;
	/**This is the school/location of the absence.*/
	private String location;
	/**This is to keep track of whether the absence has been filled.*/
	private boolean scheduled;
	/**This is to keep track of the absent teacher's specialties.*/
	private String teachables;
	
	/**
	 * This basic constructor parameterizes the Shift object.
	 * @param absentTeacherName - The name of the teacher who is absent.
	 * @param date - the date the teacher will be absent.
	 * @param time - the time of the absence, either morning (A) or evening (P).
	 * */
	public Shift(String absentTeacherName, String date, char time, String location) {
		this.absentTeacherName = absentTeacherName;
		this.date = date;
		this.time = time;
		this.location = location;
		scheduled = false;
	}//end constructor 1
	
	/**
	 * This second constructor does not require a location for the shift, or an absent teacher name.
	 * It is meant for use in creating unavailabilities.
	 * @param date - the date of the unavailability.
	 * @param time - the time of the unavailability.
	 * */
	public Shift(String date, char time) {
		this.absentTeacherName = "N/A (Unavailability)";
		this.date = date;
		this.time = time;
		this.location = "N/A (Unavailability)";
	}//end constructor 2
	
	public String getAbsentTeacherName() { return absentTeacherName; };
	public String getDate() { return date; };
	public char getTime() { return time; };
	public String getLocation() { return location; };
	public boolean getScheduling() { return scheduled; };
	public String getTeachables() { return teachables; };
	public void setTeachables(String teachables) { this.teachables = teachables; };
	
	public void setToScheduled() {
		scheduled = true;
	}//end setToScheduled
	
	/**
	 * This overrided compareTo method compares shift objects based on time, and then date,
	 * but not location. Shifts with the same date and time are treated as duplicates.
	 * This is needed so as to avoid assigning one substitute to be in different places
	 * at the same time.
	 * @param other - the Shift object that is being compared to this Shift object.
	 * */
	public int compareTo(Shift other) {
		int result = 0;
		String thisYear = "";
		String otherYear = "";
		String thisDay = "";
		String otherDay = "";
		String thisMonth = "";
		String otherMonth = "";
		if(this.time != other.time) {
			if(this.time > other.time) {
				result = 1;
			} else {
				result = -1;
			}//end if
		} else if(this.date.compareTo(other.date)!=0) {
			thisYear = this.date.substring(6);
			otherYear = other.date.substring(6);
			thisMonth = this.date.substring(3, 3);
			otherMonth = other.date.substring(3,3);
			thisDay = this.date.substring(0,1);
			otherDay = other.date.substring(0,1);
			if(thisYear.compareTo(otherYear)!=0) {
				result = thisYear.compareTo(otherYear);
			} else if (thisMonth.compareTo(otherMonth)!=0) {
				result = thisMonth.compareTo(otherMonth);
			} else if (thisDay.compareTo(otherDay)!=0) {
				result = thisDay.compareTo(otherDay);
			}//end if
		}//end if
		return result;
	}//end compareTo
	
	/**
	 * This overrided toString method lists the fields of the Shift object.
	 * @return the parameters of the Shift object.
	 * */
	public String toString() {
		return date + ", " + time + ", " + location;
	}//end toString
}//end Shift