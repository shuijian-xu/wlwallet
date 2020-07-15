package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;

public class Test12 {

	public static void main(String[] args) {
		String raw_tx_str = "0100000001813f79011acb80925dfe69b3def355fe914bd1d96a3f5f71bf8303c6a989c7d1000000006b4"
				+ "83045022100ed81ff192e75a3fd2304004dcadb746fa5e24c5031ccfcf21320b0277457c98f02207a986d955c"
				+ "6e0cb35d446a89d3f56100f4d7f67801c31967743a9c8e10615bed01210349fc4e631e3624a5"
				+ "45de3f89f5d8684c7b8138bd94bdd531d2e213bf016b278afeffffff02a135ef01000000"
				+ "001976a914bc3b654dca7e56b04dca18f2566cdaf02e8d9ada88ac99c398000000"
				+ "00001976a9141c4bc762dd5423e332166702cb75f40df79fea1288ac19430600";
		BigInteger raw_tx = new BigInteger(raw_tx_str, 16);
		String s = raw_tx.toString(16);
		ByteArrayInputStream stream = new ByteArrayInputStream(Utils.getBytes(raw_tx));
		Tx transaction = Tx.parse(stream, false);
		System.out.println(transaction);
	}

}
