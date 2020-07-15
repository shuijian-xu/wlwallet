package core.wlwallet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class BloomFilter {

	public static int BIP37_CONSTANT = Integer.parseUnsignedInt("fba4c795", 16);

	public BigInteger size;
	
	public byte[] bitField;
	public BigInteger functionCount;
	public BigInteger tweak;

	public BloomFilter(BigInteger size, BigInteger functionCount,
			BigInteger tweak) {
		super();

		this.size = size;
		this.bitField = new byte[size.intValue()*8];
		for (int i = 0; i < size.intValue(); i++) {
			bitField[0] = 0x00;
		}
		this.functionCount = functionCount;
		this.tweak = tweak;
	}

	public void add(byte[] item) {
		for (int i = 0; i < this.functionCount.intValue(); i++) {
			long h = Integer.toUnsignedLong(Utils.murmurHash3(
					this.tweak.intValue(), i, item));
			int bit = (int) (h % (this.size.intValue() * 8));
			this.bitField[bit] = 1;
		}
	}

	public byte[] filterBytes() {
		return Utils.bitFieldToBytes(this.bitField);
	}

	public GenericMessage filterload() {
		BigInteger flag = BigInteger.ONE;
		return this.filterload(flag);
	}
	
	public GenericMessage filterload(BigInteger flag) {

		byte[] payload = null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] b1 = Utils.encodeVarint(this.size);
		byte[] b2 = this.filterBytes();
		byte[] b3 = Utils.biginteger_to_little_endian(this.functionCount, 4);
		byte[] b4 = Utils.biginteger_to_little_endian(this.tweak, 4);
		byte[] b5 = Utils.biginteger_to_little_endian(flag, 1);

		try {
			baos.write(b1);
			baos.write(b2);
			baos.write(b3);
			baos.write(b4);
			baos.write(b5);
			payload = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		String payloadStr = String.format("%0"+payload.length*2+"x", new BigInteger(1, payload));
		System.out.println(payloadStr);
		
		GenericMessage genericMessage = new GenericMessage(new byte[] {'f', 'i', 'l', 't', 'e', 'r',
				'l', 'o', 'a', 'd'},
				payload);
		return genericMessage;

	}

}
