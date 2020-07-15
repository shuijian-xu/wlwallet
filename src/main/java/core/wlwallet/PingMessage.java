package core.wlwallet;

import java.io.DataInputStream;
import java.io.IOException;

public class PingMessage extends Message{
	public byte[] command = new byte[]{'p','i','n','g'};
	
	public byte[] nonce;
	
	public PingMessage(byte[] nonce){
		this.nonce=nonce;
	}
	
	public static PingMessage parse(DataInputStream s){
		byte[] b = new byte[8];
		try {
			s.read(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new PingMessage(b);
	}
	
	public byte[] serialize(){
		return this.nonce;
	}

	@Override
	public byte[] getCommand() {
		// TODO Auto-generated method stub
		return this.command;
	}
}
