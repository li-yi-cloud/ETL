package hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;

/**
 * Hello world!
 *
 */
//import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;  
//import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Random; 

public class XdrTableHandle {

	private Configuration conf = null;
	private Connection connection = null;
	private String tblName;

	private static final Logger logger = LoggerFactory.getLogger(XdrTableHandle.class);

	public XdrTableHandle(){
		this.tblName = "xdrtbl";
		setConfig();
	}
	
	public XdrTableHandle(String tblName){
		this.tblName = tblName;
		setConfig();
	}
	
	private void setConfig(){
        conf = HBaseConfiguration.create();
        conf.set("hbase.rootdir", "/hbase");
        conf.set("fs.hdfs.impl", DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", LocalFileSystem.class.getName());
        conf.set("hbase.zookeeper.quorum", "192.168.3.10");   // 使用eclipse时必须添加这个，否则无法定位master需要配置hosts
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.client.keyvalue.maxsize","104857600");
	}
	/* use to connect to HBase , and gain a connection variable*/
    public void connectHbase(){
        try {
			connection = ConnectionFactory.createConnection(conf);
		} catch (IOException e) {
            logger.error("connect hbase error.:",e);
		}
    }
    
	public boolean insertHB(HbSchema data){	
		String f1 = data.f1;
		String f2 = data.f2;
		String f3 = data.f3;
		
		/* get row key string*/
//		if (null == data.getRowKeyStr()) {
//			logger.error("insertXdr2HB: get rowkey failed .");
//			return false;
//		}
		
		logger.info("insertXdr2HB: rowkey len is " + data.rowKeyStr.length());
		logger.info("insertXdr2HB: rowkey is " + data.rowKeyStr);
		
		/* initiate the Put object*/
	    Put put = new Put(Bytes.toBytes(data.rowKeyStr));// 设置rowkey
	    try {
	    	Table table = connection.getTable(TableName.valueOf(tblName));// HTabel负责跟记录相关的操作如增删改查等// // 获取表
	    	
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.ssnStatusQuality), Bytes.toBytes(data.ssnStatus)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.appQuality), Bytes.toBytes(data.app)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.classTypeQuality), Bytes.toBytes(data.classType)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.regionTypeQuality), Bytes.toBytes(data.regionType)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.ipVerQuality), Bytes.toBytes(data.ipVer)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.dirQuality), Bytes.toBytes(data.dir)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.l4ProtoQuality), Bytes.toBytes(data.l4Proto)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.sptQuality), Bytes.toBytes(data.spt)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.dptQuality), Bytes.toBytes(data.dpt)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.sipQuality), Bytes.toBytes(data.sip)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.sipStrQuality), Bytes.toBytes(data.sipStr)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.dipStrQuality), Bytes.toBytes(data.dipStr)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.srcCountryQuality), Bytes.toBytes(data.srcCountry)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.srcProvinceQuality), Bytes.toBytes(data.srcProvince)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.dstCountryQuality), Bytes.toBytes(data.dstCountry)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.dstProvinceQuality), Bytes.toBytes(data.dstProvince)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.upFlowQuality), Bytes.toBytes(data.upFlow)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.downFlowQuality), Bytes.toBytes(data.downFlow)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.upPktsQuality), Bytes.toBytes(data.upPkts)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.downPktsQuality), Bytes.toBytes(data.downPkts)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.tcpDisorderUpQuality), Bytes.toBytes(data.tcpDisorderUp)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.tcpDisorderDownQuality), Bytes.toBytes(data.tcpDisorderDown)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.tcpRetransUpQuality), Bytes.toBytes(data.tcpRetransUp)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.tcpRetransDownQuality), Bytes.toBytes(data.tcpRetransDown)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.fragPktsUpQuality), Bytes.toBytes(data.fragPktsUp)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.fragPktsDownQuality), Bytes.toBytes(data.fragPktsDown)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.upDurationTimeQuality), Bytes.toBytes(data.upDurationTime)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.downDurationTimeQuality), Bytes.toBytes(data.downDurationTime)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.ssnInitTimeQuality), Bytes.toBytes(data.ssnInitTime)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.startTimeQuality), Bytes.toBytes(data.startTime)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.endTimeQuality), Bytes.toBytes(data.endTime)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.l7AppStatusQuality), Bytes.toBytes(data.l7AppStatus)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.l7ClassTypeQuality), Bytes.toBytes(data.l7ClassType)); 
	    	put.addColumn(Bytes.toBytes(f1), Bytes.toBytes(data.l7ProtocolQuality), Bytes.toBytes(data.l7Protocol)); 
	    	
		    /* fill put object for column family 1*/		
		    put.addColumn(Bytes.toBytes(f2), Bytes.toBytes(data.fileTypeQuality), Bytes.toBytes(data.fileType) ); 
//		    
//		    /* fill put object for column family 2*/	
		    
		    table.put(put);
	    } catch (Exception e) {
	    	logger.error("insertXdr2HB: caught Execption.  tableName is " + tblName+" : \n"+e);
	    	// TODO write log, and count
	    	return false;
	    }	  
	    
	    logger.info(" insertXdr2HB insert xdr Success!");
	    return true;
	}
	
	public void closeHbase() throws IOException{
		connection.close();
	}

}