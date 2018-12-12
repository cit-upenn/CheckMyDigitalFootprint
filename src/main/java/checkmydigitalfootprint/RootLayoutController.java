package checkmydigitalfootprint;

import java.io.File;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;

public class RootLayoutController {
	
	private MainApp mainApp;
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
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
	
	@FXML
	private void handleSaveAs() {
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
		
		if (file != null) {
			mainApp.saveListServerToFile(file);
		}
	}
	
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