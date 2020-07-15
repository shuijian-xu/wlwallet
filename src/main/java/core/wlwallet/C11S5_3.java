package core.wlwallet;
import java.math.BigInteger;
import java.util.ArrayList;
public class C11S5_3 {

	public static void main(String[] args) {
		
//		int total=16;
		int total=27;
		int maxDepth = Utils.log(total, 2);
		
		ArrayList<byte[][]> merkleTree = new ArrayList<byte[][]>();
		
		for(int depth=0;depth<maxDepth+1;depth++){
			int numItems = (int) Math.ceil((total/Math.pow(2, maxDepth-depth)));
			byte[][] levelHash = new byte[numItems][];
			
			for(int i=0;i<numItems;i++){
				levelHash[i] = new byte[]{};
			}
			merkleTree.add(levelHash);
		}
		
		StringBuffer sb = new StringBuffer();
		for(byte[][] sList: merkleTree){
			for(byte[] s: sList){
				BigInteger b = new BigInteger(1, s);
				sb.append(b);
			}
			sb.append("\n");
		}
		System.out.println(sb.toString());
		
		
		
		
		
	}

}
