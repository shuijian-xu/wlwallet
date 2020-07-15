package core.wlwallet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import org.bouncycastle.util.Arrays;

public class GetHeadersMessage extends Message{
	public byte[] command = new byte[] { 'g', 'e', 't', 'h', 'e', 'a',
			'd', 'e', 'r', 's' };

	BigInteger version;
	BigInteger numHashes;
	byte[] startBlock;
	byte[] endBlock;

	public GetHeadersMessage(byte[] startBlock) {
		this(BigInteger.valueOf(70015), BigInteger.ONE, startBlock, null);
	}

	public GetHeadersMessage(BigInteger version, BigInteger numHashes,
			byte[] startBlock, byte[] endBlock) {
		super();
		this.version = version;
		this.numHashes = numHashes;
		if (startBlock == null) {
			System.out.println("A start block is required");
			System.exit(1);
		}
		this.startBlock = startBlock;
		if (endBlock == null) {
			this.endBlock = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
					0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
					0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
					0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
		} else {
			this.endBlock = endBlock;
		}
	}

	public byte[] serialize() {
		byte[] result = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			byte[] versionBin = Utils.biginteger_to_little_endian(this.version,
					4);
			baos.write(versionBin);
			byte[] numHashesBin = Utils.encodeVarint(this.numHashes);
			baos.write(numHashesBin);
			byte[] startBlockBin = Arrays.reverse(this.startBlock);
			baos.write(startBlockBin);
			byte[] endBlockBin = Arrays.reverse(this.endBlock);
			baos.write(endBlockBin);
			result = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String payloadStr = String.format("%0"+result.length*2+"x", new BigInteger(1, result));
		System.out.println("GetHeadersMessage payload: "+payloadStr);
		return result;
	}

	@Override
	public byte[] getCommand() {
		return this.command;
	}
}
