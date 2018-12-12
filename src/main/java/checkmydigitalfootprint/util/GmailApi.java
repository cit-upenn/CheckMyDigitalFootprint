package checkmydigitalfootprint.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.JSONArray;
import org.json.JSONObject;

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
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

import checkmydigitalfootprint.model.ListServer;
import javafx.collections.ObservableList;

public class GmailApi {
	
	private final String APPLICATION_NAME = "Gmail API Java Quickstart";
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final String TOKENS_DIRECTORY_PATH = "tokens";
	
    private final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);
    private String CREDENTIALS_FILE_PATH = "";
	
    private FileInputStream in;
    
    private Gmail service;
        
	public GmailApi(File file) {
		
		CREDENTIALS_FILE_PATH = file.getAbsolutePath();

	
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
	
	public void scanInbox(AtomicBoolean paused, ObservableList<ListServer> listServerList) {
		String user = "me";
		
		try {

			JsonBatchCallback<Message> batchCallback = new JsonBatchCallback<Message>() {

				@Override
				public void onSuccess(Message msg, HttpHeaders responseHeaders) throws IOException {
					
					Base64 base64Url = new Base64(true);
					byte[] emailBytes = base64Url.decodeBase64(msg.getRaw());
					
					Properties props = new Properties();
					Session session = Session.getDefaultInstance(props, null);
					
					try {
						MimeMessage mail = new MimeMessage(session, new ByteArrayInputStream(emailBytes));						
						
						mail.getAllHeaderLines();
						for (Enumeration<Header> e = mail.getAllHeaders(); e.hasMoreElements();) {
						    Header h = e.nextElement();
						
						    if (h.getName().equals("List-Unsubscribe")) {
//						    	System.out.println("from : " + InternetAddress.toUnicodeString(mail.getFrom()));
						    	String fromHeader = InternetAddress.toUnicodeString(mail.getFrom());
						    	String emailRegex = "(.*)(?:<)(?<=<)([\\w\\d-.]+@[\\w\\d.-]+)(?=>)";
						    	
						    	Pattern pattern = Pattern.compile(emailRegex);
						    	Matcher matches = pattern.matcher(fromHeader);

						    	if (matches.find()) {
						    		String fromName = matches.group(1);
						    		String fromEmail = matches.group(2);
						    	}
						    }
						    
						}
						
					} catch (MessagingException e1) {
						e1.printStackTrace();
					}
				}

				@Override
				public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) throws IOException {

				}
			};

			int totalEmailCount = service.users().getProfile(user).execute().getMessagesTotal();
			
			System.out.println("email count: " + totalEmailCount);
			long start = System.currentTimeMillis();
			
			ListMessagesResponse response = service.users().messages().list(user).execute();

			while (response.getMessages() != null) {
				BatchRequest batch = service.batch();
				synchronized (paused) {
					if (paused.get()) {
						try {
							paused.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				List<Message> messages = response.getMessages();
				
				for (Message message : messages) {
					synchronized (paused) {
						if (paused.get()) {
							try {
								paused.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					service.users().messages().get(user, message.getId()).setFormat("raw").queue(batch, batchCallback);
				}
				batch.execute();
				
				if (response.getNextPageToken() != null) {
					String pageToken = response.getNextPageToken();
					response = service.users().messages().list(user).setPageToken(pageToken).execute();
				} else {
					break;
				}
			}
			
			long end = System.currentTimeMillis();
			
			System.out.println("Time elapsed to scan inbox: " + (end - start));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
