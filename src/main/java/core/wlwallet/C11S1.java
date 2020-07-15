package core.wlwallet;

import java.math.BigInteger;

import org.bouncycastle.util.Arrays;

public class C11S1 {

	public static void main(String[] args) {
		
		String txhStr1 = "c117ea8ec828342f4dfb0ad6bd140e03a50720ece40169ee38bdc15d9eb64cf5";
		BigInteger txhBigInt1 = new BigInteger(txhStr1, 16);
		byte[] hash0 = Utils.getBytes(txhBigInt1, BigInteger.valueOf(32));
		
		String txhStr2 = "c131474164b412e3406696da1ee20ab0fc9bf41c8f05fa8ceea7a08d672d7cc5";
		BigInteger txhBigInt2 = new BigInteger(txhStr2, 16);
		byte[] hash1 = Utils.getBytes(txhBigInt2, BigInteger.valueOf(32));
		
		byte[] parent = Utils.hash256(Arrays.concatenate(hash0, hash1));
		BigInteger parentBigInt = new BigInteger(1,parent);
		
		System.out.println(String.format("%0"+parent.length*2+"x", parentBigInt));
		
	}

}
