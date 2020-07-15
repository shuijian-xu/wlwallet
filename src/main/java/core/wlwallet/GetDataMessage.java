package core.wlwallet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bouncycastle.util.Arrays;

public class GetDataMessage extends Message {

	public static int TX_DATA_TYPE = 1;
	public static int BLOCK_DATA_TYPE = 2;
	public static int FILTERED_BLOCK_DATA_TYPE = 3;
	public static int COMPACT_BLOCK_DATA_TYPE = 4;

	public byte[] command = new byte[] { 'g', 'e', 't', 'd', 'a', 't', 'a' };
	
	public ArrayList<DataElement> data;
	
	public GetDataMessage() {
		super();
		this.data = new ArrayList<DataElement>();
	}

	public void addData(DataElement data) {
		this.data.add(data);
	}

	public byte[] serialize() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] lengthBin = Utils.encodeVarint(BigInteger.valueOf(this.data
				.size()));
		try {
			baos.write(lengthBin);
			ArrayList<DataElement> al = this.data;
			for (DataElement de : al) {
				int i = de.getKey();
				byte[] item = de.getValue();
				BigInteger dataType = BigInteger.valueOf(i);
				baos.write(Utils.biginteger_to_little_endian(dataType, 4));
				baos.write(Arrays.reverse(item));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] result = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		String payloadStr = String.format("%0"+result.length*2+"x", new BigInteger(1, result));
//		System.out.println();
//		System.out.println("GetDataMessage payload: "+payloadStr);
//		System.out.println();
		return result;
	}

	@Override
	public byte[] getCommand() {
		// TODO Auto-generated method stub
		return this.command;
	}

}
