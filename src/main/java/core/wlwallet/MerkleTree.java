package core.wlwallet;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class MerkleTree {
	int total;
	int maxDepth;
	int currentDepth;
	int currentIndex;
	ArrayList<byte[][]> nodes;
	
	public MerkleTree(int total) {
		super();
		this.total = total;
		this.maxDepth = Utils.log(total, 2);
		this.nodes = new ArrayList<byte[][]>();
		for(int depth=0;depth<this.maxDepth+1;depth++){
			int numItems = (int) Math.ceil((this.total/Math.pow(2, this.maxDepth-depth)));
			byte[][] levelHash = new byte[numItems][];
			for(int i=0;i<numItems;i++){
				levelHash[i] = new byte[]{};
			}
			this.nodes.add(levelHash);
		}
		this.currentDepth = 0;
		this.currentIndex = 0;
	}
	
	
	
	
	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		
		int depth=0;
		for(byte[][] sList: this.nodes){
			
			
			
			int index=0;
			for(byte[] h: sList){
				byte[] shorts=null;		
				if(h[0]==0x00){
					shorts = new byte[]{0x00};
				}else{
					shorts = Arrays.copyOfRange(h, 0, 8);
				}
				if(depth==this.currentDepth&&index==this.currentIndex){
					BigInteger bigInt = new BigInteger(1, shorts);
					sb.append(String.format("%0"+shorts.length*2+"x", bigInt));
				}else{
					BigInteger bigInt = new BigInteger(1, shorts);
					sb.append("*"+String.format("%0"+shorts.length*2+"x", bigInt)+"*");
				}
				sb.append(h);
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
	
}
