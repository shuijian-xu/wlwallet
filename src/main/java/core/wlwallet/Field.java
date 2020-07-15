package core.wlwallet;

import java.math.BigInteger;

public interface Field {
	
	public boolean eq(Field obj);
	public boolean ne(Field obj);
	
	public Field add(Field obj);
	public Field sub(Field obj);
	public Field mul(Field obj);
	public Field div(Field obj);
	public Field pow(BigInteger exponent);
	
}
