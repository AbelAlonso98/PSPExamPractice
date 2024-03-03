package main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;

public class SignAFile {

	public static void main(String[] args) throws Exception {
		KeyStore ks = KeyStore.getInstance("pkcs12");
		char[] pwdArray = "4327".toCharArray();
		ks.load(new FileInputStream("C:\\cygwin64\\home\\Abel Alonso\\certs\\keystore.p12"), pwdArray);
		PrivateKey privKey = (PrivateKey) ks.getKey("abel", pwdArray);
		// El nombre del algoritmo sale de los apuntes de programacion segura.
		
		try (BufferedInputStream in = new BufferedInputStream(SignAFile.class.getResourceAsStream("/OpenSSL.pdf"));
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(System.getProperty("user.home") + "//Desktop//OpenSSL.pdf.sign"))) {
			Signature sign = Signature.getInstance("SHA512withRSA");
			sign.initSign(privKey);
			// El readAllBytes puede dar problemas porque pasa el contenido entero a la memoria, si el fichero es muy grande
			// podemos quedar sin memoria.
//			sign.update(in.readAllBytes());

			byte[] buffer = new byte[1024];
			int n; // para contar el numero de bytes leidos
			while((n = in.read(buffer))> 0) {
				// Cuidado con el final de fichero, puede que no sean 1024 bytes y corrompa la firma.
				sign.update(buffer, 0, n);
			}
			byte[] signature = sign.sign();
			out.write(signature);
			

		}
		

	}

}
