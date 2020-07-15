package core.wlwallet;

import java.math.BigInteger;

public class C4E5 {

	public static void main(String[] args) {

		PrivateKey priv;
		priv = new PrivateKey(5002);
		System.out.println();
		System.out.println(priv.point.address(false, true));
		
		priv = new PrivateKey(BigInteger.valueOf(2020).pow(5));
		System.out.println(priv.point.address(true, true));
		
		priv = new PrivateKey(new BigInteger("12345deadbeef", 16));
		System.out.println(priv.point.address(true, false));

		
	}

}
