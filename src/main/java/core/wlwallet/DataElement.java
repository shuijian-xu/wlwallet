package core.wlwallet;

public class DataElement {
	
	public int key;
	public byte[] value;
	
	public DataElement(int key, byte[] value) {
		super();
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public byte[] getValue() {
		return value;
	}
	
}
