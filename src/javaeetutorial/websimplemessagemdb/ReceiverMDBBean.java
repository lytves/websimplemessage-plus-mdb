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
//@JMSDestinationDefinition(
//        name = "java:/jms/topic/webappTopic",
//        interfaceName = "javax.jms.Topic",
//        destinationName = "PhysicalNewsTopic")
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup",
            propertyValue = "java:/jms/topic/webappTopic"),
    @ActivationConfigProperty(propertyName = "destinationType",
            propertyValue = "javax.jms.Topic"),
    @ActivationConfigProperty(propertyName = "messageSelector",
            propertyValue = "nameSender = 'ETSE'"),
//    @ActivationConfigProperty(propertyName = "subscriptionDurability",
//            propertyValue = "Durable"),
//    @ActivationConfigProperty(propertyName = "clientId",
//            propertyValue = "MyID"),
//    @ActivationConfigProperty(propertyName = "subscriptionName",
//            propertyValue = "MySub")
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
                //logger.log(Level.INFO, "ReceiverMDBBean: Message received: {0}", inMessage.getBody(String.class));

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
