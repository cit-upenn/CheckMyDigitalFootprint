import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class GmailHandle {
	
	public static void main(String[] args) {
		ArrayList<myEmail> rawEmails = new ArrayList<>();
		
		
		/* Add emails to rawEmails */
		
		//read in email with JSON data
		try {
			// first email 
			Scanner scan;
			scan = new Scanner(new File("testemail.json"));
			String jsonText = "";
			while(scan.hasNext()) {
				jsonText += scan.next();	
			}

			// second email 
			Scanner scan2;
			//read in email with JSON data
				scan2 = new Scanner(new File("testemail2.json"));
			

			String jsonText2 = "";
			while(scan.hasNext()) {
				jsonText2 += scan.next();	
			}
			// create as myEmail object and put into rawEmails
			myEmail email2 = new myEmail(jsonText2);
			rawEmails.add(email2);
			scan2.close();
			
			/* Parse rawEmails into map */
			EmailParser parser = new EmailParser(rawEmails);
			// create as myEmail object and put into rawEmails
			myEmail email1 = new myEmail(jsonText);
			rawEmails.add(email1);
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
