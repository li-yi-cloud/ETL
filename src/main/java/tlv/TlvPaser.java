package tlv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TlvPaser {
	public int offSet;
	public int OutOffSet;
	public int curTlvLength;
	public String filename;
	
	public static final Logger LOG = LoggerFactory.getLogger(TlvPaser.class);
//C中的对应关系如下：
//	
//	uint8_t  id;
//  uint8_t  short_data;
//  uint16_t type:4;
//  uint16_t length:12;
//  uint32_t data[0];

	
	public short getTlvId(byte[] bufData){
		//一个字节
		int arrayId = offSet;
		return (short) (bufData[arrayId] & 0xFF);
	}
	
	//一个字节的高四bit
	public byte getTlvType(byte[] bufData){
		//偏移ID一个字节		
		byte TlvType = bufData[offSet];
		
		//取这个字节的低四位
		byte highBit =  (byte) ((TlvType & 0xFF )& 0x0f);
		//取这个字节的高四位作为 type
		//byte highBit =  (byte) (((TlvType & 0xFF )& 0xf0) >> 4);
		return highBit;
	}
	
	//一个字节的高四bit
	public int getTlvLength(byte[] bufData){
		
		//偏移ID一个字节
//		short id = getTlvId(bufData);
		offSet += 1;
		//偏移shortData一个字节
		offSet += 1;
		
		//type和length公用两个字节，type站高四bit,剩下12bit是length
//		int type =  getTlvType(bufData);
		
		//00 0E 应该是0E 00
		//40 00 应该是00 04
		int lowBit =  ((bufData[offSet] & 0xFF) >> 4);		
		int length = ((bufData[offSet + 1] & 0xFF) << 4) | lowBit;
		return length;
	}
}
