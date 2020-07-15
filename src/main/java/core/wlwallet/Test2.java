package core.wlwallet;

import java.math.BigInteger;

public class Test2 {

	public static void main(String[] args) {

		BigInteger b1 = new BigInteger(
				"c6047f9441ed7d6d3045406e95c07cd85c778e4b8cef3ca7abac09b95c709ee5",
				16);
		BigInteger b2 = new BigInteger(
				"79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798",
				16);

		System.out.println(b1.bitLength());
		System.out.println(b2.bitLength());

		byte[] b1s = b1.toByteArray();
		byte[] b2s = b2.toByteArray();
		
		byte[] b3s=new byte[32];
		

		BigInteger rb1 = new BigInteger(1, b1s);
		BigInteger rb2 = new BigInteger(1, b2s);

		System.out.println(rb1.bitLength());
		System.out.println(rb2.bitLength());

		System.arraycopy(b1s, 1, b3s, 0, 32);
		BigInteger b3 = new BigInteger(1,b3s);
		

		System.out.println();
		System.out.println("bls length: "+b1s.length); // the output is 33
		String str1 = "Byte array representation of " + b1.toString(16) + " is: ";
		System.out.println(str1);
		// print byte array b1s using for loop
		for (int i = 0; i < b1s.length; i++) {
			System.out.format("0x%02X\n", b1s[i]);
		}
		System.out.println();
		System.out.println("b2s length: "+b2s.length); // the output is 32
		String str2 = "Byte array representation of " + b2.toString(16) + " is: ";
		System.out.println(str2);
		// print byte array b1s using for loop
		for (int i = 0; i < b2s.length; i++) {
			System.out.format("0x%02X\n", b2s[i]);
		}
		System.out.println();
		System.out.println("b3s length: "+b3s.length); 
		System.out.println("b3s length: "+b3.toByteArray().length);
		String str3 = "Byte array representation of " + b3.toString(16) + " is: ";
		System.out.println(str3);
		// print byte array b1s using for loop
		for (int i = 0; i < b3s.length; i++) {
			System.out.format("0x%02X\n", b3s[i]);
		}

	}

}
