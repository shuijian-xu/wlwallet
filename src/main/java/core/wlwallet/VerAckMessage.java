package core.wlwallet;

import java.io.DataInputStream;

public class VerAckMessage extends Message{

	public byte[] command = new byte[]{'v', 'e', 'r','a', 'c', 'k'};
	
	public VerAckMessage(){
		
	}
	public static VerAckMessage parse(DataInputStream dis, boolean testnet){
		return new VerAckMessage();
	}
	public byte[] serialize(){
		return new byte[]{};
	}
	public byte[] getCommand() {
		return command;
	}
	
	
}