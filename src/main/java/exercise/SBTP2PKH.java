package exercise;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Stack;

import org.bouncycastle.util.Arrays;

import core.wlwallet.PrivateKey;
import core.wlwallet.S256Point;
import core.wlwallet.Script;
import core.wlwallet.Signature;
import core.wlwallet.Tx;
import core.wlwallet.TxIn;
import core.wlwallet.TxOut;
import core.wlwallet.Utils;

public class SBTP2PKH {

	public static void main(String[] args) {
		
		String addressf = "n4Tq6UkcMJUEdnPC1NhvuAgqAwNe1HZH58";
		String addresst= "mwC8oLY27zbPnhx53zPSVg19PaT9rsCCCU";
		
		
		BigInteger pretx = new BigInteger("1b4a5abaa5bcf328c7608d0689a94dd26edf5e96c02ee443c1a3dda1e7602f32",16);
		BigInteger index = BigInteger.ONE;
		TxIn txIn = new TxIn(pretx, index);
		ArrayList<TxIn> txins = new ArrayList<TxIn>();
		txins.add(txIn);
		
		BigInteger camount = BigInteger.valueOf((long) (0.00005*100000000));
		// 1. get prefix -> 2. sec pubkey h160 -> 3. prefix + h160 -> 4. hash256 of (prefix + h160) and get the first 4 bytes -> 
		// 5. combine #3 & #4 and encode it in base58 
		byte[] h160 = Utils.decodeBase58(addressf);
//		byte[] h160 = Arrays.copyOfRange(b, 1, b.length-4);
		Stack s = new Stack();
		s.push(new byte[]{0x76});
		s.push(new byte[]{(byte) 0xa9});
		s.push(h160);
		s.push(new byte[]{(byte) 0x88});
		s.push(new byte[]{(byte) 0xac});
		Script scriptPubkey = new Script(s);
		TxOut txoutc = new TxOut(camount, scriptPubkey);
		
		
		BigInteger tamount = BigInteger.valueOf((long) (0.00013*100000000));
		h160 = Utils.decodeBase58(addresst);
//		h160 = Arrays.copyOfRange(b, 1, b.length-4);
		s = new Stack();
		s.push(new byte[]{0x76});
		s.push(new byte[]{(byte) 0xa9});
		s.push(h160);
		s.push(new byte[]{(byte) 0x88});
		s.push(new byte[]{(byte) 0xac});
		scriptPubkey = new Script(s);
		TxOut txoutt = new TxOut(tamount, scriptPubkey);
		
		ArrayList<TxOut> txouts = new ArrayList<TxOut>();
		txouts.add(txoutc);
		txouts.add(txoutt);
		
		Tx raw = new Tx(BigInteger.ONE, txins, txouts,BigInteger.ZERO,true);
		byte[] txb = raw.serialize();
		System.out.println(String.format("%0"+txb.length*2+"x", new BigInteger(1,txb)));
		
		BigInteger z = raw.signHash(BigInteger.ZERO);
		
		String pks;
		byte[] pksb;
		BigInteger secret;
		PrivateKey priv;
		pks = "ShuiJin Xu secret";
		pksb = pks.getBytes();
		secret = Utils.little_endian_to_biginteger(Utils
				.hash256(pksb));
		priv = new PrivateKey(secret);
		
		S256Point point = priv.point;
		
		System.out.println(point.address(true));
		
		Signature signature = priv.sign(z);
		System.out.println(point.verify(z, signature));
		
		byte[] sigder = signature.der();
		byte[] sig = Arrays.concatenate(sigder, new byte[]{1});
		byte[] sec = point.sec(true);
		
		Stack cmd = new Stack();
		cmd.push(sig);
		cmd.push(sec);
		Script scriptSig = new Script(cmd);
		raw.txIns.get(0).scriptSig=scriptSig;
		
		txb = raw.serialize();
		
		System.out.println(String.format("%0"+txb.length*2+"x", new BigInteger(1,txb)));

	}

}
