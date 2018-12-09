package json_tester;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class EmailParser {
	
//	public static void main(String[] args) {
	
	public Reader() {
		
	}
	
	   Scanner scan;
	   try {
		//read in file with JSON data
		scan = new Scanner(new File("testemail.json"));
		String jsonText = "";
		while(scan.hasNext()) {
			jsonText += scan.next();
		}
		
//		System.out.println(jsonText);
			
		//create JSON object with json String
		JSONObject jObj = new JSONObject(jsonText);
		
		//Look at the raw String response
		//Look for the results key
		//After the colon there is a square bracket indicating a JSONArray
		//get the int value returned by key "stationId"
	    String id = jObj.getString("id");
	    String threadId = jObj.getString("threadId");
	    
				
//		JSONArray labelIds = jObj.getJSONArray("labelIds");
//		JSON
				
//		for(int i=0;i<labelIds.length();i++) {
//			System.out.println(labelIds.getJsonString(i));
//			System.out.println(labelIds.getJSONObject(i));
//			System.out.println(labelIds.getJSONObject(i));
//			System.out.println(labelIds.getJSONObject(i));
//		}
	    
	    // create payload object
		JSONObject payload = jObj.getJSONObject("payload");
		
		JSONArray headers = payload.getJSONArray("headers"); 
//		System.out.println(headers);
		
		// get 'from' field
		for(int i=0;i<headers.length();i++) {
			JSONObject object = headers.getJSONObject(i);
			String field = object.getString("name");
			if (field.equals("From")) {
				Website website = new Website(object.getString("value"), true);
				System.out.println(website.getWebsite());
			}
		}
	    
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//	}

}