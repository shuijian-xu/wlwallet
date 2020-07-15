package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;

public class C10S5 {

	public static void main(String[] args) {
		
//		String blockStr = "0100000035158869187e847cda0d32280015060f87141c57f4d63f2c59c3317d00000000b1ad99b3de17d2b87f6234843deb5a49454bc889498d24e3cac05052c15000ebfef16a49ffff001d25bbd0b0";
//		String blockStr = "010000006dda37e759836f6c455cd2fe56e37a4f80e6916e7be4581c3c0402f10000000089504189b02d1b5b26230ab1bea07b772a171e53c6648880caee6ddbcf8b0efd32b06d49ffff001d33a025b2";
//		String blockStr = "0100000053fb045b4d3ca149faca8e7ea53cdb3168bc58b875e47196b3a6b3f100000000406468307c915485a9c9eabe31cc853e68311176e07e71475c3e26888fb7b7ed30846949ffff001d2b740f74";
		
		String blockStr = "010000002b120517ca99a3d8361c2a9eef3126fff7c18e3ec365dc2201c315ca000000001d2e4b642f3d14c24f57f237d38acb8e4939855a8ca6ce7dab48e2cd85843d9ad97a6949ffff001d1622692a";
		
		BigInteger bigInt = new BigInteger(blockStr, 16);
		byte[] blockBin = Utils.getBytes(bigInt, BigInteger.valueOf(80));
		
		ByteArrayInputStream s = new ByteArrayInputStream(blockBin);
		Block block = Block.parse(s);
		System.out.println(block.checkPow());
	}

}
