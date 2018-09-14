package kafka;

import org.apache.storm.Config;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.topology.IBasicBolt;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KafkaAvroBolt implements IBasicBolt{
    public static Logger logger = LoggerFactory.getLogger(KafkaAvroBolt.class);
    //	private OutputCollector _collector;
    private Properties kafkaconf = new Properties();
    private static final String topic = "avro";
    private AvroProducer avroProducer;

    public Map<String,Object> getComponentConfiguration(){
//        Map<String,Object> bolt_conf = new HashMap<String, Object>();
//        bolt_conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS,1);
//        return bolt_conf;
    	return null;
    }

    public void prepare(Map conf, TopologyContext context){
        kafkaconf.put("bootstrap.servers","node0.com:9092");
        kafkaconf.put("serializer.class","kafka.serializer.DefaultEncoder");
        kafkaconf.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaconf.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        Producer<String,byte[]> producer = new KafkaProducer<String,byte[]>(kafkaconf);
        avroProducer = new AvroProducer(producer);
    }

    public void execute(Tuple tuple,BasicOutputCollector collector) {
        byte[] message = tuple.getBinary(0);
        avroProducer.send_message(topic,message);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // TODO Auto-generated method stub
        declarer.declare(new Fields("kafka-avro-msg"));

    }

    public void cleanup(){
         avroProducer.close();
    }
}
