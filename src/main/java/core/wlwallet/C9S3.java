package core.wlwallet;

import java.math.BigInteger;

import org.bouncycastle.util.Arrays;

public class C9S3 {

	public static void main(String[] args) {
		
		String blockHeaderStr = "020000208ec39428b17323fa0ddec8e887b4a7c53b8c0a0a220cfd0000000000000000005b0750fce0"
				+ "a889502d40508d39576821155e9c9e3f5c3157f961db38fd8b25be1e77a759e93c0118a4ffd71d";
		BigInteger blockHeaderBig = new BigInteger(blockHeaderStr, 16);
		
		byte[] blockHeaderBin = Arrays.reverse(Utils.hash256(Utils.getBytes(blockHeaderBig)));
		BigInteger blockId = new BigInteger(1, blockHeaderBin);
		System.out.println(String.format("%0"+blockHeaderBin.length*2+"x", blockId));
		
	}

}
