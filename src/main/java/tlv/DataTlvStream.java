
package tlv;

import java.io.DataInput;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.io.PushbackInputStream;
import java.io.UTFDataFormatException;

import com.google.common.primitives.UnsignedBytes;

import tlv.TlvData.SsnStatus;

/**
 * A data input stream lets an application read primitive Java data
 * types from an underlying input stream in a machine-independent
 * way. An application uses a data output stream to write data that
 * can later be read by a data input stream.
 * <p>
 * DataInputStream is not necessarily safe for multithreaded access.
 * Thread safety is optional and is the responsibility of users of
 * methods in this class.
 *
 * @author  Arthur van Hoff
 * @see     java.io.DataOutputStream
 * @since   JDK1.0
 */
public
class DataTlvStream extends FilterInputStream implements DataInput {

    /**
     * Creates a DataInputStream that uses the specified
     * underlying InputStream.
     *
     * @param  in   the specified input stream
     */
    public DataTlvStream(InputStream in) {
        super(in);
    }

    /**
     * working arrays initialized on demand by readUTF
     */
    private byte bytearr[] = new byte[80];
    private char chararr[] = new char[80];

    /**
     * Reads some number of bytes from the contained input stream and
     * stores them into the buffer array <code>b</code>. The number of
     * bytes actually read is returned as an integer. This method blocks
     * until input data is available, end of file is detected, or an
     * exception is thrown.
     *
     * <p>If <code>b</code> is null, a <code>NullPointerException</code> is
     * thrown. If the length of <code>b</code> is zero, then no bytes are
     * read and <code>0</code> is returned; otherwise, there is an attempt
     * to read at least one byte. If no byte is available because the
     * stream is at end of file, the value <code>-1</code> is returned;
     * otherwise, at least one byte is read and stored into <code>b</code>.
     *
     * <p>The first byte read is stored into element <code>b[0]</code>, the
     * next one into <code>b[1]</code>, and so on. The number of bytes read
     * is, at most, equal to the length of <code>b</code>. Let <code>k</code>
     * be the number of bytes actually read; these bytes will be stored in
     * elements <code>b[0]</code> through <code>b[k-1]</code>, leaving
     * elements <code>b[k]</code> through <code>b[b.length-1]</code>
     * unaffected.
     *
     * <p>The <code>read(b)</code> method has the same effect as:
     * <blockquote><pre>
     * read(b, 0, b.length)
     * </pre></blockquote>
     *
     * @param      b   the buffer into which the data is read.
     * @return     the total number of bytes read into the buffer, or
     *             <code>-1</code> if there is no more data because the end
     *             of the stream has been reached.
     * @exception  IOException if the first byte cannot be read for any reason
     * other than end of file, the stream has been closed and the underlying
     * input stream does not support reading after close, or another I/O
     * error occurs.
     * @see        java.io.FilterInputStream#in
     * @see        java.io.InputStream#read(byte[], int, int)
     */
    public final int read(byte b[]) throws IOException {
        return in.read(b, 0, b.length);
    }

