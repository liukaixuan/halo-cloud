/**
 * 
 */
package com.guzzservices.manager.impl.ip;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.guzz.util.Assert;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class CZIPLoader {
	private static Log log = LogFactory.getLog(CZIPLoader.class);

	private static class IPLocation {
		private String country;
		private String area;
	}

	// 一些固定常量，比如记录长度等等
	private static final int IP_RECORD_LENGTH = 7;
	private static final byte REDIRECT_MODE_1 = 0x01;
	private static final byte REDIRECT_MODE_2 = 0x02;

	// 随机文件访问类
	private RandomAccessFile ipFile;
	// 内存映射文件
	private MappedByteBuffer mbb;
	// 单一模式实例
	// 起始地区的开始和结束的绝对偏移
	private long ipBegin, ipEnd;
	// 为提高效率而采用的临时变量
	private IPLocation loc = new IPLocation();
	private byte[] buf = new byte[1000];
	private byte[] b4 = new byte[4];
	private String unknown_area;

	public CZIPLoader(File file) throws IOException {
		ipFile = new RandomAccessFile(file, "r");
		Assert.assertResouceNotNull(ipFile, "unable to open QQWry.Dat file:" + file) ;
		
		// 如果打开文件成功，读取文件头信息
		ipBegin = readLong4(0);
		ipEnd = readLong4(4);
		if (ipBegin == -1 || ipEnd == -1) {
			ipFile.close();
			ipFile = null;
			
			throw new IOException("unknown format of QQWry.Dat file:" + file) ;
		}
		
		// 映射IP信息文件到内存中
		FileChannel fc = ipFile.getChannel();
		mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, ipFile.length());
		mbb.order(ByteOrder.LITTLE_ENDIAN);
	}

	/**
	 * 从offset位置读取4个字节为一个long，因为java为big-endian格式，所以没办法 用了这么一个函数来做转换
	 * 
	 * @param offset
	 * @return 读取的long值，返回-1表示读取文件失败
	 */
	private long readLong4(long offset) {
		long ret = 0;
		try {
			ipFile.seek(offset);
			ret |= (ipFile.readByte() & 0xFF);
			ret |= ((ipFile.readByte() << 8) & 0xFF00);
			ret |= ((ipFile.readByte() << 16) & 0xFF0000);
			ret |= ((ipFile.readByte() << 24) & 0xFF000000);
			return ret;
		} catch (IOException e) {
			return -1;
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	public int loadIPTable(BSTree<CityMark> tree) throws IOException {
		int count = 0;

		int endOffset = (int) ipEnd;

		for (int offset = (int) ipBegin + 4; offset <= endOffset; offset += IP_RECORD_LENGTH) {
			int temp = readInt3(offset);
			if (temp != -1) {
				IPLocation ipLoc = getIPLocation(temp);
				// read detail

				// 得到起始IP
				readIP(offset - 4, b4);
				String beginIp = CZUtil.getIpStringFromBytes(b4);
				// 得到结束IP
				readIP(temp, b4);
				String endIp = CZUtil.getIpStringFromBytes(b4);
				// 添加该记录

				String country = CleanUtil.escapeCZ(loc.country) ;
				String area = CleanUtil.escapeCZ(loc.area) ;
				
				if(country == null) continue ;
				
				String cityMarker = CleanUtil.getCityMarker(country) ;
				
				if(country.equals(cityMarker)){
					cityMarker = null ;
				}
				
				CityMark cm = new CityMark(beginIp, endIp, country, area, cityMarker);

				tree.insert(cm);
				count++;
			}
		}

		return count;
	}

	/**
	 * 从内存映射文件的offset位置开始的3个字节读取一个int
	 * 
	 * @param offset
	 * @return
	 */
	private int readInt3(int offset) {
		mbb.position(offset);
		return mbb.getInt() & 0x00FFFFFF;
	}

	/**
	 * 从内存映射文件的当前位置开始的3个字节读取一个int
	 * 
	 * @return
	 */
	private int readInt3() {
		return mbb.getInt() & 0x00FFFFFF;
	}

	/**
	 * 从offset位置读取四个字节的ip地址放入ip数组中，读取后的ip为big-endian格式，但是
	 * 文件中是little-endian形式，将会进行转换
	 * 
	 * @param offset
	 * @param ip
	 */
	private void readIP(int offset, byte[] ip) {
		mbb.position(offset);
		mbb.get(ip);
		byte temp = ip[0];
		ip[0] = ip[3];
		ip[3] = temp;
		temp = ip[1];
		ip[1] = ip[2];
		ip[2] = temp;
	}

	/**
	 * 给定一个ip国家地区记录的偏移，返回一个IPLocation结构，此方法应用与内存映射文件方式
	 * 
	 * @param offset
	 *            国家记录的起始偏移
	 * @return IPLocation对象
	 */
	private IPLocation getIPLocation(int offset) {
		// 跳过4字节ip
		mbb.position(offset + 4);
		// 读取第一个字节判断是否标志字节
		byte b = mbb.get();
		if (b == REDIRECT_MODE_1) {
			// 读取国家偏移
			int countryOffset = readInt3();
			// 跳转至偏移处
			mbb.position(countryOffset);
			// 再检查一次标志字节，因为这个时候这个地方仍然可能是个重定向
			b = mbb.get();
			if (b == REDIRECT_MODE_2) {
				loc.country = readString(readInt3());
				mbb.position(countryOffset + 4);
			} else
				loc.country = readString(countryOffset);
			// 读取地区标志
			loc.area = readArea(mbb.position());
		} else if (b == REDIRECT_MODE_2) {
			loc.country = readString(readInt3());
			loc.area = readArea(offset + 8);
		} else {
			loc.country = readString(mbb.position() - 1);
			loc.area = readArea(mbb.position());
		}
		return loc;
	}


	/**
	 * @param offset
	 *            地区记录的起始偏移
	 * @return 地区名字符串
	 */
	private String readArea(int offset) {
		mbb.position(offset);
		byte b = mbb.get();
		if (b == REDIRECT_MODE_1 || b == REDIRECT_MODE_2) {
			int areaOffset = readInt3();
			if (areaOffset == 0)
				return unknown_area;
			else
				return readString(areaOffset);
		} else
			return readString(offset);
	}

	/**
	 * 从内存映射文件的offset位置得到一个0结尾字符串
	 * 
	 * @param offset
	 *            字符串起始偏移
	 * @return 读取的字符串，出错返回空字符串
	 */
	private String readString(int offset) {
		try {
			mbb.position(offset);
			int i;
			for (i = 0, buf[i] = mbb.get(); buf[i] != 0; buf[++i] = mbb.get())
				;
			if (i != 0){
				
				if(i > 100){
					log.info(CZUtil.getString(buf, 0, i, "GBK")) ;
				}
				
				return CZUtil.getString(buf, 0, i, "GBK");
			}
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
		}
		return "";
	}

}
