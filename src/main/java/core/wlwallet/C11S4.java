package core.wlwallet;

import java.math.BigInteger;
import java.util.ArrayList;

import org.bouncycastle.util.Arrays;

public class C11S4 {

	public static void main(String[] args) {
		
		String[] txHexHashes={"42f6f52f17620653dcc909e58bb352e0bd4bd1381e2955d19c00959a22122b2e",
				"94c3af34b9667bf787e1c6a0a009201589755d01d02fe2877cc69b929d2418d4",
				"959428d7c48113cb9149d0566bde3d46e98cf028053c522b8fa8f735241aa953",
				"a9f27b99d5d108dede755710d4a1ffa2c74af70b4ca71726fa57d68454e609a2",
				"62af110031e29de1efcad103b3ad4bec7bdcf6cb9c9f4afdd586981795516577",
				"766900590ece194667e9da2984018057512887110bf54fe0aa800157aec796ba",
				"e8270fb475763bc8d855cfe45ed98060988c1bdcad2ffc8364f783c98999a208"};
		
		ArrayList<byte[]> hashes = new ArrayList<byte[]>();
		for (int i = 0; i < txHexHashes.length; i++) {
			BigInteger hashBigInt = new BigInteger(txHexHashes[i], 16);
			byte[] hash = Utils.getBytes(hashBigInt, BigInteger.valueOf(32));
			hashes.add(Arrays.reverse(hash));
		}
		
		byte[] merkleRootBin = Arrays.reverse(Utils.merkleRoot(hashes));
		BigInteger merkleRootBigInt = new BigInteger(1, merkleRootBin);
		
		System.out.println(String.format("%0" + merkleRootBin.length * 2
				+ "x", merkleRootBigInt));
		
	}

}
