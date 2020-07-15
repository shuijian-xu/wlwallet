package core.wlwallet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.bouncycastle.util.Arrays;

public class OP {

	public static HashMap<Integer, String> OP_CODE_FUNCTIONS = new HashMap<Integer, String>();
	public static HashMap<Integer, String> OP_CODE_NAME = new HashMap<Integer, String>();
	
	{
		try {
			
			OP_CODE_FUNCTIONS.put(0,"oP0");
			OP_CODE_NAME.put(0, "oP0");
			
			OP_CODE_FUNCTIONS.put(81,"oP1");
			OP_CODE_NAME.put(81, "oP1");
			
			OP_CODE_FUNCTIONS.put(82,"oP2");
			OP_CODE_NAME.put(82, "oP2");
			
			OP_CODE_FUNCTIONS.put(135,"oPequal");
			OP_CODE_NAME.put(135, "oPequal");
			
			OP_CODE_FUNCTIONS.put(118,"oPdup");
			OP_CODE_NAME.put(118, "oPdup");
			
			OP_CODE_FUNCTIONS.put(169, "oPhash160");
			OP_CODE_NAME.put(169, "oPhash160");
			
			OP_CODE_FUNCTIONS.put(136, "oPequalVerify");
			OP_CODE_NAME.put(136, "oPequalVerify");
			
			OP_CODE_FUNCTIONS.put(170, "oPhash256");
			OP_CODE_NAME.put(170, "oPhash256");
			
			OP_CODE_FUNCTIONS.put(172, "oPcheckSig");
			OP_CODE_NAME.put(172, "oPcheckSig");
			
			OP_CODE_FUNCTIONS.put(174, "oPcheckMultisig");
			OP_CODE_NAME.put(174, "oPcheckMultisig");
			
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	public OP() {
	}

	public boolean oPdup(Stack stack) {

		if (stack.size() < 1) {
			return false;
		}
		stack.push(stack.peek());
		return true;
	}

	public boolean oPhash256(Stack stack) {
		if (stack.size() < 1) {
			return false;
		}
		byte[] eb = (byte[])stack.pop();
		byte[] h256 = Utils.hash256(eb);
		stack.push(h256);
		return true;
	}

	public boolean oPhash160(Stack stack) {
		if (stack.size() < 1) {
			return false;
		}
		byte[] eb = (byte[])stack.pop();
		byte[] hash160 = Utils.hash160(eb);
		stack.push(hash160);
		return true;
	}
	
	public boolean oP0(Stack stack){
		stack.push(this.encodeNum(BigInteger.ZERO));
		return true;
	}
	
	public boolean oP1(Stack stack){
		stack.push(this.encodeNum(BigInteger.ONE));
		return true;
	}
	
	public boolean oP2(Stack stack){
		stack.push(this.encodeNum(BigInteger.valueOf(2)));
		return true;
	}
	
	public boolean oPcheckSig(Stack stack, BigInteger z){
		if(stack.size()<2){
			return false;
		}
		byte[] secPubkey = (byte[]) stack.pop();
		
		byte[] sigTmp = (byte[])stack.pop();
		byte[] derSignature = new byte[sigTmp.length-1];
		System.arraycopy(sigTmp, 0, derSignature, 0, sigTmp.length-1);
		
		S256Point point = S256Point.parse(secPubkey);
		Signature sig = Signature.parse(derSignature);
		if(point.verify(z, sig)){
			stack.push(this.encodeNum(BigInteger.ONE));
		}else{
			stack.push(this.encodeNum(BigInteger.ZERO));
		}
		return true;
	}
	
	public boolean oPcheckMultisig(Stack stack, BigInteger z){
		if(stack.size()<1){
			return false;
		}
		byte[] tmp = (byte[]) stack.pop();
		BigInteger n = this.decodeNum(tmp);
		if(stack.size()<n.add(BigInteger.ONE).intValue()){
			return false;
		}
		ArrayList<byte[]> sec_pubkeys = new ArrayList<byte[]>();
		for(int i=0; i<n.intValue();i++){
			sec_pubkeys.add((byte[])stack.pop());
		}
		tmp = (byte[])stack.pop();
		BigInteger m = this.decodeNum(tmp);
		if(stack.size()<m.add(BigInteger.ONE).intValue()){
			return false;
		}
		ArrayList<byte[]> der_signatures = new ArrayList<byte[]>();
		for(int i=0; i< m.intValue();i++){
			byte[] t1 = (byte[])stack.pop();
			byte[] t2 =Arrays.copyOfRange(t1, 0, t1.length-1);
			der_signatures.add(t2);
		}
		stack.pop();
		
		Stack<S256Point> points = new Stack<S256Point>();
		for(int i=0; i<sec_pubkeys.size();i++){
			byte[] sec = sec_pubkeys.get(i);
			S256Point point = S256Point.parse(sec);
			points.push(point);
		}
		Stack<Signature> sigs = new Stack<Signature>();
		for(int i=0; i<der_signatures.size();i++){
			byte[] der = der_signatures.get(i);
			Signature sig = Signature.parse(der);
			sigs.push(sig);
		}
		
		for(Signature sig: sigs){
			if(points.size()==0){
				return false;
			}
			while(points.size()!=0){
				S256Point point = points.remove(0);
				if(point.verify(z, sig)){
					break;
				}
			}
		}
		stack.push(this.encodeNum(BigInteger.ONE));
		return true;
	}
	
	public boolean oPverify(Stack stack){
		if(stack.size()<1){
			return false;
		}
		byte[] eb = (byte[]) stack.pop();
		BigInteger element = new BigInteger(1, eb);
		if(element.equals(BigInteger.ZERO)){
			return false;
		}
		return true;
	}

	public boolean oPequal(Stack stack){
		if(stack.size()<2){
			return false;
		}
		byte[] eb1 = (byte[]) stack.pop();
		byte[] eb2 = (byte[]) stack.pop();
		BigInteger element1 = new BigInteger(1, eb1);
		BigInteger element2 = new BigInteger(1, eb2);
		if(element1.equals(element2)){
			stack.push(this.encodeNum(BigInteger.ONE));
		}else{
			stack.push(this.encodeNum(BigInteger.ZERO));
		}
		return true;
	}
	
	public boolean oPequalVerify(Stack stack){
		return this.oPequal(stack) && this.oPverify(stack);
	}
	
	public byte[] encodeNum(BigInteger num) {
		if (num.equals(BigInteger.ZERO)) {
	//		num.byteValue();
			byte b = num.byteValue();
			return new byte[]{b};
		}
		BigInteger absNum = num.abs();
		boolean negative = num.compareTo(BigInteger.ZERO) < 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte tmp = 0;
		while (absNum.compareTo(BigInteger.ZERO) != 0) {
			tmp = absNum.and(BigInteger.valueOf(0xff)).byteValue();
			baos.write(tmp);
			absNum = absNum.shiftRight(8);
		}
		byte[] bb = null;
		if ((tmp & 0x80) != 0) {
			if (negative) {
				baos.write(0x80);
			} else {
				baos.write(0);
			}
			bb = baos.toByteArray();
		} else if (negative) {
			bb = baos.toByteArray();
			bb[bb.length-2] = (byte) (bb[bb.length-2] & 0x80);
		}else{
			bb = baos.toByteArray();
		}
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(new BigInteger(bb));
		return bb;
	}
	
	public BigInteger decodeNum(byte[] element){
		if(element.length==0){
			return BigInteger.ZERO;
		}
		
		byte[] bigEndian = Arrays.reverse(element);	
		boolean negative;
		byte[] result = new byte[bigEndian.length];
		if((bigEndian[0] & 0x80)!=0){
			negative = true;
			result[0] = (byte) (bigEndian[0] & 0x7f);
		}else{
			negative = false;
			result[0] = bigEndian[0];
		}
		for(int i=1;i<bigEndian.length;i++){
			byte c = bigEndian[i];
			result[i] = c;
		}
		if(negative){
			return new BigInteger(result).negate();
		}else{
			return new BigInteger(result);
		}
	}
	
	
}
