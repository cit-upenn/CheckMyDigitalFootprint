import static org.junit.Assert.assertEquals;

import java.awt.Menu;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import checkmydigitalfootprint.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

/**
 * Test for RootLayoutController
 * @author Joseph
 *
 */
public class RootLayoutControllerTest extends ApplicationTest {
	
	Parent parent;
	MenuBar menuBar;
	MenuItem open;
	
	@Override
	public void start(Stage stage) throws Exception {
		parent = FXMLLoader.load(MainApp.class.getResource("RootLayout.fxml"));
		stage.setScene(new Scene(parent));
		stage.show();
		stage.toFront();
	}
	
	@Before
	public void setUp() throws Exception {
		menuBar = find(".menu-bar");
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
	public void testMenuBarBackgroundColour() {
		final String backgroundColour = "0x383838ff";
		assertEquals(backgroundColour, menuBar.getBackground().getFills().get(0).getFill().toString());
	}
	
}
