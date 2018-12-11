package checkmydigitalfootprint;


import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.collect.Table.Cell;
import com.jfoenix.controls.JFXButton;

import checkmydigitalfootprint.model.Website;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

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
	
	@FXML
	private StackPane stackPane;
	
	private BooleanProperty isScanning = new SimpleBooleanProperty(false);

	private MainApp mainApp;
	
	private FilteredList<Website> keepData;
	
	private FilteredList<Website> deleteData;
	
	private FilteredList<Website> newData;
	
	@FXML
	private ListView<Website> newList;
	
	private AtomicBoolean paused = new AtomicBoolean(false);
	
	private Thread thread;
	
	public WebsiteOverviewController() {
		
	}
	
	@FXML
	public void initialize() {
		scanButton.setContentDisplay(ContentDisplay.RIGHT);
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		
		keepData = new FilteredList<>(mainApp.getWebsiteData(), p -> !p.getKeep());
		deleteData = new FilteredList<>(mainApp.getWebsiteData(), p -> !p.getDelete());
		newData = new FilteredList<>(mainApp.getWebsiteData(), p -> {
			if (p.getKeep() == false && p.getDelete() == false) {
				return true;
			}
			return false;
		});
		
		
		deleteTable.setItems(deleteData);
		keepTable.setItems(keepData);
		newList.setItems(newData);
		
		deleteColumn.setCellValueFactory(new PropertyValueFactory<Website, String>("website"));
		keepColumn.setCellValueFactory(new PropertyValueFactory<Website, String>("website"));
		scanButton.visibleProperty().bind(isScanning.not());
		pauseButton.visibleProperty().bind(isScanning);

		
	}
	
	@FXML
	public void handleScanInbox() {
		isScanning.set(true);
		
		if (thread == null) {
			thread = new Thread() {
				public void run() {
					mainApp.handleScanInbox(paused);
				}
			};
			thread.setDaemon(true);
			thread.start();
		} else {
			synchronized (paused) {
				if (paused.get()) {
					paused.set(false);
					paused.notify();
				}
			}
		}
						
	}
	
	@FXML
	public void handlePauseScanInbox() {
		paused.compareAndSet(false,  true);
		isScanning.set(false);
		
	}
	
	public Pane createDisplay() {
		Pane pane = new Pane();
		return pane;
	}
	
	
}