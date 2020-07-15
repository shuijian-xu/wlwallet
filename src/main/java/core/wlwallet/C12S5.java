package core.wlwallet;

import java.math.BigInteger;
import java.util.ArrayList;
public class C12S5 {

	public static void main(String[] args) {
		
		String host = "217.23.5.68";
//		String host = "seed.bitcoin.sipa.be";
//		String host = "seed.tbtc.petertodd.org";
		
		String lastBlockStr = "00000000000000000a346aa6eb36be5376222b1b8e176c7ecaec533606b1c422";
		BigInteger lastBlockHex = new BigInteger(lastBlockStr, 16);
		String addressStr = "1Ffhmq2dPcM7MHSkc1T6DZPogFsS66Snb2";
		
		byte[] h160 = Utils.decodeBase58(addressStr);
		SimpleNode node = new SimpleNode(host, false);
		BloomFilter bf = new BloomFilter(BigInteger.valueOf(30), BigInteger.valueOf(5), BigInteger.valueOf(90210));
		
		bf.add(h160);
		node.handShake();
		node.send(bf.filterload());
		
//		byte[] startBlock = Utils.getBytes(lastBlockHex);
		byte[] startBlock = Utils.getBytes(lastBlockHex,BigInteger.valueOf(32));
		
		GetHeadersMessage getheaders = new GetHeadersMessage(startBlock);
		
		String getheadersStr = new String(getheaders.command).trim();
//		SendHeadersMessage sendheaders = new SendHeadersMessage();
		
		node.send(getheaders);
		
		String m1 = "core.wlwallet.HeadersMessage";
		ArrayList al = new ArrayList<String>();
		al.add(m1);
		HeadersMessage headers = (HeadersMessage) node.waitFor(al);
	
		GetDataMessage getdata = new GetDataMessage();
		
		int blockCount=1;
		ArrayList<Block> blockList = headers.blocks;
//		System.out.println(blockList.size());
		
		for(Block b: blockList){
			System.out.println(blockCount);
			blockCount++;
			
			if(!b.checkPow()){
				System.out.println("proof of work is invalid");
				System.exit(1);
			
			}
			DataElement de = new DataElement(GetDataMessage.FILTERED_BLOCK_DATA_TYPE, b.hash());
			getdata.addData(de);
		}
		node.send(getdata);
		
		boolean found = false;
		while(!found){
			
			System.out.println("...");
			String m2 = "core.wlwallet.MerkleBlock";
			String m3 = "core.wlwallet.Tx";
			
			ArrayList a2 = new ArrayList<String>();
			a2.add(m2);
			a2.add(m3);
			
			Message message = node.waitFor(a2);
			
			if(message instanceof MerkleBlock){
				MerkleBlock m = (MerkleBlock) message;
				
				if(!m.isValid()){		
					System.out.println("invalid merkle proof");
					System.exit(1);
				
				}
			}else{
				Tx tx = (Tx) message;
				
				ArrayList<TxOut> txOuts = tx.txOuts;
				for(int i=0;i<txOuts.size();i++){
					TxOut txOut = txOuts.get(i);
					Script scriptPub = txOut.scriptPubkey;
//					String addr = scriptPub.address(tx.testnet);
					String addr = scriptPub.address(false);
					
					if(addr.equals(addressStr)){
						String.format("%064x", tx.id());
						System.out.println("found: "+String.format("%064x", tx.id())+":"+i);
						
						found=true;
						break;
					}
					
				}
			}
		}
		
	}
}