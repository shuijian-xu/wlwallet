package core.wlwallet;

import java.math.BigInteger;

public class C4E9 {

	public static void main(String[] args) {

		String pwd = "shuijian.xu@outlook.com secrect key";
//		String pwd = "jimmy@programmingblockchain.com my secret";
		byte[] b1 = pwd.getBytes();
		byte[] b2 = Utils.hash256(b1);
		BigInteger secret = Utils.little_endian_to_biginteger(b2);
		PrivateKey priv;
		priv = new PrivateKey(secret);
		System.out.println(priv.point.address(true, true));
		
	}
}
