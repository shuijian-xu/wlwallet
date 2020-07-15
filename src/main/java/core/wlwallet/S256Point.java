package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;

public class S256Point implements Point {

	public S256Field x;
	public S256Field y;
	public S256Field a;
	public S256Field b;

	public static BigInteger A = BigInteger.valueOf(0);
	public static BigInteger B = BigInteger.valueOf(7);
	public static BigInteger N = new BigInteger(
			"fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141",
			16);

	BigInteger gx = new BigInteger(
			"79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798",
			16);
	BigInteger gy = new BigInteger(
			"483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8",
			16);

	public S256Point(S256Field x, S256Field y, S256Field a, S256Field b) {
		this.x = x;
		this.y = y;
		this.a = a;
		this.b = b;
	}

	public S256Point(BigInteger x, BigInteger y) {
		this.a = new S256Field(A);
		this.b = new S256Field(B);
		if (x == null && y == null) {
			this.x = null;
			this.y = null;
			return;
		} else {
			this.x = new S256Field(x);
			this.y = new S256Field(y);
		}
		if (this.y.pow(BigInteger.valueOf(2)).ne(
				this.x.pow(BigInteger.valueOf(3)).add(this.a.mul(this.x))
						.add(this.b))) {
			System.out.println("Point(" + x + ", " + y
					+ ") is not on the curve");
			System.exit(1);
		}
	}

	public boolean eq(Point obj) {
		S256Point other = (S256Point) obj;
		if (this.x == null && other.x == null) {
			return true;
		}
		return this.x.eq(other.x) && this.y.eq(other.y) && this.a.eq(other.a)
				&& this.b.eq(other.b);
	}

	public boolean ne(Point obj) {
		S256Point other = (S256Point) obj;
		return !(this.eq(other));
	}

	public Point add(Point obj) {
		S256Point other = (S256Point) obj;
		if (this.a.ne(other.a) || this.b.ne(other.b)) {
			System.out.println("Point " + this + ", " + other
					+ " are not on the same curve");
			System.exit(1);
		}
		if (this.x == null) {
			return other;
		}
		if (other.x == null) {
			return this;
		}
		if (this.x.eq(other.x) && this.y.ne(other.y)) {
			return new S256Point(null, null, this.a, this.b);
		}

		// s = (y2-y1)/(x2-x1)
		// x3 = s^2-x1-x2
		// y3 = s(x1-x2)-y1
		if (this.x.ne(other.x)) {
			S256Field s = (other.y.sub(this.y)).div(other.x.sub(this.x));
			S256Field x = s.pow(BigInteger.valueOf(2)).sub(this.x).sub(other.x);
			S256Field y = (s.mul(this.x.sub(x))).sub(this.y);
			return new S256Point(x, y, this.a, this.b);
		}

		// s = (3x1^2+a)/(2y1)
		// x3 = s^2 - 2x1
		// y3 = s(x1-x3)-y1
		if (this.eq(other)) {

			S256Field s = (S256Field.rmul(BigInteger.valueOf(3),
					this.x.pow(BigInteger.valueOf(2))).add(this.a))
					.div(S256Field.rmul(BigInteger.valueOf(2), this.y));

			S256Field x = s.pow(BigInteger.valueOf(2)).sub(
					S256Field.rmul(BigInteger.valueOf(2), this.x));

			S256Field y = (s.mul(this.x.sub(x))).sub(this.y);

			return new S256Point(x, y, this.a, this.b);
		}

		if (this.eq(other)
				&& this.y.eq(S256Field.rmul(BigInteger.ZERO, this.x))) {
			return new S256Point(null, null, this.a, this.b);
		}
		return null;
	}

	public static Point rmul(BigInteger coefficient, Point other) {
		S256Point obj = (S256Point) other;
		BigInteger coef = coefficient.mod(N);

		S256Point current = obj;
		S256Point result = new S256Point(null, null, obj.a, obj.b);
		int len = coef.bitLength();
		int count = len;
		while (count > 0) {
			if (coef.testBit(len - count)) {
				result = (S256Point) result.add(current);
			}
			current = (S256Point) current.add(current);
			count--;
		}
		return result;
	}

	public boolean verify(BigInteger z, Signature sig) {

		BigInteger gx = new BigInteger(
				"79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798",
				16);
		BigInteger gy = new BigInteger(
				"483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8",
				16);
		S256Point G = new S256Point(gx, gy);

		BigInteger s_inv = sig.s.modPow(N.subtract(BigInteger.valueOf(2)), N);
		BigInteger u = z.multiply(s_inv).mod(N);
		BigInteger v = sig.r.multiply(s_inv).mod(N);
		S256Point total = (S256Point) S256Point.rmul(u, G).add(
				S256Point.rmul(v, this));
		return total.x.num.equals(sig.r);
	}

