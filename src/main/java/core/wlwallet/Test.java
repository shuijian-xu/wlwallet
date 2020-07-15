package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;

import javafx.util.converter.ByteStringConverter;

public class Test {

	public static void main(String[] args) throws Exception{
		
		BigInteger gx = new BigInteger(
				"79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798",
				16);
		BigInteger gy = new BigInteger(
				"483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8",
				16);
		S256Point G = new S256Point(gx, gy);
		
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		String mySecret = "my secret";
		byte[] b1 = messageDigest.digest(messageDigest.digest(mySecret
				.getBytes()));
		BigInteger e = new BigInteger(1, b1); // pk
		
		String myMessage = "my message";
		byte[] b2 = messageDigest.digest(messageDigest.digest(myMessage
				.getBytes()));
		BigInteger z = new BigInteger(1, b2); // z
		
		BigInteger k = new BigInteger("1234567890");
		
		BigInteger r = ((S256Point)S256Point.rmul(k, G)).x.num;
		BigInteger k_inv = k.modPow(S256Point.N.subtract(BigInteger.valueOf(2)), S256Point.N);
		BigInteger s = (z.add(r.multiply(e))).multiply(k_inv).mod(S256Point.N);

		System.out.println(S256Point.rmul(e, G));
		System.out.println(z.toString(16));
		System.out.println(r.toString(16));
		System.out.println(s.toString(16));
		
//		BigInteger prefixInt = BigInteger.valueOf(04);
		BigInteger prefixInt = new BigInteger("04", 16);
		byte[] b = prefixInt.toByteArray();
		System.out.println(prefixInt.bitLength());
		System.out.println(prefixInt.toString(2));
		
//		System.out.println(new BigInteger(prefixInt.toByteArray()));
//		System.out.println(new BigInteger(prefixInt.toByteArray()));
//		Byte.parseByte("04",16);
		
		System.out.println();
		byte[] pre_tmp = new BigInteger("04", 16).toByteArray();
		byte[] prefix = new byte[1];
		System.arraycopy(pre_tmp, 0, prefix, 0, 1);
		
		byte[] x_tmp = G.x.num.toByteArray();
		byte[] x = new byte[32];
		int x_padding_size = 32-x_tmp.length;
		System.arraycopy(x_tmp, 0, x, x_padding_size, x_tmp.length);
		
		byte[] y_tmp = G.y.num.toByteArray();
		byte[] y = new byte[32];
		int y_padding_size = 32-y_tmp.length;
		System.arraycopy(y_tmp, 0, y, y_padding_size, y_tmp.length);
		
		
		System.out.println(prefix.length);
		System.out.println(x.length);
		System.out.println(y.length);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] target = null;
		try {
			baos.write(prefix);
			baos.write(x);
			baos.write(y);
			target = baos.toByteArray();
			baos.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println(target.length);
		
		BigInteger t = new BigInteger(target);
		System.out.println(String.format("%0130x", t));
		
		ByteArrayInputStream bais = new ByteArrayInputStream(target);
		
		byte[] prefix2 = new byte[1];
		bais.read(prefix2, 0, 1);
		BigInteger pre = new BigInteger(prefix2);
		System.out.println();
		System.out.println(pre.toString(16));
		System.out.println(pre.equals(BigInteger.valueOf(4)));
		
		byte[] xs = new byte[32];
		bais.read(xs, 0, 32);
		BigInteger xss = new BigInteger(xs);
		System.out.println(xss.toString(16));
		
		/*PrivateKey priv = new PrivateKey(5001);
		byte sec[] = priv.point.sec(true);
		System.out.println();
		System.out.println(priv.point);
		System.out.println(sec.length);
		System.out.println(new BigInteger(sec).toString(16));*/
		

//		PrivateKey priv2 = new PrivateKey(1);
		PrivateKey priv2 = new PrivateKey(2); // issue
//		PrivateKey priv2 = new PrivateKey(3); // issue
//		PrivateKey priv2 = new PrivateKey(4); // issue
//		PrivateKey priv2 = new PrivateKey(5);
//		PrivateKey priv2 = new PrivateKey(100); // issue
//		PrivateKey priv2 = new PrivateKey(101);
//		PrivateKey priv2 = new PrivateKey(102);
//		PrivateKey priv2 = new PrivateKey(103);
//		PrivateKey priv2 = new PrivateKey(104);
//		PrivateKey priv2 = new PrivateKey(105); // issue
//		PrivateKey priv2 = new PrivateKey(5001);
//		PrivateKey priv2 = new PrivateKey(BigInteger.valueOf(2019).pow(5));
//		PrivateKey priv2 = new PrivateKey(new BigInteger("33549155665686099‬"));
		// ‭
		
		
		System.out.println(G.x.num.toByteArray().length);
		System.out.println(G.y.num.toByteArray().length);
		
		String s1 = "c6047f9441ed7d6d3045406e95c07cd85c778e4b8cef3ca7abac09b95c709ee5";
		String s2 = "79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798";
		System.out.println("s1: "+s1.length());
		System.out.println("s2: "+s2.length());
		BigInteger bt=new BigInteger("c6047f9441ed7d6d3045406e95c07cd85c778e4b8cef3ca7abac09b95c709ee5", 16);
		byte[] bts = bt.toByteArray();
		byte[] bts_c = new byte[32];
		System.arraycopy(bts, 1, bts_c, 0, 32);
		System.out.println();
		System.out.println("bts length: "+bts.length);
		System.out.println("bts_c length: "+bts_c.length);
		BigInteger btsI = new BigInteger(1,bts);
		BigInteger btscI = new BigInteger(1,bts_c);
		System.out.println("btsI : "+btsI.toString(16));
		System.out.println("btscI : "+btscI.toString(16));
		System.out.println("bts length post: "+btsI.toByteArray().length);
		System.out.println("bts_c length post: "+btscI.toByteArray().length);
		
		BigInteger bt_1=new BigInteger("79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798", 16);
		byte[] bts_1 = bt_1.toByteArray();
		System.out.println();
		System.out.println("bts_1 length: "+bts_1.length);
		
		
		S256Point pg = (S256Point) G.add(G);
//		S256Point pg = (S256Point) G.add(G).add(G);
		System.out.println();
//		S256Point pg = (S256Point) G.add(G).add(G).add(G);
//		S256Point pg = (S256Point) G.add(G).add(G).add(G).add(G);
		System.out.println(pg);
		System.out.println(pg.x.num.toByteArray().length);
		
		
		
		S256Point p = priv2.point;
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(p);
		
		System.out.println(p.x);
		System.out.println(p.x.num.signum());
		
		byte[] x_bin = p.x.num.toByteArray();
		System.out.println(x_bin.length);
		
		BigInteger vvv = p.y.num.mod(BigInteger.valueOf(2));
		System.out.println(vvv);
		
		byte[] sec2 = priv2.point.sec(true);
		System.out.println(sec2.length);
		System.out.println(new BigInteger(sec2).toString(16));
		
	}

}
