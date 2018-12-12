package checkmydigitalfootprint.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ListServer {

	private final StringProperty name;
	private final StringProperty email;
	private final BooleanProperty subscribed;
	private final BooleanProperty keep;
	
	
	public ListServer() {
		this(null, null);
	}
	
	public ListServer(String name, String email) {
		this.name = new SimpleStringProperty(name);
		this.email = new SimpleStringProperty(email);
		this.subscribed = new SimpleBooleanProperty(true);
		this.keep = new SimpleBooleanProperty(false);
	}
	
	public StringProperty nameProperty() {
		return name;
	}
	
	public String getName() {
		return name.get();
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public StringProperty emailProperty() {
		return email;
	}
	
	public String getEmail() {
		return email.get();
	}
	
	public void setEmail(String email) {
		this.email.set(email);
	}
	
	public BooleanProperty subscribedProperty() {
		return subscribed;
	}
	
	public boolean getSubscribed() {
		return subscribed.get();
	}
	
	public void setSubscribed(boolean bool) {
		this.subscribed.set(bool);
	}
	
	public BooleanProperty keepProperty() {
		return keep;
	}
	
	public boolean getKeep() {
		return keep.get();
	}
	
	public void setKeep(boolean bool) {
		this.keep.set(bool);
	}
}
