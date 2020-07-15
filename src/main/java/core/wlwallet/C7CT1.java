package core.wlwallet;

import java.math.BigInteger;
import java.util.ArrayList;

public class C7CT1 {

	public static void main(String[] args) {
		BigInteger prevTx = new BigInteger("0d6fe5213c0b3291f208cba8bfb59b7476dffacc4e5cb66f6eb20a080843a299",16);
		BigInteger prevIndex = BigInteger.valueOf(13);
		
		TxIn txIn = new TxIn(prevTx, prevIndex);
		ArrayList<TxIn> txIns = new ArrayList<TxIn>();
		txIns.add(txIn);
		
		
		
		BigInteger changeAmount = BigInteger.valueOf((long) (0.33*100000000));
		
		byte[] changeH160 = Utils.decodeBase58("mzx5YhAH9kNHtcN481u6WkjeHjYtVeKVh2");
		
		Script changeScript = new Script().p2pkhScript(changeH160);
		
		TxOut changeOutput = new TxOut(changeAmount, changeScript);
		
		BigInteger targetAmount = BigInteger.valueOf((long) (0.1*100000000));
		
		byte[] targetH160 = Utils.decodeBase58("mnrVtF8DWjMu839VW3rBfgYaAfKk8983Xf");
		Script targetScript = new Script().p2pkhScript(targetH160);
		
		TxOut targetOutput = new TxOut(targetAmount, targetScript);
		
		ArrayList<TxOut> txOuts = new ArrayList<TxOut>();
		txOuts.add(changeOutput);
		txOuts.add(targetOutput);
		
		Tx obj = new Tx(BigInteger.ONE, txIns, txOuts, BigInteger.ZERO, true);
		
		System.out.println(obj);
		
	}

}
