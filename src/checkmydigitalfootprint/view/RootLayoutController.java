package checkmydigitalfootprint.view;

import java.io.File;
import java.util.prefs.Preferences;

import checkmydigitalfootprint.MainApp;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

public class RootLayoutController {
	
	private MainApp mainApp;
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
	public void handleAddCredentials() {
		
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
		
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		
		if (file != null) {
			mainApp.loadCredentialsFromFile(file);
		}
	}

}