package core.wlwallet;

import java.math.BigInteger;
import java.util.Stack;

import org.bouncycastle.util.Arrays;

public class Test16 {

	public static void main(String[] args) {
		
		int total = 756;
		
		byte[] flagBits = new byte[]{1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0};
		Stack<byte[]> flagBitsStack = new Stack<byte[]>();
		for (int i = 0; i < flagBits.length; i++) {
			byte[] h = new byte[]{flagBits[i]};
			flagBitsStack.add(h);
		}
		
		String[] hashesStr = new String[]{"d692c08368c012630d7a9354ff23e9700776960bf86a76fd2fb136ce7ecdee8e",
				"527f2394a3882be3f981ac614d24f9620e1d5412405d8eafd6b5aa60375671f5",
				"72c775ae7220e68e39ff0d6f38f01a93492ccd17e86cf6e3ade598683968a97c",
				"2d07e94403319b39373d7a23344f7a443d9e951eb3409dcbdab42c03aa38434f",
				"464a413e40286cd08b2baf33e30c79fde70f1dc8ab42ff56ae2c3e8d5bb9615f",
				"17f09dac86fe30c715c81be58028addf246704e950abbbfbb403e7528930e6ed",
				"fccee41a3a43581ed565b118c336c10c17fcefc30bdf418044b18df1b573d9be",
				"24d66607a10c30dc7396d3a6db02b0321c91a106bcc5e8e50fdb0696f71a4482",
				"ac821b5b8b26838252310d62d8f3bb78d3ff9f922eef6ee1679453791c135ff1",
				"08ca874d568173ac331cb9ac72b77c3e482f225327be6d9ab82871e906a2809f"};
		
		Stack<byte[]> hashes = new Stack<byte[]>();
		for(int i=0;i<hashesStr.length;i++){
			byte[] h = Utils.getBytes(new BigInteger(hashesStr[i],16), BigInteger.valueOf(32));
			hashes.add(Arrays.reverse(h));
		}
		
		MerkleTree2 merkleTree = new MerkleTree2(total);
		merkleTree.populateTree(flagBitsStack, hashes);
		System.out.println(merkleTree.total);
		System.out.println(merkleTree);

	}

}
