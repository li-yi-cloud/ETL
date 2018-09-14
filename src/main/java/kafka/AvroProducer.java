package kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.Producer;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class AvroProducer {
    private static final  Logger logger = LoggerFactory.getLogger(AvroProducer.class);
    private Producer producer;
    public AvroProducer(Producer producer) {
        this.producer = producer;
    }
    public void send_message(String topic,byte[] message){
        logger.info("create kafka Avro ProducerRecord,topic-%s .",topic);
        ProducerRecord<String,byte[]> record = new ProducerRecord<String, byte[]>(topic,message);
        producer.send(record);
    }
    public void close(){
        producer.close();
    }
}
