package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

public class C10S4 {

	public static void main(String[] args) throws IOException {
		
		String host = "217.23.5.68";
		
		byte[] genesisBlock = Block.GENESISBLOCK;
		ByteArrayInputStream bais = new ByteArrayInputStream(genesisBlock);
		Block previous = Block.parse(bais);
		bais.close();
		
		BigInteger firstEpochTimestamp = previous.timestamp;
		byte[] expectedBits = Block.LOWEST_BITS;
		
		BigInteger count =BigInteger.ONE;
		
		SimpleNode node = new SimpleNode(host, false);
		node.handShake();
		
		for(int i=0;i<19;i++){
			GetHeadersMessage getheaders = new GetHeadersMessage(previous.hash());
			node.send(getheaders);
			ArrayList<String> list = new ArrayList<String>();
			list.add("core.wlwallet.HeadersMessage");
			HeadersMessage headers = (HeadersMessage) node.waitFor(list);
			ArrayList<Block> blockList = headers.blocks;
			System.out.println(blockList.size());
			for(Block header: blockList){
				byte[] headerBin = header.serialize();
				String headerHex = String.format("%0160x", new BigInteger(1, headerBin));
				
				if(!header.checkPow()){
					System.out.println("bad Pow at Block "+count);
					System.exit(1);
				}
				
				BigInteger headerPreBlockBigInt = new BigInteger(1,header.prevBlock);
				BigInteger previousHashBigInt = new BigInteger(1,previous.hash());
				
				if(!(headerPreBlockBigInt.equals(previousHashBigInt))){
					System.out.println("discontinous block at  "+count);
					System.exit(1);
				}
//				byte[] expectedBits = new byte[]{};
				
				if(count.mod(BigInteger.valueOf(2016)).equals(BigInteger.ZERO)){
					BigInteger timeDiff = previous.timestamp.subtract(firstEpochTimestamp);
					expectedBits = Utils.calculateNewBits(previous.bits, timeDiff);
					String expectedBitsHex = new BigInteger(1,expectedBits).toString(16);
					System.out.println(expectedBitsHex);
					firstEpochTimestamp=header.timestamp;
				}
				if(!new BigInteger(1,header.bits).equals(new BigInteger(1,expectedBits))){
					System.out.println("bad bits at Block "+count);
					System.exit(1);
				}
				previous = header;
				
System.out.println(headerHex+": "+count);
				
				count = count.add(BigInteger.ONE);
				
				
			}
		
		}
		
	}
}
