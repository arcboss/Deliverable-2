import java.util.*;

/**
 * This class represents an assignment of a substitute teacher to an absence to be filled.
 * Documentation for basic accessors and mutators is omitted.
 * @author Theodore Campbell, 3461781, Abdur Rahman Chowdhury, 3690692
 * */
public class Assignment {
	/**This is the substitute teacher for this assignment.*/
	private Teacher sub;
	/**This is the absence for this assignment.*/
	private Shift shift;
	/**This is the text for the auto-generated email notification.*/
	private String emailText;
	/**This is the set of all data for the assignment, ready to be written to the output csv file.*/
	private ArrayList<String> parameters;
	
	/**
	 * This constructor parameterizes the Assignment object, without generating the email text or populating
	 * the parameters array list/
	 * @param sub - the Teacher object for this assignment.
	 * @param shift - the Shift object absence for this assignment.
	 * */
	public Assignment(Teacher sub, Shift shift) {
		this.sub = sub;
		this.shift = shift;
		emailText = "";
		parameters = new ArrayList<String>();
	}//end constructor
	
	public Teacher getSub() { return sub; };
	public Shift getAbsence() { return shift; };
	public String getEmailText() { return emailText; };
	public ArrayList<String> getParameters() { return parameters; };
	
	/**
	 * This method creates an email notification template for the assignment.
	 * */
	private void generateEmailNotification() {
		emailText = "RE: Filling in for " + shift.getAbsentTeacherName() 
		+ "\nThis is an auto-generated message.\n\n"
		+ shift.getAbsentTeacherName() +
		" is not available on " + shift.getDate() + " in the " +
		shift.getTime() + "M at " + shift.getLocation() +
		". We ask that " + sub.getFirstName() + " " +
		sub.getLastName() + " fills in for this absence.\n\nThank you.";
	}//end generateEmailNotification
	
	/**
	 * This method generates formatted data that is ready to be written to the csv output file.
	 * */
	public void generateParameters() {
		parameters.add(shift.getDate());
		parameters.add("" + shift.getTime());
		parameters.add(shift.getLocation());
		parameters.add(shift.getAbsentTeacherName());
		parameters.add(sub.getFirstName() + " " + sub.getLastName());
		generateEmailNotification();
		parameters.add(emailText);
	}//end generateParameters
}
