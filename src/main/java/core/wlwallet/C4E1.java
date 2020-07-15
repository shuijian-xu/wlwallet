package core.wlwallet;

import java.math.BigInteger;

public class C4E1 {

	public static void main(String[] args) {
		
		PrivateKey priv;
		byte[] sec;
		priv = new PrivateKey(5000);
		sec = priv.point.sec(false);
		System.out.println(sec.length);
		System.out.println(String.format("%0130x", new BigInteger(1, sec)));
		
		priv = new PrivateKey(BigInteger.valueOf(2018).pow(5));
		sec = priv.point.sec(false);
		System.out.println(sec.length);
		System.out.println(String.format("%0130x", new BigInteger(1, sec)));
		
		priv = new PrivateKey(new BigInteger("deadbeef12345", 16));
		sec = priv.point.sec(false);
		System.out.println(sec.length);
		System.out.println(String.format("%0130x", new BigInteger(1, sec)));
		
	}

}
