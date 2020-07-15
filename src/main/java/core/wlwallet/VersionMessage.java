package core.wlwallet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

import org.bouncycastle.util.Arrays;

public class VersionMessage extends Message {

	public byte[] command = new byte[] { 'v', 'e', 'r', 's', 'i', 'o', 'n' };

	BigInteger version;
	BigInteger services;
	BigInteger timestamp;
	BigInteger receiverServices;
	byte[] receiverIp;
	BigInteger receiverPort;
	BigInteger senderServices;
	byte[] senderIp;
	BigInteger senderPort;
	byte[] nonce;
	byte[] userAgent;
	BigInteger latestBlock;
	boolean relay;

	public VersionMessage() {
		this(BigInteger.valueOf(70015), BigInteger.ZERO, null, BigInteger.ZERO,
				new byte[] { 0x00, 0x00, 0x00, 0x00 },
				BigInteger.valueOf(8333), BigInteger.ZERO, new byte[] { 0x00,
						0x00, 0x00, 0x00 }, BigInteger.valueOf(8333), null,
				"/programmingbitcoin:0.1/".getBytes(), BigInteger.ZERO, false);
	}

	public VersionMessage(BigInteger version, BigInteger services,
			BigInteger timestamp, BigInteger receiverServices,
			byte[] receiverIp, BigInteger receiverPort,
			BigInteger senderServices, byte[] senderIp, BigInteger senderPort,
			byte[] nonce, byte[] userAgent, BigInteger latestBlock,
			boolean relay) {
		super();

		this.version = version;
		this.services = services;

		if (timestamp == null) {
			this.timestamp = BigInteger
					.valueOf(System.currentTimeMillis() / 1000);
		} else {
			this.timestamp = timestamp;
		}
		this.receiverServices = receiverServices;
		this.receiverIp = receiverIp;
		this.receiverPort = receiverPort;

		this.senderServices = senderServices;
		this.senderIp = senderIp;
		this.senderPort = senderPort;

		if (this.nonce == null) {
			Random r = new Random();
			BigInteger random = new BigInteger(64, r);
			this.nonce = Utils.biginteger_to_little_endian(random, 8);
		} else {
			this.nonce = nonce;
		}

		this.userAgent = userAgent;
		this.latestBlock = latestBlock;
		this.relay = relay;
	}

	public static VersionMessage parse(DataInputStream s, boolean testnet) {
		VersionMessage versionMessage = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			byte[] versionBin = new byte[4];
			s.read(versionBin);
			byte[] versionB = Arrays.reverse(versionBin);
			BigInteger version = new BigInteger(1, versionB);

			byte[] servicesBin = new byte[8];
			s.read(servicesBin);
			byte[] servicesB = Arrays.reverse(servicesBin);
			BigInteger services = new BigInteger(1, servicesB);

			byte[] timestampBin = new byte[8];
			s.read(timestampBin);
			byte[] timestampB = Arrays.reverse(timestampBin);
			BigInteger timestamp = new BigInteger(1, timestampB);

			byte[] receiverSerivcesBin = new byte[8];
			s.read(receiverSerivcesBin);
			byte[] receiverServiceB = Arrays.reverse(receiverSerivcesBin);
			BigInteger receiverService = new BigInteger(1, receiverServiceB);

			byte[] receiverAddressBin = new byte[16];
			s.read(receiverAddressBin);
			byte[] receiverIp = receiverAddressBin;

			byte[] receiverPortBin = new byte[2];
			s.read(receiverPortBin);
			byte[] receiverPortB = receiverPortBin;
			BigInteger receiverPort = new BigInteger(1, receiverPortB);

			byte[] senderSerivcesBin = new byte[8];
			s.read(senderSerivcesBin);
			byte[] senderServicesB = Arrays.reverse(senderSerivcesBin);
			BigInteger senderServices = new BigInteger(1, senderServicesB);

			byte[] senderAddressBin = new byte[16];
			s.read(senderAddressBin);
			byte[] senderIp = senderAddressBin;

			byte[] senderPortBin = new byte[2];
			s.read(senderPortBin);
			byte[] senderPortB = senderPortBin;
			BigInteger senderPort = new BigInteger(1, senderPortB);

			byte[] nonceBin = new byte[8];
			s.read(nonceBin);
			byte[] nonce = nonceBin;

			BigInteger userAgentLength = Utils.readVarint(s);
			byte[] userAgentBin = new byte[userAgentLength.intValue()];
			s.read(userAgentBin);
			byte[] userAgent = userAgentBin;

			byte[] lastBlockBin = new byte[4];
			s.read(lastBlockBin);
			byte[] lastBlockB = Arrays.reverse(lastBlockBin);
			BigInteger lastBlock = new BigInteger(1, lastBlockB);

			byte[] relayBin = new byte[1];
			s.read(relayBin);
			byte[] relayB = relayBin;
			boolean relay = (relayB[0] != 0x00);
			versionMessage = new VersionMessage(version, services, timestamp,
					receiverService, receiverIp, receiverPort, senderServices,
					senderIp, senderPort, nonce, userAgent, lastBlock, relay);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return versionMessage;

	}

	public byte[] serialize() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] result = null;
		try {
			baos.write(Utils.biginteger_to_little_endian(this.version, 4));
			baos.write(Utils
					.biginteger_to_little_endian(this.senderServices, 8));
			baos.write(Utils.biginteger_to_little_endian(this.timestamp, 8));
			baos.write(Utils.biginteger_to_little_endian(this.receiverServices,
					8));

			for (int i = 0; i < 10; i++) {
				baos.write(new byte[] { 0x00 });
			}
			baos.write(new byte[] { (byte) 0xff, (byte) 0xff });
			baos.write(this.receiverIp);
			baos.write(Utils.getBytes(this.receiverPort, BigInteger.valueOf(2)));
			baos.write(Utils
					.biginteger_to_little_endian(this.senderServices, 8));

			for (int i = 0; i < 10; i++) {
				baos.write(new byte[] { 0x00 });
			}
			baos.write(new byte[] { (byte) 0xff, (byte) 0xff });
			baos.write(this.senderIp);
			baos.write(Utils.getBytes(this.senderPort, BigInteger.valueOf(2)));

			baos.write(this.nonce);

			baos.write(Utils.encodeVarint(BigInteger
					.valueOf(this.userAgent.length)));
			baos.write(this.userAgent);
			baos.write(Utils.biginteger_to_little_endian(this.latestBlock, 4));

			if (this.relay) {
				baos.write(new byte[] { 0x01 });
			} else {
				baos.write(new byte[] { 0x00 });
			}
			result = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public byte[] getCommand() {
		return this.command;
	}

}
