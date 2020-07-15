package core.wlwallet;

import java.math.BigInteger;

public class Test15 {

	public static void main(String[] args) {
		byte[] b = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
		BigInteger big = new BigInteger(1, b);
		System.out.println(big);
		System.out.println(big.equals(BigInteger.ZERO));
		;
	}

}
