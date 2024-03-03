package main;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;

public class VerifySign {

	public static void main(String[] args) {
		// Necesito abrir el documento original y la firma

		try (BufferedInputStream inF = new BufferedInputStream(new FileInputStream(System.getProperty("user.home") + "//Documents//OpenSSL.pdf"));
				BufferedInputStream inS = new BufferedInputStream(
						new FileInputStream(System.getProperty("user.home") + "/Documents/OpenSSL.pdf.sign.cifrado.pdf"));) {
			Signature sign = Signature.getInstance("SHA512withRSA");

			KeyStore ks = KeyStore.getInstance("pkcs12");
			char[] pwdArray = "4327".toCharArray();
			ks.load(new FileInputStream("C:\\cygwin64\\home\\Abel Alonso\\certs\\keystore.p12"), pwdArray);
//			Certificate miCert = ks.getCertificate("abel");
			PublicKey pubKey = ks.getCertificate("Abel").getPublicKey();
			sign.initVerify(pubKey);

			byte[] buffer = new byte[1024];
			int n; // para contar el numero de bytes leidos
			while ((n = inF.read(buffer)) > 0) {
				// Cuidado con el final de fichero, puede que no sean 1024 bytes y corrompa la
				// firma.
				sign.update(buffer, 0, n);
			}
			System.out.println(sign.verify(inS.readAllBytes()));
			// Aqui si se puede usar el readAllBytes porque es muy pequeña (512 bytes
			// en este caso por el algoritmo)

		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}

	}

}
