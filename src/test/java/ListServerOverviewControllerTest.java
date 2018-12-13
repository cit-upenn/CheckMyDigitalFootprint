import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import checkmydigitalfootprint.ListServerOverviewController;
import checkmydigitalfootprint.MainApp;
import checkmydigitalfootprint.model.ListServer;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Test for ListServerOverviewController
 * @author Joseph
 *
 */
public class ListServerOverviewControllerTest extends ApplicationTest {
	
	Parent parent;
	Label headerText;
	Pane headerBox;
	JFXButton scanButton;
	JFXButton deleteButton;
	JFXButton keepButton;
	ObservableList<ListServer> list;
	ListServerOverviewController controller;
	MainApp mainApp;
	
	@Override
	public void start(Stage stage) throws Exception {
		parent = FXMLLoader.load(MainApp.class.getResource("ListServerOverview.fxml"));
		stage.setScene(new Scene(parent));
		stage.show();
		stage.toFront();
	}
	
	@Before
	public void setUp() throws Exception {
		headerText = find("#header-text");
		headerBox = find("#header-box");
		scanButton = find("#scan-btn");
		deleteButton = find("#delete-btn");
		keepButton = find("#keep-btn");
		
		controller = new ListServerOverviewController();
		mainApp = new MainApp();
		list = mainApp.getListServerList();
	}
	
	@After
	public void tearDown() throws TimeoutException {
		FxToolkit.hideStage();
		release(new KeyCode[] {});
		release(new MouseButton[] {});
	}
	
	public <T extends Node> T find(final String query) {
		return (T) lookup(query).queryAll().iterator().next();
	}
	
	@Test
	public void testHeaderText() {
		final String text = "CheckMyDigitalFootprint";
		assertEquals(text, headerText.getText());
	}
	
	@Test
	public void testHeaderBoxBackgroundColour() {
		final String backgroundColour = "0x6d1b7bff";
		assertEquals(backgroundColour, headerBox.getBackground().getFills().get(0).getFill().toString());
	}
	
	@Test
	public void testScanButtonText() {
		final String text = "Scan";
		assertEquals(text, scanButton.getText());
	}
	
	@Test
	public void testDeleteButtonText() {
		final String text = "Delete List >>";
		assertEquals(text, deleteButton.getText());
	}
	
	@Test
	public void testKeepButtonText() {
		final String text = "<<  Keep List";
		assertEquals(text, keepButton.getText());
	}
	
	@Test
	public void testScanButtonBackgroundColour() {
		final String backgroundColour = "0x6d1b7bff";
		assertEquals(backgroundColour, scanButton.getBackground().getFills().get(0).getFill().toString());
	}
	
	@Test
	public void testKeepButtonBackgroundColour() {
		final String backgroundColour = "0x6d1b7bff";
		assertEquals(backgroundColour, keepButton.getBackground().getFills().get(0).getFill().toString());
	}
	
	@Test
	public void testDeleteButtonBackgroundColour() {
		final String backgroundColour = "0x6d1b7bff";
		assertEquals(backgroundColour, deleteButton.getBackground().getFills().get(0).getFill().toString());
	}
	
	@Test
	public void testAddListServerToList() {
		ListServer listServer = new ListServer("test", "test");
		list.add(listServer);
		assertEquals(1, list.size());
	}
	
	
}
