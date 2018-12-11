package checkmydigitalfootprint;


import com.jfoenix.controls.JFXButton;

import checkmydigitalfootprint.model.Website;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class WebsiteOverviewController {
	
	@FXML
	private TableView<Website> deleteTable;
	
	@FXML
	private TableColumn<Website, String> deleteColumn;
	
	@FXML
	private TableView<Website> keepTable;
	
	@FXML 
	private TableColumn<Website, String> keepColumn;
	
	@FXML
	private JFXButton scanButton;
	
	@FXML
	private JFXButton pauseButton;
	
	private BooleanProperty isScanning = new SimpleBooleanProperty(false);

	private MainApp mainApp;
	
	private FilteredList<Website> keepData;
	
	private FilteredList<Website> deleteData;
	
	Task<Void> task;
	Thread thread;
	
	
	public WebsiteOverviewController() {
		
	}
	
	@FXML
	public void initialize() {
		deleteColumn.setCellValueFactory(cellData -> cellData.getValue().websiteProperty());
		keepColumn.setCellValueFactory(cellData -> cellData.getValue().websiteProperty());
		
		scanButton.visibleProperty().bind(isScanning.not());
		pauseButton.visibleProperty().bind(isScanning);
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		
		keepData = new FilteredList<>(mainApp.getWebsiteData(), p -> p.getKeep());
		deleteData = new FilteredList<>(mainApp.getWebsiteData(), p -> !p.getKeep());
		deleteTable.setItems(deleteData);
		keepTable.setItems(keepData);
	}
	
	@FXML
	public void handleScanInbox() {
		isScanning.set(true);

			
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				mainApp.handleScanInbox();
			}
			
		});
		thread.start();
		
		
	}
	
	@FXML
	public void handlePauseScanInbox() {
		isScanning.set(false);
		try {
			synchronized(thread) {
				thread.wait();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
}