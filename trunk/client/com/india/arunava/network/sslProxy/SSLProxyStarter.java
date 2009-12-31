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

package com.india.arunava.network.sslProxy;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import com.india.arunava.network.utils.ProxyConstants;

/**
 * Start SSL Proxy Server in local port which receive CONNECT Command.
 * 
 * @author http://arunava.in
 */
public class SSLProxyStarter extends Thread {
	public static int started = 0;

	public static void main(final String[] args) {
		new SSLProxyStarter().run();
	}

	@Override
	public void run() {
		if (ProxyConstants.logLevel >= 1) {
			System.out.println(new Date()
					+ " SSLProxyStarter :: SSL Proxy started :: On "
					+ ProxyConstants.SSLProxyPort);
		}

		try {
			final ServerSocket sc = new ServerSocket(
					ProxyConstants.SSLProxyPort);
			while (true) {
				started = 1;
				final Socket cSocket = sc.accept();
				new SSLProxyHandler(cSocket).start();
				if (ProxyConstants.logLevel >= 2) {
					System.out.println(new Date()
							+ " SSLProxyStarter :: New client connected from  "
							+ cSocket);
				}
			}
		} catch (final Exception e) {
			started = 2;
			System.out.println(new Date() + "SSLProxyStarter :: Error " + e);
		}
	}
}