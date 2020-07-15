package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Stack;

import org.bouncycastle.util.Arrays;

public class Tx extends Message {

	public byte[] command = new byte[] { 't', 'x' };

	public BigInteger version;
	public ArrayList<TxIn> txIns;
	public ArrayList<TxOut> txOuts;
	public BigInteger locktime;
	public boolean testnet;
	public boolean segwit;
	public byte[] hashPrevOuts;
	public byte[] hashSequence;
	public byte[] hashOutputs;

	public Tx() {
	}

	public Tx(BigInteger version, ArrayList<TxIn> txIns,
			ArrayList<TxOut> txOuts, BigInteger locktime, boolean testnet) {
		this(version, txIns, txOuts, locktime, testnet, false);
	}

	public Tx(BigInteger version, ArrayList<TxIn> txIns,
			ArrayList<TxOut> txOuts, BigInteger locktime, boolean testnet,
			boolean segwit) {
		super();
		this.version = version;
		this.txIns = txIns;
		this.txOuts = txOuts;
		this.locktime = locktime;
		this.testnet = testnet;
		this.segwit = segwit;
		this.hashPrevOuts = null;
		this.hashSequence = null;
		this.hashOutputs = null;
	}

	public BigInteger id() {
		return new BigInteger(1, this.hash());
	}

	public byte[] hash() {
		byte[] b = this.serialize_legacy();
		byte[] hash256 = Utils.hash256(b);
		return Arrays.reverse(hash256);
	}

	public static Tx parse(ByteArrayInputStream s) {
		boolean testnet = false;
		return Tx.parse(s, testnet);
	}

