package com.example.demo;

import com.bigchaindb.builders.BigchainDbConfigBuilder;
import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.model.MetaData;
import com.bigchaindb.model.Transaction;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.security.KeyPair;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@SpringBootApplication
public class DemoApplication {

    public static final String HTTPS_TEST_BIGCHAINDB_COM = "https://test.bigchaindb.com/";

    public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner test(){
		return args -> {
			BigchainDbConfigBuilder
					.baseUrl(HTTPS_TEST_BIGCHAINDB_COM).setup();

            // prepare your keys
            net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator();
            KeyPair keyPair = edDsaKpg.generateKeyPair();

            // New asset
            Map<String, String> assetData = new TreeMap<String, String>() {{
                put("city", "London/UK");
            }};

            // New metadata
            MetaData metaData = new MetaData();
            metaData.setMetaData("field1", "Once");
            metaData.setMetaData("field2", "upon");
            metaData.setMetaData("field3", "a");
            metaData.setMetaData("field4", "time");
            metaData.setMetaData("field5", ",");
            metaData.setMetaData("field6", "there");
            metaData.setMetaData("field7", "was");
            metaData.setMetaData("field8", "a");
            metaData.setMetaData("field9", "little");
            metaData.setMetaData("field10", "princess...");

            // Set up, sign, and send your transaction
            Transaction createTransaction = BigchainDbTransactionBuilder
                    .init()
                    .addAssets(assetData, TreeMap.class)
                    .addMetaData(metaData)
                    .operation(Operations.CREATE)
                    .buildAndSign((EdDSAPublicKey) keyPair.getPublic(), (EdDSAPrivateKey) keyPair.getPrivate())
                    .sendTransaction();

            System.out.println("curl -X GET https://test.bigchaindb.com/api/v1/transactions/" +
                    createTransaction.getId());
		};
	}
}

