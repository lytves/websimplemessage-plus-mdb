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
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSRuntimeException;
import javax.jms.Queue;

//@JMSDestinationDefinition(
//        name = "java:/jms/queue/webappQueue",
//        interfaceName = "javax.jms.Queue",
//        destinationName = "webappQueue")
@Named
@RequestScoped
public class SenderBean {

    static final Logger logger = Logger.getLogger("SenderBean");
    
    /*
     * It is possible to define the factory as variable or through annotation
     * (Es posible definir la factoría como el variable o bien atraves de anotación)
     */
//  @Resource(lookup = "java:/jms/factoriaConexiones")
//  private ConnectionFactory connectionFactory;
    
    @Inject
    @JMSConnectionFactory("java:/jms/factoriaConexiones") 
    private JMSContext context;
    
    @Resource(lookup = "java:/jms/queue/webappQueue")
    private Queue queue;
    private String messageText;

    /**
     * Creates a new instance of SenderBean
     */
    public SenderBean() {
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
    public void sendMessage() {
        try {
            String text = "Message from producer: " + messageText;
//          context = connectionFactory.createContext();
            context.createProducer().send(queue, text);

            FacesMessage facesMessage =
                    new FacesMessage("Sent message: " + text);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (JMSRuntimeException t) {
            logger.log(Level.SEVERE,
                    "SenderBean.sendMessage: Exception: {0}",
                    t.toString());
        }
    }
}
