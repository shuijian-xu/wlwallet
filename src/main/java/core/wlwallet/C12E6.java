package core.wlwallet;

import java.math.BigInteger;
import java.util.ArrayList;

public class C12E6 {

	public static void main(String[] args) throws InterruptedException {

		String lastBlockHexStr = "";
		BigInteger lastBlockHex = new BigInteger(lastBlockHexStr, 16);

		String secrectStr = "";
		BigInteger secrect = Utils.little_endian_to_biginteger(Utils
				.hash256(secrectStr.getBytes()));

		PrivateKey privateKey = new PrivateKey(secrect);
		String addressStr = privateKey.point.address(true);

		byte[] h160 = Utils.decodeBase58(addressStr);

		String targetAdrress = "";
		byte[] targetH160 = Utils.decodeBase58(targetAdrress);
		Script targetScript = Script.p2pkhScript(targetH160);

		BigInteger fee = BigInteger.valueOf(500);

		String host = "seed.tbtc.petertodd.org";
		SimpleNode node = new SimpleNode(host, true);
		BloomFilter bf = new BloomFilter(BigInteger.valueOf(30),
				BigInteger.valueOf(5), BigInteger.valueOf(90210));

		bf.add(h160);

		node.handShake();

		node.send(bf.filterload());

		byte[] startBlock = Utils
				.getBytes(lastBlockHex, BigInteger.valueOf(32));

		GetHeadersMessage getheaders = new GetHeadersMessage(startBlock);

		node.send(getheaders);

		String m1 = "core.wlwallet.HeadersMessage";
		ArrayList al = new ArrayList<String>();
		al.add(m1);
		HeadersMessage headers = (HeadersMessage) node.waitFor(al);

		byte[] lastBlock = null;
		GetDataMessage getdata = new GetDataMessage();
		ArrayList<Block> blocks = headers.blocks;
		for (Block b : blocks) {
			if (!b.checkPow()) {
				System.out.println("proof of work is invalid");
				System.exit(1);
			}
			if (lastBlock != null && b.prevBlock != lastBlock) {
				System.out.println("chain broken");
				System.exit(1);
			}
			DataElement de = new DataElement(
					GetDataMessage.FILTERED_BLOCK_DATA_TYPE, b.hash());
			getdata.addData(de);
			lastBlock = b.hash();
		}

		node.send(getdata);

		byte[] prevTx = null;
		BigInteger prevIndex = null;
		BigInteger prevAmount = null;
		while (prevTx == null) {

			System.out.println("...");
			String m2 = "core.wlwallet.MerkleBlock";
			String m3 = "core.wlwallet.Tx";

			ArrayList a2 = new ArrayList<String>();
			a2.add(m2);
			a2.add(m3);

			Message message = node.waitFor(a2);

			if (message instanceof MerkleBlock) {
				MerkleBlock m = (MerkleBlock) message;
				if (!m.isValid()) {
					System.out.println("invalid merkle proof");
					System.exit(1);
				}
			} else {
				Tx tx = (Tx) message;
				tx.testnet = true;
				ArrayList<TxOut> txOuts = tx.txOuts;
				for (int i = 0; i < txOuts.size(); i++) {
					TxOut txOut = txOuts.get(i);
					Script scriptPub = txOut.scriptPubkey;
					String addr = scriptPub.address(true);
					if (addr.equals(addressStr)) {
						prevTx = tx.hash();
						prevIndex = BigInteger.valueOf(i);
						prevAmount = txOut.amount;
						String.format("%064x", tx.id());
						System.out.println("found: "
								+ String.format("%064x", tx.id()) + ":" + i);
					}
				}

			}
		}
		
		BigInteger prevTxBigInt = new BigInteger(1, prevTx);
		TxIn txIn = new TxIn(prevTxBigInt, prevIndex);
		
		ArrayList<TxIn> txIns = new ArrayList<TxIn>();
		txIns.add(txIn);
		
		BigInteger outputAmount = prevAmount.subtract(fee);
		TxOut txOut = new TxOut(outputAmount, targetScript);
		ArrayList<TxOut> txOuts = new ArrayList<TxOut>();
		
		Tx txObj = new Tx(BigInteger.ONE, txIns, txOuts, BigInteger.ZERO, true);
		
		System.out.println(txObj.signInput(BigInteger.ZERO, privateKey));
		
		node.send(txObj);
		Thread.sleep(1000);
		
		GetDataMessage getdata2 = new GetDataMessage();
		
		DataElement txDe = new DataElement(
				GetDataMessage.TX_DATA_TYPE, txObj.hash());
		getdata2.addData(txDe);
		node.send(getdata2);
		
		String m4 = "core.wlwallet.Tx";
		ArrayList a4 = new ArrayList<String>();
		a4.add(m4);
		Tx receivedTx = (Tx) node.waitFor(a4);
		
		if(receivedTx.id().equals(txObj.id())){
			System.out.println("success");
		}
		
	}

}
