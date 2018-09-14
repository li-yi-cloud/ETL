package hbase;

import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;  
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xdrSchema.XdrSchema;

import org.apache.storm.hbase.common.ColumnList;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.storm.hbase.common.Utils.*;

public class XdrAvroDecode 
{
    //private Schema xdrChema;
    /* rowkey */
    private String rowKeyStr;
    private HbSchema xdrChema;
    private ColumnList xdrObj;
    private XdrSchema payload = null;
    private ColumnList blankCols;

    // family 1
    public int app = 0;
    private int ipVer = 0;
    private int spt = 0;
    private int dpt = 0;
    private long sip = 0L;
    private long dip = 0L;
    private String sipStr = "";
    private String dipStr = "";
    public long startTime = 0;
    // family 2
    public long fileType = 0L;

	private static final Logger logger = LoggerFactory.getLogger(XdrAvroDecode.class);

	public XdrAvroDecode() {
		xdrObj = new ColumnList();
		xdrChema = new HbSchema();
		blankCols = new ColumnList();
	}
	
	/* decode the Avro message which is formed in byte array */
	public void decodeXdrAvro (byte[] inPut) {
		DatumReader<XdrSchema> reader = new SpecificDatumReader<XdrSchema>(XdrSchema.class);
        Decoder decoder = DecoderFactory.get().binaryDecoder(inPut, null);  
        try {
        	payload = reader.read(null, decoder); 
        } catch (Exception e) {
        	e.printStackTrace();
        }
        //System.out.println("Avro Message received : " + payload); 
        logger.info("Avro Message received ... ");
	}
	
