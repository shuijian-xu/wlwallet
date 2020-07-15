package core.wlwallet;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Stack;

import org.apache.commons.codec.digest.MurmurHash3;

public class C12E2_1 {

	public static void main(String[] args) {
		
		BigInteger fieldSize = BigInteger.valueOf(10);
		BigInteger functionCount = BigInteger.valueOf(5);
		BigInteger tweak=BigInteger.valueOf(99);
		
		byte[][] items = new byte[][] { "Hello World".getBytes(),
				"Goodbye!".getBytes() };
		
		BigInteger bitFieldSize = fieldSize.multiply(BigInteger.valueOf(8));
		
//		BigInteger bitField;
		
		
		
//		Stack<Byte> bitField = new Stack<Byte>();
		BitSet bitField =new BitSet(bitFieldSize.intValue());
		
		int length = bitField.size();
		
		for(int i=0;i<length;i++){
			bitField.set(i, false);

		}
		
		for(byte[] item: items){
			for(int i=0;i<functionCount.intValue();i++){
				int seed = i*0xfba4c795+tweak.intValue();
				int h =MurmurHash3.hash32x86(item,0, item.length, seed)&0xffff;
				int bit = h%(bitFieldSize.intValue());
				bitField.set(bit, true);
			}
		}
		
		
	
		byte[] b = Utils.bitFieldToBytes(bitField);
		BigInteger num = new BigInteger(1, b);
		System.out.println(String.format("%0"+b.length*2+"x", num));
		
	/*	for (int i = 0; i < bitField.length; i++) {
			System.out.print(bitField[i] + " ");
		}
		*/
		
		
	}

}
