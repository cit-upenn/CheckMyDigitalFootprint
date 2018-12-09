import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import javafx.collections.FXCollections;
//import javafx.collections.ObservableMap;

public class EmailParser {
	
	private ArrayList<myEmail> rawEmails; 
	private HashMap<String, Website> emailMap; 
	
	EmailParser(ArrayList<myEmail> rawEmails) {
		this.rawEmails = rawEmails;
	}
	
	
	public void parserToMap() throws JSONException {
		 for (int i = 0; i < rawEmails.size(); i++) {
			//create JSON object with json String
			 JSONObject jObj = new JSONObject(rawEmails.get(i));
			 
			 // get email id  
			 String id = jObj.getString("id");
			 
		    // create payload object
			JSONObject payload = jObj.getJSONObject("payload");
			
			JSONArray headers = payload.getJSONArray("headers"); 
//				System.out.println(headers);
			
			// get "From" field from headers
			for (int j=0; i<headers.length(); j++) {
				
				// get name
				JSONObject object = headers.getJSONObject(j);
				String field = object.getString("name");
				
				//  extract "From" 
				if (field.equals("From")) {
					// create website object to hold value of "From"
					Website website = new Website(object.getString("value"), true);
					// add to emailMap 
					emailMap.put(id, website);
					System.out.println(website.getWebsite());
				}
			}
		 }
	}

}
