package core.wlwallet;

import org.bouncycastle.util.Arrays;

public class Test8 {

	public static void main(String[] args) {
		byte[] b = {1,2,3,4,5,6,7};
		for(int i=0;i<b.length;i++){
			System.out.println(b[i]);
		}
		System.out.println();
		byte[] c = Arrays.reverse(b);
		for(int i=0;i<c.length;i++){
			System.out.println(c[i]);
		}
	}

}
