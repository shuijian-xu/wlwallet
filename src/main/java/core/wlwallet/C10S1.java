package core.wlwallet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

public class C10S1 {

	public static void main(String[] args) {
		
		
		
		String host = "seed.tbtc.petertodd.org";
		int port = 18333;
//		String host = "217.23.5.68";
//		int port = 8333;
		
		try {
			Socket socket = new Socket(host, port);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			VersionMessage version = new VersionMessage();
System.out.println(new BigInteger(version.command).toString(16));
System.out.println(new BigInteger(version.serialize()).toString(16));
			NetworkEnvelope envelope = new NetworkEnvelope(version.command, version.serialize());
System.out.println();
System.out.println(new BigInteger(1, envelope.serialize()).toString(16));
System.out.println();
			dos.write(envelope.serialize());
			dos.flush();
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			while(true){
				NetworkEnvelope newMessage = NetworkEnvelope.parse(dis, true);
			//	NetworkEnvelope newMessage = NetworkEnvelope.parse(dis);
				System.out.println(newMessage);
//				break;
			}
//			dos.close();
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
