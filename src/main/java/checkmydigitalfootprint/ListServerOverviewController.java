package checkmydigitalfootprint;


import java.util.concurrent.atomic.AtomicBoolean;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import checkmydigitalfootprint.model.ListServer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;


public class ListServerOverviewController {
	
	
	@FXML
	private JFXListView<ListServer> deleteListView;
	
	@FXML 
	private JFXListView<ListServer> keepListView;

	@FXML
	private JFXButton scanButton;
	
	@FXML
	private JFXButton pauseButton;
	
	@FXML
	private JFXButton moveDeleteButton;
	
	@FXML
	private JFXButton moveKeepbutton;
	
	private BooleanProperty isScanning = new SimpleBooleanProperty(false);

	private MainApp mainApp;
	
	private FilteredList<ListServer> keepList;
	
	private FilteredList<ListServer> deleteList;
		
	private AtomicBoolean paused = new AtomicBoolean(false);
	
	private Thread thread;
	
	private ListServer selected;
	

	public ListServerOverviewController() {
		
	}
	
	@FXML
	public void initialize() {
		scanButton.setContentDisplay(ContentDisplay.RIGHT);
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	
		wrapDeleteList();
		wrapKeepList();
		
		scanButton.visibleProperty().bind(isScanning.not());
		pauseButton.visibleProperty().bind(isScanning);
		
		deleteListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			selected = newValue;
		});
		
		keepListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			selected = newValue;
		});
	}
	
	private void wrapKeepList() {
		keepList = new FilteredList<>(mainApp.getListServerList(), p -> p.getKeep());
		
		if (!keepList.isEmpty()) {
			keepListView.setItems(keepList);
			keepListView.setCellFactory(param -> new ListCell<ListServer>() {
				@Override
				protected void updateItem(ListServer item, boolean empty) {
					super.updateItem(item,  empty);
					
					if (empty || item == null) {
						setText(null);
					} else {
						setText(item.getName());
					}
				}
			});
		}
	}
	
	private void wrapDeleteList() {
		deleteList = new FilteredList<>(mainApp.getListServerList(), p -> !p.getKeep());

		if (!deleteList.isEmpty()) {
			deleteListView.setItems(deleteList);
			
			deleteListView.setCellFactory(param -> new ListCell<ListServer>() {
				@Override
				protected void updateItem(ListServer item, boolean empty) {
					super.updateItem(item,  empty);
					
					if (empty || item == null) {
						setText(null);
					} else {
						setText(item.getName());
					}
				}
			});
		}
	}
	
	@FXML
	public void moveKeep() {
		if (selected != null) {
			selected.setKeep(true);
			wrapKeepList();
		}
	}
	
	@FXML
	public void moveDelete() {
		if (selected != null) {
			selected.setKeep(false);
			wrapDeleteList();
		}
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

	
	
}