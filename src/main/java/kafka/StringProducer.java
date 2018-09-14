package kafka;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class StringProducer {
    private static final  Logger logger = LoggerFactory.getLogger(StringProducer.class);

    private Producer producer;

    public StringProducer(Producer producer){
        this.producer = producer;
    }

    public void send_message(String topic,String message){
        logger.info("create kafka String ProducerRecord,topic-%s ,value-%s .",topic,message);
        ProducerRecord <String,String> msg = new ProducerRecord<String, String>(topic,message);
        producer.send(msg);
    }
    public void  close(){
        producer.close();
    }
}
