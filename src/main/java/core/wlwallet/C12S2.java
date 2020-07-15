package core.wlwallet;

import java.math.BigInteger;

public class C12S2 {

	public static void main(String[] args) {

		BigInteger bitFieldSize = BigInteger.valueOf(10);
		byte[] bitField = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00 };

		for (byte[] item : new byte[][] { "hello world".getBytes(),
				"goodbye".getBytes() }) {
			byte[] h = Utils.hash160(item);
			BigInteger bit = new BigInteger(1, h).mod(bitFieldSize);
			bitField[bit.intValue()] = 1;
		}

		for (int i = 0; i < bitField.length; i++) {
			System.out.print(bitField[i] + " ");
		}

	}

}
