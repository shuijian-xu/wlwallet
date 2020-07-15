package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;

import org.bouncycastle.util.Arrays;

public class NetworkEnvelope {

	public static byte[] NETWORK_MAGIC = { (byte) 0xf9, (byte) 0xbe,
			(byte) 0xb4, (byte) 0xd9 };
	public static byte[] TESTNET_NETWORK_MAGIC = { (byte) 0xb0, 0x11, 0x09,
			0x07 };
	public byte[] command;
	public byte[] payload;
	public byte[] magic;
	public boolean testnet;

	public NetworkEnvelope() {
	}

	public NetworkEnvelope(byte[] command, byte[] payload) {
		this(command, payload, false);
	}

	public NetworkEnvelope(byte[] command, byte[] payload, boolean testnet) {
		super();
		this.command = command;
		this.payload = payload;
		if (testnet) {
			this.magic = NetworkEnvelope.TESTNET_NETWORK_MAGIC;
		} else {
			this.magic = NetworkEnvelope.NETWORK_MAGIC;
		}
	}
	
	public static NetworkEnvelope parse(DataInputStream s) {
		return NetworkEnvelope.parse(s, false);
	}

	public static NetworkEnvelope parse(DataInputStream s, boolean testnet) {
		NetworkEnvelope networkEnvelope = null; 
		byte[] magicBin = new byte[4];
		try {
			s.read(magicBin,0,4);
			if (magicBin.length == 0) {
				System.out.println("Connection reset!");
				System.exit(1);
			}
			byte[] expected_magicBin;
			if (testnet) {
				expected_magicBin = NetworkEnvelope.TESTNET_NETWORK_MAGIC;
			} else {
				expected_magicBin = NetworkEnvelope.NETWORK_MAGIC;
			}
			if (!Arrays.areEqual(magicBin, expected_magicBin)) {
				System.out.println("magic is not right");
				System.exit(1);
			}
			byte[] commandBin = new byte[12];
			s.read(commandBin,0,12);
			commandBin = Utils.getBytes(new BigInteger(commandBin)); // strip
																		// 0x00

			byte[] payloadLengthBin = new byte[4];
			s.read(payloadLengthBin,0,4);
			BigInteger payloadLength = Utils
					.little_endian_to_biginteger(payloadLengthBin);

			byte[] checkSumBin = new byte[4];
			s.read(checkSumBin,0,4);

			int payLoadLengthInt = payloadLength.intValue();
			byte[] payloadBin = new byte[payLoadLengthInt];
			int readReadLength = s.read(payloadBin,0,payLoadLengthInt);
			
			while(readReadLength<payLoadLengthInt){
				int currP = readReadLength;
				readReadLength =readReadLength+ s.read(payloadBin,currP,payLoadLengthInt-currP);
			}
			

			byte[] calculatedChechSumBin =Arrays.copyOfRange(Utils.hash256(payloadBin), 0, 4);
			BigInteger calculatedChechSumBigInt = new BigInteger(1, calculatedChechSumBin);
			BigInteger checkSumBigInt = new BigInteger(1, checkSumBin);
			
			
			if(!calculatedChechSumBigInt.equals(checkSumBigInt)){
				System.out.println("Checksum does not match");
//				System.out.println(new BigInteger(1, payloadBin).toString(16));
				System.exit(1);
			}
			networkEnvelope = new NetworkEnvelope(commandBin, payloadBin, testnet);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return networkEnvelope;
	}
	
	public byte[] serialize(){
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] result = null;
		try {
			baos.write(this.magic);
			
			baos.write(this.command);
			int n = 12-this.command.length;
			for(int i=0;i<n;i++){
				baos.write(new byte[]{0x00});
			}
			
			baos.write(Utils.biginteger_to_little_endian(BigInteger.valueOf(this.payload.length), 4));

			baos.write(Arrays.copyOfRange(Utils.hash256(this.payload), 0, 4));
			
			baos.write(this.payload);
			result = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public DataInputStream stream(){
		ByteArrayInputStream bais = new ByteArrayInputStream(this.payload);
		DataInputStream dis = new DataInputStream(bais);
		return dis;
	}

	@Override
	public String toString() {
		String comm = new String(command).trim();
		BigInteger payloadBigInt = new BigInteger(1, payload);
		return comm
				+ ": " + payloadBigInt.toString(16);
		
		
	}
	
	

}
