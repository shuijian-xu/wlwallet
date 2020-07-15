package core.wlwallet;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Stack;

public class C6NE {

	public static void main(String[] args) {
		
		String zStr = "7c076ff316692a3d7eb3c3bb0f8b1488cf72e1afcd929e29307032997a838a3d";
		BigInteger z = new BigInteger(zStr, 16);
		
		String secStr = "";
		BigInteger secB = new BigInteger(secStr, 16);
		byte[] sec = secB.toByteArray();
		
		String sigStr = "";
		BigInteger sigB = new BigInteger(sigStr, 16);
		byte[] sig = sigB.toByteArray();
		
		Stack al1 = new Stack();
		al1.add(sec);
		al1.add(0xac);
		
		Script script_pubkey = new Script(al1);
		
		Stack al2 = new Stack();
		al2.add(sig);
		
		Script script_sig = new Script(al2);
		
		Script combined_script = script_sig.add(script_pubkey);
		
		
	}

}
