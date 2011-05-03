package com.guzzservices.rpc.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.guzz.util.CloseUtil;
import org.guzz.util.StringUtil;

import com.guzzservices.rpc.CommandException;

/**
 * 
 * Socket through TCP/IP
 *
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class SocketCommandService {
	private static final Log log = LogFactory.getLog(SocketCommandService.class) ;
	private final Properties props ;
	
	private final Socket socket ;
	private InputStream is ;
	private OutputStream os ;
	private final String charset ;
	private final short version = 1 ;
	
	private boolean isClosed ;
	
	private boolean disposedForIOException ;
	
	public boolean isClosed(){
		return isClosed ;
	}
	
	public void dispose(){
		if(!isClosed){
			CloseUtil.close(socket) ;
			isClosed = true ;
			this.disposedForIOException = false ;
		}
	}
	
	public SocketCommandService(Properties props) throws UnknownHostException, IOException{
		this.props = props ;
		
		//int idleTimeSeconds = StringUtil.toInt(props.getProperty("idleTimeSeconds"), 300) ;
		int soTimeoutSeconds = StringUtil.toInt(props.getProperty("soTimeoutSeconds"), 10) ;
		String host = props.getProperty("host") ;
		int port = StringUtil.toInt(props.getProperty("port"), 6618) ;
		this.charset = props.getProperty("charset", "UTF-8") ;
		
		socket = new Socket() ;
		socket.setTcpNoDelay(true) ;
//		socket.setSoTimeout(idleTimeSeconds * 1000) ;
		socket.setSoTimeout(soTimeoutSeconds * 1000) ;
		socket.setPerformancePreferences(1, 3, 0) ;
		socket.setTrafficClass(0x10) ;
		socket.setSendBufferSize(4096) ;
		socket.connect(new InetSocketAddress(host, port)) ;
	}
	
	public String executeCommand(String command, String param) throws Exception {
		byte[] bs = executeCommand(command, param == null ? null : param.getBytes(charset), true) ;
		
		if(bs == null){
			return null ;
		}
		
		return new String(bs, charset) ;
	}
	
	public byte[] executeCommand(String command, byte[] param) throws Exception {
		return executeCommand(command, param, false) ;
	}
	
	protected byte[] executeCommand(String command, byte[] param, boolean isStringParam) throws Exception {
		try {
			writeRequest(command, param, isStringParam) ;
			
			return readResponse() ;
		} catch (CommandException e){
			//CommandException is fine, we have cleaned the channel.
			throw e ;
		} catch (Exception e) {
			//on error dispose.
			this.dispose() ;
			this.disposedForIOException = true ;
			
			throw e ;
		}
	}
	
	protected void writeRequest(String command, byte[] param, boolean isStringParam) throws IOException{
		byte[] commandBs = command.getBytes(charset) ;
		
		ByteBuffer buffer = null ;
		
		if(param == null){
			buffer = ByteBuffer.allocate(10 + commandBs.length) ;
			
			buffer.putShort(version) ;
			buffer.putShort((short) (isStringParam ? 1 : 0)) ;
			buffer.putShort((short) commandBs.length) ;
			buffer.putInt(-1) ;
			buffer.put(commandBs) ;
		}else{
			buffer = ByteBuffer.allocate(10 + commandBs.length + param.length) ;

			buffer.putShort(version) ;
			buffer.putShort((short) (isStringParam ? 1 : 0)) ;
			buffer.putShort((short) commandBs.length) ;
			buffer.putInt(param.length) ;
			buffer.put(commandBs) ;
			
			if(param.length > 0){
				buffer.put(param) ;
			}
		}
		
		byte[] toSent = buffer.array() ;
		
		if(log.isDebugEnabled()){
			log.debug("package sent:" + Base64.encodeBase64String(toSent)) ;
		}
		
		os = socket.getOutputStream() ;
		
		os.write(toSent) ;
		os.flush() ;
		
		is = socket.getInputStream() ;
	}
	
	protected byte[] readResponse() throws IOException, CommandException{
		//read version
		byte[] header = new byte[2] ;
		
		if(is.read(header) != header.length){
			if(log.isDebugEnabled()){
				log.debug("package header received:" + Base64.encodeBase64String(header)) ;
			}
			
			throw new SocketException("header length error.") ;
		}
				
		ByteBuffer buffer = ByteBuffer.wrap(header) ;
		
		short version = buffer.getShort() ;
		if(version != this.version){
			throw new SocketException("only support version 1. The received version is:" + version) ;
		}
		
		//read the header
		header = new byte[8] ;
		if(is.read(header) != header.length){
			throw new SocketException("header length error.") ;
		}
		
		buffer = ByteBuffer.wrap(header) ;
		boolean isException = buffer.getShort() == 1 ;
		boolean isStringResult = buffer.getShort() == 1 ;
			
		int resultLen = buffer.getInt() ;
		byte[] result = null ;
		
		if(resultLen > 0){
			buffer = ByteBuffer.allocate(resultLen) ;
			
			int readCount = 0 ;
			
			while(readCount != resultLen){
				byte[] body = new byte[Math.min(is.available(), resultLen - readCount)] ;
				
				int count = is.read(body) ;
				
				//nothing available. jump out to avoid dead loop.
				if(count == -1){
					break ;
				}
				
				buffer.put(body, 0, count) ;
				
				readCount += count ;
				
				if(readCount == resultLen){
					break ;
				}
			}
			
			if(readCount != resultLen){
				throw new IOException("result body length error.") ;
			}
			
			result = buffer.array() ;
		}else if(resultLen == 0){
			result = new byte[0] ;
		}
		
		//server exception. Throw after reading all responded data.
		if(isException){
			if(result == null){
				throw new CommandException() ;
			}else{
				throw new CommandException(new String(result, charset)) ;
			}
		}
		
		return result ;
	}

	public boolean isDisposedForIOException() {
		return disposedForIOException;
	}

	public Properties getProps() {
		return props;
	}

	public void setDisposedForIOException(boolean disposedForIOException) {
		this.disposedForIOException = disposedForIOException;
	}
	
}
