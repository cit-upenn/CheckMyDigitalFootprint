package checkmydigitalfootprint.view;

import java.util.concurrent.CountDownLatch;

import com.jfoenix.controls.JFXButton;

import checkmydigitalfootprint.MainApp;
import checkmydigitalfootprint.model.Website;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.transformation.FilteredList;
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
	
	private boolean isScanning = false;

	private MainApp mainApp;
	
	private FilteredList<Website> keepData;
	
	private FilteredList<Website> deleteData;
	
	
	public WebsiteOverviewController() {
		
	}
	
	@FXML
	public void initialize() {
		deleteColumn.setCellValueFactory(cellData -> cellData.getValue().websiteProperty());
		
		keepColumn.setCellValueFactory(cellData -> cellData.getValue().websiteProperty());
		
		scanButton.visibleProperty().bind(new SimpleBooleanProperty(!isScanning));
		
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
	
		isScanning = true;
		mainApp.handleScanInbox();
			
	}
	
	
}