package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;

import org.bouncycastle.util.Arrays;
import org.json.JSONObject;


public class Test13 {

	public static void main(String[] args) {
		try {
			FileInputStream fis = new FileInputStream("C:\\Users\\xushui\\workspace\\wlwallet\\src\\main\\java\\core\\wlwallet\\transaction.json");
			int i;
			StringBuffer sb = new StringBuffer();
			while((i = fis.read())!=-1){
				sb.append((char)i);
			}
			fis.close();
			String output = sb.toString();
			System.out.println(output);
			
			BigInteger txId = new BigInteger("0d6fe5213c0b3291f208cba8bfb59b7476dffacc4e5cb66f6eb20a080843a299",16);
			String key = String.format("%064x", txId);
			
			JSONObject jObject  = new JSONObject(output); 
//			String value = jObject.getString("bde8b6a853ae18b0cb665bf37875a1c50ab489bf0413a52b017325ec1f9fbc48"); 
//			String value = jObject.getString("0d6fe5213c0b3291f208cba8bfb59b7476dffacc4e5cb66f6eb20a080843a299"); 
			String value = jObject.getString(key); 
			
			System.out.println(value);
			
			BigInteger bigInteger= new BigInteger(value, 16);
			byte[] raw = Utils.getBytes(bigInteger);
			Tx tx;
			if(raw[4]==0){
				byte[] r = Arrays.concatenate(Arrays.copyOfRange(raw, 0, 4), Arrays.copyOfRange(raw, 6, raw.length));
				ByteArrayInputStream bais = new ByteArrayInputStream(r);
				tx = Tx.parse(bais, true);
				tx.locktime = Utils.little_endian_to_biginteger(Arrays.copyOfRange(raw, raw.length-4, raw.length));
			}else{
				byte[] r = raw;
				ByteArrayInputStream bais = new ByteArrayInputStream(r);
				tx = Tx.parse(bais, true);
			}
			if(!(tx.id().equals(txId))){
				System.out.println("Tx id is not match!!!");
				System.exit(1);
			}
			
			
			System.out.println();
			System.out.println(tx);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
