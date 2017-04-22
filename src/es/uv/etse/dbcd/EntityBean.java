package es.uv.etse.dbcd;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;

/**
 * Session Bean implementation class EntityBean
 */
@Singleton
@LocalBean
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class EntityBean {
	
	private List<String> listMessages;
	
	public EntityBean() {
    	listMessages = new ArrayList<String>();
    }

	public List<String> getListMessages() {
		return listMessages;
	}

	public void setListMessages(List<String> listMessages) {
		this.listMessages = listMessages;
	}

	public void addMessage(String message) {
		listMessages.add(message);
	}
}
