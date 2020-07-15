package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import org.bouncycastle.util.Arrays;

public class Block {

	public static byte[] LOWEST_BITS = Utils.getBytes(new BigInteger(
			"ffff001d", 16));

	static String genesisBlockStr = "0100000000000000000000000000000000000000000000000000000000"
			+ "000000000000003ba3edfd7a7b12b27ac72c3e67768f617fc81bc3888a51323"
			+ "a9fb8aa4b1e5e4a29ab5f49ffff001d1dac2b7c";
	static BigInteger genesisBlockBigInt = new BigInteger(genesisBlockStr, 16);
	public static byte[] GENESISBLOCK = Utils.getBytes(genesisBlockBigInt,
			BigInteger.valueOf(80));

	static String testGenesisBlockStr = "0100000000000000000000000000000000000000000000000000000"
			+ "000000000000000003ba3edfd7a7b12b27ac72c3e67768f617fc81bc3888a513"
			+ "23a9fb8aa4b1e5e4adae5494dffff001d1aa4ae18";
	static BigInteger testGenesisBlockBigInt = new BigInteger(
			testGenesisBlockStr, 16);
	public static byte[] TESTGENESISBLOCK = Utils.getBytes(
			testGenesisBlockBigInt, BigInteger.valueOf(80));

	public BigInteger version;
	byte[] prevBlock;
	byte[] merkleRoot;
	BigInteger timestamp;
	byte[] bits;
	byte[] nonce;

	public ArrayList<byte[]> txHashes;

	public Block() {
	}

	public Block(BigInteger version, BigInteger prevBlock,
			BigInteger merkleRoot, BigInteger timestamp, BigInteger bits,
			BigInteger nonce) {
		super();
		this.version = version;
		this.prevBlock = Utils.getBytes(prevBlock, BigInteger.valueOf(32));
		this.merkleRoot = Utils.getBytes(merkleRoot, BigInteger.valueOf(32));
		this.timestamp = timestamp;
		this.bits = Utils.getBytes(bits, BigInteger.valueOf(4));
		this.nonce = Utils.getBytes(nonce, BigInteger.valueOf(4));
	}

	public Block(BigInteger version, byte[] prevBlock, byte[] merkleRoot,
			BigInteger timestamp, byte[] bits, byte[] nonce) {
		super();
		this.version = version;
		this.prevBlock = prevBlock;
		this.merkleRoot = merkleRoot;
		this.timestamp = timestamp;
		this.bits = bits;
		this.nonce = nonce;
	}

	public Block(BigInteger version, BigInteger prevBlock,
			BigInteger merkleRoot, BigInteger timestamp, BigInteger bits,
			BigInteger nonce, ArrayList<byte[]> txHashes) {
		super();
		this.version = version;
		this.prevBlock = Utils.getBytes(prevBlock, BigInteger.valueOf(32));
		this.merkleRoot = Utils.getBytes(merkleRoot, BigInteger.valueOf(32));
		this.timestamp = timestamp;
		this.bits = Utils.getBytes(bits, BigInteger.valueOf(4));
		this.nonce = Utils.getBytes(nonce, BigInteger.valueOf(4));
		this.txHashes = txHashes;
	}

