package core.wlwallet;

import java.math.BigInteger;
import java.util.ArrayList;

public class C11S2 {

	public static void main(String[] args) {
		
		String[] hexHashes={"c117ea8ec828342f4dfb0ad6bd140e03a50720ece40169ee38bdc15d9eb64cf5","c131474164b412e3406696da1ee20ab0fc9bf41c8f05fa8ceea7a08d672d7cc5","f391da6ecfeed1814efae39e7fcb3838ae0b02c02ae7d0a5848a66947c0727b0","3d238a92a94532b946c90e19c49351c763696cff3db400485b813aecb8a13181","10092f2633be5f3ce349bf9ddbde36caa3dd10dfa0ec8106bce23acbff637dae"};
		ArrayList<byte[]> hashes = new ArrayList<byte[]>();
		for(int i=0;i<hexHashes.length;i++){
			BigInteger hashBigInt = new BigInteger(hexHashes[i], 16);
			byte[] hash = Utils.getBytes(hashBigInt, BigInteger.valueOf(32));
			hashes.add(hash);
		}
		int hashesSize = hashes.size();
		if(hashesSize%2==1){
			hashes.add(hashes.get(hashesSize-1));
		}
		
		ArrayList<byte[]> parentLevel = new ArrayList<byte[]>();
		for(int i=0;i<hashes.size();i=i+2){
			byte[] parent = Utils.merkleParent(hashes.get(i), hashes.get(i+1));
			parentLevel.add(parent);
		}
		
		for(byte[] item: parentLevel){
			BigInteger hashBigInt = new BigInteger(1, item);
			System.out.println(String.format("%0"+item.length*2+"x", hashBigInt));
		}
	}

}
