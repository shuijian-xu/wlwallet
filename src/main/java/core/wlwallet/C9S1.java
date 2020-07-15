package core.wlwallet;

import java.math.BigInteger;

import org.bouncycastle.util.Arrays;

public class C9S1 {

	public static void main(String[] args) {
		BigInteger blockHash = new BigInteger("020000208ec39428b17323fa0ddec8e887b4a7c53b8c0a0a220cfd000000000"
				+ "0000000005b0750fce0a889502d40508d39576821155e9c9e3f5c3157f961db38fd8b2"
				+ "5be1e77a759e93c0118a4ffd71d", 16);
		
		byte[] b = Utils.getBytes(blockHash);
		byte[] blockIdBin = Arrays.reverse(Utils.hash256(b));
		BigInteger blockId = new BigInteger(1, blockIdBin);
		System.out.println(String.format("%0"+blockIdBin.length*2+"x", blockId));
	
	}

}
