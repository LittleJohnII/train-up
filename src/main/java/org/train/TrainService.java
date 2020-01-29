package org.train;

import org.jboss.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class TrainService {

    public static final String TIMESTAMP_PROPERTY_NAME = "timestamp";
    public static final String TRAIN_ID_PROPERTY_NAME = "trainID";
    public static final String DATA_PROPERTY_NAME = "data";
    private static final Logger log = Logger.getLogger(TrainService.class.getName());
    @Inject
    JMSContext jmsContext;
    @Resource(lookup = "java:/topic/demoTopic")
    private Topic topic;
    private DataSource dataSource;

    @Resource(name = "demoDB", mappedName = "java:/jdbc/demoDS")
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    void log(String trainID, int timestamp, int data) throws JMSException {
        log.info("sending message to AMQ...");
        Message message = jmsContext.createMessage();
        message.setStringProperty(TRAIN_ID_PROPERTY_NAME, trainID);
        message.setIntProperty(TIMESTAMP_PROPERTY_NAME, timestamp);
        message.setIntProperty(DATA_PROPERTY_NAME, data);
        jmsContext.createProducer().send(topic, message);
    }

    void processMessage(Message message) {
        try (Connection connection = dataSource.getConnection()) {
            log.info("saving data");

            String trainID = message.getStringProperty(TrainService.TRAIN_ID_PROPERTY_NAME);
            int data = message.getIntProperty(TrainService.DATA_PROPERTY_NAME);
            int timestamp = message.getIntProperty(TrainService.TIMESTAMP_PROPERTY_NAME);

            PreparedStatement ps = connection.prepareStatement("INSERT INTO demo_table (demo_train_id, demo_timestamp, demo_data) VALUES  (?, ?, ?)");

            ps.setString(1, trainID);
            ps.setString(2, String.valueOf(timestamp));
            ps.setString(3, String.valueOf(data));
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    Map<String, Integer> getInfo() {
        Map<String, Integer> map = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(demo_train_id) as demo_count, AVG(demo_data) FROM demo_table as demo_avg");
            ResultSet resultSet = ps.executeQuery();

            map.put("train_count", resultSet.getInt("demo_count"));
            map.put("average_data:", resultSet.getInt("demo_avg"));

            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
