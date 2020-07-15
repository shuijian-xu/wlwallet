package core.wlwallet;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Stack;

import org.bouncycastle.util.Arrays;

public class C7CT3 {

	public static void main(String[] args) {
		
		BigInteger prevTx = new BigInteger("830b00e369e66e18a6c739c1fec112bab53d25c8865180716555397c5522338b",16);
		BigInteger prevIndex = BigInteger.valueOf(0);
		
		TxIn txIn = new TxIn(prevTx, prevIndex);
		ArrayList<TxIn> txIns = new ArrayList<TxIn>();
		txIns.add(txIn);
		
		BigInteger changeAmount = BigInteger.valueOf((long) (0.003*100000000));
		
		byte[] changeH160 = Utils.decodeBase58("n4Tq6UkcMJUEdnPC1NhvuAgqAwNe1HZH58");
		
		Script changeScript = new Script().p2pkhScript(changeH160);
		
		TxOut changeOutput = new TxOut(changeAmount, changeScript);
		
		BigInteger targetAmount = BigInteger.valueOf((long) (0.006*100000000));
		
		byte[] targetH160 = Utils.decodeBase58("msDsANjvGjzjbWXq45eopZDhqjQbhoTaSs");
		Script targetScript = new Script().p2pkhScript(targetH160);
		
		TxOut targetOutput = new TxOut(targetAmount, targetScript);
		
		ArrayList<TxOut> txOuts = new ArrayList<TxOut>();
		txOuts.add(changeOutput);
		txOuts.add(targetOutput);
		
		Tx obj = new Tx(BigInteger.ONE, txIns, txOuts, BigInteger.ZERO, true);
		BigInteger z = obj.signHash(BigInteger.ZERO);
		System.out.println(z);
	
		PrivateKey privateKey = new PrivateKey(Utils.little_endian_to_biginteger(Utils.hash256("ShuiJin Xu secret".getBytes())));
		
		Signature signature = privateKey.sign(z);
		
		System.out.println(signature.r);
		System.out.println(signature.s);
		
		System.out.println();
		
		System.out.println(privateKey.secret);
		System.out.println(privateKey.point);
		
		byte[] der = signature.der();
		byte[] sig = Arrays.append(der, (byte)1);
		byte[] sec = privateKey.point.sec(true);
		Stack s = new Stack();
		s.push(sig);
		s.push(sec);
		Script scriptSig = new Script(s);
		obj.txIns.get(0).scriptSig=scriptSig;

		System.out.println(obj);
		
		byte[] tbin = obj.serialize();
		
		System.out.println(String.format("%0"+tbin.length*2+"x", new BigInteger(tbin)));
	}

}
