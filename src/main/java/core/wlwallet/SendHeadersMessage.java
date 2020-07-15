package core.wlwallet;

import java.io.DataInputStream;

public class SendHeadersMessage extends Message{

	public byte[] command = new byte[]{'s', 'e', 'n','d', 'h', 'e', 'a', 'd', 'e', 'r', 's'};
	
	public SendHeadersMessage(){
		
	}
	public static SendHeadersMessage parse(DataInputStream dis, boolean testnet){
		return new SendHeadersMessage();
	}
	public byte[] serialize(){
		return new byte[]{};
	}
	public byte[] getCommand() {
		return command;
	}
	
	
}