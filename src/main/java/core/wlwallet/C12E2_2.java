package core.wlwallet;

import java.math.BigInteger;
import java.util.BitSet;

import org.apache.commons.codec.digest.MurmurHash3;

public class C12E2_2 {

	public static void main(String[] args) {
		
		BigInteger fieldSize = BigInteger.valueOf(10);
		BigInteger functionCount = BigInteger.valueOf(5);
		BigInteger tweak=BigInteger.valueOf(99);
		
		byte[][] items = new byte[][] { "Hello World".getBytes(),
				"Goodbye!".getBytes() };
		
		BigInteger bitFieldSize = fieldSize.multiply(BigInteger.valueOf(8));
		byte[] bitField = new byte[bitFieldSize.intValue()];
		for(int i=0;i<bitField.length;i++){
			bitField[i]=0x00;
		}
		
		for(byte[] item: items){
			System.out.println();
			System.out.println(String.format("%0"+item.length*2+"x", new BigInteger(1, item)));
			
			for(int i=0;i<functionCount.intValue();i++){
				
				int BIP37_CONSTANT = Integer.parseUnsignedInt("fba4c795",16);
				int seed = (int) (i*0xFBA4C795L+99);
				int h2 = Utils.murmurHash3(99L, i, item);
				long h3 = Integer.toUnsignedLong(h2);
				System.out.println(h3);
				int h = Utils.murmur3(item, seed);
				int bit = (int) (h3%(bitFieldSize.intValue()));
				bitField[bit]=1;
			}
		}
		
		for (int i = 0; i < bitField.length; i++) {
			System.out.print(bitField[i] + " ");
		}
		
		System.out.println();
	
		byte[] b = Utils.bitFieldToBytes(bitField);
		BigInteger num = new BigInteger(1, b);
		System.out.println(String.format("%0"+b.length*2+"x", num));
		
	}

}
