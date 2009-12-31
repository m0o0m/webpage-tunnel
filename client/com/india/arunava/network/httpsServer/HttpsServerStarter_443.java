/*		   
                     GNU GENERAL PUBLIC LICENSE
		       Version 2, June 1991

   Copyright (C)  2009 Arunava Bhowmick ( http://arunava.in ).
                       Kolkata India
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 If it is run professionally Powered by India web proxy must be mentioned.


If any portion of this section is held invalid or unenforceable under
any particular circumstance, the balance of the section is intended to
apply and the section as a whole is intended to apply in other
circumstances.

It is not the purpose of this section to induce you to infringe any
patents or other property right claims or to contest validity of any
such claims; this section has the sole purpose of protecting the
integrity of the free software distribution system, which is
implemented by public license practices.  Many people have made
generous contributions to the wide range of software distributed
through that system in reliance on consistent application of that
system; it is up to the author/donor to decide if he or she is willing
to distribute software through any other system and a licensee cannot
impose that choice.
 */

package com.india.arunava.network.httpsServer;

import java.util.Date;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import com.india.arunava.network.utils.ProxyConstants;

/**
 * Local Https Server. User first connects to this SSL server through SSL tunnel
 * proxy.
 * 
 * @author http://arunava.in
 * 
 */
public class HttpsServerStarter_443 extends Thread {
	public static int started = 0;

	public static void main(final String[] args) {
		// Set SSL Certificate .
		System.setProperty("javax.net.ssl.keyStore",
				ProxyConstants.KEYSTORE_PATH);
		System.setProperty("javax.net.ssl.keyStorePassword",
				ProxyConstants.KEYSTORE_PASSWORD);

		/*
		 * System.setProperty("javax.net.ssl.trustStore", "c:\\proxy_trust");
		 * System.setProperty("javax.net.ssl.trustStorePassword", "aaaaaaa");
		 */

		new HttpsServerStarter_443().run();
	}

	@Override
	public void run() {
		if (ProxyConstants.logLevel >= 1) {
			System.out.println(new Date()
					+ " HttpsServerStarter :: HTTPS-Server started :: on "
					+ ProxyConstants.HTTPSPort_443);
		}
		try {

			if (ProxyConstants.logLevel >= 1) {
				System.out.println(new Date()
						+ " HttpsServerStarter :: Init HttpsServer ...... ");
			}
			final SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory
					.getDefault();
			final SSLServerSocket sslserversocket = (SSLServerSocket) sslserversocketfactory
					.createServerSocket(ProxyConstants.HTTPSPort_443);

			while (true) {
				started = 1;
				final SSLSocket sslsocket = (SSLSocket) sslserversocket
						.accept();
				new HttpsServerThread(sslsocket, 443).start();
				if (ProxyConstants.logLevel >= 1) {
					System.out
							.println(new Date()
									+ " HttpsServerStarter :: Client connected to HttpsServer :: "
									+ sslsocket.getRemoteSocketAddress());
				}
			}
		} catch (final Exception e) {
			started = 2;
			System.out.println(new Date() + " " + e);
		}
	}
}
