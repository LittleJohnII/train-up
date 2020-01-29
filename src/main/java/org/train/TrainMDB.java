package org.train;


import org.jboss.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(name = "SimpleMdbToDb",
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
                @ActivationConfigProperty(propertyName = "destination", propertyValue = "demoTopic"),
                @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "mySubscription"),
                @ActivationConfigProperty(propertyName = "clientID", propertyValue = "myClientId"),
                @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
        })
public class TrainMDB implements MessageListener {

    private static final Logger log = Logger.getLogger(TrainMDB.class.getName());

    @Inject
    TrainService trainService;

    @Override
    public void onMessage(Message message) {
        log.info("saving data");
        try {
            trainService.processMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
