package core.wlwallet;
import java.util.ArrayList;
public class C11S5 {

	public static void main(String[] args) {
		
		int total=16;
//		int total=27;
		int maxDepth = Utils.log(total, 2);
		
		ArrayList<String[]> merkleTree = new ArrayList<String[]>();
		for(int depth=0;depth<maxDepth+1;depth++){
			int numItems = (int) Math.ceil((total/Math.pow(2, maxDepth-depth)));
			
			String[] levelHash = new String[numItems];
			for(int i=0;i<numItems;i++){
				levelHash[i]=String.valueOf(0);
			}
			merkleTree.add(levelHash);
		}
		
		for(String[] sList: merkleTree){
			for(String s: sList){
				System.out.print(s);
			}
			System.out.println();
		}
		
		
		
		
		
		
	}

}
