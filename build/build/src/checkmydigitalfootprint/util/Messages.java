package checkmydigitalfootprint.util;

public class Messages {
	String ID;
	String threadID;
	
	Messages (String ID, String threadID){
		this.ID = ID;
		this.threadID = threadID;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getThreadID() {
		return threadID;
	}

	public void setThreadID(String threadID) {
		this.threadID = threadID;
	}
	
	

}
