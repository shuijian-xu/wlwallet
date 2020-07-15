package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class C8E4 {

	public static void main(String[] args) {
		
		String txStr = "0100000001868278ed6ddfb6c1ed3ad5f8181eb0c7a385aa0836f01d5e4789e6bd304d87221a000000db00483045022100dc92"
				+ "655fe37036f47756db8102e0d7d5e28b3beb83a8fef4f5dc0559bddfb94e02205a36d4e4e6c7fcd16658c50783e00c3416099"
				+ "77aed3ad00937bf4ee942a8993701483045022100da6bee3c93766232079a01639d07fa869598749729ae323eab8eef"
				+ "53577d611b02207bef15429dcadce2121ea07f233115c6f09034c0be68db99980b9a6c5e75402201475221"
				+ "022626e955ea6ea6d98850c994f9107b036b1334f18ca8830bfff1295d21cfdb702103b287eaf12"
				+ "2eea69030a0e9feed096bed8045c8b98bec453e1ffac7fbdbd4bb7152aeffffffff04d3b"
				+ "11400000000001976a914904a49878c0adfc3aa05de7afad2cc15f483a56a88ac7"
				+ "f400900000000001976a914418327e3f3dda4cf5b9089325a4b95abdfa03"
				+ "34088ac722c0c00000000001976a914ba35042cfe9fc66fd35ac"
				+ "2224eebdafd1028ad2788acdc4ace020000000017a9147"
				+ "4d691da1574e6b3c192ecfb52cc8984ee7b6c56"
				+ "8700000000";
		BigInteger txBigI= new BigInteger(txStr, 16);
		
		String secStr = "03b287eaf122eea69030a0e9feed096bed8045c8b98bec453e1ffac7fbdbd4bb71";
		BigInteger secBigI = new BigInteger(secStr, 16);
		
		String derStr = "3045022100da6bee3c93766232079a01639d07fa869598749729ae323eab8eef"
				+ "53577d611b02207bef15429dcadce2121ea07f233115c6f09034c0be68db99980b9a6c5e754022";
		BigInteger derBigI = new BigInteger(derStr, 16);
		
		String redeemScriptStr = "475221022626e955ea6ea6d98850c994f9107b036b1334f18ca8830b"
				+ "fff1295d21cfdb702103b287eaf122eea69030a0e9feed09"
				+ "6bed8045c8b98bec453e1ffac7fbdbd4bb7152ae";
		BigInteger redeemScriptBigI = new BigInteger(redeemScriptStr, 16);
		
		
		byte[] txBin = Utils.getBytes(txBigI);
		
		byte[] sec = Utils.getBytes(secBigI);
		byte[] der = Utils.getBytes(derBigI);
		
		byte[] redeemScriptBin = Utils.getBytes(redeemScriptBigI);
		ByteArrayInputStream bais = new ByteArrayInputStream(redeemScriptBin);
		
		Script redeemScript = Script.parse(bais);
		
		ByteArrayInputStream bais2 = new ByteArrayInputStream(txBin);
		Tx txObj = Tx.parse(bais2);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		
		byte[] versionBin = Utils.biginteger_to_little_endian(txObj.version, 4);
		try {
			baos.write(versionBin);
			byte[] txInSizeBin = Utils.encodeVarint(BigInteger.valueOf(txObj.txIns.size()));
			baos.write(txInSizeBin);
			
			TxIn i = txObj.txIns.get(0);
			byte[] txSerialize = new TxIn(i.prevTx, i.prevIndex, redeemScript, i.sequence).serialize();
			baos.write(txSerialize);
			
			byte[] txOutSizeBin = Utils.encodeVarint(BigInteger.valueOf(txObj.txOuts.size()));
			baos.write(txOutSizeBin);
			
			for(TxOut txOut: txObj.txOuts){
				byte[] txOutBin = txOut.serialize();
				baos.write(txOutBin);
			}
			
			byte[] lockTimeBin = Utils.biginteger_to_little_endian(txObj.locktime, 4);
			baos.write(lockTimeBin);
			
			byte[] sigHashAllBin = Utils.biginteger_to_little_endian(BigInteger.ONE, 4);
			baos.write(sigHashAllBin);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte[] s = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BigInteger z = new BigInteger(1, Utils.hash256(s));
		S256Point point = S256Point.parse(sec);
		Signature sig = Signature.parse(der);
		System.out.println(point.verify(z, sig));
		
	}

}
