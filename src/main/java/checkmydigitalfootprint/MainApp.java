package checkmydigitalfootprint;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import checkmydigitalfootprint.model.Website;
import checkmydigitalfootprint.model.WebsiteListWrapper;
import checkmydigitalfootprint.util.GmailApi;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
		websiteData.add(new Website("www.linkedin.com"));
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

			loader.setLocation(MainApp.class.getResource("RootLayout.fxml"));
			
			rootLayout = (BorderPane) loader.load();
			
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);
			
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File credentialsFile = getCredentialsFilePath();
		if (credentialsFile != null) {
			loadCredentialsFromFile(credentialsFile);
		} else {
			showLoadCredentialsWindow();
		}
		
		File websiteFile = getWebsiteFilePath();
		if (websiteFile != null) {
			loadWebsiteDataFromFile(websiteFile);
		} 
	}
	
	public void showWebsiteOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("WebsiteOverview.fxml"));
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
			loader.setLocation(MainApp.class.getResource("FileUpload.fxml"));
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
		
		try {
			JAXBContext context = JAXBContext.newInstance(WebsiteListWrapper.class);
			
			Unmarshaller um = context.createUnmarshaller();
			WebsiteListWrapper wrapper = (WebsiteListWrapper) um.unmarshal(file);
			websiteData.clear();
			websiteData.addAll(wrapper.getWebsites());
			
			setWebsiteFilePath(file);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());
			
			alert.showAndWait();
		}
	}
	
	public void saveWebsiteDataToFile(File file) {
		
		try {
			
			JAXBContext context = JAXBContext.newInstance(WebsiteListWrapper.class);
			
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			WebsiteListWrapper wrapper = new WebsiteListWrapper();
			wrapper.setPersons(websiteData);
			
			m.marshal(wrapper,  file);
			
			setWebsiteFilePath(file);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + file.getPath());
			
			alert.showAndWait();
		}
		
	}
	
	public void setWebsiteFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (file != null) {
			prefs.put("websiteFilePath", file.getPath());
		} else {
			prefs.remove("websiteFilePath");
		}
	}
	
	public File getWebsiteFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("websiteFilePath",  null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}
	
	public void handleScanInbox() {
		gmailApi.scanInbox();
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}