    /**
     * Reads up to <code>len</code> bytes of data from the contained
     * input stream into an array of bytes.  An attempt is made to read
     * as many as <code>len</code> bytes, but a smaller number may be read,
     * possibly zero. The number of bytes actually read is returned as an
     * integer.
     *
     * <p> This method blocks until input data is available, end of file is
     * detected, or an exception is thrown.
     *
     * <p> If <code>len</code> is zero, then no bytes are read and
     * <code>0</code> is returned; otherwise, there is an attempt to read at
     * least one byte. If no byte is available because the stream is at end of
     * file, the value <code>-1</code> is returned; otherwise, at least one
     * byte is read and stored into <code>b</code>.
     *
     * <p> The first byte read is stored into element <code>b[off]</code>, the
     * next one into <code>b[off+1]</code>, and so on. The number of bytes read
     * is, at most, equal to <code>len</code>. Let <i>k</i> be the number of
     * bytes actually read; these bytes will be stored in elements
     * <code>b[off]</code> through <code>b[off+</code><i>k</i><code>-1]</code>,
     * leaving elements <code>b[off+</code><i>k</i><code>]</code> through
     * <code>b[off+len-1]</code> unaffected.
     *
     * <p> In every case, elements <code>b[0]</code> through
     * <code>b[off]</code> and elements <code>b[off+len]</code> through
     * <code>b[b.length-1]</code> are unaffected.
     *
     * @param      b     the buffer into which the data is read.
     * @param off the start offset in the destination array <code>b</code>
     * @param      len   the maximum number of bytes read.
     * @return     the total number of bytes read into the buffer, or
     *             <code>-1</code> if there is no more data because the end
     *             of the stream has been reached.
     * @exception  NullPointerException If <code>b</code> is <code>null</code>.
     * @exception  IndexOutOfBoundsException If <code>off</code> is negative,
     * <code>len</code> is negative, or <code>len</code> is greater than
     * <code>b.length - off</code>
     * @exception  IOException if the first byte cannot be read for any reason
     * other than end of file, the stream has been closed and the underlying
     * input stream does not support reading after close, or another I/O
     * error occurs.
     * @see        java.io.FilterInputStream#in
     * @see        java.io.InputStream#read(byte[], int, int)
     */
    public final int read(byte b[], int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    /**
     * See the general contract of the <code>readFully</code>
     * method of <code>DataInput</code>.
     * <p>
     * Bytes
     * for this operation are read from the contained
     * input stream.
     *
     * @param      b   the buffer into which the data is read.
     * @exception  EOFException  if this input stream reaches the end before
     *             reading all the bytes.
     * @exception  IOException   the stream has been closed and the contained
     *             input stream does not support reading after close, or
     *             another I/O error occurs.
     * @see        java.io.FilterInputStream#in
     */
    public final void readFully(byte b[]) throws IOException {
        readFully(b, 0, b.length);
    }

    /**
     * See the general contract of the <code>readFully</code>
     * method of <code>DataInput</code>.
     * <p>
     * Bytes
     * for this operation are read from the contained
     * input stream.
     *
     * @param      b     the buffer into which the data is read.
     * @param      off   the start offset of the data.
     * @param      len   the number of bytes to read.
     * @exception  EOFException  if this input stream reaches the end before
     *               reading all the bytes.
     * @exception  IOException   the stream has been closed and the contained
     *             input stream does not support reading after close, or
     *             another I/O error occurs.
     * @see        java.io.FilterInputStream#in
     */
    public final void readFully(byte b[], int off, int len) throws IOException {
        if (len < 0)
            throw new IndexOutOfBoundsException();
        int n = 0;
        while (n < len) {
            int count = in.read(b, off + n, len - n);
            if (count < 0)
                throw new EOFException();
            n += count;
        }
    }

    /**
     * See the general contract of the <code>skipBytes</code>
     * method of <code>DataInput</code>.
     * <p>
     * Bytes for this operation are read from the contained
     * input stream.
     *
     * @param      n   the number of bytes to be skipped.
     * @return     the actual number of bytes skipped.
     * @exception  IOException  if the contained input stream does not support
     *             seek, or the stream has been closed and
     *             the contained input stream does not support
     *             reading after close, or another I/O error occurs.
     */
    public final int skipBytes(int n) throws IOException {
        int total = 0;
        int cur = 0;

        while ((total<n) && ((cur = (int) in.skip(n-total)) > 0)) {
            total += cur;
        }

        return total;
    }

    /**
     * See the general contract of the <code>readBoolean</code>
     * method of <code>DataInput</code>.
     * <p>
     * Bytes for this operation are read from the contained
     * input stream.
     *
     * @return     the <code>boolean</code> value read.
     * @exception  EOFException  if this input stream has reached the end.
     * @exception  IOException   the stream has been closed and the contained
     *             input stream does not support reading after close, or
     *             another I/O error occurs.
     * @see        java.io.FilterInputStream#in
     */
    public final boolean readBoolean() throws IOException {
        int ch = in.read();
        if (ch < 0)
            throw new EOFException();
        return (ch != 0);
    }

    /**
     * See the general contract of the <code>readByte</code>
     * method of <code>DataInput</code>.
     * <p>
     * Bytes
     * for this operation are read from the contained
     * input stream.
     *
     * @return     the next byte of this input stream as a signed 8-bit
     *             <code>byte</code>.
     * @exception  EOFException  if this input stream has reached the end.
     * @exception  IOException   the stream has been closed and the contained
     *             input stream does not support reading after close, or
     *             another I/O error occurs.
     * @see        java.io.FilterInputStream#in
     */
    public final byte readByte() throws IOException {
        int ch = in.read();
        if (ch < 0)
            throw new EOFException();
        return (byte)(ch);
    }

    /**
     * See the general contract of the <code>readUnsignedByte</code>
     * method of <code>DataInput</code>.
     * <p>
     * Bytes
     * for this operation are read from the contained
     * input stream.
     *
     * @return     the next byte of this input stream, interpreted as an
     *             unsigned 8-bit number.
     * @exception  EOFException  if this input stream has reached the end.
     * @exception  IOException   the stream has been closed and the contained
     *             input stream does not support reading after close, or
     *             another I/O error occurs.
     * @see         java.io.FilterInputStream#in
     */
    public final int readUnsignedByte() throws IOException {
        int ch = in.read();
        if (ch < 0)
            throw new EOFException();
        return ch;
    }

    /**
     * See the general contract of the <code>readShort</code>
     * method of <code>DataInput</code>.
     * <p>
     * Bytes
     * for this operation are read from the contained
     * input stream.
     *
     * @return     the next two bytes of this input stream, interpreted as a
     *             signed 16-bit number.
     * @exception  EOFException  if this input stream reaches the end before
     *               reading two bytes.
     * @exception  IOException   the stream has been closed and the contained
     *             input stream does not support reading after close, or
     *             another I/O error occurs.
     * @see        java.io.FilterInputStream#in
     */
    public final short readShort() throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (short)((ch2 << 8) + (ch1 << 0));
    }

