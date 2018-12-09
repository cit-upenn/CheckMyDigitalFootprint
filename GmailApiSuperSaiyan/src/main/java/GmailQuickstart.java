package main.java;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
import org.json.JSONArray; 
import org.json.JSONException; 
import org.json.JSONObject; 

public class GmailQuickstart {
    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/main/resources/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws Exception 
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception {
        // Load client secrets.
        InputStream in = GmailQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
  
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String... args) throws Exception {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Print the labels in the user's account.
        String user = "me";
        ListLabelsResponse listResponse = service.users().labels().list(user).execute();
        List<Label> labels = listResponse.getLabels();
        
        
        ArrayList<String> emails = new ArrayList<String>();
        
        ListMessagesResponse idList = service.users().messages().list(user).setQ("").execute();
        
        // create json string 
        String idsJson = idList.toString();
        JSONObject jObj = new JSONObject(idsJson);
        
        JSONArray jsonIds = jObj.getJSONArray("messages"); 
        
//        Writer wr = new FileWriter("PositionCounter.txt");
        
        int tracker = 0;
        
//        for (Thread thread : threads) {	
//        	service.users().threads().get("me", threads.getId()).queue(b, bc);
//        }
        //i = tracker, i<tracker+10
        long start = System.currentTimeMillis();
        for (int i = 0; i < jsonIds.length(); i++) {
//        	emails.add(jsonIds.getJSONObject(i).getString("id"));
        	tracker = i;
        	System.out.println(jsonIds.getJSONObject(i).getString("id"));
        	emails.add(service.users().messages().get("me", jsonIds.getJSONObject(i).getString("id")).execute().toString());
        }
        long finish = System.currentTimeMillis();
        System.out.println(finish-start);
        System.out.println(emails.size());
        
        int counter = 0;
//        while (counter<ids.size()) {
//        	System.out.println(ids.get(counter));
//        	counter++;
//        }
//        wr.write(tracker +"");
//        wr.close();
//        System.out.println(service.users().messages().get("me", jsonIds.getJSONObject(37).getString("id")).execute());
//        System.out.println(jsonIds.length());
        
//        System.out.println(service.users().messages().get("me", "1678ee3c3d6e1799").execute());
//        ListMessagesResponse messageResponse = service.users().messages().list(user).execute();
//        if (labels.isEmpty()) {
//            System.out.println("No labels found.");
//        } else {
//            System.out.println("Labels:");
//            for (Label label : labels) {
//                System.out.printf("- %s\n", label.getName());
//            }
//        }
        
//        Tester test = new Tester();
//        Get email = new Get();
        
//        test.listMessagesMatchingQuery(service, "me", "a");
    }
}