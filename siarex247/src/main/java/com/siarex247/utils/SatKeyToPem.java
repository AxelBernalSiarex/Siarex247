package com.siarex247.utils;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;

public class SatKeyToPem {
	public static final Logger logger = Logger.getLogger("siarex247");
	
    public static void convertirKeytoPem(String keyPath, String password, String rutaSalida){
        // Rutas y contraseña (ajústalo)
        //Path keyDerPath = Path.of("C:/ruta/llave.key");
        // String password = "TU_CONTRASEÑA_DEL_SAT";
        //Path outPemPath = Path.of("C:/ruta/llave.pem");
    	
    	try {
        	Path keyDerPath = Path.of(keyPath);
        	Path outPemPath = Path.of(rutaSalida);

        	
            // Registrar el provider de BouncyCastle (a veces no es necesario, pero es seguro hacerlo)
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            byte[] keyBytes = Files.readAllBytes(keyDerPath);

            // El .key del SAT es normalmente un PKCS#8 cifrado
            PKCS8EncryptedPrivateKeyInfo encInfo = new PKCS8EncryptedPrivateKeyInfo(keyBytes);

            // Construimos un decryptor con la contraseña
            JceOpenSSLPKCS8DecryptorProviderBuilder decryptorBuilder =
                    new JceOpenSSLPKCS8DecryptorProviderBuilder();
            InputDecryptorProvider decryptorProvider =
                    decryptorBuilder.build(password.toCharArray());

            PrivateKeyInfo privateKeyInfo = encInfo.decryptPrivateKeyInfo(decryptorProvider);

            // Lo convertimos a PrivateKey de Java
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyInfo.getEncoded());
            KeyFactory kf = KeyFactory.getInstance(privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm().getId(), "BC");
            PrivateKey privateKey = kf.generatePrivate(keySpec);

            // Guardar en formato PEM
            try (JcaPEMWriter writer = new JcaPEMWriter(
                    new OutputStreamWriter(new FileOutputStream(outPemPath.toFile()), StandardCharsets.UTF_8))) {
                writer.writeObject(privateKey);
            }
            logger.info("Listo. Se generó: " + outPemPath);
            
    	}catch(Exception e) {
    		Utils.imprimeLog("", e);
    	}
    	

    }
}