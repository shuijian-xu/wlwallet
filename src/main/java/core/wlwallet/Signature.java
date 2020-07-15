package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;

public class Signature {
	BigInteger r;
	BigInteger s;
	
	public Signature(BigInteger r, BigInteger s) {
		super();
		this.r = r;
		this.s = s;
	}
	
	public byte[] der(){

		byte[] r = Utils.getBytes(this.r, BigInteger.valueOf(32));
		int rj=0;
		for(int i=0;i<r.length;i++){
			if(r[i]==0x00){
				rj++;
				continue;
			}else{
				break;
			}
		}
		byte[] rbin = new byte[r.length-rj];
		System.arraycopy(r, rj, rbin, 0, r.length-rj);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		byte[] rbin_2 = new byte[33];
		if((rbin[0]<0x00)){ // if r's first byte >= 0x08 prepend it with 0x00
			rbin_2[0] = 0x00;
			System.arraycopy(rbin, 0, rbin_2, 1, rbin.length);
			try {
				baos.write(0x02);
				baos.write(rbin_2.length);
				baos.write(rbin_2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			try {
				baos.write(0x02);
				baos.write(rbin.length);
				baos.write(rbin);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		
		byte[] s = Utils.getBytes(this.s, BigInteger.valueOf(32));
		int sj=0;
		for(int i=0;i<s.length;i++){
			if(s[i]==0x00){
				sj++;
				continue;
			}else{
				break;
			}
		}
		byte[] sbin = new byte[s.length-sj];
		System.arraycopy(s, sj, sbin, 0, s.length-sj);
	
		byte[] sbin_2 = new byte[33];
		if((sbin[0]<0x00)){ // if s's first byte >= 0x08 prepend it with 0x00
			sbin_2[0] = 0x00;
			System.arraycopy(sbin, 0, sbin_2, 1, sbin.length);
			try {
				baos.write(0x02);
				baos.write(sbin_2.length);
				baos.write(sbin_2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			try {
				baos.write(0x02);
				baos.write(sbin.length);
				baos.write(sbin);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		byte[] result = baos.toByteArray();
		
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
		baos2.write(0x30);
		baos2.write(result.length);
		try {
			baos2.write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos2.toByteArray();
	
	}
	
	public static Signature parse(byte[] derSignature){
		ByteArrayInputStream s = new ByteArrayInputStream(derSignature);
		
		try {
			byte[] b1 = new byte[1];
			s.read(b1);
			byte compound = b1[0];
			if(compound != 0x30){
				System.out.println("Bad Signature");
				System.exit(1);
			}
			byte[] b2 = new byte[1];
			s.read(b2);
			int length = b2[0];
			if((length+2)!=derSignature.length){
				System.out.println("Bad Signature length");
				System.exit(1);
			}
			byte[] b3 = new byte[1];
			s.read(b3);
			int marker = b3[0];
			if(marker!=0x02){
				System.out.println("Bad Signature");
				System.exit(1);
			}
			byte[] b4 = new byte[1];
			s.read(b4);
			int rlength = b4[0];
			byte[] sig_r = new byte[rlength];
			s.read(sig_r);
			
			byte[] b5 = new byte[1];
			s.read(b5);
			marker = b5[0];
			if(marker!=0x02){
				System.out.println("Bad Signature");
				System.exit(1);
			}
			byte[] b6 = new byte[1];
			s.read(b6);
			int slength = b6[0];
			byte[] sig_s = new byte[slength];
			s.read(sig_s);
			if(derSignature.length != (6+rlength+slength)){
				System.out.println("Signature too long");
				System.exit(1);
			}
			s.close();
			return new Signature(new BigInteger(sig_r), new BigInteger(sig_s));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "Signature [r=" + r.toString(16) + ", s=" + s.toString(16) + "]";
	}

	public static void main(String[] args) {
		
	}
}
