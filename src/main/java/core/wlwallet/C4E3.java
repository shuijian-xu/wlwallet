package core.wlwallet;

import java.math.BigInteger;

public class C4E3 {

	public static void main(String[] args) {

		
		BigInteger r = new BigInteger(
				"37206a0610995c58074999cb9767b87af4c4978db68c06e8e6e81d282047a7c6",
				16);
		BigInteger s = new BigInteger(
				"8ca63759c1157ebeaec0d03cecca119fc9a75bf8e6d0fa65c841c8e2738cdaec",
				16);
		
		Signature sig = new Signature(r,s);
		byte[] result = sig.der();
		BigInteger b = new BigInteger(1, result);
		System.out.println(b.toString(16));

	}

}
