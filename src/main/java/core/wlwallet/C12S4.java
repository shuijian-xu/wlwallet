package core.wlwallet;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

import org.apache.commons.codec.digest.MurmurHash3;

public class C12S4 {

	public static void main(String[] args) throws NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		
		BigInteger fieldSize = BigInteger.valueOf(2);
		BigInteger numFunctions = BigInteger.valueOf(2);
		BigInteger tweak=BigInteger.valueOf(42);
		
		BigInteger bitFieldSize = fieldSize.multiply(BigInteger.valueOf(8));
		byte[] bitField = new byte[bitFieldSize.intValue()];
		for(int i=0;i<bitField.length;i++){
			bitField[i]=0x00;
		}
		
		for (byte[] phrase : new byte[][] { "hello world".getBytes(),
				"goodbye".getBytes() }) {
			for(int i=0;i<numFunctions.intValue();i++){
				int seed = i*0xfba4c795+tweak.intValue();
				int h =MurmurHash3.hash32x86(phrase,0, phrase.length, seed)&0xffff;
				int bit = h%(bitFieldSize.intValue());
				bitField[bit]=1;
			}	
		}
	
		for (int i = 0; i < bitField.length; i++) {
			System.out.print(bitField[i] + " ");
		}

	}

}
