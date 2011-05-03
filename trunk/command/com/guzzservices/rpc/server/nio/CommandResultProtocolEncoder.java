/**
 * 
 */
package com.guzzservices.rpc.server.nio;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.guzzservices.rpc.server.CommandResponse;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class CommandResultProtocolEncoder extends ProtocolEncoderAdapter {
	
	private final String charset ;
	
	private final static short version = 1 ;
	
	public CommandResultProtocolEncoder(String charset){
		this.charset = charset ;
	}

	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		//form CommandResponse
		CommandResponse cr = (CommandResponse) message ;		
		
		if(cr.isStringResult){
			if(cr.resultS != null){
				cr.resultB = cr.resultS.getBytes(charset) ;
			}
		}
		
		int resultLen = cr.resultB == null ? -1 : cr.resultB.length ;
		
		IoBuffer buffer = IoBuffer.allocate(11 + resultLen) ;
		
		buffer.putShort(version) ;
		buffer.putShort((short) (cr.isException ? 1 : 0)) ;
		buffer.putShort((short) (cr.isStringResult ? 1 : 0)) ;
		buffer.putInt(resultLen) ;
		
		if(resultLen > 0){
			buffer.put(cr.resultB) ;
		}
		
		buffer.flip() ;
		out.write(buffer) ;
	}

}
