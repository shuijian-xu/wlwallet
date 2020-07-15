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

public class SBTP2SH {

	public static void main(String[] args) {
		
		BigInteger version = BigInteger.ONE;
		
		String preTxStr = "c04b57c2e5ab08455926b9940207e1dda4631460e2e35355e7ce9e0ed122c427";
		BigInteger preTx = new BigInteger(preTxStr, 16);
		BigInteger index = BigInteger.ONE;
		TxIn txin = new TxIn(preTx, index);
		
		ArrayList<TxIn> txins = new ArrayList<TxIn>();
		txins.add(txin);
		
		BigInteger amountt = BigInteger.valueOf((long) (0.000015*100000000));
//		String addrTo="2MtLr8sEYadyuhdsQtDyPKAhUw6farWuQBw";
		String addrTo="2NFcsKJNr8b3xmv9z1ncsQY1sqfWyCU7ey3";
		
		byte[] addrToH160 = Utils.decodeBase58(addrTo);
		Stack cmds = new Stack();
		cmds.push(new byte[]{(byte) 0xa9});
		cmds.push(addrToH160);
		cmds.push(new byte[]{(byte) 0x87});
		Script scriptPubkey = new Script(cmds);
		TxOut txout = new TxOut(amountt, scriptPubkey);
		
		BigInteger amountc = BigInteger.valueOf((long) (0.00004*100000000));
		String addrC="mwC8oLY27zbPnhx53zPSVg19PaT9rsCCCU";
		byte[] addrToH160C = Utils.decodeBase58(addrC);
		
		Stack cmdsc = new Stack();
		cmdsc.push(new byte[]{(byte) 0x76});
		cmdsc.push(new byte[]{(byte) 0xa9});
		cmdsc.push(addrToH160C);
		cmdsc.push(new byte[]{(byte) 0x88});
		cmdsc.push(new byte[]{(byte) 0xac});
		Script scriptPubkeyC = new Script(cmdsc);
		TxOut txoutc = new TxOut(amountt, scriptPubkeyC);
		
		ArrayList<TxOut> txouts = new ArrayList<TxOut>();
		txouts.add(txout);
		txouts.add(txoutc);
		
		BigInteger locktime = BigInteger.ZERO;
		
		Tx tx = new Tx(version, txins, txouts, locktime, true);
		
		byte[] txb = tx.serialize();
		System.out.println(String.format("%0"+txb.length*2+"x", new BigInteger(1,txb)));
		
		BigInteger z = tx.signHash(BigInteger.ZERO);
		
		String pks;
		byte[] pksb;
		BigInteger secret;
		PrivateKey priv;
		pks = "shuijian.xu@outlook.com address 1";
		pksb = pks.getBytes();
		secret = Utils.little_endian_to_biginteger(Utils
				.hash256(pksb));
		priv = new PrivateKey(secret);
		
		Signature signature = priv.sign(z);
		byte[] sig = Arrays.concatenate(signature.der(), new byte[]{1});
		byte[] sec = priv.point.sec(true);
		Stack cmdss = new Stack();
		cmdss.push(sig);
		cmdss.push(sec);
		Script scriptSig = new Script(cmdss);
		
		tx.txIns.get(0).scriptSig=scriptSig;
		
		txb = tx.serialize();
		System.out.println(String.format("%0"+txb.length*2+"x", new BigInteger(1,txb)));
		
		System.out.println(priv.point.verify(z, signature));
		
	}

}
