package checkmydigitalfootprint;


import java.util.concurrent.atomic.AtomicBoolean;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import checkmydigitalfootprint.model.ListServer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;


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
	
	@FXML
	private Pane listServerDetailsPane;
	
	private BooleanProperty isScanning = new SimpleBooleanProperty(false);

	private MainApp mainApp;
	
	private FilteredList<ListServer> keepList;
	
	private FilteredList<ListServer> deleteList;
		
	private AtomicBoolean paused = new AtomicBoolean(false);
	
	private Thread thread;
		

	public ListServerOverviewController() {
		
	}
	
	public void initialize() {
		
		deleteListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showListServerDetails(newValue));
		keepListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showListServerDetails(newValue));

	}
	
	private void showListServerDetails(ListServer listServer) {	
		if (listServer != null) {
			VBox vBox = new VBox();
			vBox.setPrefSize(300, 100);
			vBox.setAlignment(Pos.CENTER);
			vBox.setStyle("-fx-background-color: #f2f2f2;");
			
			Label nameLabel = new Label(listServer.getName());
			Label emailLabel = new Label(listServer.getEmail());
			
			nameLabel.setTextAlignment(TextAlignment.CENTER);
			emailLabel.setTextAlignment(TextAlignment.CENTER);
			nameLabel.setStyle("-fx-text-fill: #000;");
			emailLabel.setStyle("-fx-text-fill: #000;");
			
			vBox.getChildren().addAll(nameLabel, emailLabel);
			listServerDetailsPane.getChildren().clear();
			listServerDetailsPane.getChildren().add(vBox);
		} else {
			listServerDetailsPane.getChildren().clear();
		}
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	
		wrapDeleteList();
		wrapKeepList();
		
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
		
		scanButton.visibleProperty().bind(isScanning.not());
		pauseButton.visibleProperty().bind(isScanning);
	}
	
	private void wrapKeepList() {
		keepList = new FilteredList<>(mainApp.getListServerList(), p -> p.getKeep());
		keepListView.setItems(keepList);
	}
	
	private void wrapDeleteList() {
		deleteList = new FilteredList<>(mainApp.getListServerList(), p -> !p.getKeep());
		deleteListView.setItems(deleteList);
	}
	
	@FXML
	public void moveKeep() {
		if (deleteListView.getSelectionModel().getSelectedItem() != null) {
			deleteListView.getSelectionModel().getSelectedItem().setKeep(true);
			wrapKeepList();
			wrapDeleteList();
			deleteListView.getSelectionModel().selectFirst();
		}
	}
	
	@FXML
	public void moveDelete() {
		if (keepListView.getSelectionModel().getSelectedItem() != null) {
			keepListView.getSelectionModel().getSelectedItem().setKeep(false);
			wrapKeepList();
			wrapDeleteList();
			keepListView.getSelectionModel().selectFirst();
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