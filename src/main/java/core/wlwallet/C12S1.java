package core.wlwallet;

import java.math.BigInteger;

public class C12S1 {

	public static void main(String[] args) {
		
		BigInteger bitFieldSize = BigInteger.valueOf(10);
		byte[] bitField = new byte[]{0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
		
		byte[] h = Utils.hash256("hello world".getBytes());
		BigInteger bit = new BigInteger(1, h).mod(bitFieldSize);
		bitField[bit.intValue()]=1;
		
		for(int i=0;i<bitField.length;i++){
			System.out.print(bitField[i]+" ");
		}
		
	}

}
