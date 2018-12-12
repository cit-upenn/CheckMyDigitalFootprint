import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import checkmydigitalfootprint.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

public class ListServerOverviewControllerTest extends ApplicationTest {
	
	Parent parent;

	@Override
	public void start(Stage stage) throws Exception {
		parent = FXMLLoader.load(MainApp.class.getResource("FileUpload.fxml"));
		stage.setScene(new Scene(parent));
		stage.show();
		stage.toFront();
	}
	
	@Before
	public void setUp() throws Exception {
		
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

}
