package core.wlwallet;

import java.math.BigInteger;
import java.util.ArrayList;

public class Test5 {

	public static void main(String[] args) {
		ArrayList al = new ArrayList();
		byte[] b = {1,2,3};
		byte b1 = 6;
		BigInteger i = BigInteger.valueOf(b1);
		
		al.add(al);
		al.add(i);
		
		for(int j=0; j< al.size();j++){
			System.out.println(al.get(j).getClass());
			System.out.println((al.get(j)) instanceof ArrayList);
			System.out.println((al.get(j)) instanceof Integer);
			System.out.println((al.get(j)) instanceof BigInteger);
		}
	}

}
