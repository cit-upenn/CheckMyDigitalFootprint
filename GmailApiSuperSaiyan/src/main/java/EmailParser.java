package main.java;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmailParser {
	
	private ArrayList<Email> rawEmails; 
	private HashMap<String, Website> emailMap; 
	private String id;
	
	EmailParser(ArrayList<Email> rawEmails) {
		this.rawEmails = rawEmails;
		this.emailMap = new HashMap<String, Website>();
	}
	
	
	public ArrayList<Email> getRawEmails() {
		return rawEmails;
	}


	public void setRawEmails(ArrayList<Email> rawEmails) {
		this.rawEmails = rawEmails;
	}


	public HashMap<String, Website> getEmailMap() {
		return emailMap;
	}


	public void setEmailMap(HashMap<String, Website> emailMap) {
		this.emailMap = emailMap;
	}


	public void parseToMap() throws JSONException {
		 for (int i = 0; i < rawEmails.size(); i++) { 
			 
			//create JSON object with json String
			 JSONObject jObj = new JSONObject(rawEmails.get(i).getEmail());
			 
			 // get email id 
			 id = jObj.getString("id");
			 
//			 String threadId = jOBj.getString("thread");
		    // create payload object
			JSONObject payload = jObj.getJSONObject("payload");
			
			JSONArray headers = payload.getJSONArray("headers"); 
			
			// get "From" field from headers
			for (int j=0; j<headers.length(); j++) {
				// get name
				JSONObject object = headers.getJSONObject(j);
				String field = object.getString("name");
				
				//  extract "From" 
				if (field.equals("From")) {
					// create website object to hold value of "From"
					Website wb = new Website(object.getString("value"));
					// add to emailMap 
					emailMap.put(id, wb);
					System.out.println(id + " " + emailMap.get(id).getWebsite());
				}
				
				// extract "List-Unsubscribe"
				else if (field.equals("List-Unsubscribe")) {
					System.out.println(id + " " + object.getString("value"));
			    }	
			}
		 }
	}
}
