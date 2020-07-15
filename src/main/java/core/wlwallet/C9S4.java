package core.wlwallet;

import java.math.BigInteger;

import org.bouncycastle.util.Arrays;

public class C9S4 {

	public static void main(String[] args) {
		
		BigInteger bits = new BigInteger("e93c0118", 16);
		byte[] bitsBin = Utils.getBytes(bits);
		
		byte[] exponentBin = Arrays.copyOfRange(bitsBin, bitsBin.length-1, bitsBin.length);
		byte[] coefficientBin = Arrays.copyOfRange(bitsBin, 0, bitsBin.length-1);
		
		BigInteger exponent = new BigInteger(1, exponentBin);
		BigInteger coefficient = Utils.little_endian_to_biginteger(coefficientBin);
		
		BigInteger target = coefficient.multiply(BigInteger.valueOf(256).pow((exponent.subtract(BigInteger.valueOf(3))).intValue()));
		System.out.println(String.format("%064x", target));
		
		String blockHeaderStr = "020000208ec39428b17323fa0ddec8e887b4a7c53b8c0a0a220cfd000000000000000"
				+ "0005b0750fce0a889502d40508d39576821155e9c9e3f5c3157f961db38fd8b25"
				+ "be1e77a759e93c0118a4ffd71d";
		BigInteger blockHeaderBigInt = new BigInteger(blockHeaderStr, 16);
		byte[] blockHeaderBin = Utils.getBytes(blockHeaderBigInt);
		byte[] h256 = Utils.hash256(blockHeaderBin);
		BigInteger proof = Utils.little_endian_to_biginteger(h256);
		System.out.println(String.format("%064x", proof));
		
		System.out.println(proof.compareTo(target)<0);
		
		Block block = new Block();
		
		System.out.println(String.format("%064x", Utils.bitsToTarget(bitsBin)));
		
		
	}

}
