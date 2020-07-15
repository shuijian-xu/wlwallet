package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;

public class C9S2 {

	public static void main(String[] args) {
		
		String blockStr = "020000208ec39428b17323fa0ddec8e887b4a7c53b8c0a0a220cfd0000000000000000005b0750fce0a889"
				+ "502d40508d39576821155e9c9e3f5c3157f961db38fd8b25be1e77a759e93c0118a4ffd71d";
		BigInteger blockBig = new BigInteger(blockStr, 16);
		
		byte[] b = Utils.getBytes(blockBig);
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		Block block = Block.parse(bais);
		
		BigInteger b1 = block.version.shiftRight(29);
		BigInteger b2 = block.version.shiftRight(4).and(BigInteger.ONE);
		BigInteger b3 = block.version.shiftRight(1).and(BigInteger.ONE);
		
		System.out.println(b1.byteValue()==0b001);
		System.out.println(b2.byteValue()==1);
		System.out.println(b3.byteValue()==1);
			
	}
	
}
