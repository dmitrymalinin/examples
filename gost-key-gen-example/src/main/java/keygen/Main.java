package keygen;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jcajce.spec.GOST3410ParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Main {
	private static final Logger logger = Logger.getLogger(Main.class.getName());
	
	private static final char[] keyStorePassword = "changeit".toCharArray();
	private static final String keyStoreFile = "/home/dm/test_keystore_gost.pfx";
	private static final String keyStoreType = "pkcs12";	
	private static final String alias = "test";
	private static final String providerName = "BC";
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	private static final String keyAlg = "ECGOST3410-2012"; // "1.2.643.7.1.1.1.1"	Алгоритм ГОСТ Р 34.10-2012 для ключей длины 256 бит, используемый при экспорте/импорте ключей
	private static final String algParams = "Tc26-Gost-3410-12-256-paramSetA"; // см. org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves.objIds
	private static final String sigAlg = "GOST3411-2012-256WITHECGOST3410-2012-256";// "1.2.643.7.1.1.3.2"	Алгоритм цифровой подписи ГОСТ Р 34.10-2012 для ключей длины 256 бит
	
	// An X.500 Distinguished Name (DN) is composed of a series of Relative Distinguished Names (RDN)
	private static final X500Name subject = new X500NameBuilder()
					.addRDN(BCStyle.ST, "Moscow")
					.addRDN(BCStyle.L, "Moscow")
					.addRDN(BCStyle.CN, "malinin")
					.addRDN(BCStyle.EmailAddress, "d_malinin@mail.ru").build();
	
	public static void main(String[] args) throws Exception {
		logger.info("Create "+keyAlg+" KeyPairGenerator...");
		KeyPairGenerator gen = KeyPairGenerator.getInstance(keyAlg, providerName);
		gen.initialize(new GOST3410ParameterSpec(algParams)); 
		KeyPair keyPair = gen.generateKeyPair();		
		logger.info(String.format("generated keyPair: \n%s\n%s\n", keyPair.getPublic(), keyPair.getPrivate()));
		
		X500Name issuer = subject; // self-signed		
		BigInteger serial = BigInteger.valueOf((long) (Long.MAX_VALUE*Math.random()));
		Date notBefore = new Date();
		Date notAfter = new Date(notBefore.getTime()+TimeUnit.DAYS.toMillis(365*10));
		SubjectPublicKeyInfo pubKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
		X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(issuer, serial, notBefore, notAfter, subject, pubKeyInfo);
		
		X509CertificateHolder certHolder = certBuilder.build(
		        new org.bouncycastle.operator.jcajce.JcaContentSignerBuilder(sigAlg).build(keyPair.getPrivate())
		);
		JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
		X509Certificate cert = certConverter.getCertificate(certHolder);
		logger.info("X509Certificate: "+cert);
		
		saveToKeyStore(Collections.singletonList(cert), keyPair.getPrivate(), keyStoreFile);
		
		readKeyStore(keyStoreFile);
	}

	
	/**
	 * Сохраняет цепочку сертификатов и приватный ключ в KeyStore
	 * @param certChain
	 * @param privKey
	 * @param keyStoreFile
	 * @param storePass
	 * @throws Exception
	 */
	private static void saveToKeyStore(List<X509Certificate> certChain, PrivateKey privKey, String keyStoreFile) throws Exception
	{
		logger.info("saveToKeyStore "+keyStoreFile);
		KeyStore keyStore = KeyStore.getInstance(keyStoreType, providerName);
		keyStore.load(null, keyStorePassword);
		keyStore.setKeyEntry(alias, privKey, keyStorePassword, certChain.toArray(new X509Certificate[certChain.size()]));
		
		try (OutputStream out = Files.newOutputStream(Paths.get(keyStoreFile));)
		{
			keyStore.store(out, keyStorePassword);
		}
	}
	
	/**
	 * Читает сертификаты и ключи из KeyStore
	 * @param keyStoreFile
	 * @param keyStorePassword
	 * @throws Exception
	 */
	private static void readKeyStore(String keyStoreFile) throws Exception
	{
		logger.info("readKeyStore "+keyStoreFile);
		KeyStore keyStore = KeyStore.getInstance(keyStoreType, providerName);
		
		try (InputStream in = Files.newInputStream(Paths.get(keyStoreFile)))
		{
			keyStore.load(in, keyStorePassword);
			if (keyStore.containsAlias(alias))
			{
				final Certificate[] chain = keyStore.getCertificateChain(alias);
				if (chain != null)
				{
					for (Certificate certificate : chain) {
						logger.info("* * * * *\nCertificate: "+certificate);
					}
				} else
				{
					logger.severe("Alias '"+alias+"' не содержит цепочки сертификатов");
				}
				final Key key = keyStore.getKey(alias, keyStorePassword);
				if (key != null)
				{
					logger.info("Key: "+key);
				} else
				{
					logger.severe("Alias '"+alias+"' не содержит ключа");
				}
			} else
			{
				logger.severe("Alias '"+alias+"' не найден");
			}
		}
	}
}
