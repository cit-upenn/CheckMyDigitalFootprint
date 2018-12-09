package checkmydigitalfootprint.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Website {
	
	private final StringProperty website;
	private final BooleanProperty accountExists;
	private final BooleanProperty keep;
	private final BooleanProperty delete;
	
	public Website() {
		this(null);
	}
	
	public Website(String website) {
		this.website = new SimpleStringProperty(website);
		this.accountExists = new SimpleBooleanProperty(true);
		this.keep = new SimpleBooleanProperty(false);
		this.delete = new SimpleBooleanProperty(false);
	}
	
	
	public StringProperty websiteProperty() {
		return website;
	}
	
	public BooleanProperty hasAccountProperty() {
		return accountExists;
	}
	
	public BooleanProperty keepProperty() {
		return keep;
	}
	
	public BooleanProperty deleteProperty() {
		return delete;
	}
	
	public String getWebsite() {
		return website.get();
	}
	
	public boolean getAccountExists() {
		return accountExists.get();
	}
	
	public boolean getKeep() {
		return keep.get();
	}
	
	public boolean getDelete() {
		return delete.get();
	}
	
	
	public void setWebsite(String website) {
		this.website.set(website);
	}
	
	public void setAccountExists(boolean bool) {
		accountExists.set(bool);
	}
	
	public void setKeep(boolean bool) {
		keep.set(bool);
	}
	
	public void setDelete(boolean bool) {
		delete.set(bool);
	}
	
}
