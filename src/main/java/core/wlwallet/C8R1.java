package core.wlwallet;

import java.math.BigInteger;

public class C8R1 {

	public static void main(String[] args) {
		String strTx = "0100000001868278ed6ddfb6c1ed3ad5f8181eb0c7a385aa0836f01d5e4789e6bd304d87221a000000475221022626e955ea6e"
				+ "a6d98850c994f9107b036b1334f18ca8830bfff1295d21cfdb702103b287eaf122eea69030a0e9feed096bed8045c8b98be"
				+ "c453e1ffac7fbdbd4bb7152aeffffffff04d3b11400000000001976a914904a49878c0adfc3aa05de7afad2cc15f"
				+ "483a56a88ac7f400900000000001976a914418327e3f3dda4cf5b9089325a4b95abdfa0334088ac722c0"
				+ "c00000000001976a914ba35042cfe9fc66fd35ac2224eebdafd1028ad2788acdc4ace020000000"
				+ "017a91474d691da1574e6b3c192ecfb52cc8984ee7b6c56870000000001000000";
		BigInteger modifiedTx = new BigInteger(strTx, 16);
		byte[] h256 = Utils.hash256(Utils.getBytes(modifiedTx));
		BigInteger z = new BigInteger(1, h256);
		System.out.println(z);
		
		String secstr = "022626e955ea6ea6d98850c994f9107b036b1334f18ca8830bfff1295d21cfdb70";
		BigInteger secbigi = new BigInteger(secstr, 16);
		byte[] sec = Utils.getBytes(secbigi);
		
		String derstr = "3045022100dc92655fe37036f47756db8102e0d7d5e28b3beb83a8fef4f5dc0559bddfb94e02205a36d4e4e6c7fcd"
				+ "16658c50783e00c341609977aed3ad00937bf4ee942a89937";
		BigInteger derbigi = new BigInteger(derstr, 16);
		byte[] der = Utils.getBytes(derbigi);
		
		S256Point point = S256Point.parse(sec);
		Signature sig = Signature.parse(der);
		
		System.out.println(point);
		System.out.println(sig);
		
		System.out.println(point.verify(z, sig));
		
	}

}
