package hbase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

public class HbSchema{
    // table Fields and family
    HashMap <String,HashMap> tableInfo = new <String,HashMap> HashMap();
    HashMap <String,Object> familyInfo = new <String,Object> HashMap();

	/* table column family info */
	String f1 = "i";
	String f2 = "f";
	String f3 = "a";
	
	/* rowkey */
	public String rowKeyStr;
	
	/* column name of col family 1 */
	public final String ssnStatusQuality = "STATUS";	
	public final String appQuality = "APP";

	public final String classTypeQuality = "CLS";
	public final String regionTypeQuality = "RGN";
	public final String countryTypeQuality = "CY";
	public final String provinceTypeQuality = "PROV";
	public final String cityQuality = "CITY";
	
	public final String ipVerQuality = "IPVR";
	public final String dirQuality = "DIR";
	public final String l4ProtoQuality = "PROT";
	public final String sptQuality = "SPT";
	public final String dptQuality = "DPT";
	public final String sipQuality = "SIP";
	public final String dipQuality = "DIP";
	public final String sipStrQuality = "SIPS";
	public final String dipStrQuality = "DIPS";
	public final String srcCountryQuality = "CY_S";
	public final String srcProvinceQuality = "PROV_S";
	public final String dstCountryQuality = "CY_D";
	public final String dstProvinceQuality = "PROV_D";
	
	public final String upFlowQuality = "U_F";
	public final String downFlowQuality = "D_F";
	public final String upPktsQuality = "U_P";
	public final String downPktsQuality = "D_P";
	public final String tcpDisorderUpQuality = "TCP_DU";
	public final String tcpDisorderDownQuality = "TCP_DD";
	public final String tcpRetransUpQuality = "TCP_RU";
	public final String tcpRetransDownQuality = "TCP_RD";
	public final String fragPktsUpQuality = "FRAG_U";
	public final String fragPktsDownQuality = "FRAG_D";
	public final String upDurationTimeQuality = "DURA_U";
	public final String downDurationTimeQuality = "DURA_D";
	
	public final String ssnInitTimeQuality = "TIME_I";
	public final String startTimeQuality = "TIME_S";
	public final String endTimeQuality = "TIME_E";
	
	public final String l7AppStatusQuality = "L7_STS";
	public final String l7ClassTypeQuality = "L7_CLS";
	public final String l7ProtocolQuality = "L7_PROT";
	
	/* column value of col family 1  Begin *************/
	public int ssnStatus = 0;	
	public int app = 0;

	public int classType = 0;
	public String regionType = "RGN";
	public String city = "CITY";
	
	public int ipVer = 0;
	public int dir = 0;
	public int l4Proto = 0;
	public int spt = 0;
	public int dpt = 0;
	public long sip = 0L;
	public long dip = 0L;
	public String sipStr = "";
	public String dipStr = "";
	public String srcCountry = "CY_S";
	public String srcProvince = "PROV_S";
	public String dstCountry = "CY_D";
	public String dstProvince = "PROV_D";
	
	public long upFlow = 0L;
	public long downFlow = 0L;
	public long upPkts = 0L;
	public long downPkts = 0L;
	public long tcpDisorderUp = 0L;
	public long tcpDisorderDown = 0L;
	public long tcpRetransUp= 0L;
	public long tcpRetransDown = 0L;
	public long fragPktsUp= 0L;
	public long fragPktsDown = 0L;
	public int upDurationTime = 0;
	public int downDurationTime= 0;
	
	public long ssnInitTime = 0;
	public long startTime = 0;
	public long endTime = 0;
	
	public int l7AppStatus = 0;
	public int l7ClassType = 0;
	public int l7Protocol = 0;
	/* column value of col family 1  End *************/
	
	/* column name of col family 2 */
	public final String md5SumQuality = "MD5";
	public final String reqFileLenQuality = "REQ_F_LEN";
	public final String rspFileLenQuality = "RSP_F_LEN";
	public final String fileLenQuality = "F_LEN";
	public final String fileTypeQuality = "REQ_F_T";
	/* column value of col family 2 Begin *************/
	public byte[] md5Sum = new byte[0];
	public long reqFileLen = 0L;
	public long rspFileLen = 0L;
	public long fileLen = 0L;
	public long fileType = 0L;
	/* column value of col family 2 End *************/
	
	/* column name of col family 3 */
	
	/* column value of col family 2 Begin *************/
	
	/* column value of col family 2 End *************/
	private static final Logger logger = LoggerFactory.getLogger(HbSchema.class);

    public HbSchema HbSchema(){

        return this;
    }

	public boolean checkClassType () {
		logger.info("checkClassType class is : " + classType);
        return true;
	}
	
	 public void printMethod(Object obj) throws Exception{
		 //获取这个对象的定义类
		 Class cz = obj.getClass();
		 //获取类的变量成员列表，注意，这个地方还有一个getDeclaredField方法，具体区别参见javadoc
		 for(java.lang.reflect.Field f : cz.getFields()){
			 //获取变量的值，当然你也可以获取变量的名字
			 Object value = f.get(obj);
			 System.out.println(f.getName() + " = " + value);
		 }
	 }
	 
}

