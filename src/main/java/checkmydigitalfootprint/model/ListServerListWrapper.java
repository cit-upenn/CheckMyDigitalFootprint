package checkmydigitalfootprint.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wraps ListServer list so that it can be saved as XML file
 * @author CheckMyDigitalFootprint
 *
 */
@XmlRootElement(name = "listServers")
@XmlAccessorType (XmlAccessType.FIELD)
public class ListServerListWrapper {
	
	/**
	 * List of ListServers which will hold all ListServers so it can be saved to XML
	 */
	@XmlElement(name = "listServer")
	private List<ListServer> listServers;
	
	/**
	 * Get List of ListServers for loading from XML
	 * @return List<ListServer>
	 */
	public List<ListServer> getListServers() {
		return listServers;
	}
	
	/**
	 * Sets List of ListServers
	 * @param listServers
	 */
	public void setListServers(List<ListServer> listServers) {
		this.listServers = listServers;
	}
}
