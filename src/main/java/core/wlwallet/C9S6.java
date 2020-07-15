package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;

public class C9S6 {

	public static void main(String[] args) {

		String lastBlockStr = 
				"00000020"
				+ "fdf740b0e49cf75bb3d5168fb3586f7613dcc5cd89675b010000000000000000"
				+ "2e37b144c0baced07eb7e7b64da916cd3121f2427005551aeb0ec6a6402ac7d7"
				+ "f0e42359"
				+ "54d80118"
				+ "7f5da9f5";
		String firstBlockStr = 
				"00000020"
				+ "1ecd89664fd205a37566e694269ed76e425803003628ab010000000000000000"
				+ "bfcade29d080d9aae8fd461254b041805ae442749f2a40100440fc0e3d5868e5"
				+ "50193459"
				+ "54d80118"
				+ "a1721b2e";

		BigInteger lastBlockBigInt = new BigInteger(lastBlockStr, 16);
		BigInteger firstBlockBigInt = new BigInteger(firstBlockStr, 16);

		byte[] lastBlockBin = Utils.getBytes(lastBlockBigInt, BigInteger.valueOf(80));
		byte[] firstBlockBin = Utils.getBytes(firstBlockBigInt, BigInteger.valueOf(80));

		ByteArrayInputStream lastBlockStream = new ByteArrayInputStream(
				lastBlockBin);
		ByteArrayInputStream firstBlockStream = new ByteArrayInputStream(
				firstBlockBin);

		Block lastBlock = Block.parse(lastBlockStream);
		try {
			lastBlockStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Block firstBlock = Block.parse(firstBlockStream);
		try {
			firstBlockStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		System.out.println(lastBlock.timestamp.toString(16));
		System.out.println(firstBlock.timestamp.toString(16));
		
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
		System.out.println(String.format("%064x", newTarget));
		
		

	}

}
