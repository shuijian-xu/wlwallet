package core.wlwallet;

public class GenericMessage extends Message{
	
	
	public byte[] command;
	public byte[] payload;
	
	public GenericMessage(byte[] command, byte[] payload) {
		super();
		this.command = command;
		this.payload = payload;
	}
	
	public byte[] serialize(){
		return this.payload;
	}

	@Override
	public byte[] getCommand() {
		// TODO Auto-generated method stub
		return this.command;
	}

}
