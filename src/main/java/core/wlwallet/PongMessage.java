package core.wlwallet;

import java.io.DataInputStream;
import java.io.IOException;

public class PongMessage extends Message{
	public byte[] command = new byte[] { 'p', 'o', 'n', 'g' };
	public byte[] nonce;

	public PongMessage(byte[] nonce) {
		this.nonce = nonce;
	}
	
	public static PongMessage parse(DataInputStream s){
		byte[] b = new byte[8];
		try {
			s.read(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new PongMessage(b);
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
