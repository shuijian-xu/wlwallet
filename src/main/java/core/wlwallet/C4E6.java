package core.wlwallet;

import java.math.BigInteger;

public class C4E6 {

	public static void main(String[] args) {

		PrivateKey priv;
		priv = new PrivateKey(5003);
		System.out.println(priv.wif(true, true));
		
		priv = new PrivateKey(BigInteger.valueOf(2021).pow(5));
		System.out.println(priv.wif(false, true));
		
		priv = new PrivateKey(new BigInteger("54321deadbeef", 16));
		System.out.println(priv.wif(true, false));
		
	}
}
