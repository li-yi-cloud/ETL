package spout;

import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

import hbase.HbaseBolt;
import kafka.KafkaAvroBolt;
import tlv.TlvBolt;

public class EtlSpout{
	private static final String TOPOLOGY_NAME = "etl-topology";
	private static final String READ_SPOUT_ID = "read-spout";
    private static final String DFS_SPOUT_ID = "dfs-spout";
//	private static final String UNCOMPRESS_BOLT_ID = "uncompress-bolt";
	private static final String PARSERTLV_BOLT_ID = "parsertlv-bolt";
	private static final String KAFKA_AVRO_BOLT_ID = "kafka-avro-bolt";
	private static final String KAFKA_STRING_BOLT_ID = "kafka-string-bolt";
	private static final String HBASE_BOLT_ID = "hbase-bolt";
	private static String namenode_host = "192.168.2.10";
	private static String username = "hdfs";
	private static String scandir = "/tmp";
	private static boolean deletefile = false;
    private static Configuration hdfsconfig = new Configuration();

	public static void main( String[] args ){
//		String topic = "test";
//    	ZkHosts zkHosts = new ZkHosts("192.168.2.10");
//    	SpoutConfig spoutConfig = new SpoutConfig(zkHosts,topic,"/etl_offset", "z4-file");
//    	java.util.List<String> zkServers = new ArrayList<String>() ;
//        zkServers.add("192.168.2.10");
//        spoutConfig.zkServers = zkServers;
//        spoutConfig.zkPort = 2181;
//        spoutConfig.socketTimeoutMs = 60 * 1000 ;
//        spoutConfig.scheme = new RawMultiScheme();//(new StringScheme());
//        spoutConfig.startOffsetTime = kafka.api.OffsetRequest.LatestTime();
        
        TopologyBuilder builder = new TopologyBuilder();
        TlvBolt tlvbolt = new TlvBolt();
        KafkaAvroBolt kafkaAvroBolt = new KafkaAvroBolt();
        HbaseBolt hbaseBolt = new HbaseBolt();

        SpoutConfig spoutConfig = new SpoutConfig(namenode_host,scandir,username,deletefile);

//        builder.setSpout(READ_SPOUT_ID,new KafkaSpout(spoutConfig),1);
        builder.setSpout(DFS_SPOUT_ID,new DfsSpout(spoutConfig),1);
        builder.setBolt(PARSERTLV_BOLT_ID, tlvbolt).localOrShuffleGrouping(READ_SPOUT_ID);
        builder.setBolt(KAFKA_AVRO_BOLT_ID, kafkaAvroBolt).localOrShuffleGrouping(PARSERTLV_BOLT_ID);
        builder.setBolt(HBASE_BOLT_ID, hbaseBolt).localOrShuffleGrouping(PARSERTLV_BOLT_ID);
        Config config = new Config();
        
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(TOPOLOGY_NAME, config, builder.createTopology());
	
        org.apache.storm.utils.Utils.sleep(60000);
        cluster.shutdown();   
	}
	
}
