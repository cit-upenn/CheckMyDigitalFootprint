package checkmydigitalfootprint;

import java.io.File;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * Controls the JavaFX file upload process
 * @author CheckMyDigitalFootprint
 *
 */
public class FileUploadController {
	
	private MainApp mainApp;
	
	@FXML
	private Pane pane;
	
	@FXML
	private Text uploadText;
	
	/**
	 * Control over drag effects
	 */
	@FXML
	private void initialize() {

		pane.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {
				event.acceptTransferModes(TransferMode.ANY);
				mouseDragOver(event);
				event.consume();
			}
		});
		
		pane.setOnDragEntered(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				event.consume();
			}
		});
		
		pane.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {
				mouseDragDropped(event);
				event.consume();
			}
		});
		
		pane.setOnDragExited(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {
				pane.setStyle("-fx-border-width: 0;");
				event.consume();
			}
		});
	}
	
	/**
	 * Adds user credentials
	 */
	@FXML
	public void handleAddCredentials() {
		
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON Files", "*.json");
		
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		
		if (file != null) {
			mainApp.loadCredentialsFromFile(file);
		}
	}
	
	/**
	 * Triggers style change to pane when mouse dragged over
	 * @param e is drag event
	 */
	private void mouseDragOver(DragEvent e) {
		Dragboard db = e.getDragboard();
		
		boolean isAccepted = db.getFiles().get(0).getName().toLowerCase().endsWith(".json");
	
		if (isAccepted) {
			File file = db.getFiles().get(0);
			System.out.println("dragging");
			pane.setStyle("-fx-border-width: 2;" + 
					"-fx-border-color: black;" +
					"-fx-border-radius: 5;" +
					"-fx-border-style: dashed;");
			uploadText.setText(file.getName());
		}
	}
	
	/**
	 * Loads credential files when mouse dragged is dropped in pane
	 * @param e is drag event
	 */
	private void mouseDragDropped(DragEvent e) {
		Dragboard db = e.getDragboard();
		
		if (db.hasFiles()) {
			
			File file = db.getFiles().get(0);
			
			uploadText.setText(file.getName());
			
			if (file != null) {
				mainApp.loadCredentialsFromFile(file);
			}
		}
	}
	
	/**
	 * Connects controller to MainApp
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

}
