package core.wlwallet;

import java.util.ArrayList;

public class Test6 {

	public static void main(String[] args) {
		ArrayList al = new ArrayList();
		al.add(1);
		al.add(2);
		al.add(3);
		
		for(int i=0; i< al.size();i++){
			System.out.println(al.get(i));
		}
		
//		ArrayList alt = al;
		ArrayList alt = new ArrayList(al);
		
		alt.remove(0);
		
		System.out.println();

		for(int i=0; i< al.size();i++){
			System.out.println(al.get(i));
		}
	}

}
