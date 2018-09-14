package tlv;

public class TlvId {
	public static final byte XDR_DATA_LENGTH = 0;
	
	public static final byte XDR_SESSION_STATUS= 1;
	public static final byte XDR_APP_ID = 2;
	public static final byte XDR_TUPLE = 3;
	public static final byte XDR_SSN_INFO = 4;
	public static final byte XDR_TIME = 5;
    public static final byte XDR_BUSI_INFO = 6;
    public static final byte XDR_TCP_INFO = 7;
    public static final byte XDR_RESPONSE_DELAY = 8;
    public static final byte XDR_L7_TYPE = 9; 
    
    public static final byte XDR_HTTP_INFO = 10;
    public static final byte XDR_HTTP_HOST = 11;
    public static final byte XDR_HTTP_URL = 12; 
    public static final byte XDR_HTTP_XONLINEHOST = 13;
    public static final byte XDR_HTTP_USERAGENT = 14;
    public static final byte XDR_HTTP_CONTENT = 15;
    public static final byte XDR_HTTP_REFER = 16;
    public static final byte XDR_HTTP_COOKIE = 17;
    public static final byte XDR_HTTP_LOCATION = 18;
    
    public static final byte XDR_SIP_INFO = 19;
    public static final byte XDR_SIP_CALLER_NUM = 20;
	public static final byte XDR_SIP_CALLED_NUM = 21;
	public static final byte XDR_SIP_CALLID_NUM = 22;
	
	public static final byte XDR_RTSP_INFO = 23;
	public static final byte XDR_RTSP_URL = 24;
	public static final byte XDR_RTSP_USERAGENT = 25;
	public static final byte XDR_RTSP_SERVERIP = 26;
	
	public static final byte XDR_FTP_STATUS = 27;
	public static final byte XDR_FTP_USR_NM = 28;
	public static final byte XDR_FTP_CUR_DIR = 29;
	public static final byte XDR_FTP_TRANS_MODE = 30;
	public static final byte XDR_FTP_TRANS_TYPE = 31;
	public static final byte XDR_FTP_FILE_NM = 32;
	public static final byte XDR_FTP_FILE_SIZ = 33;
	public static final byte XDR_FTP_RSP_TM = 34;
	public static final byte XDR_FTP_TRANS_TM = 35;
	
	public static final byte XDR_MAIL_MSG_TYPE = 36;
	public static final byte XDR_MAIL_RSP_STATUS = 37;
	public static final byte XDR_MAIL_USR_NM = 38;
	public static final byte XDR_MAIL_SND_INFO = 39;
	public static final byte XDR_MAIL_LEN = 40;
	public static final byte XDR_MAIL_DOMAIN = 41;
	public static final byte XDR_MAIL_RCV_ACCOUNT = 42;
	public static final byte XDR_MAIL_HDR = 43;
	public static final byte XDR_MAIL_ACS_TYPE = 44;
    
	public static final byte XDR_DNS_DOMAIN = 45;
	public static final byte XDR_DNS_IP_NUM = 46;
	public static final byte XDR_DNS_IPV4 = 47;
	public static final byte XDR_DNS_IPV6 = 48;
	public static final byte XDR_DNS_RSP_CODE = 49;
	public static final byte XDR_DNS_REQ_CNT = 50;
	public static final byte XDR_DNS_RSP_RECORD_CNT = 51;
	public static final byte XDR_DNS_AUTH_CNTT_CNT = 52;
	public static final byte XDR_DNS_EXTRA_RECORD_CNT = 53;
	public static final byte XDR_DNS_RSP_DELAY = 54;
    
	public static final byte XDR_VPN_TYPE = 55;
	public static final byte XDR_PROXY_TYPE = 56;
    
	//未组装///////////////////////////////////////////
	public static final byte XDR_PROTOCOL_TYPE = 57;
    public static final byte XDR_VPN_GRE_IP = 58;
    public static final byte XDR_DNS_VALID = 59;
    public static final byte XDR_IM_QQ_NUMBER = 60;
    public static final byte XDR_PKT_DIR_FLG = 61;
    public static final byte XDR_HTTP_VALID = 62;
    public static final byte ACCOUNT_XDR_ID = 63;
    public static final byte XDR_VLAN_ID = 64;
    ////////////////////////////////////////////////
    
    public static final short HTTP_REQ_CONTENT = 201;
    public static final short HTTP_RSP_CONTENT = 202;
    public static final short FILE_CONTENT = 203;
    public static final short SSL_SERVER_CERT = 204;
    public static final short SSL_CLIENT_CERT = 205;
    public static final short SSL_CONNECT_FAIL_REASON = 206;
}
