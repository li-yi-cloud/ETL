package tlv;

import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import kafka.KafkaAvroBolt;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.BytesWritable;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4SafeDecompressor;
import net.jpountz.util.UnsafeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xdrSchema.XdrSchema;


public class TlvBolt implements IBasicBolt {
	byte[] uncomData;
	byte[] decodeData;
	int decodeLen = 0;
	DataTlvStream file_flow;
    public static Logger logger = LoggerFactory.getLogger(TlvBolt.class);

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("tlv-bolt-msg"));
	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	public void prepare(Map stormConf, TopologyContext context) {
		// TODO Auto-generated method stub
		decodeData = new byte[10000000];
//		IP.load("/home/zhaoyuchao/mydata4vipday2.dat");
	}
    public byte[] toByteArray (Object obj) {
        byte[] bytes = null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }
    public void execute(Tuple tuple, BasicOutputCollector collector) {
		// TODO Auto-generated method stub
//		uncomData = tuple.getBinary(1);
        BytesWritable value = (BytesWritable)tuple.getValue(1);
        uncomData = value.copyBytes();
//        logger.info("obj class:",tuple.getValue(1).getClass());
//        logger.info(value.toString());

//		uncomData = toByteArray(value);
		logger.info("%d",uncomData.length);
		uncompressLz4();
		System.out.println("file total length xdrByteArray.length = " + uncomData.length );

		if (0 != decodeLen) {
			try {
				paser_tlv(collector);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("tlv bold uncompress and parse xdr.length = 0 .");
		}

	}

	public void cleanup() {
		// TODO Auto-generated method stub
		
	}
	
	private void paser_tlv(BasicOutputCollector collector) throws IOException{
	    int ulTotalLen = 0; //表示已经处理过的byte的总长度
		int tlv_len = 0;
		int data_len = 0;
	    ByteArrayInputStream InputStream= new ByteArrayInputStream(decodeData);
	    file_flow = new DataTlvStream(InputStream);
	    while(ulTotalLen < decodeLen){
	    	//解析头
	    	file_flow.parserTlvHead();
	    	tlv_len = file_flow.getTlvLength();
	    	data_len = file_flow.getDataLength();
	    	parserData(collector,data_len);
	    	ulTotalLen+=tlv_len;
	    }
	    file_flow.close();
	}
    public void parserData(BasicOutputCollector collector,int datalength) throws IOException{
    	TlvData tlvData = new TlvData();
    	XdrSchema xdrBuild = XdrSchema.newBuilder().build();

		int ulTotalLen = 0; //表示已经处理过的byte的总长度
		while(ulTotalLen < datalength){
	    	//解析头
			file_flow.parserTlvHead();
	    	int tlv_len = file_flow.getTlvLength();
	    	int data_len = file_flow.getDataLength();
	    	int tlv_id = file_flow.getTlvId();
	    	int tlv_shortdata = file_flow.getShortData();
//	    	System.out.println("tlvid=" + tlv_id);
//	    	System.out.println("tlvlength=" + tlv_len);
//	    	file_flow.skip(data_len);
	    	parserTlvData(tlvData,xdrBuild,tlv_id,tlv_shortdata,tlv_len,data_len);
	    	ulTotalLen+= tlv_len;
	    }

    	DatumWriter<XdrSchema> userDatumWriter = new SpecificDatumWriter<XdrSchema>(XdrSchema.class);
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
    	userDatumWriter.write(xdrBuild,encoder);
    	encoder.flush();
        out.close();
        byte[] serializedBytes = out.toByteArray();
		collector.emit(new Values(serializedBytes));
	}

    public void parserTlvData(TlvData tlvData,XdrSchema xdrBuild,int tlv_id,int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	xdrBuild.setFileType(1);
    	switch(tlv_id){
			case TlvId.XDR_SESSION_STATUS:
				//1
				TlvData.SsnStatus ssn = tlvData.new SsnStatus();
				file_flow.parserSsnStatus(ssn,tlv_shortdata,tlv_len,data_len);
				xdrBuild.setSsnStatus(ssn.ssnStatus);
				break;
			case TlvId.XDR_APP_ID:
				//2
				TlvData.AppId app = tlvData.new AppId();
				file_flow.parserAppId(app,tlv_shortdata,tlv_len,data_len);
				xdrBuild.setAppId(app.appId);
				break;
			case TlvId.XDR_TUPLE:
				//3
				TlvData.Tuple tuple = tlvData.new Tuple();
				xdrSchema.XdrTuple xdrTuple = new xdrSchema.XdrTuple();
				file_flow.parserTuple(tuple,tlv_shortdata,tlv_len,data_len);
				xdrTuple.setIpVer(tuple.ipVer);
				xdrTuple.setL4Proto(tuple.l4Proto);
				xdrTuple.setSrcPort(tuple.srcPort);
				xdrTuple.setDstPort(tuple.dstPort);
				xdrTuple.setV4SrcIP(tuple.v4SrcIP);
				xdrTuple.setV4DstIP(tuple.v4DstIP);
				xdrTuple.setStrSrcIP(tuple.strSrcIP);
                String[] srcIpbuf = {"beijing","beijing"};
				//String[] srcIpbuf = IP.find(tuple.strSrcIP);
				xdrTuple.setStrSrcCountry(srcIpbuf[0]);
				xdrTuple.setStrSrcProvince(srcIpbuf[1]);
				xdrTuple.setStrDstIP(tuple.strDstIP);
                String[] dstIpbuf = {"shanghai","shanghai"};
//				String[] dstIpbuf = IP.find(tuple.strDstIP);
				xdrTuple.setStrDstCountry(dstIpbuf[0]);;
				xdrTuple.setStrDstProvince(dstIpbuf[1]);
				xdrBuild.setTuple(xdrTuple);
//				System.out.println("src_port=" + tuple.srcPort);
//				System.out.println("dst_port=" + tuple.dstPort);
//				System.out.println("src_ip=" + tuple.strSrcIP);
//				System.out.println("dst_ip=" + tuple.strDstIP);
//				System.out.println("StrSrcCountry=" + srcIpbuf[0]);
//				System.out.println("StrSrcProvince=" + srcIpbuf[1]);
//				System.out.println("StrDstCountry=" + dstIpbuf[0]);
//				System.out.println("StrDstProvince=" + dstIpbuf[1]);
		    	break;
			case TlvId.XDR_SSN_INFO:
				//4
				TlvData.SsnInfo ssnInfo = tlvData.new SsnInfo();
				xdrSchema.XdrSsnInfo xdrSsnInfo = new xdrSchema.XdrSsnInfo();
				file_flow.parserSsnInfo(ssnInfo,tlv_shortdata,tlv_len,data_len);
				xdrSsnInfo.setUpFlow(ssnInfo.upFlow);
				xdrSsnInfo.setDownFlow(ssnInfo.downFlow);
				xdrSsnInfo.setUpPackets(ssnInfo.upPackets);
				xdrSsnInfo.setDownPackets(ssnInfo.downPackets);
				xdrSsnInfo.setUpDuraUpTime(ssnInfo.upDuraUpTime);
				xdrSsnInfo.setDownDuraUpTime(ssnInfo.downDuraUpTime);
				xdrSsnInfo.setUpFragPackets(ssnInfo.upFragPackets);
				xdrSsnInfo.setDownFragPackets(ssnInfo.downFragPackets);
				xdrSsnInfo.setUpTcpDisorder(ssnInfo.upTcpDisorder);
				xdrSsnInfo.setDownTcpDisorder(ssnInfo.downTcpDisorder);
				xdrSsnInfo.setUpTcpRetransmit(ssnInfo.upTcpRetransmit);
				xdrSsnInfo.setDownTcpRetransmit(ssnInfo.downTcpRetransmit);
				xdrBuild.setSSnInfo(xdrSsnInfo);
		    	break;
			case TlvId.XDR_TIME:
				//5
				TlvData.Time times = tlvData.new Time();
				xdrSchema.XdrTimes xdrTimes = new xdrSchema.XdrTimes();
				file_flow.parserTime(times,tlv_shortdata,tlv_len,data_len);
				xdrTimes.setSsnInitTime(times.ssnInitTime);
				xdrTimes.setStartTime(times.startTime);
				xdrTimes.setEndTime(times.endTime);
				xdrBuild.setTimes(xdrTimes);
		    	break;
			case TlvId.XDR_BUSI_INFO:
				TlvData.BusiInfo busiInfo = tlvData.new BusiInfo();
				file_flow.parserBusiInfo(busiInfo,tlv_shortdata,tlv_len,data_len);
		    	break;
			case TlvId.XDR_TCP_INFO:
				TlvData.TcpInfo tcpInfo = tlvData.new TcpInfo();
				file_flow.parserTcpInfo(tcpInfo,tlv_shortdata,tlv_len,data_len);
		    	break;
			case TlvId.XDR_RESPONSE_DELAY:
				TlvData.ResponseDelay responseDelay = tlvData.new ResponseDelay();
				file_flow.parserResponseDelay(responseDelay,tlv_shortdata,tlv_len,data_len);
		    	break;
			case TlvId.XDR_L7_TYPE:
				//9
				TlvData.L7Type l7Type = tlvData.new L7Type();
				xdrSchema.XdrL7Type xdrL7Type = new xdrSchema.XdrL7Type();
				file_flow.parserL7Type(l7Type,tlv_shortdata,tlv_len,data_len);
				xdrL7Type.setAppStatus(l7Type.appStatus);
				xdrL7Type.setClassID(l7Type.classID);
				xdrL7Type.setProtocol(l7Type.protocol);
				xdrBuild.setL7Type(xdrL7Type);
		    	break;
			case TlvId.XDR_HTTP_INFO:
				TlvData.HttpInfo httpInfo = tlvData.new HttpInfo();
				file_flow.parserHttpInfo(httpInfo,tlv_shortdata, tlv_len, data_len);
		    	break;
			case TlvId.XDR_HTTP_HOST:
				tlvData.httpHost = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				break;
			case TlvId.XDR_HTTP_URL:
				tlvData.httpUrl = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				break;
			case TlvId.XDR_HTTP_XONLINEHOST:
				tlvData.httpXonlineHost = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				break;
			case TlvId.XDR_HTTP_USERAGENT:
				tlvData.httpUserAgent = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				break;
			case TlvId.XDR_HTTP_CONTENT:
				tlvData.httpContent = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				break;
			case TlvId.XDR_HTTP_REFER:
				tlvData.httpRefer = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				break;
			case TlvId.XDR_HTTP_COOKIE:
				tlvData.httpCookie = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				break;
			case TlvId.XDR_HTTP_LOCATION:
				tlvData.httpLocation = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				break;
			case TlvId.XDR_SIP_INFO:
				TlvData.SipInfo sipInfo = tlvData.new SipInfo();
				file_flow.parserSipInfo(sipInfo,tlv_shortdata, tlv_len, data_len);
				break;
			case TlvId.XDR_SIP_CALLER_NUM:
				tlvData.sipCallerNum = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				break;
			case TlvId.XDR_SIP_CALLED_NUM:
				tlvData.sipCalledNum = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				break;
			case TlvId.XDR_SIP_CALLID_NUM:
				tlvData.sipCallidNum = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				break;
			case TlvId.XDR_RTSP_INFO:
				TlvData.RtspInfo rtspInfo = tlvData.new RtspInfo();
				file_flow.parserRtspInfo(rtspInfo,tlv_shortdata, tlv_len, data_len);
				break;
			case TlvId.XDR_RTSP_URL:
				tlvData.rtspUrl = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				break;
			case TlvId.XDR_RTSP_USERAGENT:
				tlvData.rtspUserAgent = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				break;
			case TlvId.XDR_RTSP_SERVERIP:
				tlvData.rtspServerIP = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				break;
			case TlvId.XDR_FTP_STATUS:
				tlvData.ftpStatus = file_flow.parserShort(tlv_shortdata, tlv_len, data_len);
				System.out.println("ftpStatus" + tlvData.ftpStatus);
				break;
			case TlvId.XDR_FTP_USR_NM:
				tlvData.ftpUsrName = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				System.out.println("ftpUsrName" + tlvData.ftpUsrName);
				break;
			case TlvId.XDR_FTP_CUR_DIR:
				tlvData.ftpCurDir = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				System.out.println("ftpCurDir" + tlvData.ftpCurDir);
				break;
			case TlvId.XDR_FTP_TRANS_MODE:
				tlvData.ftpTransMode = file_flow.parserByte(tlv_shortdata, tlv_len, data_len);
				System.out.println("ftpTransMode" + tlvData.ftpTransMode);
				break;
			case TlvId.XDR_FTP_TRANS_TYPE:
				tlvData.ftpTransType = file_flow.parserByte(tlv_shortdata, tlv_len, data_len);
				System.out.println("ftpTransType" + tlvData.ftpTransType);
				break;
			case TlvId.XDR_FTP_FILE_NM:
				tlvData.ftpFileName = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				System.out.println("ftpFileName" + tlvData.ftpFileName);
				break;
			case TlvId.XDR_FTP_FILE_SIZ:
				tlvData.ftpFileSize = file_flow.parserInt(tlv_shortdata, tlv_len, data_len);
				System.out.println("ftpFileSize" + tlvData.ftpFileSize);
				break;
			case TlvId.XDR_FTP_RSP_TM:
				tlvData.ftpResDelay = file_flow.parserLong(tlv_shortdata, tlv_len, data_len);
				System.out.println("ftpResDelay" + tlvData.ftpResDelay);
				break;
			case TlvId.XDR_FTP_TRANS_TM:
				tlvData.ftpTransTime = file_flow.parserLong(tlv_shortdata, tlv_len, data_len);
				System.out.println("ftpTransTime" + tlvData.ftpTransTime);
				break;
			case TlvId.XDR_MAIL_MSG_TYPE:
				tlvData.mailMessType = file_flow.parserShort(tlv_shortdata, tlv_len, data_len);
				System.out.println("mailMessType" + tlvData.mailMessType);
				break;
			case TlvId.XDR_MAIL_RSP_STATUS:
				tlvData.mailStatusType = file_flow.parserShort(tlv_shortdata, tlv_len, data_len);
				System.out.println("mailStatusType" + tlvData.mailStatusType);
				break;
			case TlvId.XDR_MAIL_USR_NM:
				tlvData.mailUsName = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				System.out.println("mailUsName" + tlvData.mailUsName);
				break;
			case TlvId.XDR_MAIL_SND_INFO:
				tlvData.mailSendInfo = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				System.out.println("mailSendInfo" + tlvData.mailSendInfo);
				break;
			case TlvId.XDR_MAIL_LEN:
				tlvData.mailLength = file_flow.parserInt(tlv_shortdata, tlv_len, data_len);
				System.out.println("mailLength" + tlvData.mailLength);
				break;
			case TlvId.XDR_MAIL_DOMAIN:
				tlvData.mailDomain = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				System.out.println("mailDomain" + tlvData.mailDomain);
				break;
			case TlvId.XDR_MAIL_RCV_ACCOUNT:
				tlvData.mailRcvAccount = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				System.out.println("mailRcvAccount" + tlvData.mailRcvAccount);
				break;
			case TlvId.XDR_MAIL_HDR:
				tlvData.mailHdr = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				System.out.println("mailHdr" + tlvData.mailHdr);
				break;
			case TlvId.XDR_MAIL_ACS_TYPE:
				tlvData.mailAcsType = file_flow.parserByte(tlv_shortdata, tlv_len, data_len);
				System.out.println("mailAcsType" + tlvData.mailAcsType);
				break;
			case TlvId.XDR_DNS_DOMAIN:
				tlvData.dnsDomain = file_flow.parserString(tlv_shortdata, tlv_len, data_len);
				System.out.println("dnsDomain" + tlvData.dnsDomain);
				break;
			case TlvId.XDR_DNS_IP_NUM:
				tlvData.dnsIpNum = file_flow.parserByte(tlv_shortdata, tlv_len, data_len);
				System.out.println("dnsIpNum" + tlvData.dnsIpNum);
				break;
			case TlvId.XDR_DNS_IPV4:
				tlvData.dnsIpV4 = file_flow.parserInt(tlv_shortdata, tlv_len, data_len);
				System.out.println("dnsIpV4" + tlvData.dnsIpV4);
				break;
			case TlvId.XDR_DNS_RSP_CODE:
				tlvData.dnsRspCode = file_flow.parserByte(tlv_shortdata, tlv_len, data_len);
				System.out.println("dnsRspCode" + tlvData.dnsRspCode);
				break;
			case TlvId.XDR_DNS_REQ_CNT:
				tlvData.dnsRequsNum = file_flow.parserByte(tlv_shortdata, tlv_len, data_len);
				System.out.println("dnsRequsNum" + tlvData.dnsRequsNum);
				break;
			case TlvId.XDR_DNS_RSP_RECORD_CNT:
				tlvData.dnsAncount = file_flow.parserByte(tlv_shortdata, tlv_len, data_len);
				System.out.println("dnsAncount" + tlvData.dnsAncount);
				break;
			case TlvId.XDR_DNS_AUTH_CNTT_CNT:
				tlvData.dnsNscount = file_flow.parserByte(tlv_shortdata, tlv_len, data_len);
				System.out.println("dnsNscount" + tlvData.dnsNscount);
				break;
			case TlvId.XDR_DNS_EXTRA_RECORD_CNT:
				tlvData.dnsArcount = file_flow.parserByte(tlv_shortdata, tlv_len, data_len);
				System.out.println("dnsArcount" + tlvData.dnsArcount);
				break;
			case TlvId.XDR_DNS_RSP_DELAY:
				tlvData.dnsRspDelay = file_flow.parserInt(tlv_shortdata, tlv_len, data_len);
				System.out.println("dnsRspDelay" + tlvData.dnsRspDelay);
				break;
			case TlvId.XDR_VPN_TYPE:
				tlvData.vpnType = file_flow.parserByte(tlv_shortdata, tlv_len, data_len);
				System.out.println("vpnType" + tlvData.vpnType);
				break;
			case TlvId.XDR_PROXY_TYPE:
				tlvData.proxyType = file_flow.parserByte(tlv_shortdata, tlv_len, data_len);
				System.out.println("proxyType" + tlvData.proxyType);
				break;
			case TlvId.HTTP_REQ_CONTENT:
				xdrBuild.setFileType(3);
				int reqLen = tlv_len-tlv_shortdata;
				byte[] req = new byte[reqLen];
				req = file_flow.parserFile(tlv_shortdata, tlv_len, data_len);
//				System.out.println("req=" + new String(req, "UTF-8"));
				break;
			case TlvId.HTTP_RSP_CONTENT:
				int rspLen = tlv_len-tlv_shortdata;
				byte[] rsp = new byte[rspLen];
				rsp = file_flow.parserFile(tlv_shortdata, tlv_len, data_len);
				break;
			case TlvId.FILE_CONTENT:
				xdrBuild.setFileType(2);
				int fileLen = tlv_len-tlv_shortdata;
				byte[] file = new byte[fileLen];
				file = file_flow.parserFile(tlv_shortdata, tlv_len, data_len);
				break;
	    	default:
	    		file_flow.skip(data_len);
	    		break;
		}
	}

	private void uncompressLz4(){
		decodeLen = 0;
		System.out.println("uncompressLz4 start");
		LZ4Factory factory = LZ4Factory.fastestInstance();
        LZ4SafeDecompressor decompressor = factory.safeDecompressor();
//        int blockIndependenceFlag = source[4]&0x020;
        int blockChecksumFlag = uncomData[4]&0x10;
//        int streamChecksumFlag = source[4]&0x4;
        int index = 7;
        int uncompLen = 0;


        while(true){
        	//最前面的七个字节表示lz4格式的头，一个包只有一个头，后四个字节表示长度
        	uncompLen = UnsafeUtils.readInt(uncomData, index);
        	index += 4;
        	int readLine = uncompLen & 0x7FFFFFFF;
        	if(readLine == 0){
        		break;
        	}
        	if(blockChecksumFlag == 1){
        		index += 4;
        	}

        	try {
                decodeLen += decompressor.decompress(uncomData, index, uncompLen, decodeData, decodeLen);
        	} catch (Exception e) {
        		System.out.println("decompressor.decompress called failed. ");
        		decodeLen = 0;
        		e.printStackTrace();
        		return;
        	}


        	index += uncompLen;
        }
        System.out.println("okkk");
//        writeFile(decodeData);
	}

	public void writeFile(byte[] buf){
        FileOutputStream out;
		try {
			out = new FileOutputStream(new File("/home/2.txt"));
			out.write(buf);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
