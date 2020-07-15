package core.wlwallet;

import java.io.DataInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class HeadersMessage extends Message{
	
	public byte[] command = new byte[]{'h','e','a','d','e','r','s'};
//	ArrayList<Block> blocks = new ArrayList<Block>();
	ArrayList<Block> blocks;
	
	public HeadersMessage() {
	}
	
	public HeadersMessage(ArrayList<Block> blocks) {
		super();
		this.blocks = blocks;
	}

	public static HeadersMessage parse(DataInputStream stream, boolean testnet){
		BigInteger numHeaders = Utils.readVarint(stream);
		int n = numHeaders.intValue();
		ArrayList<Block> blocks = new ArrayList<Block>();
		for(int i=0;i<n;i++){
			if(i==1200){
				final Block b = Block.parse(stream);
				blocks.add(b);
				BigInteger numTxs = Utils.readVarint(stream);
				if(!(numTxs.equals(BigInteger.ZERO))){
					System.out.println("number of txs not 0");
					System.exit(1);
				}
			}else{
				final Block b = Block.parse(stream);
				blocks.add(b);
				BigInteger numTxs = Utils.readVarint(stream);
				if(!(numTxs.equals(BigInteger.ZERO))){
					System.out.println("number of txs not 0");
					System.exit(1);
				}
			}
			
		}
		HeadersMessage headersMessage =new HeadersMessage(blocks);
		return headersMessage;
	}

	@Override
	public byte[] serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getCommand() {
		// TODO Auto-generated method stub
		return this.command;
	}
}
