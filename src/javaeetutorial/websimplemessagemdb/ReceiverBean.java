/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package javaeetutorial.websimplemessagemdb;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSRuntimeException;
import javax.jms.Queue;

import es.uv.etse.dbcd.EntityBean;

@Named
@RequestScoped
public class ReceiverBean {

    static final Logger logger = Logger.getLogger("ReceiverBean");
    
    /*
     * It is possible to define the factory as variable or through annotation
     * (Es posible definir la factoría como el variable o bien atraves  anotación)
     */
//  @Resource(lookup = "java:/jms/factoriaConexiones")
//  private ConnectionFactory connectionFactory;
    
    @Inject
    @JMSConnectionFactory("java:/jms/factoriaConexiones")
    private JMSContext context;
    
    @Resource(lookup = "java:/jms/queue/webappQueue")
    private Queue queue;

    //inyectamos el bean que nos sirve como almacenamiento de datos
    @EJB(lookup = "java:global/websimplemessage-plus-mdb/EntityBean!es.uv.etse.dbcd.EntityBean")
    private EntityBean entityBean;
    
    /**
     * Creates a new instance of ReceiverBean
     */
    public ReceiverBean() {
    }

    /*
     * Within a try-with-resources block, create context.
     * Create consumer, then receive message, using a timeout.
     * Create FacesMessage to display on page.
     */
    public void getMessage() {
        try {
//        	context = connectionFactory.createContext();
//          JMSConsumer receiver = context.createConsumer(queue);
//          String text = receiver.receiveBody(String.class, 1000);
        	
        	List<String> listMessages = entityBean.getListMessages(); 
        	
            if (listMessages.size() > 0) {
            	
                FacesMessage facesMessage = new FacesMessage("Reading messages:");
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                
                int i = 1;
                
                for (String message: listMessages) {
                    facesMessage = new FacesMessage(i + ") " + message);
                    FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                    i++;
                }
                
            } else {
                FacesMessage facesMessage = new FacesMessage("No message received for this client");
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }
        } catch (JMSRuntimeException t) {
            logger.log(Level.SEVERE, "ReceiverBean.getMessage: Exception: {0}", t.toString());
        }
    }
}
