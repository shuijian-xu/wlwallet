package core.wlwallet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.HashMap;

public class Test7 {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		String a = "1";
        HashMap<String,Object> map = new HashMap<String,Object>();
        Class aClass=Test7.class;
        Method method1 = aClass.getMethod("ccc", null);
        map.put(a, method1);
    
        Method m = (Method) map.get(a);
        m.invoke(aClass.newInstance(), null);
        
        System.out.println(BigInteger.ZERO.byteValue());
        System.out.println(BigInteger.ZERO.clearBit(0));
	}
	
	public void  ccc(){
        System.out.println("aa");
    }

}
