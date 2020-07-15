package core.wlwallet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

public class C12S3 {

	public static void main(String[] args) throws NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {

		BigInteger bitFieldSize = BigInteger.valueOf(10);
		byte[] bitField = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00 };

		for (byte[] item : new byte[][] { "hello world".getBytes(),
				"goodbye".getBytes() }) {
			Class c = Utils.class;
			for (Method m : new Method[] {
					c.getMethod("hash256", byte[].class),
					c.getMethod("hash160", byte[].class) }) {
				byte[] h = (byte[]) m.invoke(c.newInstance(), item);
				BigInteger bit = new BigInteger(1, h).mod(bitFieldSize);
				bitField[bit.intValue()] = 1;
			}
		}

		for (int i = 0; i < bitField.length; i++) {
			System.out.print(bitField[i] + " ");
		}

	}

}
