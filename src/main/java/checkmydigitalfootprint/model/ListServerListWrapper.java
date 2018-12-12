package checkmydigitalfootprint.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "listServers")
@XmlAccessorType (XmlAccessType.FIELD)
public class ListServerListWrapper {
	
	@XmlElement(name = "listServer")
	private List<ListServer> listServers;
	
	public List<ListServer> getListServers() {
		return listServers;
	}
	
	public void setListServers(List<ListServer> listServers) {
		this.listServers = listServers;
	}
}
