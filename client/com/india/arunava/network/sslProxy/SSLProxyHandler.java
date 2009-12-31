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

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import com.india.arunava.network.utils.ProxyConstants;

/**
 * Thread which receive CONNECT Command from user and send it to Local SSL
 * Server.
 * 
 * @author http://arunava.in
 * 
 */
public class SSLProxyHandler extends Thread {

	private Socket socket = null;

	public SSLProxyHandler(final Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		if (ProxyConstants.logLevel >= 1) {
			System.out.println(new Date()
					+ " SSLProxyHandler :: ProxyHandler Started "
					+ currentThread());
		}
		try {
			final OutputStream os = socket.getOutputStream();
			final InputStream is = socket.getInputStream();

			if (ProxyConstants.logLevel >= 1) {
				System.out.println("Reading header :: ProxyHandler "
						+ currentThread());
			}
			int rdL;
			final StringBuffer sb = new StringBuffer(9999);
			while (true) {
				rdL = is.read();
				if (rdL == -1) {
					break;
				}
				sb.append((char) rdL);
				if (sb.indexOf("\r\n\r\n") != -1) {
					break;
				}
			}
			if (ProxyConstants.logLevel >= 2) {
				System.out.println(new Date()
						+ " SSLProxyHandler : Header Received from browser ::"
						+ currentThread() + "\n\n" + sb);
			}
			if (ProxyConstants.logLevel >= 2) {
				System.out
						.println(new Date()
								+ " SSLProxyHandler : Header Reading completing :: Checking protocol "
								+ currentThread());
			}

			if (sb.toString().toLowerCase().startsWith("connect")) {
				if (ProxyConstants.logLevel >= 2) {
					System.out
							.println(new Date()
									+ " SSLProxyHandler :: Its a SSL ( Connect ) request"
									+ currentThread());
				}
				new SSLProxy(socket, is, os, sb.toString()).start();
			}
			if (ProxyConstants.logLevel >= 1) {
				System.out
						.println(new Date()
								+ " SSLProxyHandler :: Child Threads are started :: Own Finish "
								+ currentThread());
			}
		} catch (final Exception exp) {
			if (ProxyConstants.logLevel >= 1) {
				System.out.println(new Date() + " SSLProxyHandler :: Error "
						+ currentThread() + " " + exp);
			}
		}
	}
}