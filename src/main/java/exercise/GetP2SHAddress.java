package exercise;

import java.math.BigInteger;
import java.util.Stack;

import org.bouncycastle.util.Arrays;

import core.wlwallet.PrivateKey;
import core.wlwallet.S256Point;
import core.wlwallet.Script;
import core.wlwallet.Utils;

public class GetP2SHAddress {

	public static void main(String[] args) {
		String pks;
		byte[] pksb;
		BigInteger secret;
		PrivateKey priv;
		
		
		pks = "shuijian.xu@outlook.com address 2";
		pksb = pks.getBytes();
		secret = Utils.little_endian_to_biginteger(Utils
				.hash256(pksb));
		priv = new PrivateKey(secret);
		S256Point point2 = priv.point;
		byte[] secPubkey2 = point2.sec(true);// user need to provide
//		System.out.println(point2.address(true, true));
		
		pks = "shuijian.xu@outlook.com address 3";
		pksb = pks.getBytes();
		secret = Utils.little_endian_to_biginteger(Utils
				.hash256(pksb));
		priv = new PrivateKey(secret);
		S256Point point3 = priv.point; 
		byte[] secPubkey3 = point3.sec(true); // user need to provide
//		System.out.println(point3.address(true, true));
		
		Stack stack = new Stack();
		stack.push(new byte[]{0x52});
		stack.push(secPubkey2);
		stack.push(secPubkey3);
		stack.push(new byte[]{0x52});
		stack.push(new byte[]{(byte) 0xae});
		
		Script redeemScript = new Script(stack);
		
//		byte[] h160 = Utils.hash160(redeemScript.serialize());
		byte[] h160 = Utils.hash160(redeemScript.raw_serialize());
		
		byte[] prefixAndH160 = Arrays.concatenate(new byte[]{(byte) 0xc4}, h160);
		byte[] checkSum = Arrays.copyOfRange(Utils.hash256(prefixAndH160), 0, 4);
		String p2shAddr = Utils.encodeBase58(Arrays.concatenate(prefixAndH160, checkSum));
		
		System.out.println(p2shAddr);
		
	}

}
