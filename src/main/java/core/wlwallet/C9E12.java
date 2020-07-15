package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;

public class C9E12 {

	public static void main(String[] args) {

		String block1Str = 
				"00000020"
				+ "3471101bbda3fe307664b3283a9ef0e97d9a38a7eacd88000000000000000000"
				+ "10c8aba8479bbaa5e0848152fd3c2289ca50e1c3e58c9a4faaafbdf5803c5448"
				+ "ddb84559"
				+ "7e8b0118"
				+ "e43a81d3";
		String block2Str = 
				"02000020"
				+ "f1472d9db4b563c35f97c428ac903f23b7fc055d1cfc26000000000000000000"
				+ "b3f449fcbe1bc4cfbcb8283a0d2c037f961a3fdf2b8bedc144973735eea707e1"
				+ "26425859"
				+ "7e8b0118"
				+ "e5f00474";

		BigInteger block1BigInt = new BigInteger(block1Str, 16);
		BigInteger block2BigInt = new BigInteger(block2Str, 16);

		byte[] block1Bin = Utils.getBytes(block1BigInt, BigInteger.valueOf(80));
		byte[] block2bin = Utils.getBytes(block2BigInt, BigInteger.valueOf(80));

		ByteArrayInputStream block1Stream = new ByteArrayInputStream(block1Bin);
		ByteArrayInputStream block2Stream = new ByteArrayInputStream(block2bin);

		Block lastBlock = Block.parse(block1Stream);
		Block firstBlock = Block.parse(block2Stream);

		BigInteger timeDifferential = lastBlock.timestamp
				.subtract(firstBlock.timestamp);
		
		if (timeDifferential.compareTo(Utils.TWO_WEEKS.multiply(BigInteger
				.valueOf(4))) > 0) {
			timeDifferential = Utils.TWO_WEEKS.multiply(BigInteger.valueOf(4));
		}
		if (timeDifferential.compareTo(Utils.TWO_WEEKS.divide(BigInteger
				.valueOf(4))) < 0) {
			timeDifferential = Utils.TWO_WEEKS.divide(BigInteger.valueOf(4));
		}
		
		BigInteger newTarget = lastBlock.target().multiply(timeDifferential)
				.divide(Utils.TWO_WEEKS);
		byte[] newTargetBitsBin = Utils.targetToBits(newTarget);
		BigInteger newTargetBits = new BigInteger(1, newTargetBitsBin);
		
		System.out.println(String.format("%08x", newTargetBits));

	}

}
