package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Stack;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.util.Arrays;

public class Utils {

	static String BASE58_ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
	static BigInteger TWO_WEEKS = BigInteger.valueOf(60 * 60 * 24 * 14);

	public static String h160_to_p2pkh_address(byte[] h160) {
		boolean testnet = false;
		return Utils.h160_to_p2pkh_address(h160, testnet);
	}

	public static String h160_to_p2pkh_address(byte[] h160, boolean testnet) {
		byte[] prefix;
		if (testnet) {
			prefix = new byte[] { 0x6f };
		} else {
			prefix = new byte[] { 0x00 };
		}
		return Utils.encodeBase58Checksum(Arrays.concatenate(prefix, h160));
	}

	public static String h160_to_p2sh_address(byte[] h160) {
		boolean testnet = false;
		return Utils.h160_to_p2sh_address(h160, testnet);
	}

	public static String h160_to_p2sh_address(byte[] h160, boolean testnet) {
		byte[] prefix;
		if (testnet) {
			prefix = new byte[] { (byte) 0xc4 };
		} else {
			prefix = new byte[] { 0x05 };
		}
		return Utils.encodeBase58Checksum(Arrays.concatenate(prefix, h160));
	}

	public static String encodeBase58(byte[] s) {

		/*
		 * byte[] s = new byte[32]; int ss_padding_size=0; if(ss.length!=33){
		 * ss_padding_size = 32 - ss.length; System.arraycopy(ss, 0, s,
		 * ss_padding_size, ss.length); }else{ System.arraycopy(ss, 1, s, 0,
		 * 32); }
		 */

		int count = 0;
		for (int i = 0; i < s.length; i++) {
			if (s[i] == 0x00) {
				count++;
				continue;
			} else {
				break;
			}
		}

		BigInteger num = new BigInteger(1, s);

		// byte[] prefixb = new byte[count];
		StringBuffer prefix = new StringBuffer();
		// String prefixc="";
		for (int i = 0; i < count; i++) {
			prefix.append("1");
			// prefixb[i] = 0x1;
			// prefixc = prefixc+1;
		}
		// String prefixbS = new String(prefixb);

		String result = "";
		while (num.compareTo(BigInteger.valueOf(0)) > 0) {
			BigInteger[] quotient_remainder = num.divideAndRemainder(BigInteger
					.valueOf(58));
			num = quotient_remainder[0];
			BigInteger mod = quotient_remainder[1];
			char c = BASE58_ALPHABET.charAt(mod.intValue());
			result = c + result;
		}

		return prefix + result;
	}

	public static byte[] decodeBase58(String s) {

		BigInteger num = BigInteger.ZERO;
		for (char c : s.toCharArray()) {
			num = num.multiply(BigInteger.valueOf(58));
			num = num.add(BigInteger.valueOf(BASE58_ALPHABET.indexOf(c)));
		}
		byte[] combined = Utils.getBytes(num, BigInteger.valueOf(25));
		byte[] checkSum = Arrays.copyOfRange(combined, combined.length - 4,
				combined.length);

		byte[] data = Arrays.copyOfRange(combined, 0, combined.length - 4);
		byte[] checkSumC = Arrays.copyOfRange(Utils.hash256(data),
				combined.length - 4, combined.length);

		if (Arrays.areEqual(checkSum, checkSumC)) {
			System.out.println("Checksum not correct");
			System.exit(1);
		}
		byte[] result = Arrays.copyOfRange(combined, 1, combined.length - 4);
		return result;

	}

