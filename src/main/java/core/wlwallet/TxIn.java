package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

public class TxIn {

	public BigInteger prevTx;
	public BigInteger prevIndex;
	public Script scriptSig;
	public BigInteger sequence;
	public ArrayList<byte[]> witness;
	
	public TxIn(BigInteger prevTx, BigInteger prevIndex){
		this(prevTx, prevIndex, null, null);
	}
	

	public TxIn(BigInteger prevTx, BigInteger prevIndex, Script scriptSig,
			BigInteger sequence) {

		super();
		this.prevTx = prevTx;
		this.prevIndex = prevIndex;

		if (scriptSig == null) {
			this.scriptSig = new Script();
		} else {
			this.scriptSig = scriptSig;
		}
		if(sequence == null){
			this.sequence= new BigInteger("ffffffff",16);
		}else{
			this.sequence = sequence;
		}
	}

	public static TxIn parse(ByteArrayInputStream s) {

		TxIn txIn = null;
		try {
			byte[] b1 = new byte[32];
			s.read(b1);
			BigInteger prevTx = Utils.little_endian_to_biginteger(b1);

			byte[] b2 = new byte[4];
			s.read(b2);
			BigInteger prevIndex = Utils.little_endian_to_biginteger(b2);

			Script scriptSig = Script.parse(s);

			byte[] b3 = new byte[4];
			s.read(b3);
			BigInteger sequence = Utils.little_endian_to_biginteger(b3);

			txIn = new TxIn(prevTx, prevIndex, scriptSig, sequence);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return txIn;
	}
	
	public static TxIn parse(DataInputStream s) {

		TxIn txIn = null;
		try {
			byte[] b1 = new byte[32];
			s.read(b1);
			BigInteger prevTx = Utils.little_endian_to_biginteger(b1);

			byte[] b2 = new byte[4];
			s.read(b2);
			BigInteger prevIndex = Utils.little_endian_to_biginteger(b2);

			Script scriptSig = Script.parse(s);

			byte[] b3 = new byte[4];
			s.read(b3);
			BigInteger sequence = Utils.little_endian_to_biginteger(b3);

			txIn = new TxIn(prevTx, prevIndex, scriptSig, sequence);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return txIn;
	}

	public byte[] serialize() {
		byte[] result = null;
		byte[] b1 = Utils.biginteger_to_little_endian(this.prevTx, 32);
		byte[] b2 = Utils.biginteger_to_little_endian(this.prevIndex, 4);
		byte[] b3 = this.scriptSig.serialize();
		byte[] b4 = Utils.biginteger_to_little_endian(this.sequence, 4);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			baos.write(b1);
			baos.write(b2);
			baos.write(b3);
			baos.write(b4);
			result = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Tx fetch_tx(boolean testnet) {
		try {
			return TxFetcher.fetch(this.prevTx, testnet, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public BigInteger value() {
		boolean testnet=false;
		return this.value(testnet);
	}
	
	public BigInteger value(boolean testnet) {
		return this.fetch_tx(testnet).txOuts.get(this.prevIndex.intValue()).amount;
	}

	public Script scriptPubkey(boolean testnet) {
		return this.fetch_tx(testnet).txOuts.get(this.prevIndex.intValue()).scriptPubkey;
	}

	
	@Override
	public String toString() {
		return "TxIn [prevTx=" + String.format("%064x", prevTx) + ", prevIndex=" + prevIndex
				+ ", scriptSig=" + scriptSig + ", sequence=" + String.format("%08x", sequence) + "]";
	}

}
