import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import com.jfoenix.controls.JFXButton;

import checkmydigitalfootprint.MainApp;
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

public class FileUploadControllerTest extends ApplicationTest {
	
	Parent parent;
	Label introHeader;
	Text disclaimer;
	JFXButton attachFile;
	Text drag;
	Hyperlink gmailApi;
	Pane introBox;
	
	@Override
	public void start(Stage stage) throws Exception {
		parent = FXMLLoader.load(MainApp.class.getResource("FileUpload.fxml"));
		stage.setScene(new Scene(parent));
		stage.show();
		stage.toFront();
	}
	
	@Before
	public void setUp() throws Exception {
		introHeader = find("#intro-header");
		disclaimer = find("#disclaimer-text");
		attachFile = find("#attach-file-btn");
		drag = find("#drag-text");
		gmailApi = find("#gmail-api");
		introBox = find("#intro-box");
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
	public void testIntroHeaderText() {
		final String headerText = "CheckMyDigitalFootprint";
		assertEquals(headerText, introHeader.getText());
	}
	
	@Test
	public void testDisclaimerText() {
		final String disclaimerText = "We will NOT steal your data. Hence why you must provide your own API key :)";
		assertEquals(disclaimerText, disclaimer.getText());
	}
	
	@Test
	public void testAttachFileButtonText() {
		final String attachFileText = "Attach File";
		assertEquals(attachFileText, attachFile.getText());	
	}
	
	@Test
	public void testDragText() {
		final String dragText = "Drag and drop credentials.json file here";
		assertEquals(dragText, drag.getText());
	}
	
	@Test
	public void testIntroBoxBackgroundColour() {
		final String backgroundColour = "0x6d1b7bff";
		assertEquals(backgroundColour, introBox.getBackground().getFills().get(0).getFill().toString());
	}
		
}
