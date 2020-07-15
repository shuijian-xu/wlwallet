package core.wlwallet;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Stack;

import org.bouncycastle.util.Arrays;

public class MerkleBlock extends Message{
	
	public byte[] command = new byte[]{ 'm', 'e', 'r', 'k', 'l', 'e',
			'b', 'l', 'o', 'c','k' };
	
	BigInteger version;
	byte[] prevBlock;
	byte[] merkleRoot;
	BigInteger timestamp;
	byte[] bits;
	byte[] nonce;
	
	BigInteger total;
	ArrayList<byte[]> hashes;
	byte[] flags;
	
	public MerkleBlock() {}
	
	public MerkleBlock(BigInteger version, byte[] prevBlock,
			byte[] merkleRoot, BigInteger timestamp, byte[] bits,
			byte[] nonce, BigInteger total, ArrayList<byte[]> hashes,
			byte[] flags) {
		super();
		this.version = version;
		this.prevBlock = prevBlock;
		this.merkleRoot = merkleRoot;
		this.timestamp = timestamp;
		this.bits = bits;
		this.nonce = nonce;
		this.total = total;
		this.hashes = hashes;
		this.flags = flags;
	}

	public static MerkleBlock parse(DataInputStream s) {
		boolean testnet=false;
		return MerkleBlock.parse(s, testnet);
	}
	
	public static MerkleBlock parse(DataInputStream s, boolean testnet) {
		MerkleBlock merkleBlock=null;
		int n = 0;
		try {
			byte[] versionBin = new byte[4];
			n = s.read(versionBin,0,4);
			BigInteger version = Utils.little_endian_to_biginteger(versionBin);
			
			byte[] prevBlockBin = new byte[32];
			n = s.read(prevBlockBin,0,32);
			byte[] prevBlock = Arrays.reverse(prevBlockBin);
			
			byte[] merkleRootBin = new byte[32];
			n = s.read(merkleRootBin,0,32);
			byte[] merkleRoot = Arrays.reverse(merkleRootBin);
			
			byte[] timestampBin = new byte[4];
			n = s.read(timestampBin,0,4);
			BigInteger timestamp = Utils.little_endian_to_biginteger(timestampBin);
			
			byte[] bitsBin = new byte[4];
			n = s.read(bitsBin,0,4);
			byte[] bits = bitsBin;
			
			byte[] nonceBin = new byte[4];
			n = s.read(nonceBin,0,4);
			byte[] nonce = nonceBin;
			
			byte[] totalBin = new byte[4];
			n = s.read(totalBin,0,4);
			BigInteger total = Utils.little_endian_to_biginteger(totalBin);
			
			BigInteger numHashes = Utils.readVarint(s);
			ArrayList<byte[]> hashes = new ArrayList<byte[]>();
			for(int i=0;i<numHashes.intValue();i++){
				byte[] tmp = new byte[32];
				n = s.read(tmp,0,32);
				System.out.println(String.format("%064x", new BigInteger(1, tmp)));
				hashes.add(Arrays.reverse(tmp));
			}
			
			BigInteger flagsLength = Utils.readVarint(s);
			byte[] flagsBin = new byte[flagsLength.intValue()];
			n = s.read(flagsBin);
			byte[] flags = flagsBin;
			
			merkleBlock=new MerkleBlock(version, prevBlock, merkleRoot, timestamp, bits, nonce, total, hashes, flags);
			
			if(merkleBlock.total.equals(BigInteger.valueOf(756))){
				System.out.println(merkleBlock.total);
				System.out.println(merkleBlock.total);
				System.out.println(merkleBlock.total);
				System.out.println(merkleBlock.total);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return merkleBlock;
	}
	
	public boolean isValid(){
		
		byte[] flagBits = Utils.bytesToBitField(this.flags);
		Stack<byte[]> flagBitsStack = new Stack<byte[]>();
		for (int i = 0; i < flagBits.length; i++) {
			byte[] h = new byte[]{flagBits[i]};
			flagBitsStack.add(h);
		}
		
		
		ArrayList<byte[]>  tmp = this.hashes;
		Stack<byte[]> hashes = new Stack<byte[]>();
		for (int i = 0; i < tmp.size(); i++) {
			byte[] h = tmp.get(i);
			hashes.add(Arrays.reverse(h));
		}
		if(this.total.equals(BigInteger.valueOf(756))){//755
			System.out.println(this.total);
			System.out.println(this.total);
			System.out.println(this.total);
			System.out.println(this.total);
			for(byte[] b: this.hashes){
				System.out.println(String.format("%064x", new BigInteger(1, b)));
			}
			
		}
		
		System.out.println(this.total);
		System.out.println(this.total);
		MerkleTree2 merkleTree = new MerkleTree2(this.total.intValue());
		merkleTree.populateTree(flagBitsStack, hashes);
		System.out.println(merkleTree.total);
		System.out.println(merkleTree);
		
		BigInteger realMerkleRoot = new BigInteger(1, this.merkleRoot);
		BigInteger calculatedMerkleRoot = new BigInteger(1, Arrays.reverse(merkleTree.root()));
		return realMerkleRoot.equals(calculatedMerkleRoot);
		
	}

	@Override
	public byte[] serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getCommand() {
		// TODO Auto-generated method stub
		return this.command;
	}
}
