package core.wlwallet;

import java.math.BigInteger;
import java.util.Stack;

public class Test10 {

	public static void main(String[] args) {
		BigInteger z = new BigInteger("7c076ff316692a3d7eb3c3bb0f8b1488cf72e1afcd929e29307032997a838a3d", 16);
		byte[] sec = new BigInteger("04887387e452b8eacc4acfde10d9aaf7f6d9a0f975aabb10d006e4da568744d06c61"
				+ "de6d95231cd89026e286df3b6ae4a894a3378e393e93a"
				+ "0f45b666329a0ae34",16).toByteArray();
		byte[] sig = new BigInteger("3045022000eff69ef2b1b"
				+ "d93a66ed5219add4fb51e11a840f404876325a1e8ffe0529a2c022100c"
				+ "7207fee197d27c618aea621406f6bf5ef6fca38681d82b2f06f"
				+ "ddbdce6feab601",16).toByteArray();
		
		Stack stack1 = new Stack();
		stack1.add(sec);
		stack1.add(0xac);
		Script scriptPubkey = new Script(stack1);
		
		Stack stack2 = new Stack();
		stack2.add(sig);
		Script scriptSig = new Script(stack2);
		
		Script combinedScript = scriptSig.add(scriptPubkey);
		System.out.println(combinedScript.evaluate(z));
	}

}
