package client;

import java.math.BigInteger;

import core.wlwallet.PrivateKey;
import core.wlwallet.Utils;

public class GetAddress {

	public static void main(String[] args) {

		String pks;
		byte[] pksb;
		BigInteger secret;
		PrivateKey priv;
		
		pks = "shuijian.xu@outlook.com address 1";
		pksb = pks.getBytes();
		secret = Utils.little_endian_to_biginteger(Utils
				.hash256(pksb));
		priv = new PrivateKey(secret);
		System.out.println(priv.point.address(true, true));
		
		pks = "shuijian.xu@outlook.com address 2";
		pksb = pks.getBytes();
		secret = Utils.little_endian_to_biginteger(Utils
				.hash256(pksb));
		priv = new PrivateKey(secret);
		System.out.println(priv.point.address(true, true));
		
		pks = "shuijian.xu@outlook.com address 3";
		pksb = pks.getBytes();
		secret = Utils.little_endian_to_biginteger(Utils
				.hash256(pksb));
		priv = new PrivateKey(secret);
		System.out.println(priv.point.address(true, true));

	}

}
