package core.wlwallet;

import java.math.BigInteger;

public class C7T1 {

	public static void main(String[] args) {
		
		String secStr = "0349fc4e631e3624a545de3f89f5d8684c7b8138bd94bdd531d2e213bf016b278a";
		BigInteger secB = new BigInteger(secStr, 16);
		String derStr = "3045022100ed81ff192e75a3fd2304004dcadb746fa5e24c5031ccfcf21320b0277457c98f02207a986d955c6e0cb35d446a89d3f56100f4d7f67801c31967743a9c8e10615bed";
		BigInteger derB = new BigInteger(derStr, 16);
		String zStr = "27e0c5994dec7824e56dec6b2fcb342eb7cdb0d0957c2fce9882f715e85d81a6";
		BigInteger z = new BigInteger(zStr, 16);
		
		byte[] sec = Utils.getBytes(secB);
		byte[] der = Utils.getBytes(derB);
		
		S256Point point = S256Point.parse(sec);
		Signature signature = Signature.parse(der);
		
		System.out.println(point.verify(z, signature));
	
	}

}
