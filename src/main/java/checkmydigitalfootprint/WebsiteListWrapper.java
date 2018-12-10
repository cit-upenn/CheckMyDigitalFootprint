package checkmydigitalfootprint;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "websites")
@XmlAccessorType (XmlAccessType.FIELD)
public class WebsiteListWrapper {
	
	@XmlElement(name = "website")
	private List<Website> websites;
	
	
	public List<Website> getWebsites() {
		return websites;
	}
	
	public void setPersons(List<Website> websites) {
		this.websites = websites;
	}
	
}
