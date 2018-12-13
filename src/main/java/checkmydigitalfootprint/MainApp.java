package checkmydigitalfootprint;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import checkmydigitalfootprint.model.ListServer;
import checkmydigitalfootprint.model.ListServerListWrapper;
import checkmydigitalfootprint.util.GmailApi;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;


/**
 * Main controller of application
 * @author CheckMyDigitalFootprint
 *
 */
public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	private Stage fileUploadStage;
	private GmailApi gmailApi;
	
	
	/**
	 * All listservers added to the list, which are then filtered to be displayed in listviews
	 */
	ObservableList<ListServer> listServerList = FXCollections.observableArrayList(new Callback<ListServer, Observable[]>() {
		@Override
		public Observable[] call(ListServer param) {
			return new Observable[] { param.keepProperty() };
		}
	});
	
	/**
	 * Map of all listservers that acts as a lookup table when deciding to add new listserver or not
	 */
	ObservableMap<String, ListServer> listServerMap = FXCollections.observableHashMap();
	
	
	/**
	 * @return list of listservers
	 */
	public ObservableList<ListServer> getListServerList() {
		return listServerList;
	}
	
	/**
	 * @return map of listservers
	 */
	public ObservableMap<String, ListServer> getListServerMap() {
		return listServerMap;
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("MyDigitalFootprint");
		System.out.println("Starting..");
		initRootLayout();
		showListServerOverview();	
	}
	
	/**
	 * Makes the menu bar, loads any saved file paths 
	 * and shows credential file dialog if no previous credential file is remembered
	 */
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
		System.out.println("Loading credentials file...: " + credentialsFile);
		if (credentialsFile != null) {
			loadCredentialsFromFile(credentialsFile);
		} else {
			showLoadCredentialsWindow();
		}
		
		File listServerFile = getListServerFilePath();
		System.out.println("Loading list data from file...: " + listServerFile);
		if (listServerFile != null) {
			loadListServerDataFromFile(listServerFile);
		}
	}
	
	/**
	 * Shows list server overview
	 */
	public void showListServerOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("ListServerOverview.fxml"));
			AnchorPane listServerOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(listServerOverview);
			
			
			ListServerOverviewController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Prompts user to enter credentials
	 */
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
	
	/**
	 * Sets the file path for set user credentials
	 * @param file is credentials.json file
	 */
	public void setCredentialsFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (file != null) {
			prefs.put("credentialsFilePath", file.getPath());
		} else {
			prefs.remove("credentialsFilePath");
		}
	}
	
	/**
	 * Sets the file path to get users credentials
	 * @return credentials file if exists, else return null
	 */
	public File getCredentialsFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("credentialsFilePath",  null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}
	
	/**
	 * Loads users credentials
	 * @param file is credentials file
	 */
	public void loadCredentialsFromFile(File file) {
		gmailApi = new GmailApi(file);
		
		setCredentialsFilePath(file);
		if (fileUploadStage != null) {
			fileUploadStage = (Stage) fileUploadStage.getScene().getWindow();
			fileUploadStage.close();
		}
	}
	
	/**
	 * Loads the file with user data
	 * @param file is XML file with all listserver data
	 */
	public void loadListServerDataFromFile(File file) {
		
		try {
			JAXBContext context = JAXBContext.newInstance(ListServerListWrapper.class);
			
			Unmarshaller um = context.createUnmarshaller();
			ListServerListWrapper wrapper = (ListServerListWrapper) um.unmarshal(file);
			listServerList.clear();
			listServerList.addAll(wrapper.getListServers());
			
			for (ListServer listServer : wrapper.getListServers()) {
				String email = listServer.getEmail();
				listServerMap.put(email,  listServer);
			}
			setListServerFilePath(file);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());
			
			alert.showAndWait();
		}
	}
	
	/**
	 * Saves the user data to a file
	 * @param file is XML file to save listserver data to
	 */
	public void saveListServerToFile(File file) {
		
		try {
			
			JAXBContext context = JAXBContext.newInstance(ListServerListWrapper.class);
			
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			ListServerListWrapper wrapper = new ListServerListWrapper();
			wrapper.setListServers(listServerList);
			
			m.marshal(wrapper, file);
			
			setListServerFilePath(file);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + file.getPath());
			
			alert.showAndWait();
		}
		
	}
	
	/**
	 * Sets the file path to the user file
	 * @param file is XML file of listserver data
	 */
	public void setListServerFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (file != null) {
			prefs.put("listServerFilePath", file.getPath());
		} else {
			prefs.remove("listServerFilePath");
		}
	}
	
	/**
	 * Gets the file path for the listserver XML file
	 * @return XML file if exists, else null
	 */
	public File getListServerFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("listServerFilePath",  null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}
	
	/**
	 * Scans inbox feature
	 * @param paused is boolean that pauses method when true
	 */
	public void handleScanInbox(AtomicBoolean paused) {
		gmailApi.scanInbox(paused, listServerList, listServerMap);
	}

	/**
	 * 
	 * @return primary stage of application screen
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	/**
	 * Main method to launch application
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}