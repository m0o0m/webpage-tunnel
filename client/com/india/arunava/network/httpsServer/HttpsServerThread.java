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

import java.net.Socket;
import java.util.Date;

import javax.net.ssl.SSLSocket;

import com.india.arunava.network.utils.ProxyConstants;

/**
 * Local Https Server. User first connects to this SSL server through SSL tunnel
 * proxy.
 * 
 * @author http://arunava.in
 * 
 */
public class HttpsServerThread extends Thread {
	private SSLSocket sslsocket;

	private int whichSSLServer;

	public HttpsServerThread(final SSLSocket sslsocket, final int whichSSLServer) {
		this.sslsocket = sslsocket;
		this.whichSSLServer = whichSSLServer;
	}

	@Override
	public void run() {
		if (ProxyConstants.logLevel >= 1) {
			System.out.println(new Date() + " HttpsServerThread Started :: "
					+ currentThread());
		}
		try {
			if (ProxyConstants.logLevel >= 1) {
				System.out
						.println(new Date()
								+ " HttpsServerThread Connectiong to local httpProxy server :: "
								+ currentThread());
			}

			final Socket toHttpProxy_Recur = new Socket(
					ProxyConstants.HTTPProxyHost, ProxyConstants.HTTPProxyPort);
			// Read from out Browser and write to server Start
			final HttpsServerBrowserThread thread2 = new HttpsServerBrowserThread(
					toHttpProxy_Recur, sslsocket, whichSSLServer);
			thread2.start();

			// Read from out server and write to browser Start
			final HttpsServerOutgoingThread thread1 = new HttpsServerOutgoingThread(
					toHttpProxy_Recur, sslsocket);
			thread1.start();

			if (ProxyConstants.logLevel >= 1) {
				System.out.println(new Date()
						+ " HttpsServerThread :: Finished :: "
						+ currentThread());
			}

		} catch (final Exception exception) {
			if (ProxyConstants.logLevel >= 1) {
				System.out.println(new Date() + " HttpsServerThread Error :: "
						+ currentThread() + " " + exception);
			}
		}
	}
}
