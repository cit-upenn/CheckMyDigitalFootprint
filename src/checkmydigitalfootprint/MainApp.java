package checkmydigitalfootprint;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import checkmydigitalfootprint.model.Website;
import checkmydigitalfootprint.util.GmailApi;
import checkmydigitalfootprint.view.FileUploadController;
import checkmydigitalfootprint.view.RootLayoutController;
import checkmydigitalfootprint.view.WebsiteOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;



public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	private Stage fileUploadStage;
	private GmailApi gmailApi;
	
	private ObservableList<Website> websiteData = FXCollections.observableArrayList();
	
	public MainApp() {
		Website linkedin = new Website("www.linkedin.com", true);
		linkedin.setKeep(true);
		websiteData.add(new Website("www.facebook.com", true));
		websiteData.add(new Website("www.google.com", true));
		websiteData.add(new Website("www.airbnb.com", true));
		websiteData.add(linkedin);
		websiteData.add(new Website("www.spotify.com", true));
	}
	
	public ObservableList<Website> getWebsiteData() {
		return websiteData;
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("MyDigitalFootprint");
		System.out.println("Starting..");
		initRootLayout();
		showWebsiteOverview();
//		showLoadCredentialsWindow();
//		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
//		prefs.remove("credentialsFilePath");
		
	}
	
	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);
			
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File file = getCredentialsFilePath();
		if (file != null) {
			loadCredentialsFromFile(file);
		} else {
			showLoadCredentialsWindow();
		}
	}
	
	public void showWebsiteOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/WebsiteOverview.fxml"));
			AnchorPane websiteOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(websiteOverview);
			
			
			WebsiteOverviewController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showLoadCredentialsWindow() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/FileUpload.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			
			fileUploadStage = new Stage();
			fileUploadStage.initModality(Modality.WINDOW_MODAL);
			fileUploadStage.initOwner(primaryStage);
			
			Scene scene = new Scene(page);
			fileUploadStage.setScene(scene);
			
			FileUploadController controller = loader.getController();
			controller.setMainApp(this);
			fileUploadStage.showAndWait();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setCredentialsFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (file != null) {
			prefs.put("credentialsFilePath", file.getPath());
		} else {
			prefs.remove("credentialsFilePath");
		}
	}
	
	public File getCredentialsFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("credentialsFilePath",  null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}
	
	public void loadCredentialsFromFile(File file) {
		gmailApi = new GmailApi(file);
		
		setCredentialsFilePath(file);
		if (fileUploadStage != null) {
			fileUploadStage = (Stage) fileUploadStage.getScene().getWindow();
			fileUploadStage.close();
		}
	}
	
	public void loadWebsiteDataFromFile(File file) {
		
	}
	
	public void handleScanInbox() {
		
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}