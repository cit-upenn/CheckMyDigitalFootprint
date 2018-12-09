package checkmydigitalfootprint.view;

import java.io.File;
import java.io.IOException;

import checkmydigitalfootprint.MainApp;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class FileUploadController {
	
	private MainApp mainApp;
	
	public FileUploadController() {
	
	}
	
	public void setOnDragOver(AnchorPane window) {
		window.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {
				event.acceptTransferModes(TransferMode.ANY);
				event.consume();
			}
			
		});
	}
	
	public void setOnDragEntered(AnchorPane window) {
		window.setOnDragEntered(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				System.out.println("entering");
				event.consume();
			}
		});
	}
	
	public void setOnDragDropped(AnchorPane window) {
		window.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {
				System.out.println(event);
				mouseDragDropped(event);
				event.consume();
			}
		});
	}
	
	public void setOnDragExited(AnchorPane window) {
		window.setOnDragExited(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {
				System.out.println("exit: " + event);
				event.consume();
			}
		});
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
	
	private void mouseDragOver(final DragEvent e) {
		final Dragboard db = e.getDragboard();
		
		final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase().endsWith(".json");
		System.out.println("dragging");
	}
	
	private void mouseDragDropped(final DragEvent e) {
		final Dragboard db = e.getDragboard();
		System.out.println("dropped");
		boolean success = false;
		
		if (db.hasFiles()) {
			success = true;
			
			final File file = db.getFiles().get(0);
			
			if (file != null) {
				mainApp.loadCredentialsFromFile(file);
			}
		}
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

}
