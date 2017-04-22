/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package javaeetutorial.websimplemessagemdb;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSRuntimeException;
import javax.jms.TextMessage;
import javax.jms.Topic;

@Named
@RequestScoped
public class SenderBean {

    static final Logger logger = Logger.getLogger("SenderBean");
   
    @Inject
    @JMSConnectionFactory("java:/jms/factoriaConexiones") 
    private JMSContext context;
    
    @Resource(lookup = "java:/jms/topic/webappTopic")
    private Topic topic;
    private String messageText;
    private String nameSender;

    /**
     * Creates a new instance of SenderBean
     */
    public SenderBean() {
    }

    public String getNameSender() {
		return nameSender;
	}

	public void setNameSender(String nameSender) {
		this.nameSender = nameSender;
	}

	/**
     * Get the value of messageText
     *
     * @return the value of messageText
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * Set the value of messageText
     *
     * @param messageText new value of messageText
     */
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    /*
     * Within a try-with-resources block, create context.
     * Create message.
     * Create producer and send message.
     * Create FacesMessage to display on page.
     */
    @Asynchronous
    public void sendMessage() {
    	TextMessage message;
        
        try {
        	message = context.createTextMessage();
        	message.setStringProperty("nameSender", nameSender);
        	message.setText(messageText);
        	
        	logger.log(Level.INFO, "SENDER BEAN: Send message with text: {0}", message.getText());
            context.createProducer().send(topic, message);

            FacesMessage facesMessage = new FacesMessage("Sent message: " + messageText + ", from: " + nameSender);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (JMSRuntimeException | JMSException t) {
            logger.log(Level.SEVERE, "SenderBean.sendMessage: Exception: {0}", t.toString());
        }
    }
}
