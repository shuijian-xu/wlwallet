package core.wlwallet;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class C10S3 {

	public static void main(String[] args) throws IOException {

		String host = "217.23.5.68";
		int port = 8333;
		SimpleNode node = new SimpleNode(host, false);
		node.handShake();

		byte[] genesisBlock = Block.GENESISBLOCK;
		ByteArrayInputStream bais = new ByteArrayInputStream(genesisBlock);
		Block genesis = Block.parse(bais);
		bais.close();
		GetHeadersMessage getheaders = new GetHeadersMessage(genesis.hash());
		node.send(getheaders);

	}

}
