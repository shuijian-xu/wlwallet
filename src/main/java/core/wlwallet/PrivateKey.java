package core.wlwallet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Random;

public class PrivateKey {

	BigInteger secret;
	public S256Point point;
	S256Point G;

	public PrivateKey(BigInteger secret) {
		super();
		BigInteger gx1 = new BigInteger(
				"79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798",
				16);
		BigInteger gy1 = new BigInteger(
				"483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8",
				16);
		this.G = new S256Point(gx1, gy1);
		this.secret = secret;
		this.point = (S256Point) S256Point.rmul(secret, G);
	}
	
	public PrivateKey(int secret) {
		this(BigInteger.valueOf(secret));
	}

	public String wif(boolean compressed, boolean testnet){
		byte[] bs = this.secret.toByteArray();
		byte[] secret_bytes = new byte[32];
		int secret_bytes_padding_size = 0;
		if (bs.length != 33) {
			secret_bytes_padding_size = 32 - bs.length;
			System.arraycopy(bs, 0, secret_bytes, secret_bytes_padding_size, bs.length);
		} else {
			System.arraycopy(bs, 1, secret_bytes, 0, 32);
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int prefix;
		if(testnet){
			prefix = 0xef;
		}else{
			prefix = 0x80;
		}
		byte[] result = null;
		try {
			baos.write(prefix);
			baos.write(secret_bytes);
			if(compressed){
				baos.write(0x01);
			}
			result = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Utils.encodeBase58Checksum(result);
	}
	
	public String hex() {
		return String.format("%064x", this.secret);
	}

	public Signature sign(BigInteger z) {
		Random random = new Random(100000000);
//		BigInteger k = BigInteger.valueOf(random.nextInt());
		BigInteger k = BigInteger.valueOf(100);
		BigInteger r = ((S256Point) S256Point.rmul(k, this.G)).x.num;
		BigInteger k_inv = k.modPow(
				S256Point.N.subtract(BigInteger.valueOf(2)), S256Point.N);
		BigInteger s = z.add(r.multiply(this.secret)).multiply(k_inv)
				.mod(S256Point.N);
		if (s.compareTo(S256Point.N.divide(BigInteger.valueOf(2))) > 0) {
			s = S256Point.N.subtract(s);
		}
		return new Signature(r, s);
	}
}
