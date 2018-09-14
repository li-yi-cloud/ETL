package main;

import org.apache.hadoop.conf.Configuration;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.hbase.bolt.mapper.SimpleHBaseMapper;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.hdfs.spout.HdfsSpout;
import org.apache.storm.tuple.Fields;

import hbase.HbaseBolt;
import hbase.IHBaseBolt;
import kafka.KafkaAvroBolt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import spout.DfsSpout;
//import spout.SpoutConfig;
import tlv.TlvBolt;


public class EtlTopology {
    private static final String TOPOLOGY_NAME = "etl-topology";
    private static final String READ_SPOUT_ID = "read-spout";
    private static final String DFS_SPOUT_ID = "dfs-spout";
    //	private static final String UNCOMPRESS_BOLT_ID = "uncompress-bolt";
    private static final String PARSERTLV_BOLT_ID = "parsertlv-bolt";
    private static final String KAFKA_AVRO_BOLT_ID = "kafka-avro-bolt";
    private static final String KAFKA_STRING_BOLT_ID = "kafka-string-bolt";
    private static final String HBASE_BOLT_ID = "hbase-bolt";
    private static String namenode_host = "192.168.3.10";
    private static String username = "hdfs";
    private static String scandir = "/dpi";
    private static boolean deletefile = false;
//    private static Configuration hdfsconfig = new Configuration();

    private static final Logger logger = LoggerFactory.getLogger(EtlTopology.class);

    public static void main( String args[] ){
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

        SimpleHBaseMapper mapper = new SimpleHBaseMapper()
                .withRowKeyField("word")
                .withColumnFields(new Fields("word"))
                .withCounterFields(new Fields("count"))
                .withColumnFamily("cf");

        IHBaseBolt iHBaseBolt = new IHBaseBolt("mytest", mapper)
                .withConfigKey("hbase.conf");

        HdfsSpout hdfsSpout = new HdfsSpout().setHdfsUri("hdfs://192.168.3.10:8020").setSourceDir("/dpi")
                .setArchiveDir("/tmp").setBadFilesDir("/tmp").setReaderType("SEQ")
                .withOutputFields("key","value");

//        SpoutConfig spoutConfig = new SpoutConfig(namenode_host,scandir,username,deletefile);
//        builder.setSpout(READ_SPOUT_ID,new KafkaSpout(spoutConfig),1);
        builder.setSpout(DFS_SPOUT_ID,hdfsSpout,1);
        builder.setBolt(PARSERTLV_BOLT_ID, tlvbolt,6).setNumTasks(6).localOrShuffleGrouping(DFS_SPOUT_ID);
        builder.setBolt(KAFKA_AVRO_BOLT_ID, kafkaAvroBolt,6).setNumTasks(6).localOrShuffleGrouping(PARSERTLV_BOLT_ID);
        builder.setBolt(HBASE_BOLT_ID, iHBaseBolt,6).setNumTasks(6).localOrShuffleGrouping(PARSERTLV_BOLT_ID);
        Config config = new Config();
        config.put("hdfsspout.reader.buffer.bytes",409600);

        if (args.length == 0) {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(TOPOLOGY_NAME, config, builder.createTopology());
            org.apache.storm.utils.Utils.sleep(60000);
            cluster.shutdown();
        }else {
            try {
                StormSubmitter.submitTopology(TOPOLOGY_NAME,config,builder.createTopology());
            }catch (AlreadyAliveException e){
                logger.error("Error: ",e);
                System.exit(1);
            }catch (InvalidTopologyException e){
                logger.error("Error: ",e);
                System.exit(1);
            }catch (AuthorizationException e){
                logger.error("Error: ",e);
                System.exit(1);
            }
        }

    }

}
