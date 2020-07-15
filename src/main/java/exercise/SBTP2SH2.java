package exercise;

import java.io.ByteArrayInputStream;
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

public class SBTP2SH2 {

	public static void main(String[] args) {
		
		String addc = "2NFcsKJNr8b3xmv9z1ncsQY1sqfWyCU7ey3";
		String addt = "mwC8oLY27zbPnhx53zPSVg19PaT9rsCCCU";
		
		String preTxStr = "f0029ccc6293b4e027138a40d5c7c5d53a3cbe0c8a0e4954dde4ef189f94a6c0";
		BigInteger preTx = new BigInteger(preTxStr, 16);
		BigInteger index = BigInteger.ZERO;
		
		TxIn txin = new TxIn(preTx, index);
		ArrayList<TxIn> txins = new ArrayList<TxIn>();
		txins.add(txin);
		
		BigInteger amountc = BigInteger.valueOf((long) (0.000008*100000000));
		Stack cmdsc = new Stack();
		cmdsc.push(new byte[]{(byte) 0xa9});
		cmdsc.push(Utils.decodeBase58(addc));
		cmdsc.push(new byte[]{(byte) 0x87});
		Script scriptPubkeyC = new Script(cmdsc);
		TxOut txoutc = new TxOut(amountc, scriptPubkeyC);
		
		BigInteger amountt = BigInteger.valueOf((long) (0.000002*100000000));
		Stack cmdst = new Stack();
		cmdst.push(new byte[]{0x76});
		cmdst.push(new byte[]{(byte) 0xa9});
		cmdst.push(Utils.decodeBase58(addt));
		cmdst.push(new byte[]{(byte) 0x88});
		cmdst.push(new byte[]{(byte) 0xac});
		Script scriptPubkeyT = new Script(cmdst);
		TxOut txoutt = new TxOut(amountt, scriptPubkeyT);
		
		ArrayList<TxOut> txouts = new ArrayList<TxOut>();
		txouts.add(txoutc);
		txouts.add(txoutt);
		
		Tx tx = new Tx(BigInteger.ONE, txins, txouts, BigInteger.ZERO, true);
		
		byte[] txb = tx.serialize();
		System.out.println(String.format("%0"+txb.length*2+"x", new BigInteger(1,txb)));
		
		
		String pks;
		byte[] pksb;
		BigInteger secret;
		PrivateKey priv;
		
		/*pks = "shuijian.xu@outlook.com address 1";
		pksb = pks.getBytes();
		secret = Utils.little_endian_to_biginteger(Utils
				.hash256(pksb));
		priv = new PrivateKey(secret);*/
		
		pks = "shuijian.xu@outlook.com address 2";
		pksb = pks.getBytes();
		secret = Utils.little_endian_to_biginteger(Utils
				.hash256(pksb));
		PrivateKey priv2 = new PrivateKey(secret);
		S256Point point2 = priv2.point;
		byte[] secPubkey2 = point2.sec(true);// user need to provide
//		System.out.println(point2.address(true, true));
		
		pks = "shuijian.xu@outlook.com address 3";
		pksb = pks.getBytes();
		secret = Utils.little_endian_to_biginteger(Utils
				.hash256(pksb));
		PrivateKey priv3 = new PrivateKey(secret);
		S256Point point3 = priv3.point; 
		byte[] secPubkey3 = point3.sec(true); // user need to provide
//		System.out.println(point3.address(true, true));
		
		
		Stack cmdsr = new Stack();
		cmdsr.push(new byte[]{0x52});
		cmdsr.push(secPubkey2);
		cmdsr.push(secPubkey3);
		cmdsr.push(new byte[]{0x52});
		cmdsr.push(new byte[]{(byte) 0xae});
		Script redeemScript = new Script(cmdsr);
		
		BigInteger z = tx.signHash(BigInteger.ZERO, redeemScript);
		
		Signature signature2 = priv2.sign(z);
		byte[] sigDer2 = Arrays.concatenate(signature2.der(), new byte[]{1});
		Signature signature3 = priv3.sign(z);
		byte[] sigDer3 = Arrays.concatenate(signature3.der(), new byte[]{1});
		
		Stack cmdssig = new Stack();
		cmdssig.push(new byte[]{0x00});
		cmdssig.push(sigDer2);
		cmdssig.push(sigDer3);
		cmdssig.push(redeemScript.raw_serialize());
		
		Script scriptSig = new Script(cmdssig);
		
		tx.txIns.get(0).scriptSig=scriptSig;
		
		txb = tx.serialize();
		System.out.println(String.format("%0"+txb.length*2+"x", new BigInteger(1,txb)));
		
		System.out.println(tx.verifyInput(BigInteger.ZERO));
		
	}

}
