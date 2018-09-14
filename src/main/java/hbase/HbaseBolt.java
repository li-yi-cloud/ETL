package hbase;

import java.io.IOException;
import java.util.Map;

import main.EtlTopology;
import org.apache.avro.generic.GenericRecord;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HbaseBolt implements IBasicBolt {
	private static final long serialVersionUID = 1L;
	private XdrTableHandle hbOper = null;
	private HbSchema xdrData;

	private static final Logger logger = LoggerFactory.getLogger(HbaseBolt.class);

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("hbase-bolt-msg"));
	}
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}
	public void prepare(Map stormConf, TopologyContext context) {
    	logger.info("HbaseBolt execute : prepare().");
    	hbOper = new XdrTableHandle("xdr");
    	hbOper.connectHbase();
	}
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		byte[] tupleBytes = tuple.getBinary(0);
		
		/* decode XDR Avro */
        logger.info("hbase recv ok");
		XdrAvroDecode xdrAvroDecode = new XdrAvroDecode();
		xdrAvroDecode.decodeXdrAvro(tupleBytes);
		
		/* parse XDR elements */
//		xdrData = xdrAvroDecode.getAndFillXdrInfo();
		if (null == xdrData) {
            logger.error("HbaseBolt execute : call getAndFillXdrInfo failed.");
			return;
		}
		
		/* insert XDR into HBase */
		if ( false == hbOper.insertHB(xdrData) ) {
//			System.out.println("HbaseBolt execute : call insertHB get null. " + HbOper.schObj.tblName);
			// TODO write log, and count
			return;
		}
		
		return;
	}
	public void cleanup() {
		try {
			hbOper.closeHbase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
