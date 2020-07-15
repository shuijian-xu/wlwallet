package core.wlwallet;

import java.util.ArrayList;

import org.bouncycastle.util.Arrays;

public class C10S2 {

	public static void main(String[] args) {
		
		
		
//		String host = "testnet-seed.bitcoin.petertodd.org";
//		int port = 18333;
		String host = "217.23.5.68";
		int port = 8333;
		
		SimpleNode node = new SimpleNode(host, false);
		VersionMessage version = new VersionMessage();
		node.send(version);
		boolean verackReceived = false;
		boolean versionReceived = false;
		
		while(!verackReceived && !versionReceived){
			
			ArrayList<String> list = new ArrayList<String>();
			list.add("core.wlwallet.VersionMessage");
			list.add("core.wlwallet.VerAckMessage");
			Message message = node.waitFor(list);
			
			if(new String(message.getCommand()).trim().equals("verack")){
				verackReceived=true;
			}else{
				versionReceived=true;
				node.send(new VerAckMessage());
			}
		}
	}

}