	/* fill avro xdr elements into hbSchema object */
	public ColumnList getAndFillXdrInfo () {
		/* begin to get and check every element */
		//xdrObj.city = payload.get("name").toString();
		CharSequence strTmp = null;
		xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.ssnStatusQuality),toBytes(payload.getSsnStatus()));

		Integer appid = payload.getAppId();
		if (appid < 100 || appid > 115) {
			logger.info("getAndFillXdrInfo : Avro App ID is illegal. " + appid);
			return blankCols;
		}

        xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.appQuality),toBytes(payload.getAppId()));
		app = appid;

		/* parse Tuple */
		xdrSchema.XdrTuple tuple = payload.getTuple();
		if (null == tuple) {
			logger.error("getAndFillXdrInfo : Avro XdrTuple is null.");
			return blankCols;
		}
        xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.ipVerQuality),toBytes(tuple.getIpVer()));
		ipVer = tuple.getIpVer();
        xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.dirQuality),toBytes(tuple.getDir()));
        xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.l4ProtoQuality),toBytes(tuple.getL4Proto()));

		if (tuple.getSrcPort() > 0xffff) {
			logger.warn("getAndFillXdrInfo : Avro src port is illegal: " + tuple.getSrcPort());
			return blankCols;
		}
        xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.sptQuality),toBytes(tuple.getSrcPort()));
		spt = tuple.getSrcPort();

		if (tuple.getDstPort() > 0xffff) {
			logger.warn("getAndFillXdrInfo : Avro dst port is illegal: " + tuple.getDstPort());
			return blankCols;
		}
        xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.dptQuality),toBytes(tuple.getDstPort()));
		dpt = tuple.getDstPort();
        xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.sipQuality),toBytes(tuple.getV4SrcIP()));
        sip = tuple.getV4SrcIP();
        xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.dipQuality),toBytes(tuple.getV4DstIP()));
        dip = tuple.getV4DstIP();
		
		strTmp = tuple.getStrSrcIP();
		if (null == strTmp) {
			logger.warn("getAndFillXdrInfo : Avro StrSrcIp is null.");
			return blankCols;
		}

		if (strTmp.length() > 16) {
			logger.warn("getAndFillXdrInfo : Avro StrSrcIp length > 16.");
			return blankCols;
		}
        xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.sipStrQuality),toBytes(tuple.getStrSrcIP().toString()));
		sipStr = tuple.getStrSrcIP().toString();

		strTmp = tuple.getStrDstIP();
		if (null == strTmp) {
			logger.warn("getAndFillXdrInfo : Avro StrDstIp is null.");
			return blankCols;
		}
		if (strTmp.length() > 16) {
			logger.warn("getAndFillXdrInfo : Avro StrDstIp length > 16.");
			return blankCols;
		}
        xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.dipStrQuality),toBytes(tuple.getStrDstIP().toString()));
		dipStr = tuple.getStrDstIP().toString();

		strTmp = tuple.getStrSrcCountry();
		if (null == strTmp) {
			logger.warn("getAndFillXdrInfo : Avro StrSrcCountry is null.");
			return blankCols;
		}
        xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.srcCountryQuality),toBytes(tuple.getStrSrcCountry().toString()));
		
		strTmp = tuple.getStrSrcProvince();
		if (null == strTmp) {
			logger.warn("getAndFillXdrInfo : Avro StrSrcProvince is null.");
			return blankCols;
		}
        xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.srcProvinceQuality),toBytes(tuple.getStrSrcProvince().toString()));
		
		strTmp = tuple.getStrDstCountry();
		if (null == strTmp) {
			logger.warn("getAndFillXdrInfo : Avro StrDstCountry is null.");
			return blankCols;
		}
        xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.dstCountryQuality),toBytes(tuple.getStrDstCountry().toString()));
	
		strTmp = tuple.getStrDstProvince();
		if (null == strTmp) {
			logger.warn("getAndFillXdrInfo : Avro StrDstProvince is null.");
			return blankCols;
		}
        xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.dstProvinceQuality),toBytes(tuple.getStrDstProvince().toString()));
		
		/* parse SsnInfo */
		xdrSchema.XdrSsnInfo ssninfo = payload.getSSnInfo();
		if (null != ssninfo) {
            xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.upFlowQuality),toBytes(ssninfo.getUpFlow()));
            xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.downFlowQuality),toBytes(ssninfo.getDownFlow()));
            xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.upPktsQuality),toBytes(ssninfo.getUpPackets()));
            xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.downPktsQuality),toBytes(ssninfo.getDownPackets()));
            xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.tcpDisorderUpQuality),toBytes(ssninfo.getUpTcpDisorder()));
            xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.tcpDisorderDownQuality),toBytes(ssninfo.getDownTcpDisorder()));
            xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.tcpRetransUpQuality),toBytes(ssninfo.getUpTcpRetransmit()));
            xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.tcpRetransDownQuality),toBytes(ssninfo.getDownTcpRetransmit()));
            xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.fragPktsUpQuality),toBytes(ssninfo.getUpFragPackets()));
            xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.fragPktsDownQuality),toBytes(ssninfo.getDownFragPackets()));
            xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.upDurationTimeQuality),toBytes(ssninfo.getUpDuraUpTime()));
            xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.downDurationTimeQuality),toBytes(ssninfo.getDownDuraUpTime()));
		} else {
			logger.warn("getAndFillXdrInfo : Avro XdrSsnInfo is null, skip it.");
			//return null;
		}
		
		/* parse Times */
		xdrSchema.XdrTimes xdrTimes = payload.getTimes();
		if (null == xdrTimes) {
			logger.warn("getAndFillXdrInfo : Avro Times is null.");
			return blankCols;
		}
        xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.ssnInitTimeQuality),toBytes(xdrTimes.getSsnInitTime()));
        xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.startTimeQuality),toBytes(xdrTimes.getStartTime()));
        startTime = xdrTimes.getStartTime();
        xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.endTimeQuality),toBytes(xdrTimes.getEndTime()));
		
		/* parse L7Type */
		xdrSchema.XdrL7Type l7info = payload.getL7Type();
		if (null != l7info) {
            xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.l7AppStatusQuality),toBytes(l7info.getAppStatus()));
            xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.l7ClassTypeQuality),toBytes(l7info.getClassID()));
            xdrObj.addColumn(toBytes(xdrChema.f1),toBytes(xdrChema.l7ProtocolQuality),toBytes(l7info.getProtocol()));
		} else {
			logger.warn("getAndFillXdrInfo : Avro XdrL7Type is null, skip it.");
			//return null;	
		}

		// xdr family 2
        xdrObj.addColumn(toBytes(xdrChema.f2),toBytes(xdrChema.fileTypeQuality),toBytes(payload.getFileType()));
		
		//try {
		//	xdrObj.printMethod(xdrObj);
		//} catch (Exception e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		
        return xdrObj;
	}

    public String getRowKeyStr() {
        if ( rowKeyStr != null) {
            return rowKeyStr;
        }

        String rowKeyType = getRowKeyType();
        if (null == rowKeyType) {
            logger.warn("getRowKeyType : get null ");
            return null;
        }

        String randomStr = getRowKeyRandom();
        Calendar timeCal = transTime2Calendar(startTime);
        String timeString = getRowKeyTimeStr(timeCal);
        if (null == timeString) {
            logger.warn("getRowKeyTimeStr : get null ");
            return null;
        }

        String srcIpStr;
        String dstipStr;
        if (0 == ipVer) {
            srcIpStr = getRowKeyIpv4Str(sip);
            dstipStr = getRowKeyIpv4Str(dip);
        } else {
            srcIpStr = getRowKeyIpv6Str();
            dstipStr = getRowKeyIpv6Str();
        }

        String srcPort = getRowKeyPort(spt);
        String dstPort = getRowKeyPort(dpt);
        String appStr = getRowKeyAppStr();

		/* compose rowkey string */
        rowKeyStr = rowKeyType + randomStr + timeString + srcIpStr + srcPort + dstipStr + dstPort + appStr;

        return rowKeyStr;
    }

    private Calendar transTime2Calendar(long timeStamp) {
        Date tmpDate = transForDate((int)(timeStamp/1000000));
        //Date secondDate = transForDate(ts2);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tmpDate);
        return calendar;
    }

    public Date transForDate(Integer ms){
        if(ms==null){
            ms=0;
        }
        long msl=(long)ms*1000;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date temp=null;
        if(ms!=null){
            try {
                String str=sdf.format(msl);
                temp=sdf.parse(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    public Object getColumeValue(String columFamily,String colum){
        Object value = null;

        return  value;
    }
    private String getRowKeyType() {
        switch ((int)fileType) {
            case 1:
                return "1";
            case 2:
                return "2";
            case 3:
                return "3";
            default:
                logger.error("getRowKeyType : fileType is illegal : " + fileType);
                return null;
        }
    }

    private String getRowKeyRandom() {
        Random randomTmp = new Random();
        byte[] rdm = new byte[1];
        rdm[0] = (byte)(randomTmp.nextInt(255));
        if (0 == rdm[0]) {
            rdm[0] = 1;
        }
        String rdmStr = new String(rdm);
        return rdmStr;
    }

    private String getRowKeyTimeStr(Calendar calIn) {
        String monStr = getRowKeyMonth(calIn);
        if (null == monStr) {
            return null;
        }
        String dayStr = getRowKeyDay(calIn);
        if (null == dayStr) {
            return null;
        }
        String hourStr = getRowKeyHour(calIn);
        if (null == hourStr) {
            return null;
        }
        String minStr = getRowKeyMinute(calIn);
        if (null == minStr) {
            return null;
        }
        String secStr = getRowKeySecond(calIn);
        if (null == secStr) {
            return null;
        }

        return monStr + dayStr + hourStr + minStr +secStr;
    }

    private String getRowKeyMonth(Calendar calIn) {
        int mon = calIn.get(Calendar.MONTH) + 1;
        return (transNum2Str(mon));
    }

    private String getRowKeyDay(Calendar calIn) {
        int dby = calIn.get(Calendar.DAY_OF_MONTH);
        return (transNum2Str(dby));
    }

    private String getRowKeyHour(Calendar calIn) {
        int hour = calIn.get(Calendar.HOUR_OF_DAY);
        return (transNum2Str(hour));
    }

    private String getRowKeyMinute(Calendar calIn) {
        int minute = calIn.get(Calendar.MINUTE);
        return (transNum2Str(minute));
    }

    private String getRowKeySecond(Calendar calIn) {
        int second = calIn.get(Calendar.SECOND);
        return (transNum2Str(second));
    }


    private String getRowKeyIpStr() {
        switch (ipVer) {
            case 0:  /* ipv4 */
                return getRowKeyIpv4Str(sip);
            default:
                return getRowKeyIpv6Str();
        }
    }

    private String getRowKeyIpv4Str(long sipIn) {
        Long longTmp = sipIn & 0x00000000FFFFFFFFL;
        String longStr = Long.toHexString(longTmp);
        longStr = prefixPadToFixedLen(longStr, 16);
        return longStr;
    }

    private String getRowKeyIpv6Str() {
        return "1234567890ABCDEF";
    }

    private String getRowKeyPort(int spt) {
        String longStr = Integer.toHexString(spt);
        longStr = prefixPadToFixedLen(longStr, 4);
        return longStr;
    }

    private static String prefixPadToFixedLen(String strIn, int totalLen) {
        String prefix = "";
        int padNum = totalLen - strIn.length();
        for(int i = 0; i<padNum; i++){
            prefix = prefix + "0";
        }
        prefix = prefix + strIn;
        //System.out.println("prefixPadTo16 result is :" + prefix );
        return prefix;
    }

    private String getRowKeyAppStr() {
//		String tmp = transNum2Str(app - 100);
//		if (null == tmp) {
//			return "0";
//		}
//		return tmp;
//		if (app < 100 || app > 115) {
//			return "0";
//		}
        String Str = Integer.toHexString(app-100);
        return Str;
    }

    private String transNum2Str(int numIn) {
        switch (numIn) {
            case 0:
                return "0";
            case 1:
                return "1";
            case 2:
                return "2";
            case 3:
                return "3";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return "6";
            case 7:
                return "7";
            case 8:
                return "8";
            case 9:
                return "9";
            case 10:
                return "A";
            case 11:
                return "B";
            case 12:
                return "C";
            case 13:
                return "D";
            case 14:
                return "E";
            case 15:
                return "F";
            case 16:
                return "G";
            case 17:
                return "H";
            case 18:
                return "I";
            case 19:
                return "J";
            case 20:
                return "K";
            case 21:
                return "L";
            case 22:
                return "M";
            case 23:
                return "N";
            case 24:
                return "O";
            case 25:
                return "P";
            case 26:
                return "Q";
            case 27:
                return "R";
            case 28:
                return "S";
            case 29:
                return "T";
            case 30:
                return "U";
            case 31:
                return "V";
            case 32:
                return "W";
            case 33:
                return "X";
            case 34:
                return "Y";
            case 35:
                return "Z";
            case 36:
                return "a";
            case 37:
                return "b";
            case 38:
                return "c";
            case 39:
                return "d";
            case 40:
                return "e";
            case 41:
                return "f";
            case 42:
                return "g";
            case 43:
                return "h";
            case 44:
                return "i";
            case 45:
                return "j";
            case 46:
                return "k";
            case 47:
                return "l";
            case 48:
                return "m";
            case 49:
                return "n";
            case 50:
                return "o";
            case 51:
                return "p";
            case 52:
                return "q";
            case 53:
                return "r";
            case 54:
                return "s";
            case 55:
                return "t";
            case 56:
                return "u";
            case 57:
                return "v";
            case 58:
                return "w";
            case 59:
                return "x";
            case 60:
                return "y";
            case 61:
                return "z";
            default:
                logger.warn("transNum2Str input num beyond limit : " + numIn);
                return null;
        }
    }

    private boolean checkRowKey() {
		/* check rowkey refered elements if needed  */
        return true;
    }


}


