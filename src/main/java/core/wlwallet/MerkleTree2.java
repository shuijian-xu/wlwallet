package core.wlwallet;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class MerkleTree2 {
	int total;
	int maxDepth;
	int currentDepth;
	int currentIndex;
	ArrayList<ArrayList<byte[]>> nodes;
	
	public MerkleTree2(int total) {
		super();
		this.total = total;
		this.maxDepth = Utils.log(total, 2);
		this.nodes = new ArrayList<ArrayList<byte[]>>();
		
		for(int depth=0;depth<this.maxDepth+1;depth++){
			int numItems = (int) Math.ceil((this.total/Math.pow(2, this.maxDepth-depth)));
			ArrayList<byte[]> levelHash = new ArrayList<byte[]>();
			for(int i=0;i<numItems;i++){
				levelHash.add(new byte[]{0x00});
			}
			this.nodes.add(levelHash);
		}
		this.currentDepth = 0;
		this.currentIndex = 0;
	}
	
	public void up(){
		this.currentDepth-=1;
		this.currentIndex/=2;
	}
	
	public void left(){
		this.currentDepth+=1;
		this.currentIndex*=2;
	}
	
	public void right(){
		this.currentDepth +=1;
		this.currentIndex=this.currentIndex*2+1;
	}
	
	public byte[] root(){
		byte[] rootV = this.nodes.get(0).get(0);
		return rootV;
	}
	
	public void setCurrentNode(byte[] value){
//		ArrayList<byte[]> element = new ArrayList<byte[]>();
		
		
		ArrayList<byte[]> element = this.nodes.get(currentDepth);
		element.set(this.currentIndex,value);
		this.nodes.set(this.currentDepth, element);
	}
	
	public byte[] getCurrentNode(){
		return this.nodes.get(this.currentDepth).get(this.currentIndex);
	}
	
	public byte[] getLeftNode(){
		return this.nodes.get(this.currentDepth+1).get(this.currentIndex*2);
	}
	
	public byte[] getRightNode(){
		return this.nodes.get(this.currentDepth+1).get(this.currentIndex*2+1);
	}
	
	public boolean isLeaf(){
		return this.currentDepth==this.maxDepth;
	}
	
	public boolean rightExists(){
		return (this.nodes.get(this.currentDepth+1).size())>(this.currentIndex*2+1);
	}
	
	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		
		int depth=0;
		for(ArrayList<byte[]> sList: this.nodes){
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
					sb.append(String.format("%0"+shorts.length*2+"x", bigInt)+" ");
				}else{
					BigInteger bigInt = new BigInteger(1, shorts);
					sb.append(String.format("%0"+shorts.length*2+"x", bigInt)+" ");
				}
				index++;
			}
			sb.append("\n");
			depth++;
		}
		return sb.toString();
	}
	
	public void populateTree(Stack<byte[]> flagBits, Stack<byte[]> hashes){
		
		while(new BigInteger(1,this.root()).intValue()==0x00){
			if(this.isLeaf()){
				flagBits.remove(0);
				this.setCurrentNode(hashes.remove(0));
				this.up();
			}else{
				byte[] leftHash = this.getLeftNode();
				if(leftHash[0]==0x00){
					if(flagBits.remove(0)[0]==0x00){
						byte[] t = hashes.remove(0);
						this.setCurrentNode(t);
						this.up();
					}else{
						this.left();
					}
				}else if(this.rightExists()){
					byte[] rightHash = this.getRightNode();
					if(rightHash[0]==0x00){
						this.right();
					}else{
						this.setCurrentNode(Utils.merkleParent(leftHash, rightHash));
						this.up();
					}
				}else{
					this.setCurrentNode(Utils.merkleParent(leftHash, leftHash));
					this.up();
				}
			}
		}
		if(hashes.size()!=0){
			System.out.println("hashes not all consumed. "+hashes.size());
			System.exit(1);
		}
		for(byte[] flagBit: flagBits){
			if(flagBit[0]!=0){
				System.out.println("flag bits not all consumes");
				System.exit(1);
			}
		}
	}
	
}
