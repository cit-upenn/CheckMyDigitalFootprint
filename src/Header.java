/**
 * Header Class models email headers
 * @author tina
 *
 */
public class Header {
	String from;
	String address;
	
	Header(String from, String address) {
		this.from = from;
		this.address = address;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
