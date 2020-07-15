package core.wlwallet;

import java.math.BigInteger;
import java.util.ArrayList;

public class C11S3 {

	public static void main(String[] args) {
		String[] hexHashes = { "c117ea8ec828342f4dfb0ad6bd140e03a50720ece40169ee38bdc15d9eb64cf5", 
				"c131474164b412e3406696da1ee20ab0fc9bf41c8f05fa8ceea7a08d672d7cc5", 
				"f391da6ecfeed1814efae39e7fcb3838ae0b02c02ae7d0a5848a66947c0727b0", 
				"3d238a92a94532b946c90e19c49351c763696cff3db400485b813aecb8a13181", 
				"10092f2633be5f3ce349bf9ddbde36caa3dd10dfa0ec8106bce23acbff637dae", 
				"7d37b3d54fa6a64869084bfd2e831309118b9e833610e6228adacdbd1b4ba161", 
				"8118a77e542892fe15ae3fc771a4abfd2f5d5d5997544c3487ac36b5c85170fc", 
				"dff6879848c2c9b62fe652720b8df5272093acfaa45a43cdb3696fe2466a3877", 
				"b825c0745f46ac58f7d3759e6dc535a1fec7820377f24d4c2c6ad2cc55c0cb59", 
				"95513952a04bd8992721e9b7e2937f1c04ba31e0469fbe615a78197f68f52b7c", 
				"2e6d722e5e4dbdf2447ddecc9f7dabb8e299bae921c99ad5b0184cd9eb8e5908", 
				"b13a750047bc0bdceb2473e5fe488c2596d7a7124b4e716fdd29b046ef99bbf0" };

		ArrayList<byte[]> hashes = new ArrayList<byte[]>();
		for (int i = 0; i < hexHashes.length; i++) {
			BigInteger hashBigInt = new BigInteger(hexHashes[i], 16);
			byte[] hash = Utils.getBytes(hashBigInt, BigInteger.valueOf(32));
			hashes.add(hash);
		}

		ArrayList<byte[]> currentHashes = new ArrayList<byte[]>(hashes);
		while (currentHashes.size() > 1) {
			currentHashes = Utils.merkleParentLevel(currentHashes);
		}
		System.out.println(currentHashes.size());
		BigInteger hashBigInt = new BigInteger(1, currentHashes.get(0));
		System.out.println(String.format("%0" + currentHashes.get(0).length * 2
				+ "x", hashBigInt));

	}

}
