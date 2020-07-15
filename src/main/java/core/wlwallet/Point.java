package core.wlwallet;

public interface Point {
	
	public boolean eq(Point obj);
	public boolean ne(Point obj);
	
	public Point add(Point obj);
	
}
