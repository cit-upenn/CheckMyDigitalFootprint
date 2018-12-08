package checkmydigitalfootprint.view;

import checkmydigitalfootprint.MainApp;
import checkmydigitalfootprint.model.Website;
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

	private MainApp mainApp;
	
	
	
	private FilteredList<Website> keepData;
	
	private FilteredList<Website> deleteData;
	
	
	public WebsiteOverviewController() {
		
	}
	
	@FXML
	public void initialize() {
		deleteColumn.setCellValueFactory(cellData -> cellData.getValue().websiteProperty());
		
		
		keepColumn.setCellValueFactory(cellData -> cellData.getValue().websiteProperty());
		
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		
		keepData = new FilteredList<>(mainApp.getWebsiteData(), p -> p.getKeep());
		deleteData = new FilteredList<>(mainApp.getWebsiteData(), p -> !p.getKeep());
		deleteTable.setItems(deleteData);
		keepTable.setItems(keepData);
	}
}