	public static Tx parse(ByteArrayInputStream s, boolean testnet) {

		byte[] version_bin = new byte[4];
		try {
			s.read(version_bin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BigInteger version = Utils.little_endian_to_biginteger(version_bin);

		BigInteger numInputs = Utils.readVarint(s);
		ArrayList<TxIn> txIns = new ArrayList<TxIn>();
		while (numInputs.compareTo(BigInteger.ZERO) > 0) {
			txIns.add(TxIn.parse(s));
			numInputs = numInputs.subtract(BigInteger.ONE);
		}

		BigInteger numOutputs = Utils.readVarint(s);
		ArrayList<TxOut> txOuts = new ArrayList<TxOut>();
		while (numOutputs.compareTo(BigInteger.ZERO) > 0) {
			txOuts.add(TxOut.parse(s));
			numOutputs = numOutputs.subtract(BigInteger.ONE);
		}

		byte[] locktime_bin = new byte[4];
		try {
			s.read(locktime_bin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BigInteger locktime = Utils.little_endian_to_biginteger(locktime_bin);

		return new Tx(version, txIns, txOuts, locktime, testnet);

	}

	public static Tx parse(DataInputStream s, boolean testnet) {

		try {
			s.mark(5);
			byte[] b = new byte[4];
			s.read(b, 0, b.length);
			byte[] flag = new byte[1];
			s.read(flag, 0, flag.length);
			if (flag[0] == (byte) (0x00)) {
				s.reset();
				return Tx.parse_segwit(s, testnet);
			} else {
				s.reset();
				return Tx.parse_legacy(s, testnet);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return null;

	}

	public static Tx parse_legacy(DataInputStream s, boolean testnet) {

		byte[] version_bin = new byte[4];
		try {
			s.read(version_bin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BigInteger version = Utils.little_endian_to_biginteger(version_bin);

		BigInteger numInputs = Utils.readVarint(s);
		ArrayList<TxIn> txIns = new ArrayList<TxIn>();
		while (numInputs.compareTo(BigInteger.ZERO) > 0) {
			txIns.add(TxIn.parse(s));
			numInputs = numInputs.subtract(BigInteger.ONE);
		}

		BigInteger numOutputs = Utils.readVarint(s);
		ArrayList<TxOut> txOuts = new ArrayList<TxOut>();
		while (numOutputs.compareTo(BigInteger.ZERO) > 0) {
			txOuts.add(TxOut.parse(s));
			numOutputs = numOutputs.subtract(BigInteger.ONE);
		}

		byte[] locktime_bin = new byte[4];
		try {
			s.read(locktime_bin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BigInteger locktime = Utils.little_endian_to_biginteger(locktime_bin);

		return new Tx(version, txIns, txOuts, locktime, testnet);

	}

	public static Tx parse_segwit(DataInputStream s, boolean testnet) {

		byte[] version_bin = new byte[4];
		try {
			s.read(version_bin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BigInteger version = Utils.little_endian_to_biginteger(version_bin);

		try {
			byte[] markerBin = new byte[2];
			s.read(markerBin, 0, markerBin.length);
			if (!(markerBin[0] == 0x00 && markerBin[2] == 0x01)) {
				System.out.println("Not a segwit transaction.");
				System.exit(1);
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		BigInteger numInputs = Utils.readVarint(s);
		ArrayList<TxIn> txIns = new ArrayList<TxIn>();
		while (numInputs.compareTo(BigInteger.ZERO) > 0) {
			txIns.add(TxIn.parse(s));
			numInputs = numInputs.subtract(BigInteger.ONE);
		}

		BigInteger numOutputs = Utils.readVarint(s);
		ArrayList<TxOut> txOuts = new ArrayList<TxOut>();
		while (numOutputs.compareTo(BigInteger.ZERO) > 0) {
			txOuts.add(TxOut.parse(s));
			numOutputs = numOutputs.subtract(BigInteger.ONE);
		}

		for (TxIn txIn : txIns) {
			BigInteger numItems = Utils.readVarint(s);
			ArrayList<byte[]> items = new ArrayList<byte[]>();
			for (int i = 0; i < numItems.intValue(); i++) {

				BigInteger itemLen = Utils.readVarint(s);

				if (itemLen.equals(BigInteger.ZERO)) {
					items.add(new byte[] { 0x00 });
				} else {
					byte[] item = new byte[itemLen.intValue()];
					try {
						s.read(item, 0, item.length);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			txIn.witness = items;
		}

		byte[] locktime_bin = new byte[4];
		try {
			s.read(locktime_bin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BigInteger locktime = Utils.little_endian_to_biginteger(locktime_bin);

		return new Tx(version, txIns, txOuts, locktime, testnet, true);

	}

	public byte[] serialize() {
		if (this.segwit) {
			return this.serialize_segwit();
		} else {
			return this.serialize_legacy();
		}
	}

	public byte[] serialize_legacy() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b1 = Utils.biginteger_to_little_endian(this.version, 4);
		byte[] result = null;
		try {
			baos.write(b1);
			int txInsize = this.txIns.size();
			byte[] b2 = Utils.encodeVarint(BigInteger.valueOf(txInsize));
			baos.write(b2);
			for (TxIn txIn : txIns) {
				baos.write(txIn.serialize());
			}
			int txOutsize = this.txOuts.size();
			byte[] b3 = Utils.encodeVarint(BigInteger.valueOf(txOutsize));
			baos.write(b3);
			for (TxOut txOut : txOuts) {
				baos.write(txOut.serialize());
			}
			baos.write(Utils.biginteger_to_little_endian(this.locktime, 4));
			result = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public byte[] serialize_segwit() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b1 = Utils.biginteger_to_little_endian(this.version, 4);
		byte[] result = null;
		try {
			baos.write(b1);
			baos.write(new byte[] { (byte) 0x00, (byte) 0x01 });
			int txInsize = this.txIns.size();
			byte[] b2 = Utils.encodeVarint(BigInteger.valueOf(txInsize));
			baos.write(b2);
			for (TxIn txIn : txIns) {
				baos.write(txIn.serialize());
			}
			int txOutsize = this.txOuts.size();
			byte[] b3 = Utils.encodeVarint(BigInteger.valueOf(txOutsize));
			baos.write(b3);
			for (TxOut txOut : txOuts) {
				baos.write(txOut.serialize());
			}
			for (TxIn txIn : this.txIns) {
				ArrayList<byte[]> list = txIn.witness;
				int length = list.size();
				byte[] lengthBin = Utils.biginteger_to_little_endian(
						BigInteger.valueOf(length), 1);
				baos.write(lengthBin);
				for (byte[] item : list) {
					if (item.length == 1) {
						baos.write(Utils.biginteger_to_little_endian(
								new BigInteger(1, item), 1));
					} else {
						byte[] lenBin = Utils.encodeVarint(BigInteger
								.valueOf(item.length));
						baos.write(lenBin);
						baos.write(item);
					}
				}
			}
			baos.write(Utils.biginteger_to_little_endian(this.locktime, 4));
			result = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public BigInteger fee(boolean testnet) {
		BigInteger inputSum = BigInteger.ZERO;
		BigInteger outputSum = BigInteger.ZERO;
		for (TxIn txIn : this.txIns) {
			inputSum = inputSum.add(txIn.value(testnet));
		}
		for (TxOut txOut : this.txOuts) {
			outputSum = outputSum.add(txOut.amount);
		}
		return inputSum.subtract(outputSum);
	}

	public BigInteger signHash(BigInteger inputIndex) {
		Script redeemScript = null;
		return this.signHash(inputIndex, redeemScript);
	}

	public BigInteger signHash(BigInteger inputIndex, Script redeemScript) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] version = Utils.biginteger_to_little_endian(this.version, 4);

		try {

			baos.write(version);
			ArrayList<TxIn> txIns = this.txIns;
			int txInSize = txIns.size();
			byte[] txInSizeBin = Utils.encodeVarint(BigInteger
					.valueOf(txInSize));
			baos.write(txInSizeBin);

			BigInteger j = BigInteger.ZERO;

			for (int i = 0; i < txIns.size(); i++) {
				TxIn txIn = txIns.get(i);
				if (j.equals(inputIndex)) {
					Script scriptSig;
					if (!(redeemScript == null)) {
						scriptSig = redeemScript;
						// new TxIn(txIn.prevTx,txIn.prevIndex, redeemScript,
						// txIn.sequence);
					} else {
						scriptSig = txIn.scriptPubkey(this.testnet);
						// new TxIn(txIn.prevTx,txIn.prevIndex,
						// txIn.scriptPubkey(this.testnet), txIn.sequence);
					}
					baos.write(new TxIn(txIn.prevTx, txIn.prevIndex, scriptSig,
							txIn.sequence).serialize());
				} else {
					baos.write(new TxIn(txIn.prevTx, txIn.prevIndex, null,
							txIn.sequence).serialize());
				}
				j = j.add(BigInteger.ONE);
			}
			ArrayList<TxOut> txOuts = this.txOuts;
			byte[] txOutSize = Utils.encodeVarint(BigInteger.valueOf(txOuts
					.size()));
			baos.write(txOutSize);

			for (int i = 0; i < txOuts.size(); i++) {
				TxOut txOut = txOuts.get(i);
				baos.write(txOut.serialize());
			}

			baos.write(Utils.biginteger_to_little_endian(this.locktime, 4));
			baos.write(Utils.biginteger_to_little_endian(BigInteger.ONE, 4));
			byte[] s = baos.toByteArray();
			baos.close();
			byte[] h256 = Utils.hash256(s);
			return new BigInteger(1, h256);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] hashPrevOuts() {
		if (this.hashPrevOuts == null) {
			byte[] allPrevOuts = null;
			byte[] allSequence = null;
			ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
			ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
			for (TxIn txIn : this.txIns) {
				byte[] prevTxBin = Utils.getBytes(txIn.prevTx,
						BigInteger.valueOf(32));
				byte[] sequenceBin = Utils.biginteger_to_little_endian(
						txIn.sequence, 4);
				try {
					baos1.write(Arrays.reverse(prevTxBin));
					baos1.write(Utils.biginteger_to_little_endian(
							txIn.prevIndex, 4));
					baos2.write(sequenceBin);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			allPrevOuts = baos1.toByteArray();
			allSequence = baos2.toByteArray();
			try {
				baos1.close();
				baos2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.hashPrevOuts = Utils.hash256(allPrevOuts);
			this.hashSequence = Utils.hash256(allSequence);
		}
		return this.hashPrevOuts;
	}

	public byte[] hashSequence() {
		if (this.hashSequence == null) {
			this.hashPrevOuts();
		}
		return this.hashSequence;
	}

	public byte[] hashOutputs() {
		if (this.hashOutputs == null) {
			byte[] allOutputs = null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (TxOut txOut : this.txOuts) {
				byte[] txOutBin = txOut.serialize();
				try {
					baos.write(txOutBin);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			allOutputs = baos.toByteArray();
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.hashOutputs = Utils.hash256(allOutputs);
		}
		return this.hashOutputs;
	}

	public BigInteger signHashBIP143(BigInteger inputIndex,
			Script redeemScript, Script witnessScript) {
		byte[] s = null;
		TxIn txIn = this.txIns.get(inputIndex.intValue());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] version = Utils.biginteger_to_little_endian(this.version, 4);
		try {
			baos.write(version);
			baos.write(this.hashPrevOuts);
			baos.write(this.hashSequence);
			baos.write(Arrays.reverse(Utils.getBytes(txIn.prevTx,
					BigInteger.valueOf(32))));
			baos.write(Utils.biginteger_to_little_endian(txIn.prevIndex, 4));
			byte[] scriptCode = null;
			if (!(witnessScript == null)) {
				scriptCode = witnessScript.serialize();
			} else if (!(redeemScript == null)) {
				scriptCode = Script.p2pkhScript(
						(byte[]) redeemScript.cmds.get(1)).serialize();
			} else {
				scriptCode = Script.p2pkhScript(
						(byte[]) txIn.scriptPubkey(this.testnet).cmds.get(1))
						.serialize();
			}
			baos.write(scriptCode);
			baos.write(Utils.biginteger_to_little_endian(txIn.value(testnet), 8));
			baos.write(Utils.biginteger_to_little_endian(txIn.sequence, 4));
			baos.write(this.hashOutputs());
			baos.write(Utils.biginteger_to_little_endian(this.locktime, 4));
			baos.write(Utils.biginteger_to_little_endian(BigInteger.ONE, 4));
			s=baos.toByteArray();
			baos.close();
			byte[] h256 = Utils.hash256(s);
			BigInteger sBigInt = new BigInteger(1,h256);
			return sBigInt;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean verifyInput(BigInteger index) {
		TxIn txIn = this.txIns.get(index.intValue());
		Script scriptPubkey = txIn.scriptPubkey(this.testnet);
		Script redeemScript;
		BigInteger z = null;
		ArrayList<byte[]> witness = null;
		if (scriptPubkey.isP2shScriptPubkey()) {
			Stack cmds = txIn.scriptSig.cmds;
			byte[] cmd = (byte[]) cmds.get(cmds.size() - 1);
			byte[] rawRedeem = Arrays.concatenate(
					Utils.encodeVarint(BigInteger.valueOf(cmd.length)), cmd);
			ByteArrayInputStream s = new ByteArrayInputStream(rawRedeem);
			redeemScript = Script.parse(s);
			if (redeemScript.isP2wpkhScriptPubkey()) {
				z = this.signHashBIP143(index, redeemScript, null);
				witness = txIn.witness;
			}else if(redeemScript.isP2wshScriptPubkey()){
				ArrayList<byte[]> witnessTmp = txIn.witness;
				int witnessLen = witnessTmp.size();
				byte[] command = witnessTmp.get(witnessLen-1);
				byte[] commandLenBin = Utils.encodeVarint(BigInteger.valueOf(command.length));
				byte[] rawWitness = Arrays.concatenate(commandLenBin, command);
				ByteArrayInputStream bais = new ByteArrayInputStream(rawWitness);
				Script witnessScript = Script.parse(bais);
				z=this.signHashBIP143(index, null, witnessScript);
				witness = txIn.witness;
			}else{
				z = this.signHash(index,redeemScript);
				witness = null;
			}
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if(scriptPubkey.isP2wpkhScriptPubkey()){
				z = this.signHashBIP143(index, null, null);
				witness = txIn.witness;
			}else if(scriptPubkey.isP2wshScriptPubkey()){
				ArrayList<byte[]> witnessTmp = txIn.witness;
				int witnessLen = witnessTmp.size();
				byte[] command = witnessTmp.get(witnessLen-1);
				byte[] commandLenBin = Utils.encodeVarint(BigInteger.valueOf(command.length));
				byte[] rawWitness = Arrays.concatenate(commandLenBin, command);
				ByteArrayInputStream bais = new ByteArrayInputStream(rawWitness);
				Script witnessScript = Script.parse(bais);
				z=this.signHashBIP143(index, null, witnessScript);
				witness = txIn.witness;
			}else{
				z = this.signHash(index);
				witness = null;
			}
			redeemScript = null;
		}
		Script combined = txIn.scriptSig.add(txIn.scriptPubkey(this.testnet));
		return combined.evaluate(z, witness);
	}

	public boolean verify() {
		if (this.fee(true).compareTo(BigInteger.ZERO) < 0) {
			return false;
		}
		for (int i = 0; i < this.txIns.size(); i++) {
			if (!this.verifyInput(BigInteger.valueOf(i))) {
				return false;
			}
		}
		return true;
	}

	public boolean isCoinBaseTx() {
		if (this.txIns.size() != 1) {
			return false;
		}
		TxIn firstInput = this.txIns.get(0);
		if (!(firstInput.prevTx.equals(BigInteger.ZERO))) {
			return false;
		}
		if (!(firstInput.prevIndex.equals(new BigInteger("ffffffff", 16)))) {
			return false;
		}
		return true;
	}

	public BigInteger coinbaseHeight() {

		if (!this.isCoinBaseTx()) {
			return null;
		}
		byte[] heightBin = (byte[]) this.txIns.get(0).scriptSig.cmds.get(0);
		BigInteger element = new BigInteger(1, heightBin);
		return element;

	}

	public boolean signInput(BigInteger inputIndex, PrivateKey privateKey) {
		BigInteger z = this.signHash(inputIndex);
		byte[] der = privateKey.sign(z).der();
		byte[] sig = Arrays.concatenate(der, new byte[] { 0x01 });
		byte[] sec = privateKey.point.sec(this.testnet);
		Stack st = new Stack();
		st.add(sig);
		st.add(sec);
		Script scriptSig = new Script(st);
		this.txIns.get(inputIndex.intValue()).scriptSig = scriptSig;
		return this.verifyInput(inputIndex);
	}

	@Override
	public String toString() {
		return "Tx [txId=" + String.format("%064x", this.id()) + ", version="
				+ version + ", txIns=" + txIns + ", txOuts=" + txOuts
				+ ", locktime=" + locktime.toString(10) + "]";

	}

	@Override
	public byte[] getCommand() {
		return this.command;
	}
}
