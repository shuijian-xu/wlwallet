package core.wlwallet;

import java.math.BigInteger;

public class C4E2 {

	public static void main(String[] args) {
		
		PrivateKey priv;
		byte[] sec;
		priv = new PrivateKey(5001);
		sec = priv.point.sec(true);
		System.out.println(sec.length);
		System.out.println(String.format("%066x", new BigInteger(1, sec)));
		
		priv = new PrivateKey(BigInteger.valueOf(2019).pow(5));
		sec = priv.point.sec(true);
		System.out.println(sec.length);
		System.out.println(String.format("%066x", new BigInteger(1, sec)));
		
		priv = new PrivateKey(new BigInteger("deadbeef54321", 16));
		sec = priv.point.sec(true);
		System.out.println(sec.length);
		System.out.println(String.format("%066x", new BigInteger(1, sec)));
		
	}

}
