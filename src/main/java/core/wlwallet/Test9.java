package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;

public class Test9 {

	public static void main(String[] args) {
		BigInteger big = new BigInteger(
				"6b483045022100ed81ff192e75a3fd2304004dcadb746fa5e24c5031ccfcf21320b02"
				+ "77457c98f02207a986d955c6e0cb35d446a89d3f56100f4d7f"
				+ "67801c31967743a9c8e10615bed0121034"
				+ "9fc4e631e3624a545de3f89f"
				+ "5d8684c7b8138bd9"
				+ "4bdd531d2"
				+ "e213bf"
				+ "016b"
				+ "27"
				+ "8"
				+ "a", 16);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(big.toByteArray());
		Script s= Script.parse(bais);
		System.out.println(s);
	}

}
