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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import com.india.arunava.network.utils.ProxyConstants;

/**
 * Read from browser and send it to Out server.
 * 
 * @author http://arunava.in
 * 
 */
class HttpsServerBrowserThread extends Thread {
	private Socket incoming;

	private int whichSSLServer;

	private Socket outgoing;

	HttpsServerBrowserThread(final Socket in, final Socket out,
			final int whichSSLServer) {
		outgoing = in;
		incoming = out;
		this.whichSSLServer = whichSSLServer;
	}

	@Override
	public void run() {
		if (ProxyConstants.logLevel >= 1) {
			System.out
					.println(new Date()
							+ " HttpsServerBrowserThread Started :: "
							+ currentThread());
		}
		try {
			final OutputStream out = outgoing.getOutputStream();
			final InputStream inputstream = incoming.getInputStream();

			final StringBuffer header = new StringBuffer(9999);
			int rd;

			while ((rd = inputstream.read()) != -1) {
				header.append((char) rd);
				if (header.indexOf("\r\n\r\n") != -1) {
					break;
				}
			}

			if (ProxyConstants.logLevel >= 1) {
				System.out.println(new Date()
						+ " HttpsServerBrowserThread :: Reading Header :: "
						+ currentThread() + " header= " + header);
			}

			// Remove last \r\n so that we can ad our custom header
			// IS-SSL-RECURSIVE:
			if (header != null && header.length() > 0) {
				header.delete(header.length() - 2, header.length());
			}

			// Append Header So that it can Understand it is mainly a SSL call (
			// CAll ssl - php script)
			if (whichSSLServer == 443) {
				header.append("X-IS-SSL-RECURSIVE: 0443\r\n\r\n");
			}
			if (whichSSLServer == 8443) {
				header.append("X-IS-SSL-RECURSIVE: 8443\r\n\r\n");
			}

			out.write((header + "\r\n").getBytes());
			out.flush();

			final byte[] buffer = new byte[ProxyConstants.MAX_BUFFER];
			int numberRead = 0;
			while (true) {
				numberRead = inputstream.read(buffer);

				if (numberRead == -1) {
					outgoing.close();
					incoming.close();
					break;
				}

				out.write(buffer, 0, numberRead);

			}
			if (ProxyConstants.logLevel >= 1) {
				System.out.println(new Date()
						+ " HttpsServerBrowserThread :: Finish :: "
						+ currentThread());
			}
		} catch (final IOException e) {
		}

	}

}
