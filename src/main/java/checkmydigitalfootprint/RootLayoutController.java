package checkmydigitalfootprint;

import java.io.File;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;

/**
 * Open, save, and save as for file
 * @author CheckMyDigitalFootprint
 *
 */
public class RootLayoutController {
	
	private MainApp mainApp;
	
	/**
	 * Connects controller to MainApp
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	/**
	 * Open XML file
	 */
	@FXML
	private void handleOpen() {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML Files", "*.xml");
		
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		System.out.println("open file: " + file);
		if (file != null) {
			mainApp.loadListServerDataFromFile(file);
		}
	}
	
	/**
	 * Saves XML file
	 */
	@FXML
	private void handleSave() {
		File listServerFile = mainApp.getListServerFilePath();
		if (listServerFile != null) {
			mainApp.saveListServerToFile(listServerFile);
		} else {
			handleSaveAs();
		}
	}
	
	/**
	 * Save as XML file
	 */
	@FXML
	private void handleSaveAs() {
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
		
		if (file != null) {
			System.out.println("Saving to... : " + file.getAbsolutePath());
			mainApp.saveListServerToFile(file);
		}
	}
	
	/**
	 * Add user credentials
	 */
	@FXML
	private void handleAddCredentials() {
		
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
		
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		
		if (file != null) {
			mainApp.loadCredentialsFromFile(file);
		}
	}

}