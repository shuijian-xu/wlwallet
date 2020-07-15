package core.wlwallet;

import java.math.BigInteger;

public class S256Field implements Field {

	public BigInteger num;
	public BigInteger prime;

	public static BigInteger P = BigInteger.valueOf(2).pow(256)
			.subtract(BigInteger.valueOf(2).pow(32))
			.subtract(BigInteger.valueOf(977));

	public S256Field(BigInteger num, BigInteger prime) {
		if (num.compareTo(prime) >= 0 || num.compareTo(BigInteger.ZERO) < 0) {
			System.exit(1);
		}
		this.num = num;
		this.prime = prime;
	}

	public S256Field(BigInteger num) {
		this(num, P);
	}

	public S256Field() {
	}


	public boolean eq(Field obj) {
		S256Field other = (S256Field) obj;
		return this.num.compareTo(other.num) == 0
				&& this.prime.compareTo(other.prime) == 0;
	}


	public boolean ne(Field obj) {
		S256Field other = (S256Field) obj;
		return !this.eq(other);
	}


	public S256Field add(Field obj) {
		S256Field other = (S256Field) obj;
		if (this.prime.compareTo(other.prime) != 0) {
			System.out.println("error");
			System.exit(1);
		}
		BigInteger sum = this.num.add(other.num).mod(this.prime);
		return new S256Field(sum, this.prime);
	}

	public S256Field sub(Field obj) {
		S256Field other = (S256Field) obj;
		if (this.prime.compareTo(other.prime) != 0) {
			System.out.println("error");
			System.exit(1);
		}
		BigInteger sub = this.num.subtract(other.num).mod(this.prime);
		return new S256Field(sub, this.prime);
	}

	public S256Field mul(Field obj) {
		S256Field other = (S256Field) obj;
		if (this.prime.compareTo(other.prime) != 0) {
			System.out.println("error");
			System.exit(1);
		}
		BigInteger mul = this.num.multiply(other.num).mod(this.prime);
		return new S256Field(mul, this.prime);
	}

	public S256Field div(Field obj) {
		S256Field other = (S256Field) obj;
		if (this.prime.compareTo(other.prime) != 0) {
			System.out.println("error");
			System.exit(1);
		}
		this.num.multiply(other.num.modPow(
				this.prime.subtract(BigInteger.valueOf(2)), this.prime));
		BigInteger div = this.num.multiply(
				other.num.modPow(this.prime.subtract(BigInteger.valueOf(2)),
						this.prime)).mod(this.prime);
		return new S256Field(div, this.prime);
	}

	public S256Field pow(BigInteger exponent) {
		BigInteger n = exponent.mod(this.prime.subtract(BigInteger.ONE));
		BigInteger pow = this.num.modPow(n, this.prime);
		return new S256Field(pow, this.prime);
	}

	public S256Field sqrt() {
		return this.pow(P.add(BigInteger.ONE).divide(BigInteger.valueOf(4)));
	}

	public static S256Field rmul(BigInteger coefficient, Field obj) {
		S256Field other = (S256Field) obj;
		BigInteger mul = (other.num.multiply(coefficient)).mod(other.prime);
		return new S256Field(mul, other.prime);
	}

	@Override
	public String toString() {
		return String.format("%064x", num);
	}

}
