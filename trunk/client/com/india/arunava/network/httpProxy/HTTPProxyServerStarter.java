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

package com.india.arunava.network.httpProxy;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import com.india.arunava.network.utils.ProxyConstants;

/**
 * Start HTTP Local Proxy server..
 * 
 * @author http://arunava.in
 * 
 */
public class HTTPProxyServerStarter extends Thread {
	public static int started = 0;

	public static void main(final String[] args) {
		new HTTPProxyServerStarter().run();
	}

	@Override
	public void run() {

		System.out.println(new Date() + " Encription Enabled ? "
				+ ProxyConstants.ENCRYPTION_ENABLED);

		if (ProxyConstants.logLevel >= 1) {
			System.out.println(new Date()
					+ " HTTPProxyServerStarter Started :: ");
		}

		try {
			final ServerSocket serverSocket = new ServerSocket(
					ProxyConstants.HTTPProxyPort);

			while (true) {
				started = 1;
				final Socket incoming = serverSocket.accept();
				new HTTPProxyStarterHelper(incoming).start();
				if (ProxyConstants.logLevel >= 1) {
					System.out
							.println(new Date()
									+ " HTTPProxyServerStarter New Client connected From  :: "
									+ incoming);
				}
			}
		} catch (final Exception e) {
			started = 2;
			System.out.println(new Date() + " HTTPProxyServerStarter Error :: "
					+ e);
		}

	}
}

/**
 * HTTP Proxy starter.
 * 
 */
class HTTPProxyStarterHelper extends Thread {
	Socket incoming;

	public HTTPProxyStarterHelper(final Socket incoming) {
		this.incoming = incoming;
	}

	@Override
	public void run() {

		try {
			Socket outgoing = null;
			// If organization http present !!!!
			if (!ProxyConstants.ORGANIZATION_HTTP_PROXY_ENABLED) {
				outgoing = new Socket(ProxyConstants.webPHP_HOST_HTTP,
						ProxyConstants.webPHP_PORT_HTTP);
			} else {

				outgoing = new Socket(
						ProxyConstants.ORGANIZATION_HTTP_PROXY_HOST,
						ProxyConstants.ORGANIZATION_HTTP_PROXY_PORT);
			}

			// -->
			final HTTPProxyThreadBrowser thread2 = new HTTPProxyThreadBrowser(
					outgoing, incoming);
			thread2.start();
			// <----
			final HTTPProxyThreadServer thread1 = new HTTPProxyThreadServer(
					outgoing, incoming);
			thread1.start();
		} catch (final Exception e) {
			if (ProxyConstants.logLevel >= 1) {
				System.out.println(new Date()
						+ " HTTPProxyServerStarter Error :: " + e);
			}
		}
	}

}