    /**
     * See the general contract of the <code>readUnsignedShort</code>
     * method of <code>DataInput</code>.
     * <p>
     * Bytes
     * for this operation are read from the contained
     * input stream.
     *
     * @return     the next two bytes of this input stream, interpreted as an
     *             unsigned 16-bit integer.
     * @exception  EOFException  if this input stream reaches the end before
     *             reading two bytes.
     * @exception  IOException   the stream has been closed and the contained
     *             input stream does not support reading after close, or
     *             another I/O error occurs.
     * @see        java.io.FilterInputStream#in
     */
    public final int readUnsignedShort() throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (ch2 << 8) + (ch1 << 0);
    }

    /**
     * See the general contract of the <code>readChar</code>
     * method of <code>DataInput</code>.
     * <p>
     * Bytes
     * for this operation are read from the contained
     * input stream.
     *
     * @return     the next two bytes of this input stream, interpreted as a
     *             <code>char</code>.
     * @exception  EOFException  if this input stream reaches the end before
     *               reading two bytes.
     * @exception  IOException   the stream has been closed and the contained
     *             input stream does not support reading after close, or
     *             another I/O error occurs.
     * @see        java.io.FilterInputStream#in
     */
    public final char readChar() throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (char)((ch2 << 8) + (ch1 << 0));
    }

    /**
     * See the general contract of the <code>readInt</code>
     * method of <code>DataInput</code>.
     * <p>
     * Bytes
     * for this operation are read from the contained
     * input stream.
     *
     * @return     the next four bytes of this input stream, interpreted as an
     *             <code>int</code>.
     * @exception  EOFException  if this input stream reaches the end before
     *               reading four bytes.
     * @exception  IOException   the stream has been closed and the contained
     *             input stream does not support reading after close, or
     *             another I/O error occurs.
     * @see        java.io.FilterInputStream#in
     */
    public final int readInt() throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
    }

    private byte readBuffer[] = new byte[8];

    /**
     * See the general contract of the <code>readLong</code>
     * method of <code>DataInput</code>.
     * <p>
     * Bytes
     * for this operation are read from the contained
     * input stream.
     *
     * @return     the next eight bytes of this input stream, interpreted as a
     *             <code>long</code>.
     * @exception  EOFException  if this input stream reaches the end before
     *               reading eight bytes.
     * @exception  IOException   the stream has been closed and the contained
     *             input stream does not support reading after close, or
     *             another I/O error occurs.
     * @see        java.io.FilterInputStream#in
     */
    public final long readLong() throws IOException {
        readFully(readBuffer, 0, 8);
        return (((long)readBuffer[7] << 56) +
                ((long)(readBuffer[6] & 255) << 48) +
                ((long)(readBuffer[5] & 255) << 40) +
                ((long)(readBuffer[4] & 255) << 32) +
                ((long)(readBuffer[3] & 255) << 24) +
                ((readBuffer[2] & 255) << 16) +
                ((readBuffer[1] & 255) <<  8) +
                ((readBuffer[0] & 255) <<  0));
    }

    /**
     * See the general contract of the <code>readFloat</code>
     * method of <code>DataInput</code>.
     * <p>
     * Bytes
     * for this operation are read from the contained
     * input stream.
     *
     * @return     the next four bytes of this input stream, interpreted as a
     *             <code>float</code>.
     * @exception  EOFException  if this input stream reaches the end before
     *               reading four bytes.
     * @exception  IOException   the stream has been closed and the contained
     *             input stream does not support reading after close, or
     *             another I/O error occurs.
     * @see        java.io.DataInputStream#readInt()
     * @see        java.lang.Float#intBitsToFloat(int)
     */
    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    /**
     * See the general contract of the <code>readDouble</code>
     * method of <code>DataInput</code>.
     * <p>
     * Bytes
     * for this operation are read from the contained
     * input stream.
     *
     * @return     the next eight bytes of this input stream, interpreted as a
     *             <code>double</code>.
     * @exception  EOFException  if this input stream reaches the end before
     *               reading eight bytes.
     * @exception  IOException   the stream has been closed and the contained
     *             input stream does not support reading after close, or
     *             another I/O error occurs.
     * @see        java.io.DataInputStream#readLong()
     * @see        java.lang.Double#longBitsToDouble(long)
     */
    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    /**
     * See the general contract of the <code>readUTF</code>
     * method of <code>DataInput</code>.
     * <p>
     * Bytes
     * for this operation are read from the contained
     * input stream.
     *
     * @return     a Unicode string.
     * @exception  EOFException  if this input stream reaches the end before
     *               reading all the bytes.
     * @exception  IOException   the stream has been closed and the contained
     *             input stream does not support reading after close, or
     *             another I/O error occurs.
     * @exception  UTFDataFormatException if the bytes do not represent a valid
     *             modified UTF-8 encoding of a string.
     * @see        java.io.DataInputStream#readUTF(java.io.DataInput)
     */
    public final String readUTF() throws IOException {
        return readUTF(this);
    }

    /**
     * Reads from the
     * stream <code>in</code> a representation
     * of a Unicode  character string encoded in
     * <a href="DataInput.html#modified-utf-8">modified UTF-8</a> format;
     * this string of characters is then returned as a <code>String</code>.
     * The details of the modified UTF-8 representation
     * are  exactly the same as for the <code>readUTF</code>
     * method of <code>DataInput</code>.
     *
     * @param      in   a data input stream.
     * @return     a Unicode string.
     * @exception  EOFException            if the input stream reaches the end
     *               before all the bytes.
     * @exception  IOException   the stream has been closed and the contained
     *             input stream does not support reading after close, or
     *             another I/O error occurs.
     * @exception  UTFDataFormatException  if the bytes do not represent a
     *               valid modified UTF-8 encoding of a Unicode string.
     * @see        java.io.DataInputStream#readUnsignedShort()
     */
    public final static String readUTF(DataInput in) throws IOException {
        int utflen = in.readUnsignedShort();
        byte[] bytearr = null;
        char[] chararr = null;
        if (in instanceof DataTlvStream) {
            DataTlvStream dis = (DataTlvStream)in;
            if (dis.bytearr.length < utflen){
                dis.bytearr = new byte[utflen*2];
                dis.chararr = new char[utflen*2];
            }
            chararr = dis.chararr;
            bytearr = dis.bytearr;
        } else {
            bytearr = new byte[utflen];
            chararr = new char[utflen];
        }

        int c, char2, char3;
        int count = 0;
        int chararr_count=0;

        in.readFully(bytearr, 0, utflen);

        while (count < utflen) {
            c = (int) bytearr[count] & 0xff;
            if (c > 127) break;
            count++;
            chararr[chararr_count++]=(char)c;
        }

        while (count < utflen) {
            c = (int) bytearr[count] & 0xff;
            switch (c >> 4) {
                case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
                    /* 0xxxxxxx*/
                    count++;
                    chararr[chararr_count++]=(char)c;
                    break;
                case 12: case 13:
                    /* 110x xxxx   10xx xxxx*/
                    count += 2;
                    if (count > utflen)
                        throw new UTFDataFormatException(
                            "malformed input: partial character at end");
                    char2 = (int) bytearr[count-1];
                    if ((char2 & 0xC0) != 0x80)
                        throw new UTFDataFormatException(
                            "malformed input around byte " + count);
                    chararr[chararr_count++]=(char)(((c & 0x1F) << 6) |
                                                    (char2 & 0x3F));
                    break;
                case 14:
                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
                    count += 3;
                    if (count > utflen)
                        throw new UTFDataFormatException(
                            "malformed input: partial character at end");
                    char2 = (int) bytearr[count-2];
                    char3 = (int) bytearr[count-1];
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
                        throw new UTFDataFormatException(
                            "malformed input around byte " + (count-1));
                    chararr[chararr_count++]=(char)(((c     & 0x0F) << 12) |
                                                    ((char2 & 0x3F) << 6)  |
                                                    ((char3 & 0x3F) << 0));
                    break;
                default:
                    /* 10xx xxxx,  1111 xxxx */
                    throw new UTFDataFormatException(
                        "malformed input around byte " + count);
            }
        }
        // The number of chars produced may be less than utflen
        return new String(chararr, 0, chararr_count);
    }

	public String readLine() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	private int tlvId;
	private int shortData;
	private int type;
	private int tlvLength;
	private int dataLength;
	public void parserTlvHead(){
		int Type_Length=0;
		try {
			this.tlvId = readUnsignedByte();
			this.shortData = readUnsignedByte();
			Type_Length = readUnsignedShort();
			this.type = (byte)(Type_Length & 0x0f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if ((this.type & 0x01)==1){
			try {
				this.tlvLength = readInt();
				this.dataLength = this.tlvLength-8;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			this.tlvLength = (int) ((Type_Length >> 4)&0xffff);
			this.dataLength = this.tlvLength-4;
		}
	}
	int getTlvId(){
		return this.tlvId;
	}
	int getShortData(){
		return this.shortData;
	}
	int getType(){
		return this.type;
	}
	int getTlvLength(){
		return this.tlvLength;
	}
	int getDataLength(){
		return this.dataLength;
	}
	public void parserSsnStatus(TlvData.SsnStatus ssn,int tlv_shortdata,int tlv_len,int data_len) throws IOException{
		ssn.ssnStatus = tlv_shortdata;
	}
	public void parserAppId(TlvData.AppId app,int tlv_shortdata,int tlv_len,int data_len) throws IOException{
		app.appId = tlv_shortdata;
	}
	public void parserTuple(TlvData.Tuple tuple,int tlv_shortdata,int tlv_len,int data_len) throws IOException{
		//1B 1B 1B 1B 2B 2B 16B 16B
		tuple.ipVer = readByte();
		tuple.dir = readByte();
		tuple.l4Proto = readByte();
		tuple.resv = readByte();
		tuple.srcPort = readUnsignedShort();
		tuple.dstPort = readUnsignedShort();
		if (tuple.ipVer == 0){
			tuple.v4SrcIP = readInt();
			tuple.strSrcIP = numToIP(tuple.v4SrcIP);
			skip(12);
			tuple.v4DstIP = readInt();
			tuple.strDstIP = numToIP(tuple.v4DstIP);
			skip(12);
		}else{
			tuple.strSrcIP = parserIpv6();
			tuple.strDstIP = parserIpv6();
//			skip(32);
		}
		skip(data_len-40);
	}
	public static String numToIP(long ip) {  
        StringBuilder sb = new StringBuilder();  
        for (int i = 3; i >= 0; i--) {  
            sb.append((ip >>> (i * 8)) & 0x000000ff);  
            if (i != 0) {  
                sb.append('.');  
            }  
        }  
        //System.out.println(sb);  
        return sb.toString();  
  
    }  
  
    public static long ipToNum(String ip) {  
        long num = 0;  
        String[] sections = ip.split("\\.");  
        int i = 3;  
        for (String str : sections) {  
            num += (Long.parseLong(str) << (i * 8));  
            i--;  
        }  
    //  System.out.println(num);  
        return num;  
    }
    public String parserIpv6() throws IOException {  
    	long a = 0L;
    	long b = 0L;
    	long c = 0L;
    	long d = 0L;
    	a = readInt();
    	b = readInt();
    	c = readInt();
    	d = readInt();
    	StringBuilder sb = new StringBuilder();
        sb.append(numToIP(d));
        sb.append(":");
        sb.append(numToIP(c));
        sb.append(":");
        sb.append(numToIP(b));
        sb.append(":");
        sb.append(numToIP(a));
        return sb.toString();
    }
    public void parserSsnInfo(TlvData.SsnInfo ssnInfo,int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//4B 4B 4B 4B 4B 4B 4B 4B 4B 4B 2B 2B
    	ssnInfo.upFlow = readInt();
		ssnInfo.downFlow = readInt();
		ssnInfo.upPackets = readInt();
		ssnInfo.downPackets = readInt();
		ssnInfo.upTcpDisorder = readInt();
		ssnInfo.downTcpDisorder = readInt();
		ssnInfo.upTcpRetransmit = readInt();
		ssnInfo.downTcpRetransmit = readInt();
		ssnInfo.upFragPackets = readInt();
		ssnInfo.downFragPackets = readInt();
		ssnInfo.upDuraUpTime = readUnsignedShort();
		ssnInfo.downDuraUpTime = readUnsignedShort();
		skip(data_len-44);
	}
    public void parserTime(TlvData.Time times,int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//8B 8B 8B
    	times.ssnInitTime = readLong();
		times.startTime = readLong();
		times.endTime = readLong();
		skip(data_len-24);
	}
    public void parserBusiInfo(TlvData.BusiInfo busiInfo,int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//4B 4B 4B 4B 4B 4B 4B 4B 4B 4B 2B 2B
    	busiInfo.upFlow = readInt();
		busiInfo.downFlow = readInt();
		busiInfo.upPackets = readInt();
		busiInfo.downPackets = readInt();
		busiInfo.upTcpDisorder = readInt();
		busiInfo.downTcpDisorder = readInt();
		busiInfo.upTcpRetransmit = readInt();
		busiInfo.downTcpRetransmit = readInt();
		busiInfo.upFragPackets = readInt();
		busiInfo.downFragPackets = readInt();
		busiInfo.upDuraUpTime = readUnsignedShort();
		busiInfo.downDuraUpTime = readUnsignedShort();
		skip(data_len-44);
	}
    public void parserTcpInfo(TlvData.TcpInfo tcpInfo,int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//2B 2B 1B 1B 2B 4B 4B 4B 2B 1B 1B 1B 1B 1B 1B
    	tcpInfo.synackToSynTime = readUnsignedShort();
		tcpInfo.ackToSynTime = readUnsignedShort();
		tcpInfo.fullReportFlag = readUnsignedByte();
		tcpInfo.ssnClosReason = readUnsignedByte();
		tcpInfo.resv = readUnsignedShort();
		tcpInfo.firstRequestDelay = readInt();
		tcpInfo.firstResponseDelay = readInt();
		tcpInfo.windowSize = readInt();
		tcpInfo.mssSize = readUnsignedShort();
		tcpInfo.tcpRetryCount = readUnsignedByte();
		tcpInfo.tcpRetryAckCount = readUnsignedByte();
		tcpInfo.tcpAckCount = readUnsignedByte();
		tcpInfo.tcpConnectStatusNo = readUnsignedByte();
		tcpInfo.tcpStatusFirst = readUnsignedByte();
		tcpInfo.tcpStatusSecond = readUnsignedByte();
		skip(data_len-28);
    }
    public void parserResponseDelay(TlvData.ResponseDelay response,int tlv_shortdata,int tlv_len,int data_len) throws IOException {
    	//4B
		if (tlv_len <= 4){
			response.responseDelay = tlv_shortdata;
		}
    	skip(data_len-4);
    }
    public void parserL7Type(TlvData.L7Type l7Type,int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//1B 1B 2B
    	if (tlv_len >4){
    		l7Type.appStatus = readUnsignedByte();
    		l7Type.classID = readUnsignedByte();
    		l7Type.protocol = readUnsignedShort();
    		skip(data_len-4);
    	}
    }
    public void parserHttpInfo(TlvData.HttpInfo httpInfo,int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//8B 8B 8B 8B 4B 2B 1B 1B 2bit 3bit 1bit 2bit 1B 1B 1B
//    	System.out.println("tlv_shortdata="+tlv_shortdata+"tlv_len="+tlv_len+"data_len"+data_len);
    	httpInfo.ctionTime = readLong();
    	httpInfo.firstPacketTime = readLong();
    	httpInfo.lastPacketTime = readLong();
    	httpInfo.serviceTime = readLong();
    	httpInfo.contentLenth = readInt();
    	httpInfo.httpStatus = readUnsignedShort();
    	httpInfo.httpMethod = readUnsignedByte();
    	httpInfo.httpVersion = readUnsignedByte();
    	int tmp = readUnsignedByte();
    	httpInfo.firstRequestFlag = (tmp&0x03);//2bit
    	httpInfo.serFlag = ((tmp>>2)&0x05);//3bit
    	httpInfo.headFlag = ((tmp>>5)&0x01);//1bit
    	httpInfo.ie = readUnsignedByte();
    	httpInfo.portal = readUnsignedByte();
    	httpInfo.resv = readUnsignedByte();
    	skip(data_len-44);
    }
    
    public String parserString(int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//string
    	int len = tlv_len-tlv_shortdata;
    	byte[] buf = new byte[len];
    	read(buf);
    	skip(data_len-len);
    	return new String(buf,"UTF-8");
    }
    public int parserByte(int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//string
    	int tmp;
    	if (tlv_len >4){
    		tmp = readUnsignedByte();
    		skip(data_len-1);
    	}else{
    		tmp = tlv_shortdata;
    	}
    	return tmp;
    }
    public int parserShort(int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//string
    	int tmp;
    	if (tlv_len >4){
    		tmp = readUnsignedShort();
    		skip(data_len-2);
    	}else{
    		tmp = tlv_shortdata;
    	}
    	return tmp;
    }
    public long parserInt(int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//string
    	long tmp = 0L;
    	if (tlv_len >4){
    		tmp = readInt();
    		skip(data_len-4);
    	}else{
    		tmp = tlv_shortdata;
    	}
    	return tmp;
    }
    public long parserLong(int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//string
    	long tmp = 0L;
    	if (tlv_len >4){
    		tmp = readLong();
    		skip(data_len-8);
    	}else{
    		tmp = tlv_shortdata;
    	}
    	return tmp;
    }
    public void parserSipInfo(TlvData.SipInfo sipInfo,int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//1B 1B 1B 1B 2B 1bit 1bit 1bit 13bit
//    	System.out.println("tlv_shortdata="+tlv_shortdata+"tlv_len="+tlv_len+"data_len"+data_len);
    	sipInfo.callDirection = readUnsignedByte();
    	sipInfo.callType = readUnsignedByte();
    	sipInfo.hookReason = readUnsignedByte();
    	sipInfo.signalType = readUnsignedByte();
    	sipInfo.dataflowNum = readUnsignedShort();
    	int tmp = readUnsignedShort();
    	sipInfo.sipInvite = (tmp&0x01);
    	sipInfo.sipBye = ((tmp>>1)&0x01);
    	sipInfo.malloc = ((tmp>>2)&0x01);
    	skip(data_len-8);
    }
    public void parserRtspInfo(TlvData.RtspInfo rtspInfo,int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//2B 2B 2B 2B 2B 2B 4B
//    	System.out.println("tlv_shortdata="+tlv_shortdata+"tlv_len="+tlv_len+"data_len"+data_len);
    	rtspInfo.cStartPort = readUnsignedShort();
    	rtspInfo.cEndPort = readUnsignedShort();
    	rtspInfo.sStartPort = readUnsignedShort();
    	rtspInfo.sEndPort = readUnsignedShort();
    	rtspInfo.ssnVideoCount = readUnsignedShort();
    	rtspInfo.ssnAudioCount = readUnsignedShort();
    	rtspInfo.resDelay = readInt();
    	skip(data_len-16);
    }
    public byte[] parserFile(int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//string
    	int len = tlv_len-tlv_shortdata;
    	byte[] buf = new byte[len];
    	read(buf);
    	skip(data_len-len);
    	return buf;
    }
    
    //单一接口
    public void parserHttpHost(TlvData tlvData, int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//string
    	int len = tlv_len-tlv_shortdata;
    	byte[] buf = new byte[len];
    	read(buf);
    	tlvData.httpHost = new String(buf,"UTF-8");
    	skip(data_len-len);
    }
    public void parserHttpUrl(TlvData tlvData, int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//string
    	int len = tlv_len-tlv_shortdata;
    	byte[] buf = new byte[len];
    	read(buf);
    	tlvData.httpUrl = new String(buf,"UTF-8");
    	skip(data_len-len);
    }
    public void parserHttpXonlineHost(TlvData tlvData, int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//string
    	int len = tlv_len-tlv_shortdata;
    	byte[] buf = new byte[len];
    	read(buf);
    	tlvData.httpXonlineHost = new String(buf,"UTF-8");
    	skip(data_len-len);
    }
    public void parserHttpUserAgent(TlvData tlvData, int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//string
    	int len = tlv_len-tlv_shortdata;
    	byte[] buf = new byte[len];
    	read(buf);
    	tlvData.httpUserAgent = new String(buf,"UTF-8");
    	skip(data_len-len);
    }
    public void parserHttpContent(TlvData tlvData, int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//string
    	int len = tlv_len-tlv_shortdata;
    	byte[] buf = new byte[len];
    	read(buf);
    	tlvData.httpContent = new String(buf,"UTF-8");
    	skip(data_len-len);
    }
    public void parserHttpRefer(TlvData tlvData, int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//string
    	int len = tlv_len-tlv_shortdata;
    	byte[] buf = new byte[len];
    	read(buf);
    	tlvData.httpRefer = new String(buf,"UTF-8");
    	skip(data_len-len);
    }
    public void parserHttpCookie(TlvData tlvData, int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//string
    	int len = tlv_len-tlv_shortdata;
    	byte[] buf = new byte[len];
    	read(buf);
    	tlvData.httpCookie = new String(buf,"UTF-8");
    	skip(data_len-len);
    }
    public void parserHttpLocation(TlvData tlvData, int tlv_shortdata,int tlv_len,int data_len) throws IOException{
    	//string
    	int len = tlv_len-tlv_shortdata;
    	byte[] buf = new byte[len];
    	read(buf);
    	tlvData.httpLocation = new String(buf,"UTF-8");
    	skip(data_len-len);
    }
}
