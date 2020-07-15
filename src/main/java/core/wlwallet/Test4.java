package core.wlwallet;

import java.math.BigInteger;

public class Test4 {

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
		
		System.out.println();
		System.out.println(b1s.length);
		System.out.println(b2s.length);
		
		
		if(b1s[0]>0x00){
			System.out.println("b1s[0]>0x00");
		}else if(b1s[0]==0x00){
			System.out.println("b1s[0]=0x00");
		}else{
			System.out.println("b1s[0]<0x00");
		}
		
		if(b2s[0]>=0x00){
			System.out.println("b2s[0]>0x00");
		}else if(b2s[0]==0x00){
			System.out.println("b2s[0]=0x00");
		}else{
			System.out.println("b2s[0]<0x00");
		}

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
	
	}

}
