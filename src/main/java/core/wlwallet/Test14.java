package core.wlwallet;

import org.bouncycastle.util.Arrays;

public class Test14 {

	public static void main(String[] args) {
		byte[] a = {1,2,3,4};
		byte[] d = {5,6};
		
		
		byte[] e = Arrays.concatenate(a, d);
		
		byte[] b = Arrays.copyOfRange(a, a.length-2, a.length);
		
		byte[] c = Arrays.copyOfRange(a, 0, a.length-1);
		
		
		byte[] f = Arrays.append(d, (byte)1);
		
		for(int i: b){
			System.out.println(i);
		}
		
		System.out.println();
		
		for(int i: c){
			System.out.println(i);
		}
		
		System.out.println();
		
		for(int i: e){
			System.out.println(i);
		}
		
		
		System.out.println();
		
		for(int i: f){
			System.out.println(i);
		}
		
	}

}
