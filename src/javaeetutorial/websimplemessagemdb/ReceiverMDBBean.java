package javaeetutorial.websimplemessagemdb;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import es.uv.etse.dbcd.EntityBean;

/**
 * Message-Driven Bean implementation class for: ReceiverMDBBean
 */

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup",
            propertyValue = "java:/jms/topic/webappTopic"),
    @ActivationConfigProperty(propertyName = "destinationType",
            propertyValue = "javax.jms.Topic"),
    
    //Aquí espicificamos el nombre del remitente "nameSender" de quien el MDB recive los mensajes
    //Here we specify the name of the sender "nameSender" from whom the MDB receives the messages
    @ActivationConfigProperty(propertyName = "messageSelector",
            propertyValue = "nameSender = 'ETSE'")
})
public class ReceiverMDBBean implements MessageListener {

    static final Logger logger = Logger.getLogger("ReceiverMDBBean");
    @Resource
    public MessageDrivenContext mdc;
    
    //inyectamos el bean que nos sirve como almacenamiento de datos
    @EJB(lookup = "java:global/websimplemessage-plus-mdb/EntityBean!es.uv.etse.dbcd.EntityBean")
    private EntityBean entityBean;
    
    public ReceiverMDBBean() {
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    @Override
    public void onMessage(Message inMessage) {

        try {
            if (inMessage instanceof TextMessage) {

                String messageReceived = inMessage.getBody(String.class);
                
                entityBean.addMessage(messageReceived);
                logger.log(Level.INFO, "ReceiverMDBBean: Message received: {0}", messageReceived);

            } else {
                logger.log(Level.WARNING, "Message of wrong type: {0}", inMessage.getClass().getName());
            }
        } catch (JMSException e) {
            logger.log(Level.SEVERE, "ReceiverMDBBean.onMessage: JMSException: {0}", e.toString());
            mdc.setRollbackOnly();
        }
    }

}
