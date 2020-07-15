package core.wlwallet;
import java.math.BigInteger;
import java.util.ArrayList;
public class C11S5_2 {

	public static void main(String[] args) {
		
//		int total=16;
		int total=27;
		int maxDepth = Utils.log(total, 2);
		
		ArrayList<ArrayList<byte[]>> merkleTree = new ArrayList<ArrayList<byte[]>>();
		
		for(int depth=0;depth<maxDepth+1;depth++){
			int numItems = (int) Math.ceil((total/Math.pow(2, maxDepth-depth)));
			ArrayList<byte[]> levelHash = new ArrayList<byte[]>();
			for(int i=0;i<numItems;i++){
				levelHash.add(new byte[]{(byte)0});
			}
			merkleTree.add(levelHash);
		}
		
		for(ArrayList<byte[]> sList: merkleTree){
			for(byte[] s: sList){
				BigInteger b = new BigInteger(1, s);
				System.out.print(b);
			}
			System.out.println();
		}
		
		
		
		
		
		
	}

}
