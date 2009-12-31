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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import com.india.arunava.network.sslProxy.SSLProxy;
import com.india.arunava.network.utils.ProxyConstants;
import com.india.arunava.network.utils.SimpleEncryptDecrypt;

/**
 * Reading from browser and write to server
 * 
 * @author http://arunava.in
 * 
 */
class HTTPProxyThreadBrowser extends Thread {

	private Socket incoming;

	private Socket outgoing;

	HTTPProxyThreadBrowser(final Socket outgoing, final Socket incoming) {
		this.incoming = incoming;
		this.outgoing = outgoing;
	}

	@Override
	public void run() {

		ByteArrayOutputStream mainBuffer = new ByteArrayOutputStream();

		long count1 = 0;
		long count2 = 0;

		if (ProxyConstants.logLevel >= 1) {
			System.out.println(new Date()
					+ " HTTPProxyThreadBrowser ::Started  " + currentThread());
		}

		final byte[] buffer = new byte[ProxyConstants.MAX_BUFFER];
		int numberRead = 0;
		OutputStream server;
		InputStream client;

		try {

			client = incoming.getInputStream();
			server = outgoing.getOutputStream();
			String proxyAuth = "";
			// If Organization proxy required Authentication
			if (!ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_NAME.equals("")) {
				final String authString = ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_NAME
						+ ":"
						+ ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_PASS;
				proxyAuth = "Basic "
						+ new sun.misc.BASE64Encoder().encode(authString
								.getBytes());
			}

			int rdL;
			final StringBuffer header = new StringBuffer(9999);
			while (true) {
				rdL = client.read();
				if (rdL == -1) {
					break;
				}
				header.append((char) rdL);
				if (header.indexOf("\r\n\r\n") != -1) {
					break;
				}
			}

			if (ProxyConstants.logLevel >= 2) {
				System.out.println(new Date()
						+ " HTTPProxyThreadBrowser :: Request header   = "
						+ currentThread() + " \n" + header);
			}

			final String allInpRequest = header.toString();

			// modify: if it is https request, resend to sslproxy
			if (allInpRequest.startsWith("CONNECT ")) {
				new SSLProxy(incoming, incoming.getInputStream(), incoming
						.getOutputStream(), allInpRequest).start();
				return;
			}
			// modify end

			String host = "";
			String port = "";
			String tmpHost = "";
			final int indexOf = allInpRequest.toLowerCase().indexOf("host:");
			if (indexOf != -1) {
				final int immediateNeLineChar = allInpRequest.toLowerCase()
						.indexOf("\r\n",
								allInpRequest.toLowerCase().indexOf("host:"));
				tmpHost = allInpRequest.substring(
						allInpRequest.toLowerCase().indexOf("host:") + 5,
						immediateNeLineChar).trim();
				final int isPortThere = tmpHost.indexOf(":");
				if (isPortThere != -1) {
					host = tmpHost.substring(0, tmpHost.indexOf(":"));
					port = tmpHost.substring(tmpHost.indexOf(":") + 1);

				} else {
					port = "80";
					host = tmpHost;
				}
			}

			// ////////////////// Added since rapidshare not opening
			// Making it relative request.

			String modifyGet = header.toString().toLowerCase();

			if (modifyGet.startsWith("get http://")) {
				int i2 = modifyGet.indexOf("/", 11);
				header.replace(4, i2, "");
			}
			if (modifyGet.startsWith("post http://")) {
				int i2 = modifyGet.indexOf("/", 12);
				header.replace(5, i2, "");
			}

			// ///////////////////////////////////////////////

			final String proxyServerURL = ProxyConstants.webPHP_URL_HTTP;
			String isSecure = "";
			final String HeaderHost = ProxyConstants.webPHP_HOST_HTTP;

			if (header.indexOf("X-IS-SSL-RECURSIVE:") == -1) {
				isSecure = "N";
			} else {
				isSecure = "Y";
				// Now detect which Port 443 or 8443 ?
				// Like : abcd X-IS-SSL-RECURSIVE: 8443
				final int p1 = header.indexOf("X-IS-SSL-RECURSIVE: ");
				port = header.substring(p1 + 20, p1 + 20 + 4);
				port = "" + Integer.valueOf(port).intValue();
			}

			if (ProxyConstants.logLevel >= 1) {
				System.out.println(new Date()
						+ " HTTPProxyThreadBrowser ::Started  "
						+ currentThread() + "URL Information :\n" + "Host="
						+ host + " Port=" + port + " ProxyServerURL="
						+ proxyServerURL + " HeaderHost=" + HeaderHost);
			}

			// Get Content length
			String contentLenght = "";
			final int contIndx = header.toString().toLowerCase().indexOf(
					"content-length: ");
			if (contIndx != -1) {
				final int endI = header.indexOf("\r\n", contIndx + 17);
				contentLenght = header.substring(contIndx + 16, endI);
			}

			String data = header + "";
			data = data.replaceFirst("\r\n\r\n",
					"\r\nConnection: Close\r\n\r\n");

			// Replace culprit KeepAlive
			// Should have used Regex
			data = data.replaceFirst("Keep-Alive: ", "X-Dummy-1: ");
			data = data.replaceFirst("keep-alive: ", "X-Dummy-1: ");
			data = data.replaceFirst("Keep-alive: ", "X-Dummy-1: ");
			data = data.replaceFirst("keep-Alive: ", "X-Dummy-1: ");

			data = data.replaceFirst("keep-alive", "Close");
			data = data.replaceFirst("Keep-Alive", "Close");
			data = data.replaceFirst("keep-Alive", "Close");
			data = data.replaceFirst("Keep-alive", "Close");

			int totallength = 0;
			if (!contentLenght.equals("")) {
				totallength = Integer.parseInt(contentLenght.trim())
						+ (data.length() + 61 + 1);
			} else {
				totallength = (data.length() + 61 + 1);
			}

			String header1 = "";
			header1 = header1 + "POST " + proxyServerURL + " HTTP/1.1\r\n";
			header1 = header1 + "Host: " + HeaderHost + "\r\n";
			header1 = header1 + "Connection: Close\r\n";
			header1 = header1 + "Content-Length: " + totallength + "\r\n";
			header1 = header1 + "Cache-Control: no-cache\r\n";

			if (!ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_NAME.equals("")) {
				header1 = header1 + "Proxy-Authorization: " + proxyAuth
						+ "\r\n";
			}

			count1 = totallength;

			header1 = header1 + "\r\n";
			server.write(header1.getBytes());
			server.flush();

			if (ProxyConstants.ENCRYPTION_ENABLED) {
				// Let know PHP waht are we using
				server.write(("Y".getBytes()));

				server.write(SimpleEncryptDecrypt.enc(host.getBytes()));
				// Padding with space
				for (int i = 0; i < 50 - host.length(); i++) {
					server.write(SimpleEncryptDecrypt.enc(" ".getBytes()));
				}
				server.write(SimpleEncryptDecrypt.enc(port.getBytes()));
				// Padding with space
				for (int i = 0; i < 10 - port.length(); i++) {
					server.write(SimpleEncryptDecrypt.enc(" ".getBytes()));
				}
				// Write fsockopen info
				server.write(SimpleEncryptDecrypt.enc(isSecure.getBytes()));

				// It is destination header
				server.write(SimpleEncryptDecrypt.enc(data.getBytes()));

			} else {
				// Let know PHP waht are we using
				server.write(("N".getBytes()));

				server.write(host.getBytes());
				// Padding with space
				for (int i = 0; i < 50 - host.length(); i++) {
					server.write(" ".getBytes());
				}
				server.write(port.getBytes());
				// Padding with space
				for (int i = 0; i < 10 - port.length(); i++) {
					server.write(" ".getBytes());
				}
				// Write fsockopen info
				server.write(isSecure.getBytes());

				// It is destination header
				server.write(data.getBytes());

			}
			server.flush();

			if (ProxyConstants.logLevel >= 2) {
				System.out.println(new Date()
						+ " HTTPProxyThreadBrowser :: destination header   = "
						+ currentThread() + " \n" + data);
			}

			while (true) {
				numberRead = client.read(buffer);
				count2 = count2 + numberRead;
				if (numberRead == -1) {
					outgoing.close();
					incoming.close();
					break;
				}

				if (ProxyConstants.ENCRYPTION_ENABLED) {
					server.write(SimpleEncryptDecrypt.enc(buffer, numberRead),
							0, numberRead);
				} else {
					server.write(buffer, 0, numberRead);
				}
				if (ProxyConstants.logLevel >= 3) {
					final ByteArrayOutputStream bo = new ByteArrayOutputStream();
					bo.write(buffer, 0, numberRead);
					System.out.println("::Readingbody::" + bo
							+ "::Readingbody::");
				}
			}

			if (ProxyConstants.logLevel >= 1) {
				System.out.println(new Date()
						+ " HTTPProxyThreadBrowser :: Finish "
						+ currentThread());
			}
		} catch (final Exception e) {
		}
		synchronized (ProxyConstants.MUTEX) {
			ProxyConstants.TOTAL_TRANSFER = ProxyConstants.TOTAL_TRANSFER
					+ count1 + count2;
		}
	}

}
