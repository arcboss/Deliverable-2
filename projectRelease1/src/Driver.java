
import java.util.*;
import java.io.*;
import java.text.*;
import org.apache.commons.csv.*;

/**
 * This driver class represents a system for assigning substitute teachers to absences to be filled.
 * @author Theodore Campbell, 3461781, Abdur Rahman Chowdhury, 3690692
 * */
public class Driver {

	public static void main(String[] args) {
		
		/*
		 * File names for the teachers file, absences file, 
		 * and output assignments file are currently forced in,
		 * in that order, as command line arguments.
		 * */
		
		String teacherFileName = "substitutes.csv";		
		String firstName = "DEFAULT";
		String lastName = "DEFAULT";
		String eMail = "default@nope.ca";
		String blacklist = "default blacklist";
		
		String absencesFileName = "absences.csv";
		String absentTeacherName = "DEFAULT";
		String date = "DEFAULT";
		char time = 'X';
		String location = "DEFAULT";
		
		String unavailabilitiesFileName = "unavailabilities.csv";
		
		String preferredTeachersFileName = "preferred.csv";
		
		CSVParser csvParse;
		FileReader teachCSVRead;
		FileReader absencesCSVRead;
		FileReader unavailabilitiesCSVRead;
		FileReader preferredCSVRead;
		
		CSVPrinter csvWriter;
		String outputFileName = "assignments.csv";
		String eMailText = "DEFAULT";
		
		
		Scheduler master = new Scheduler();
		try {
			
			//The four following blocks set up the file readers and load everything into the Scheduler object.
			teachCSVRead = new FileReader(teacherFileName);
			csvParse = new CSVParser(teachCSVRead, CSVFormat.EXCEL.withFirstRecordAsHeader());
			for(CSVRecord finder : csvParse) {
				firstName = finder.get("first name");
				lastName = finder.get("last name");
				eMail = finder.get("email address");
				blacklist = finder.get("blacklist");
				Teacher teach = new Teacher(firstName, lastName, eMail, blacklist);
				master.addTeacher(teach);
				//System.out.println("master:\n" + master.toString());
			}//end for each
			
			absencesCSVRead = new FileReader(absencesFileName);
			csvParse = new CSVParser(absencesCSVRead, CSVFormat.EXCEL.withFirstRecordAsHeader());
			for(CSVRecord finder : csvParse) {
				absentTeacherName = finder.get("teacher name");
				date = finder.get("date");
				time = finder.get("time").charAt(0);
				location = finder.get("location");
				Shift shift = new Shift(absentTeacherName, date, time, location);
				master.addShift(shift);
				System.out.println("Absence:\n" + shift.toString());
			}//end for each
			
			unavailabilitiesCSVRead = new FileReader(unavailabilitiesFileName);
			csvParse = new CSVParser(unavailabilitiesCSVRead, CSVFormat.EXCEL.withFirstRecordAsHeader());
			for(CSVRecord finder : csvParse) {
				firstName = finder.get("substitute first name");
				lastName = finder.get("substitute last name");
				Teacher teach = master.findSub(firstName, lastName);
				date = finder.get("date");
				time = finder.get("time").charAt(0);
				teach.makeUnavailable(date, time);
				//System.out.println("unavailablities:\n" + teach.toString());
			}//end for each
			
			preferredCSVRead = new FileReader(preferredTeachersFileName);
			csvParse = new CSVParser(preferredCSVRead, CSVFormat.EXCEL.withFirstRecordAsHeader());
			for(CSVRecord finder : csvParse) {
				String teacherName = finder.get("teacher");
				String subFirstName = finder.get("substitute first name");
				String subLastName = finder.get("substitute last name");
				Teacher sub = master.findSub(subFirstName, subLastName);
				sub.addAsPreffered(teacherName);
				//System.out.println("preferred:\n" + sub.toString());
			}//end for each
			
			
			
			
			
			
			//This next section is the where the user selects a method of processing, and the processing occurs.
			
			System.out.print("Enter a number to choose an option\n"
					+"\n\t0 : Exit the program and write all assignments to the output file."
					+"\n\t1 : Create all assignments randomly."
					+"\n\t2 : Create all preferred assignments and randomly assign remaining assignments.");
			Scanner userOptions = new Scanner(System.in);
			int userSelection = userOptions.nextInt();
			
			
			if(userSelection==0) {
				System.out.println("Program terminated");
				return;
			} else if((userSelection!=1)&&(userSelection!=2)) {
				System.out.println("Invalid input");
				return;
			}
			
			for(Shift curAbsence : master.getAbsencesPool()) {
				while(master.getAssignments().size()<master.getAbsencesPool().size()) {
					Teacher teach = master.selectRandomTeacher();
					Assignment tentativeAssignment = new Assignment(teach, curAbsence);
					blacklist = teach.getBlacklist();
					Scanner blacklistRead = new Scanner(blacklist);
					blacklistRead.useDelimiter("\n");
					boolean allowed = true;
					//System.out.println(tentativeAssignment.getSub().getFirstName());
					while(blacklistRead.hasNext()) {
						if(blacklistRead.next().equals(tentativeAssignment.getAbsence().getLocation())) {
							allowed = false;
							break;
						}//end if
					}//end while
					if(teach.binaryShiftSearch(curAbsence)) {
						allowed = false;
						break;
					}//end if
					if(allowed) {
						tentativeAssignment.getAbsence().setToScheduled();
						master.addAssignment(tentativeAssignment);
						teach.assign(curAbsence);
					}//end if
					blacklistRead.close();
				}//end while
			}
			
				
			
			csvWriter = new CSVPrinter(new FileWriter(outputFileName), CSVFormat.EXCEL.withHeader
					("Date", "Time", "Location", "Absent Teacher", "Substitute Name", "Email Notification"));
			if(master.getAssignments().size()>0){
				for(Assignment curAssignment : master.getAssignments()) {
					curAssignment.generateParameters();
					ArrayList<String> parameters = curAssignment.getParameters();
					csvWriter.printRecord(parameters.get(0), parameters.get(1), parameters.get(2),
							parameters.get(3), parameters.get(4), parameters.get(5));
				}//end
			}//end if
			csvWriter.close();
			csvParse.close();
			
			System.out.println("Writing success, program terminated.");
			/*
			for(Shift curAbsence : master.getAbsencesPool()) {
				boolean done = false;
				if(master.getAbsencesPool().size()==1) {
					Teacher onlyTeacher = master.getTeacherPool().get(0);
					if(!onlyTeacher.binaryShiftSearch(curAbsence)) {
						onlyTeacher.assign(curAbsence);
					}//end if
				} else {
					while(!done) {
						Teacher teach = new Teacher("","","","");
						if(userSelection==1) {
							teach = master.selectRandomTeacher();
						} else if(userSelection==2) {
							String absentTeacher = curAbsence.getAbsentTeacherName();
							teach = master.selectPreferredTeacher(absentTeacher);
						}//end if
						blacklist = teach.getBlacklist();
						Scanner blacklistRead = new Scanner(blacklist);
						blacklistRead.useDelimiter("/");
						
						//System.out.println(teach.toString());
						if(!teach.binaryShiftSearch(curAbsence)) {
							while(blacklistRead.hasNext()) {
								String temp = blacklistRead.next();
								//System.out.println(temp + " " + randTeach.toString());
								if(curAbsence.getLocation().equals(temp)) {
									break;
								} else {
									teach.assign(curAbsence);
									done = true;
								}//end else
							}//end while
						}//end if
					}//end while
				}
			}//end for
			csvWriter = new CSVPrinter(new FileWriter(outputFileName), CSVFormat.EXCEL.withHeader
					("Date", "Time", "Location", "Absent Teacher", "Substitute Name", "Email Notification"));
			for(Teacher curTeacher : master.getTeacherPool()) {
				firstName = curTeacher.getFirstName();
				lastName = curTeacher.getLastName();
				for(Shift curShift : curTeacher.getShifts()) {
					eMailText = "RE: Filling in for " + curShift.getAbsentTeacherName() 
							+ "\nThis is an auto-generated message.\n\n"
							+ curShift.getAbsentTeacherName() +
							" is not available on " + curShift.getDate() + " in the " +
							curShift.getTime() + "M at " + curShift.getLocation() +
							". We ask that " + curTeacher.getFirstName() + " " +
							curTeacher.getLastName() + " fills in for this absence.\n\nThank you.";
					if(!curShift.getAbsentTeacherName().equals("N/A (Unavailability)")) {
						csvWriter.printRecord(curShift.getDate(), curShift.getTime(), curShift.getLocation(), 
								curShift.getAbsentTeacherName(), (firstName + " " + lastName), eMailText);
					}//end if
				}//end for
			}//end for
			
			csvWriter.close();
			csvParse.close();
			*/
			
			
		} catch (FileNotFoundException fne) {
			System.err.println("EXCEPTION: File not found! " + fne.getMessage());
		} catch (IOException ioe) {
			System.err.println("EXCEPTION: Bad input for teacher file name." + ioe.getMessage());
		} catch (Exception e) {
			System.err.println
			("EXCEPTION: Generic exception (probably can't close CSVParser or CSVPrinter) " + e.getMessage());
			e.printStackTrace();
		}
		
		//ArrayList<Shift> shifts = new ArrayList<Shift>();
		//Teacher teacher1 = new Teacher("John", "Freeman", shifts)
	}

}
