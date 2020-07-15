package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;

import org.bouncycastle.util.Arrays;
import org.json.JSONObject;

public class TxFetcher {
	HashMap<BigInteger, BigInteger> hmap = new HashMap<BigInteger, BigInteger>();

	public static String get_url(boolean testnet) {
		if (testnet) {

		} else {

		}
		return null;
	}

	public static Tx fetch(BigInteger txId, boolean testnet, boolean fresh)
			throws IOException {

		String key = String.format("%064x", txId);
		
		FileInputStream fis = new FileInputStream(
				"C:\\Users\\xushui\\workspace\\wlwallet\\src\\main\\java\\core\\wlwallet\\transaction.json");
		int i;
		StringBuffer sb = new StringBuffer();
		while ((i = fis.read()) != -1) {
			sb.append((char) i);
		}
		fis.close();
		String output = sb.toString();
//		System.out.println(output);

		JSONObject jObject = new JSONObject(output);
		String value = jObject.getString(key); 
		
		BigInteger bigInteger= new BigInteger(value, 16);
		byte[] raw = Utils.getBytes(bigInteger);
		Tx tx=null;
		if(raw[4]==0){
			byte[] r = Arrays.concatenate(Arrays.copyOfRange(raw, 0, 4), Arrays.copyOfRange(raw, 6, raw.length));
			ByteArrayInputStream bais = new ByteArrayInputStream(r);
			tx = Tx.parse(bais, testnet);
			tx.locktime = Utils.little_endian_to_biginteger(Arrays.copyOfRange(raw, raw.length-4, raw.length));
		}else{
			byte[] r = raw;
			ByteArrayInputStream bais = new ByteArrayInputStream(r);
			tx = Tx.parse(bais, testnet);
		}
		if(!(tx.id().equals(txId))){
			System.out.println("Tx id is not match!!!");
			System.exit(1);
		}
		return tx;
	}
}
