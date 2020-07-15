package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;

public class C5E5 {

	public static void main(String[] args) {
		String rawTx = "0100000001813f79011acb80925dfe69b3def355fe914bd1d96a3f5f71bf830"
				+ "3c6a989c7d1000000006b483045022100ed81ff192e75a3fd2304004dcadb746fa5e24c5031ccfcf21320b0"
				+ "277457c98f02207a986d955c6e0cb35d446a89d3f56100f4d7f67801c31967743a9c8e10615bed01210349fc4e631e"
				+ "3624a545de3f89f5d8684c7b8138bd94bdd531d2e213bf016b278afeffffff02a135ef01000000001976a914bc3b65"
				+ "4dca7e56b04dca18f2566cdaf02e8d9ada88ac99c39800000000001976a9141c4bc762dd5423e332166702cb75f4"
				+ "0df79fea1288ac19430600";
		BigInteger bigInteger = new BigInteger(rawTx, 16);
		
		ByteArrayInputStream stream = new ByteArrayInputStream(bigInteger.toByteArray());
		
		Tx obj = Tx.parse(stream, true);
		

	}

}
