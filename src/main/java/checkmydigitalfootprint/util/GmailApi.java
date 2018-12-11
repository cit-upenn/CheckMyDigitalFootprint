package checkmydigitalfootprint.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;

public class GmailApi {
	
	private final String APPLICATION_NAME = "Gmail API Java Quickstart";
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final String TOKENS_DIRECTORY_PATH = "tokens";
	
    private final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);
    private String CREDENTIALS_FILE_PATH = "";
	
    private FileInputStream in;
    
    private Gmail service;
    
    private ArrayList<Email> emails;
    
	public GmailApi(File file) {
		
		CREDENTIALS_FILE_PATH = file.getAbsolutePath();
        emails = new ArrayList<Email>();

	
		try {
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			FileReader reader = new FileReader(file);
			in = new FileInputStream(CREDENTIALS_FILE_PATH);
			
			service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
	                .setApplicationName(APPLICATION_NAME)
	                .build();
			
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
    
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        System.out.println("client secrets: " + clientSecrets);

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8000).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
	
	public void scanInbox(AtomicBoolean paused) {
		String user = "me";
		
		try {
			ListMessagesResponse idList = service.users().messages().list(user).setQ("").execute();

			String idsJson = idList.toString();
			JSONObject jObj = new JSONObject(idsJson);

			JSONArray jsonIds = jObj.getJSONArray("messages"); 


			int tracker = 0;

			long start = System.currentTimeMillis();
			for (int i = 0; i < jsonIds.length(); i++) {
				
				synchronized (paused) {
					if (paused.get()) {
						try {
							paused.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}				
				tracker = i;
				System.out.println(jsonIds.getJSONObject(i).getString("id"));
				Email email = new Email(service.users().messages().get("me", jsonIds.getJSONObject(i).getString("id")).execute().toString());
				emails.add(email);
				System.out.println("tracker: " + i);
			}
			long finish = System.currentTimeMillis();
			System.out.println(finish-start);
			System.out.println(emails.size());

			int counter = 0;
			while (counter<emails.size()) {
				System.out.println(emails.get(counter).getEmail());
				counter++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}
