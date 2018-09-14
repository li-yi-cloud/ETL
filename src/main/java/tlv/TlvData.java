package tlv;

public class TlvData {
	public class SsnStatus{
		public int ssnStatus = 0;
	}
	public class AppId{
		public int appId = 0;
	}
	public class Tuple{
		//1B 1B 1B 1B 2B 2B 16B 16B
		public int ipVer = 0;
		public int dir = 0;
		public int l4Proto = 0;
		public int resv = 0;
		public int srcPort = 0;
		public int dstPort = 0;
		public long v4SrcIP = 0;
		public long v4DstIP = 0;
		public String strSrcIP ;
		public String strDstIP ;
//		public long long v6SrcIP = 0L;
//		public long long v6DstIP = 0L;
	}
	public class SsnInfo{
		//4B 4B 4B 4B 4B 4B 4B 4B 4B 4B 2B 2B
		public long upFlow = 0L;
		public long downFlow = 0L;
		public long upPackets = 0L;
		public long downPackets = 0L;
		public long upTcpDisorder = 0L;
		public long downTcpDisorder = 0L;
		public long upTcpRetransmit = 0L;
		public long downTcpRetransmit = 0L;
		public long upFragPackets = 0L;
		public long downFragPackets = 0L;
		public int upDuraUpTime = 0;
		public int downDuraUpTime = 0;
	}
	public class Time{
		//8B 8B 8B
		public long ssnInitTime = 0L;
		public long startTime = 0L;
		public long endTime = 0L;
	}
	public class BusiInfo{
		//4B 4B 4B 4B 4B 4B 4B 4B 4B 4B 2B 2B
		public long upFlow = 0L;
		public long downFlow = 0L;
		public long upPackets = 0L;
		public long downPackets = 0L;
		public long upTcpDisorder = 0L;
		public long downTcpDisorder = 0L;
		public long upTcpRetransmit = 0L;
		public long downTcpRetransmit = 0L;
		public long upFragPackets = 0L;
		public long downFragPackets = 0L;
		public int upDuraUpTime = 0;
		public int downDuraUpTime = 0;
	}
	public class TcpInfo{
		//2B 2B 1B 1B 2B 4B 4B 4B 2B 1B 1B 1B 1B 1B 1B
		public int synackToSynTime = 0;
		public int ackToSynTime = 0;
		public int fullReportFlag = 0;
		public int ssnClosReason = 0;
		public int resv = 0;
		public long firstRequestDelay = 0L;
		public long firstResponseDelay = 0L;
		public long windowSize = 0L;
		public int mssSize = 0;
		public int tcpRetryCount = 0;
		public int tcpRetryAckCount = 0;
		public int tcpAckCount = 0;
		public int tcpConnectStatusNo = 0;
		public int tcpStatusFirst = 0;
		public int tcpStatusSecond = 0;
	}
	public class ResponseDelay{
		//4B 
		public long responseDelay = 0L;
	}
	public class L7Type{
		//1B 1B 2B
		public int appStatus = 0;
		public int classID = 0;
		public int protocol = 0;
	}
	public class HttpInfo{
		//8B 8B 8B 8B 4B 2B 1B 1B 2bit 3bit 1bit 2bit 1B 1B 1B
		public long ctionTime = 0L;
		public long firstPacketTime = 0L;
		public long lastPacketTime = 0L;
		public long serviceTime = 0L;
		public long contentLenth = 0L;
		public int httpStatus = 0;
		public int httpMethod = 0;
		public int httpVersion = 0;
		public int firstRequestFlag = 0;
		public int serFlag = 0;
		public int headFlag = 0;
		public int res = 0;
		public int ie = 0;
		public int portal = 0;
		public int resv = 0;
	}
	public String httpHost ;
	public String httpUrl ;
	public String httpXonlineHost ;
	public String httpUserAgent ;
	public String httpContent ;
	public String httpRefer ;
	public String httpCookie ;
	public String httpLocation ;
	public class SipInfo{
		//1B 1B 1B 1B 2B 1bit 1bit 1bit 13bit
		public int callDirection = 0;
		public int callType = 0;
		public int hookReason = 0;
		public int signalType = 0;
		public int dataflowNum = 0;
		public int sipInvite = 0;
		public int sipBye = 0;
		public int malloc = 0;
		public int resv = 0;
	}
	public String sipCallerNum ;
	public String sipCalledNum ;
	public String sipCallidNum ;
	public class RtspInfo{
		//2B 2B 2B 2B 2B 2B 4B
		public int cStartPort = 0;
		public int cEndPort = 0;
		public int sStartPort = 0;
		public int sEndPort = 0;
		public int ssnVideoCount = 0;
		public int ssnAudioCount = 0;
		public long resDelay = 0L;
	}
	public String rtspUrl ;
	public String rtspUserAgent ;
	public String rtspServerIP ;
	
	public int ftpStatus = 0 ;
	public String ftpUsrName ;
	public String ftpCurDir ;
	public int ftpTransMode = 0 ;
	public int ftpTransType = 0 ;
	public String ftpFileName ;
	public long ftpFileSize = 0L ;
	public long ftpResDelay = 0L ;
	public long ftpTransTime =0L ;
	
	public int mailMessType =0 ;
	public int mailStatusType = 0;
	public String mailUsName ;
	public String mailSendInfo ;
	public long mailLength =0L ;
	public String mailDomain ;
	public String mailRcvAccount ;
	public String mailHdr ;
	public int mailAcsType = 0 ;
	
	public String dnsDomain ;
	public int dnsIpNum = 0 ;
	public long dnsIpV4 =0L ;
//	public String dnsIpv6 ;
	public int dnsRspCode = 0 ;
	public int dnsRequsNum = 0 ;
	public int dnsAncount = 0 ;
	public int dnsNscount = 0 ;
	public int dnsArcount = 0 ;
	public long dnsRspDelay = 0L ;
	
	public int vpnType = 0 ;
	public int proxyType = 0 ;
}