	public static Block parse(ByteArrayInputStream s) {
		Block block = null;
		try {
			byte[] versionBin = new byte[4];
			s.read(versionBin);
			BigInteger version = Utils.little_endian_to_biginteger(versionBin);

			byte[] prevBlockBin = new byte[32];
			s.read(prevBlockBin);
			BigInteger prevBlock = Utils
					.little_endian_to_biginteger(prevBlockBin);

			byte[] merkleRootBin = new byte[32];
			s.read(merkleRootBin);
			BigInteger merkleRoot = Utils
					.little_endian_to_biginteger(merkleRootBin);

			byte[] timestampBin = new byte[4];
			s.read(timestampBin);
			BigInteger timestamp = Utils
					.little_endian_to_biginteger(timestampBin);

			byte[] bitsBin = new byte[4];
			s.read(bitsBin);
			BigInteger bits = new BigInteger(1, bitsBin);

			byte[] nonceBin = new byte[4];
			s.read(nonceBin);
			BigInteger nonce = new BigInteger(1, nonceBin);

			block = new Block(version, prevBlock, merkleRoot, timestamp, bits,
					nonce);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return block;
	}

	public static Block parse(DataInputStream s) {
		
		Block block = null;
		int i = 0;
		try {
			byte[] versionBin = new byte[4];
			i = s.read(versionBin);
			BigInteger version=null;
			if (i != -1) {
				version = Utils.little_endian_to_biginteger(versionBin);
			}else{
				System.out.println("Parse block error!");
				System.exit(1);
			}
			

			byte[] prevBlockBin = new byte[32];
			BigInteger prevBlock =null;
			i = s.read(prevBlockBin);
			if (i != -1) {
				prevBlock = Utils
						.little_endian_to_biginteger(prevBlockBin);
			}else{
				System.out.println("Parse block error!");
				System.exit(1);
			}
			

			byte[] merkleRootBin = new byte[32];
			i = s.read(merkleRootBin);
			BigInteger merkleRoot =null;
			if (i != -1) {
				merkleRoot = Utils
						.little_endian_to_biginteger(merkleRootBin);
			}else{
				System.out.println("Parse block error!");
				System.exit(1);
			}
			

			byte[] timestampBin = new byte[4];
			i = s.read(timestampBin);
			BigInteger timestamp =null;
			if (i != -1) {
				timestamp = Utils
						.little_endian_to_biginteger(timestampBin);
			}else{
				System.out.println("Parse block error!");
				System.exit(1);
			}
			

			byte[] bitsBin = new byte[4];
			i = s.read(bitsBin);
			BigInteger bits =null;
			if (i != -1) {
				bits = new BigInteger(1, bitsBin);
			}else{
				System.out.println("Parse block error!");
				System.exit(1);
			}
			

			byte[] nonceBin = new byte[4];
			i = s.read(nonceBin);
			BigInteger nonce =null;
			if (i != -1) {
				nonce = new BigInteger(1, nonceBin);
			}else{
				System.out.println("Parse block error!");
				System.exit(1);
			}
			

			block = new Block(version, prevBlock, merkleRoot, timestamp, bits,
					nonce);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return block;
	}

	public byte[] serialize() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] versionBin = Utils.biginteger_to_little_endian(this.version, 4);

		Arrays.reverse(this.prevBlock);

		byte[] prevBlockBin = Arrays.reverse(this.prevBlock);
		byte[] merkleRootBin = Arrays.reverse(this.merkleRoot);
		byte[] timestampBin = Utils.biginteger_to_little_endian(this.timestamp,
				4);
		byte[] bitsBin = this.bits;
		byte[] nonceBin = this.nonce;

		try {
			baos.write(versionBin);
			baos.write(prevBlockBin);
			baos.write(merkleRootBin);
			baos.write(timestampBin);
			baos.write(bitsBin);
			baos.write(nonceBin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] result = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;

	}

	public byte[] hash() {
		byte[] blockBin = this.serialize();
		byte[] h256 = Arrays.reverse(Utils.hash256(blockBin));
		return h256;
	}

	public boolean bip9() {
		return (this.version.shiftRight(29).byteValue() == 0b001);
	}

	public boolean bip91() {
		return this.version.shiftRight(4).and(BigInteger.ONE).byteValue() == 1;
	}

	public boolean bip141() {
		return this.version.shiftRight(1).and(BigInteger.ONE).byteValue() == 1;
	}

	public BigInteger target() {
		BigInteger exponent = new BigInteger(1, this.bits);
		if (exponent.equals(BigInteger.ZERO)) {
			byte[] hashBin = this.hash();
			System.out.println(this);
			System.out.println(String.format("%0" + hashBin.length * 2 + "x",
					new BigInteger(1, hashBin)));
		}
		return Utils.bitsToTarget(this.bits);
	}

	public BigInteger difficulty() {
		BigInteger lowest = BigInteger.valueOf(0xffff).multiply(
				BigInteger.valueOf(256).pow(0x1d - 3));
		return lowest.divide(this.difficulty());
	}

	public boolean checkPow() {
		byte[] sha = Utils.hash256(this.serialize());
		BigInteger proof = Utils.little_endian_to_biginteger(sha);
		BigInteger target = this.target();
		return proof.compareTo(target) < 0;
	}

	public boolean validateMerkleRoot() {
		ArrayList<byte[]> list = this.txHashes;
		ArrayList<byte[]> hashes = new ArrayList<byte[]>();
		for (int i = 0; i < list.size(); i++) {
			hashes.add(Arrays.reverse(list.get(i)));
		}
		byte[] merkleRootBin = Arrays.reverse(Utils.merkleRoot(hashes));
		BigInteger merkleRootBigInt = new BigInteger(1, merkleRootBin);
		return this.merkleRoot.equals(merkleRootBigInt);
	}

	@Override
	public String toString() {
		return "Block [version="
				+ String.format("%08x", version)
				+ ", prevBlock="
				+ String.format("%0" + prevBlock.length * 2 + "x",
						new BigInteger(1, prevBlock))
				+ ", merkleRoot="
				+ String.format("%0" + merkleRoot.length * 2 + "x",
						new BigInteger(1, merkleRoot))
				+ ", timestamp="
				+ String.format("%08x", timestamp)
				+ ", bits="
				+ String.format("%0" + bits.length * 2 + "x", new BigInteger(1,
						bits))
				+ ", nonce="
				+ String.format("%0" + nonce.length * 2 + "x", new BigInteger(
						1, nonce)) + "]";
	}

}
