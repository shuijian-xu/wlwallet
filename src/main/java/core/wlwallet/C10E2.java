package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.math.BigInteger;

public class C10E2 {

	public static void main(String[] args) {
		
		String messageStr = "f9beb4d976657261636b000000000000000000005df6e0e2";
		BigInteger messageHex = new BigInteger(messageStr, 16);
		byte[] messageBin = Utils.getBytes(messageHex);
		ByteArrayInputStream bais = new ByteArrayInputStream(messageBin);
		
		DataInputStream stream = new DataInputStream(bais);
		NetworkEnvelope envelope = NetworkEnvelope.parse(stream);
		
		System.out.println(envelope.command);
		
		System.out.println(envelope.payload);
		
	}

}
