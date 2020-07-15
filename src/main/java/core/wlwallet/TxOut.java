package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;

public class TxOut {
	
	public BigInteger amount;
	public Script scriptPubkey;
	
	public TxOut(BigInteger amount, Script scriptPubkey) {
		super();
		this.amount = amount;
		this.scriptPubkey = scriptPubkey;
	}
	
	public static TxOut parse(ByteArrayInputStream s){
		byte[] b1 = new byte[8];
		try {
			s.read(b1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BigInteger amount = Utils.little_endian_to_biginteger(b1);
		Script scriptPubkey = Script.parse(s);
		
		return new TxOut(amount, scriptPubkey);
	}
	
	public static TxOut parse(DataInputStream s){
		byte[] b1 = new byte[8];
		try {
			s.read(b1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BigInteger amount = Utils.little_endian_to_biginteger(b1);
		Script scriptPubkey = Script.parse(s);
		
		return new TxOut(amount, scriptPubkey);
	}
	
	public byte[] serialize(){
		byte[] b1 = Utils.biginteger_to_little_endian(this.amount, 8);
		byte[] b2 = this.scriptPubkey.serialize();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] result = null;
		try {
			baos.write(b1);
			baos.write(b2);
			result = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	

	@Override
	public String toString() {
		return "TxOut [amount=" + amount + ", scriptPubkey=" + scriptPubkey
				+ "]";
	}
	
}
