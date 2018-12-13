package checkmydigitalfootprint.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ListServer object. Contains information about name, email and booleans about whether person is subscribed 
 * and which lists they're currently on.
 * @author CheckMyDigitalFootprint
 *
 */
public class ListServer {

	private final StringProperty name;
	private final StringProperty email;
	private final BooleanProperty subscribed;
	private final BooleanProperty keep;
	
	/**
	 * Default constructor
	 */
	public ListServer() {
		this(null, null);
	}
	
	/**
	 * Initializes ListServer with name and email. Subscribed initialized to true and keep initalized to false
	 * @param name is name of listserver
	 * @param email is email of listserver
	 */
	public ListServer(String name, String email) {
		this.name = new SimpleStringProperty(name);
		this.email = new SimpleStringProperty(email);
		this.subscribed = new SimpleBooleanProperty(true);
		this.keep = new SimpleBooleanProperty(false);
	}
	
	/**
	 * StringProperty of listserver name
	 * @return name StringProperty
	 */
	public StringProperty nameProperty() {
		return name;
	}
	
	/**
	 * Get String of name
	 * @return name string
	 */
	public String getName() {
		return name.get();
	}
	
	/**
	 * Set name to new name
	 * @param name String
	 */
	public void setName(String name) {
		this.name.set(name);
	}
	
	/**
	 * StringProperty of email
	 * @return email
	 */
	public StringProperty emailProperty() {
		return email;
	}
	
	/**
	 * Get String of email
	 * @return email String
	 */
	public String getEmail() {
		return email.get();
	}
	
	/**
	 * Set email to new email
	 * @param email String
	 */
	public void setEmail(String email) {
		this.email.set(email);
	}
	
	/**
	 * BooleanProperty of whether person is subscribed or not
	 * @return subscribed
	 */
	public BooleanProperty subscribedProperty() {
		return subscribed;
	}
	
	/**
	 * Get boolean of subscribed
	 * @return subscribed boolean
	 */
	public boolean getSubscribed() {
		return subscribed.get();
	}
	
	/**
	 * Set subscribed to new boolean
	 * @param bool
	 */
	public void setSubscribed(boolean bool) {
		this.subscribed.set(bool);
	}
	
	/**
	 * BooleanProperty of whether listserver is on keep list or delete list
	 * @return keep BooleanProperty
	 */
	public BooleanProperty keepProperty() {
		return keep;
	}
	
	/**
	 * Get boolean of keep. True if listserver on keep list. False if listserver on delete list
	 * @return keep boolean
	 */
	public boolean getKeep() {
		return keep.get();
	}
	
	/**
	 * Set keep to new boolean
	 * @param bool
	 */
	public void setKeep(boolean bool) {
		this.keep.set(bool);
	}
}
