public class HRCommunicator {

	public static String developersName = "Pawel";
	// Declare the important constants!
	public static final String GREAT_COMPANY = "Toast";
	public static final String LOCATION = "Dublin (see: Merrion Square, Pearse Street DART station)";
	public static final String COMPANY_TYPE = "all-in-one Restaurant Management and POS system";
	public static final String[] CHALLENGES = {
			"scaling", "data synchronization", 
			"payment processing", "wi-fi networking"};
	public static final String YOUR_NEW_ROLE = "Senior Engineer on a team of 3-5 engineers";
	public static final String[] TECH_STACK = {
			"Java", "Android", "microservices", "AWS", 
			"with HTML, Angular, React, ES6 on the front end"};
	public static final int SMALL_DRAMATIC_PAUSE = 2000; //time in ms
	public static final int LARGE_DRAMATIC_PAUSE = 3000;
	
	public static void main(String[] args) {
		if(args.length==1){
			developersName = args[0];
		}
		System.out.format("Hey There, %s!\n",developersName);
		System.out.println("Recruiters often suck at connecting with engineers..."
				+ "so I thought I would try speaking your language!");
		HRCommunicator hrComm = new HRCommunicator();
		hrComm.startSpiel();
	}
	
	/**
	 * Method to run through the spiel
	 */
	public void startSpiel(){
		dramaticPause(2000);
		spielWriter
		(
			"\nThe important 0's & 1's", 
			LARGE_DRAMATIC_PAUSE, 
			true
		);
		spielWriter
		(
			GREAT_COMPANY+" is an "+COMPANY_TYPE, 
			LARGE_DRAMATIC_PAUSE, 
			true
		);
		spielWriter
		(
			"We are conveniently located in "+LOCATION, 
			LARGE_DRAMATIC_PAUSE, 
			true
		);
		spielWriter
		(
			"The role we are looking to fill is for a "+YOUR_NEW_ROLE, 
			LARGE_DRAMATIC_PAUSE, 
			true
		);
		spielWriter
		(
			"\nWe are looking for someone who can help us be great at: ",
			LARGE_DRAMATIC_PAUSE, 
			false
		);
		stringArrayWriter(CHALLENGES);
		spielWriter
		(
			"Our tech stack is: ",
			LARGE_DRAMATIC_PAUSE, 
			false
		);
		stringArrayWriter(TECH_STACK);
		spielWriter
		(
			"Our success thus far has included gaining "
					+ "venture capital funding, customers in all 50 states and adoption "
					+ "by lots of restaurants great and small.\n", 
			LARGE_DRAMATIC_PAUSE,
			true
		);
		spielWriter
		(
			"Interested in hearing more? Suggest a few times that you're available "
					+ "for a quick conversation. I’d be happy to fill you in on us and our open roles.",
			LARGE_DRAMATIC_PAUSE,
			true
		);
	}
	
	/**
	 * Method for printing out the contents of String arrays to the console
	 * @param arrayToWrite
	 */
	public void stringArrayWriter(String[] arrayToWrite){
		for(int arrayIndex=0; arrayIndex<arrayToWrite.length; arrayIndex++){
			spielWriter(arrayToWrite[arrayIndex], SMALL_DRAMATIC_PAUSE, false);
			if(arrayIndex+1<arrayToWrite.length-1){
				System.out.print(", ");
			}
			else if(arrayIndex+1 == arrayToWrite.length-1){
				System.out.print(" and ");
			}
			else{
				System.out.println(".");
			}
			
		}
	}
	
	/**
	 * Method for printing out awesome info to the console
	 * @param message
	 * @param pause
	 * @param newLine
	 */
	public void spielWriter(String message, int pause, boolean newLine){
		if(newLine==true){
			System.out.println(message);
		}
		else{
			System.out.print(message);
		}
		if(pause>0 && pause<10000){
			dramaticPause(pause);
		}
	}
	
	/**
	 * Method for delaying the inevitable
	 * @param delayTime
	 */
	public void dramaticPause(int delayTime){
		try {
			Thread.sleep(delayTime);
		} catch (InterruptedException e) {
			System.out.println("You interrupted my dramatic pause!");
		}
	}
	
}