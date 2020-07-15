package core.wlwallet;

import java.io.DataInputStream;

public abstract class Message {
	public static byte[] command;
	public abstract byte[] serialize();
	public abstract byte[] getCommand();
	
}
