package core.wlwallet;

import java.math.BigInteger;

import org.bouncycastle.util.Arrays;

public class C9S5 {

	public static void main(String[] args) {

		BigInteger bitsBigInt = new BigInteger("e93c0118", 16);
		byte[] bits = Utils.getBytes(bitsBigInt);
		byte[] exponentBin = Arrays.copyOfRange(bits, bits.length - 1,
				bits.length);

		BigInteger exponent = new BigInteger(1, exponentBin);
		
		BigInteger coefficient = Utils.little_endian_to_biginteger(Arrays.copyOfRange(bits, 0, bits.length - 1));
		BigInteger target = coefficient.multiply(BigInteger.valueOf(256).pow(
				exponent.intValue() - 3));
		BigInteger difficult = BigInteger.valueOf(0xffff).multiply(BigInteger.valueOf(256).pow(0x1d-0x3)).divide(target);
		System.out.println(difficult);
		
	}

}