	public byte[] sec(boolean compressed) {
		// return the binary version of the sec format
		if (compressed) {
			if (this.y.num.mod(BigInteger.valueOf(2))
					.compareTo(BigInteger.ZERO) == 0) {
				byte[] pre_tmp = new BigInteger("02", 16).toByteArray();
				byte[] prefix = new byte[1];
				System.arraycopy(pre_tmp, 0, prefix, 0, 1);
				byte[] x_tmp = this.x.num.toByteArray();
				byte[] x = new byte[32];

				int x_padding_size = 0;
				if (x_tmp.length != 33) {
					x_padding_size = 32 - x_tmp.length;
					System.arraycopy(x_tmp, 0, x, x_padding_size, x_tmp.length);
				} else {
					System.arraycopy(x_tmp, 1, x, 0, 32);
				}

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] result = null;
				try {
					baos.write(prefix);
					baos.write(x);
					result = baos.toByteArray();
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return result;
			} else {
				byte[] pre_tmp = new BigInteger("03", 16).toByteArray();
				byte[] prefix = new byte[1];
				System.arraycopy(pre_tmp, 0, prefix, 0, 1);
				byte[] x_tmp = this.x.num.toByteArray();
				byte[] x = new byte[32];

				int x_padding_size = 0;
				if (x_tmp.length != 33) {
					x_padding_size = 32 - x_tmp.length;
					System.arraycopy(x_tmp, 0, x, x_padding_size, x_tmp.length);
				} else {
					System.arraycopy(x_tmp, 1, x, 0, 32);
				}

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] result = null;
				try {
					baos.write(prefix);
					baos.write(x);
					result = baos.toByteArray();
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return result;
			}
		} else {
			byte[] pre_tmp = new BigInteger("04", 16).toByteArray();
			byte[] prefix = new byte[1];
			System.arraycopy(pre_tmp, 0, prefix, 0, 1);

			byte[] x_tmp = this.x.num.toByteArray();
			byte[] x = new byte[32];

			int x_padding_size = 0;
			if (x_tmp.length != 33) {
				x_padding_size = 32 - x_tmp.length;
				System.arraycopy(x_tmp, 0, x, x_padding_size, x_tmp.length);
			} else {
				System.arraycopy(x_tmp, 1, x, 0, 32);
			}

			byte[] y_tmp = this.y.num.toByteArray();
			byte[] y = new byte[32];

			int y_padding_size = 0;
			if (y_tmp.length != 33) {
				y_padding_size = 32 - y_tmp.length;
				System.arraycopy(y_tmp, 0, y, y_padding_size, y_tmp.length);
			} else {
				System.arraycopy(y_tmp, 1, y, 0, 32);
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] result = null;
			try {
				baos.write(prefix);
				baos.write(x);
				baos.write(y);
				result = baos.toByteArray();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public static S256Point parse(byte[] sec_bin) {
		
		
		if(sec_bin[0]==(byte)4){
			byte[] xbin = Arrays.copyOfRange(sec_bin, 1, 33);
			byte[] ybin = Arrays.copyOfRange(sec_bin, 33, 65);
			BigInteger x = new BigInteger(1, xbin);
			BigInteger y = new BigInteger(1, ybin);
			return new S256Point(x, y);
		}
		
		boolean is_even = (sec_bin[0] ==(byte)2);
		byte[] x_sec_bin = Arrays.copyOfRange(sec_bin, 1, sec_bin.length);
		BigInteger xbigi = new BigInteger(1, x_sec_bin);
		S256Field x = new S256Field(xbigi);
		
		S256Field alpha = (S256Field) x.pow(BigInteger.valueOf(3)).add(
				new S256Field(B));
		S256Field beta = alpha.sqrt();
		
		S256Field even_beta;
		S256Field odd_beta;
		if(beta.num.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)){
			even_beta = beta;
			odd_beta = new S256Field(S256Field.P.subtract(beta.num));
		}else{
			even_beta = new S256Field(S256Field.P.subtract(beta.num));
			odd_beta = beta;
		}
		if(is_even){
			return new S256Point(x.num, even_beta.num);
		}else{
			return new S256Point(x.num, odd_beta.num);
		}
		
	}
	
	public byte[] hash160(boolean compressed){
		return Utils.hash160(this.sec(compressed));
	}
	
	public String address(boolean testnet){
		boolean compressed = true;
		return this.address(compressed, testnet);
	}
	
	public String address(boolean compressed, boolean testnet){
		byte[] h160 = this.hash160(compressed);
		byte prefix;
		if(testnet){
			prefix = 0x6f;
		}else{
			prefix = 0x00;
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] result = null;
		baos.write(prefix);
		try {
			baos.write(h160);
			result = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Utils.encodeBase58Checksum(result);
	}

	@Override
	public String toString() {
		if (this.x == null) {
			return "S256Point(infinity)";
		}
		return "S256Point(" + this.x + "," + this.y + ")";
	}
}
