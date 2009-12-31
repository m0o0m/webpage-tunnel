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
public class SSLProxy extends Thread {

	private OutputStream os;

	private InputStream is;

	private String header;

	private Socket sock;

	public SSLProxy(final Socket sock, final InputStream is,
			final OutputStream os, final String header) {
		this.is = is;
		this.os = os;
		this.header = header;
		this.sock = sock;
	}

	@Override
	public void run() {
		if (ProxyConstants.logLevel >= 1) {
			System.out.println(new Date() + " SSLProxy :: Started "
					+ currentThread());
		}

		try {

			final String s = header.toString();
			final int a1 = s.indexOf("CONNECT ");
			final int a2 = s.indexOf(" HTTP/1.");
			final String s2 = s.substring(a1 + 8, a2);
			final int a3 = s2.indexOf(":");
			String mainhost = s2;
			String mainport = "443";
			if (a3 != -1) {
				// NOT REQUIRED Currently : Overriding Destination addr.Always
				// Bridge Server.
				mainhost = s2.substring(0, a3);
				mainport = s2.substring(a3 + 1);
			}

			if (ProxyConstants.logLevel >= 3) {
				System.out.println(new Date()
						+ " SSLProxy :: Extract Host =  & Port For CONNECT"
						+ currentThread() + " Host= " + mainhost + " Port="
						+ mainport);
			}

			Socket sslServerSocket = null;
			OutputStream sslServerOut = null;
			InputStream sslServerin = null;
			boolean error = false;

			try {
				// There are two instance of HTTPS Server running 443 , 8443
				// Other port not supported.
				if (mainport.equals("443")) {
					sslServerSocket = new Socket(
							ProxyConstants.HTTPSServer_443,
							ProxyConstants.HTTPSPort_443);
				}
				if (mainport.equals("8443")) {
					sslServerSocket = new Socket(
							ProxyConstants.HTTPSServer_8443,
							ProxyConstants.HTTPSPort_8443);
				}
				// Only 443 and 8443 supported.
				if (!mainport.equals("8443") && !mainport.equals("443")) {
					error = true;
				}
				sslServerOut = sslServerSocket.getOutputStream();
				sslServerin = sslServerSocket.getInputStream();
			} catch (final Exception e) {
				error = true;
			}

			if (error) {
				os.write("HTTP/1.0 500 Error\r\n".getBytes());
				os.write("Proxy-Agent: Mozilla\r\n".getBytes());
				os.write("\r\n".getBytes());
				os.flush();
				if (ProxyConstants.logLevel >= 1) {
					System.out
							.println(new Date()
									+ " SSLProxy :: Error could not connect to Local SSL server (due to non 443 || 8443 port) "
									+ currentThread());
				}
				return;
			}

			if (ProxyConstants.logLevel >= 2) {
				System.out
						.println(new Date()
								+ " SSLProxy :: Sending Reply HTTP/1.0 200 Connection established "
								+ currentThread());
			}

			os.write("HTTP/1.0 200 Connection established\r\n".getBytes());
			os.write("Proxy-Agent: Mozilla\r\n".getBytes());
			os.write("Connection: Keep-Alive\r\n".getBytes());
			os.write("\r\n".getBytes());
			os.flush();

			final SSLProxyThread thread1 = new SSLProxyThread(is, sslServerOut);
			thread1.setName("SSLProxyThread-1" + sock.getRemoteSocketAddress());
			thread1.start();

			final SSLProxyThread thread2 = new SSLProxyThread(sslServerin, os);
			thread1.setName("SSLProxyThread-2" + sock.getRemoteSocketAddress());
			thread2.start();

			if (ProxyConstants.logLevel >= 1) {
				System.out.println(new Date()
						+ " SSLProxy :: 2 Threads started  " + currentThread());
			}

			thread1.join();
			thread2.join();
			/*
			 * os.close(); is.close();
			 */
			sock.close();
			/*
			 * sslServerOut.close(); sslServerin.close();
			 */
			sslServerSocket.close();
			if (ProxyConstants.logLevel >= 1) {
				System.out.println(new Date() + " SSLProxy :: Finish "
						+ currentThread());
			}

		} catch (final Exception e) {
			if (ProxyConstants.logLevel >= 1) {
				System.out.println(new Date() + " SSLProxy :: Error "
						+ currentThread() + " " + e);
			}

		}
	}
}