	public static byte[] hash256(byte[] b) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		return messageDigest.digest(messageDigest.digest(b));
	}
	
	public static byte[] sha256(byte[] b) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		return messageDigest.digest(b);
	}
	

	public static byte[] hash160(byte[] b) {

		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}

		byte[] data = messageDigest.digest(b);

		RIPEMD160Digest d = new RIPEMD160Digest();
		d.update(data, 0, data.length);
		byte[] out = new byte[d.getDigestSize()];
		d.doFinal(out, 0);

		return out;
	}

	public static String encodeBase58Checksum(byte[] b) {
		byte[] hash256 = Utils.hash256(b);
		byte[] checksum = new byte[4];
		for (int i = 0; i < 4; i++) {
			checksum[i] = hash256[i];
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			baos.write(b);
			baos.write(checksum);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] result = baos.toByteArray();

		String r = Utils.encodeBase58(result);
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return r;
	}

	public static BigInteger little_endian_to_biginteger(byte[] buf) {

		byte[] res = Arrays.reverse(buf);
		BigInteger bigInteger = new BigInteger(1, res);
		return bigInteger;

	}

	public static byte[] biginteger_to_little_endian(BigInteger n, int length) {
		byte[] b = new byte[length];
		byte[] buf = Utils.getBytes(n);
		int i = length - buf.length;

		System.arraycopy(buf, 0, b, i, buf.length);

		byte[] res = Arrays.reverse(b);
		return res;

	}

	public static BigInteger readVarint(ByteArrayInputStream s) {
		byte[] b1 = new byte[1];
		try {
			s.read(b1);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		BigInteger bigInteger = new BigInteger(1, b1);
		long i = bigInteger.longValue();

		if (i == 0xfd) { // 0xfd means the next 2 bytes are the number
			byte[] b2 = new byte[2];
			try {
				s.read(b2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return Utils.little_endian_to_biginteger(b2);
		} else if (i == 0xfe) { // 0xfe means the next 4 bytes are the number
			byte[] b2 = new byte[4];
			try {
				s.read(b2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return Utils.little_endian_to_biginteger(b2);
		} else if (i == 0xff) { // 0xff means the next eight bytes are the
								// number
			byte[] b2 = new byte[8];
			try {
				s.read(b2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return Utils.little_endian_to_biginteger(b2);
		} else { // anything else is just the integer
			return bigInteger;
		}

	}
	
	public static BigInteger readVarint(DataInputStream s) {
		byte[] b1 = new byte[1];
		try {
			s.read(b1);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		BigInteger bigInteger = new BigInteger(1, b1);
		long i = bigInteger.longValue();

		if (i == 0xfd) { // 0xfd means the next 2 bytes are the number
			byte[] b2 = new byte[2];
			try {
				s.read(b2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return Utils.little_endian_to_biginteger(b2);
		} else if (i == 0xfe) { // 0xfe means the next 4 bytes are the number
			byte[] b2 = new byte[4];
			try {
				s.read(b2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return Utils.little_endian_to_biginteger(b2);
		} else if (i == 0xff) { // 0xff means the next eight bytes are the
								// number
			byte[] b2 = new byte[8];
			try {
				s.read(b2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return Utils.little_endian_to_biginteger(b2);
		} else { // anything else is just the integer
			return bigInteger;
		}

	}

	public static byte[] encodeVarint(BigInteger bigInteger) {
		if (bigInteger.compareTo(new BigInteger("fd", 16)) < 0) {
			byte[] b = { (byte) bigInteger.intValue() };
			return b;
		} else if (bigInteger.compareTo(new BigInteger("10000", 16)) < 0) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(0xfd);
			try {
				baos.write(Utils.biginteger_to_little_endian(bigInteger, 2));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return baos.toByteArray();

		} else if (bigInteger.compareTo(new BigInteger("100000000", 16)) < 0) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(0xfe);
			try {
				baos.write(Utils.biginteger_to_little_endian(bigInteger, 4));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (bigInteger
				.compareTo(new BigInteger("10000000000000000", 16)) < 0) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(0xff);
			try {
				baos.write(Utils.biginteger_to_little_endian(bigInteger, 8));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("integer is too large " + bigInteger);
			System.exit(-1);

		}
		return null;
	}

	public static byte[] getBytes(BigInteger big) {
		byte[] bigBytes = big.toByteArray();
		if ((big.bitLength() % 8) != 0) {
			return bigBytes;
		} else {
			byte[] smallerBytes = new byte[big.bitLength() / 8];
			System.arraycopy(bigBytes, 1, smallerBytes, 0, smallerBytes.length);
			return smallerBytes;
		}
	}

	public static byte[] getBytes(BigInteger big, BigInteger length) {

		byte[] bigBytes = big.toByteArray();
		byte[] result;
		if ((big.bitLength() % 8) != 0) {
			result = bigBytes;
		} else {
			byte[] smallerBytes = new byte[big.bitLength() / 8];
			System.arraycopy(bigBytes, 1, smallerBytes, 0, smallerBytes.length);
			result = smallerBytes;
		}
		byte[] b = new byte[length.intValue()];

		if (result.length < length.intValue()) {
			int i = length.intValue() - result.length;
			System.arraycopy(result, 0, b, i, result.length);
			return b;
		} else {
			return result;
		}
	}

	public static BigInteger bitsToTarget(byte[] bits) {
//		byte[] bitsBin = Utils.getBytes(bits);
		byte[] bitsBin = bits;
		byte[] exponentBin = Arrays.copyOfRange(bitsBin, bitsBin.length - 1,
				bitsBin.length);
		BigInteger exponent = new BigInteger(1, exponentBin);
		
		byte[] coefficientBin = Arrays
				.copyOfRange(bitsBin, 0, bitsBin.length - 1);
		BigInteger coefficient = Utils.little_endian_to_biginteger(coefficientBin);
	
		int powExponentV=exponent.subtract(BigInteger.valueOf(3)).intValue();
		if(exponent.equals(BigInteger.ZERO)){
			System.out.println(exponent+":"+powExponentV);
			System.out.println();
			for(int i=0;i<bits.length;i++){
				System.out.println(bits[i]);
			}
		}
		
		return coefficient.multiply(BigInteger.valueOf(256).pow(
				(powExponentV)));
	}
	
	public static byte[] targetToBits(BigInteger target) {
		byte[] rawBytes = Utils.getBytes(target);
		byte[] exponent = null;
		byte[] coefficient = null;
		byte sign = rawBytes[0];
		if (sign < 0) {
			exponent = new byte[] { (byte) (rawBytes.length + 1) };
			coefficient = Arrays.concatenate(new byte[] { 0x00 },
					Arrays.copyOfRange(rawBytes, 0, 3));
		} else {
			exponent = new byte[] { (byte) (rawBytes.length) };
			coefficient = Arrays.copyOfRange(rawBytes, 0, 3);
		}
		byte[] newBits = Arrays.concatenate(Arrays.reverse(coefficient),
				exponent);
		return newBits;
	}

	public static byte[] calculateNewBits(byte[] previousBits,
			BigInteger timeDifferential) {
		if (timeDifferential.compareTo(Utils.TWO_WEEKS.multiply(BigInteger
				.valueOf(4))) > 0) {
			timeDifferential = Utils.TWO_WEEKS.multiply(BigInteger.valueOf(4));
		}
		if (timeDifferential.compareTo(Utils.TWO_WEEKS.divide(BigInteger
				.valueOf(4))) < 0) {
			timeDifferential = Utils.TWO_WEEKS.divide(BigInteger.valueOf(4));
		}
		BigInteger newTarget = Utils.bitsToTarget(previousBits)
				.multiply(timeDifferential)
				.divide(Utils.TWO_WEEKS);
		return Utils.targetToBits(newTarget);
	}
	
	public static byte[] merkleParent(byte[] hash1, byte[] hash2){
		return Utils.hash256(Arrays.concatenate(hash1, hash2));
	}
	
	public static ArrayList<byte[]> merkleParentLevel(ArrayList<byte[]> hashes){
		int hashesSize = hashes.size();
		if(hashesSize%2==1){
			hashes.add(hashes.get(hashesSize-1));
		}
		ArrayList<byte[]> parentLevel = new ArrayList<byte[]>();
		for(int i=0;i<hashes.size();i=i+2){
			byte[] parent = Utils.merkleParent(hashes.get(i), hashes.get(i+1));
			parentLevel.add(parent);
		}
		return parentLevel;
	}
	
	/*
	public static ArrayList<byte[]> merkleParentLevel(byte[][] hashes){
		int hashesSize = hashes.length;
		
		byte[][] result;
		if(hashesSize%2==1){
			result = new byte[hashesSize+1][];
			hashes.add(hashes.get(hashesSize-1));
		}
		ArrayList<byte[]> parentLevel = new ArrayList<byte[]>();
		for(int i=0;i<hashes.size();i=i+2){
			byte[] parent = Utils.merkleParent(hashes.get(i), hashes.get(i+1));
			parentLevel.add(parent);
		}
		return parentLevel;
	}
	*/
	
	public static byte[] merkleRoot(ArrayList<byte[]> hashes){
		ArrayList<byte[]> currentHashes = new ArrayList<byte[]>(hashes);
		while (currentHashes.size() > 1) {
			currentHashes = Utils.merkleParentLevel(currentHashes);
		}
		return currentHashes.get(0);
	}
	
	public static int log(int x, int base){
		return (int) Math.ceil(Math.log(x)/Math.log(base));
	}
	
/*	public static Stack<byte[]> bytesToBitField(byte[] someBytes){
		Stack<byte[]>  flagBits = new Stack<byte[]>();
		for(byte b: someBytes){
			flagBits.add(new byte[]{b});
			for(int i=0;i<8;i++){
				flagBits.add(new byte[]{(byte) (b&0x01)});
				b>>=1;
			}
		}
		return flagBits;
	}*/
	
	
	
	public static byte[] bytesToBitField(byte[] someBytes){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for(byte b: someBytes){
			for(int i=0;i<8;i++){
				baos.write(b&1);
//				Arrays.append(flagBits, (byte) (b&1));
				b>>=1;
			}
		}
		byte[] flagBits = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flagBits;
		
		
//		int someByteLength = someBytes.length;
//		if(someByteLength==1&&someBytes[0]==0){
//			byte[] flagBits = new byte[]{0};
//			return flagBits;
//		}else{
//			byte[] flagBits = new byte[someBytes.length*8];
	//		for(byte b: someBytes){
	//			for(int i=0;i<8;i++){
	//				Arrays.append(flagBits, (byte) (b&1));
	//				b>>=1;
	//			}
	//		}
	//		return flagBits;
	//	}
	}
	
	public static byte[] bitFieldToBytes(byte[] bitField){
		System.out.println();
		for(int i=0;i<bitField.length;i++){
			System.out.print(bitField[i]+", ");
		}
		System.out.println();
		
		
		int bitFieldLength = bitField.length;
		if(bitFieldLength%8!=0){
			System.out.println("bitField does not have a length that is divisible by 8");
			System.exit(1);
		}
		
		byte[] result = new byte[bitFieldLength/8];
		for(int i=0;i<bitFieldLength;i++){
			byte bit = bitField[i];
			int byteIndex = i/8;
			int bitIndex = i%8;
			
			if(bit!=0){
				result[byteIndex] |= 1 << bitIndex;
			}
			
		}
		String resultStr = String.format("%0"+result.length*2+"x", new BigInteger(1, result));
		System.out.println(resultStr);
		return result;
	}
	
	public static byte[] bitFieldToBytes(BitSet bitField){
		int bitFieldLength = bitField.size();
		if(bitFieldLength%8!=0){
			System.out.println("bitField does not have a length that is divisible by 8");
			System.exit(1);
		}
		
		byte[] result = new byte[bitFieldLength/8];
		for(int i=0;i<result.length;i++){
			boolean bit = bitField.get(i);
			int byteIndex = i/8;
			int bitIndex = i%8;
			if(bit){
				result[byteIndex] &= 1<<bitIndex;
			}
		}
		return result;
	}
	
	
	
	public static int murmur3(byte[] data, int seed){
		int c1 = 0xcc9e2d51;
		int c2 = 0x1b873593;

		
		int length = data.length;
		int h1 = seed;
		int roundedEnd = (length&0xfffffffc); // round down to 4 byte block
		for(int i=0;i<roundedEnd;i=i+4){
			int k1 = (data[i]&0xff)|((data[i+1]&0xff)<<8) | ((data[i+2]&0xff)<<16) | (data[i+3]<<24);
			k1 *=c1;
			k1 = (k1<<15) | ((k1&0xffffffff)>>17); // ROTL32(k1, 15)
			k1 *=c2;
			h1 ^=k1;
			h1 = (h1<<13)|((h1&0xffffffff)>>19); // ROTL32(h1, 13)
			h1 = h1*5 + 0xe6546b64;
		}
		//tail
		int k1 = 0;
		int val = length&0x03;
		if(val==3){
			k1 = (data[roundedEnd+2]&0xff)<<16;
		}
		// fallthrough
		if(val ==2 || val==3){
			k1 |= (data[roundedEnd+1]&0xff)<<8;
		}
		if(val ==1 || val==2 || val==3){
			k1 |= data[roundedEnd]&0xff;
			k1 *=c1;
			k1 = (k1<<15)|((k1&0xffffffff)>>17); // ROTL32(k1, 15)
			k1 *= c2;
			h1 ^=k1;
		}
		// finalization
		h1 ^= length;
		// fmix(h1)
		h1 ^= ((h1 & 0xffffffff)>>16);
		h1 *= 0x85ebca6b;
		h1 ^= ((h1&0xffffffff)>>13);
		h1 *= 0xc2b2ae35;
		h1 ^= ((h1 & 0xffffffff)>>16);
		return h1&0xffffffff;
	}
	
	public static int murmurHash3(byte[] data, long nTweak, int hashNum, byte[] object) {
        int h1 = (int)(hashNum * 0xFBA4C795L + nTweak);
        final int c1 = 0xcc9e2d51;
        final int c2 = 0x1b873593;

        int numBlocks = (object.length / 4) * 4;
        // body
        for(int i = 0; i < numBlocks; i += 4) {
            int k1 = (object[i] & 0xFF) |
                  ((object[i+1] & 0xFF) << 8) |
                  ((object[i+2] & 0xFF) << 16) |
                  ((object[i+3] & 0xFF) << 24);
            
            k1 *= c1;
            k1 = rotateLeft32(k1, 15);
            k1 *= c2;

            h1 ^= k1;
            h1 = rotateLeft32(h1, 13);
            h1 = h1*5+0xe6546b64;
        }
        
        int k1 = 0;
        switch(object.length & 3)
        {
            case 3:
                k1 ^= (object[numBlocks + 2] & 0xff) << 16;
                // Fall through.
            case 2:
                k1 ^= (object[numBlocks + 1] & 0xff) << 8;
                // Fall through.
            case 1:
                k1 ^= (object[numBlocks] & 0xff);
                k1 *= c1; k1 = rotateLeft32(k1, 15); k1 *= c2; h1 ^= k1;
                // Fall through.
            default:
                // Do nothing.
                break;
        }

        // finalization
        h1 ^= object.length;
        h1 ^= h1 >>> 16;
        h1 *= 0x85ebca6b;
        h1 ^= h1 >>> 13;
        h1 *= 0xc2b2ae35;
        h1 ^= h1 >>> 16;
        
        return (int)((h1&0xFFFFFFFFL) % (data.length * 8));
    }
	
	private static int rotateLeft32(int x, int r) {
        return (x << r) | (x >>> (32 - r));
    }
	
	public static int murmurHash3(long nTweak, int hashNum, byte[] object) {
        int h1 = (int)(hashNum * 0xFBA4C795L + nTweak);
        final int c1 = 0xcc9e2d51;
        final int c2 = 0x1b873593;

        int numBlocks = (object.length / 4) * 4;
        // body
        for(int i = 0; i < numBlocks; i += 4) {
            int k1 = (object[i] & 0xFF) |
                  ((object[i+1] & 0xFF) << 8) |
                  ((object[i+2] & 0xFF) << 16) |
                  ((object[i+3] & 0xFF) << 24);
            
            k1 *= c1;
            k1 = rotateLeft32(k1, 15);
            k1 *= c2;

            h1 ^= k1;
            h1 = rotateLeft32(h1, 13);
            h1 = h1*5+0xe6546b64;
        }
        
        int k1 = 0;
        switch(object.length & 3)
        {
            case 3:
                k1 ^= (object[numBlocks + 2] & 0xff) << 16;
                // Fall through.
            case 2:
                k1 ^= (object[numBlocks + 1] & 0xff) << 8;
                // Fall through.
            case 1:
                k1 ^= (object[numBlocks] & 0xff);
                k1 *= c1; k1 = rotateLeft32(k1, 15); k1 *= c2; h1 ^= k1;
                // Fall through.
            default:
                // Do nothing.
                break;
        }

        // finalization
        h1 ^= object.length;
        h1 ^= h1 >>> 16;
        h1 *= 0x85ebca6b;
        h1 ^= h1 >>> 13;
        h1 *= 0xc2b2ae35;
        h1 ^= h1 >>> 16;
        
        return (int)((h1&0xFFFFFFFFL));
    }
	
}
