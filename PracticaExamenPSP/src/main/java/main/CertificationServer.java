package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CertificationServer {
	
	static KeyStore ks;


	public static void main(String[] args) {
		 try {
			ks = KeyStore.getInstance(KeyStore.getDefaultType());
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 ExecutorService es = Executors.newCachedThreadPool();
		try (ServerSocket ss = new ServerSocket(9000)) {
			while (true) {
				try {
					System.out.println("Waiting for clients.");
					Socket s = ss.accept();
					System.out.println("Client joined: " + s.getInetAddress().toString());
					es.submit(new Certificator(s));
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		} catch (IOException e1) {
			System.err.println("In/out error: " + e1);
		}

	}

}
