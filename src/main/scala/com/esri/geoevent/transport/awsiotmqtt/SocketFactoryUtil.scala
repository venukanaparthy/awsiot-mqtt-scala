package com.esri.geoevent.transport.awsiotmqtt

import java.io.{ByteArrayInputStream, InputStream, InputStreamReader}
import java.nio.file.{Files, Paths}
import java.security.cert.{CertificateFactory, X509Certificate}
import java.security.{KeyPair, KeyStore, Security}
import javax.net.ssl.{KeyManagerFactory, SSLContext, SSLSocketFactory, TrustManagerFactory}

import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.bouncycastle.openssl.{PEMKeyPair, PEMParser}

/** Factory for [[javax.net.ssl.SSLSocketFactory]] instances. */
object SocketFactoryUtil {

  /**
   * Generate [[javax.net.ssl.SSLSocketFactory]] from pem file paths.
   *
   * @param rootCaFilePath Root CA file path
   * @param certFilePath Certificate file path
   * @param keyFilePath Private key file path
   * @return Generated [[javax.net.ssl.SSLSocketFactory]]
   */
  def generateFromFilePath(rootCaFilePath:String, certFilePath:String, keyFilePath:String):SSLSocketFactory = {
    Security.addProvider(new BouncyCastleProvider())

    // load Root CA certificate
    val rootCaParser:PEMParser  = new PEMParser(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(rootCaFilePath)))))
    val rootCaCertHolder:X509CertificateHolder = rootCaParser.readObject().asInstanceOf[X509CertificateHolder]
    val rootCaCert:X509Certificate = convertToJavaCertificate(rootCaCertHolder)
    rootCaParser.close()

    // load Server certificate
    val certParser:PEMParser = new PEMParser(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(certFilePath)))))
    val serverCertHolder:X509CertificateHolder = certParser.readObject.asInstanceOf[X509CertificateHolder]
    val serverCert:X509Certificate = convertToJavaCertificate(serverCertHolder)
    certParser.close()

    // load Private Key
    val keyParser:PEMParser = new PEMParser(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(keyFilePath)))))
    val pemKeyPair:PEMKeyPair = keyParser.readObject.asInstanceOf[PEMKeyPair]
    val keyPair:KeyPair = new JcaPEMKeyConverter().getKeyPair(pemKeyPair)
    keyParser.close()

    // Root CA certificate is used to authenticate server
    val rootCAKeyStore:KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
    rootCAKeyStore.load(null, null)
    rootCAKeyStore.setCertificateEntry("ca-certificate", convertToJavaCertificate(rootCaCertHolder))
    val tmf:TrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    tmf.init(rootCAKeyStore);

    // client key and certificates are sent to server so it can authenticate us
    val ks:KeyStore  = KeyStore.getInstance(KeyStore.getDefaultType())
    ks.load(null, null)
    ks.setCertificateEntry("certificate", serverCert)
    ks.setKeyEntry("private-key", keyPair.getPrivate(), "DummyPassword".toCharArray, Array(serverCert))
    val kmf:KeyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
    kmf.init(ks, "DummyPassword".toCharArray());

    // finally, create SSL socket factory
    val context:SSLContext = SSLContext.getInstance("TLSv1.2")
    context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null)

    context.getSocketFactory()
  }


  def convertToJavaCertificate(certificateHolder:X509CertificateHolder):X509Certificate = {
     val is:InputStream = new ByteArrayInputStream(certificateHolder.toASN1Structure.getEncoded);
    try {
      CertificateFactory.getInstance("X.509").generateCertificate(is).asInstanceOf[X509Certificate]
    } finally is.close()
  }
}
