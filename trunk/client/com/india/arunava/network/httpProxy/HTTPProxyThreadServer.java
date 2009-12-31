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

import com.india.arunava.network.utils.ProxyConstants;
import com.india.arunava.network.utils.SimpleEncryptDecrypt;

/**
 * Reading from server and write to browser.
 * 
 * @author http://arunava.in
 * 
 */
class HTTPProxyThreadServer extends Thread {
	private Socket incoming;

	private Socket outgoing;

	HTTPProxyThreadServer(final Socket outgoing, final Socket incoming) {
		this.incoming = incoming;
		this.outgoing = outgoing;
	}

	@Override
	public void run() {
		long count1 = 0;
		long count2 = 0;
		long count3 = 0;

		if (ProxyConstants.logLevel >= 1) {
			System.out.println(new Date()
					+ " HTTPProxyThreadServer ::Started  " + currentThread());
		}
		final byte[] buffer = new byte[ProxyConstants.MAX_BUFFER];
		int numberRead = 0;
		OutputStream client;
		InputStream server;

		try {
			server = outgoing.getInputStream();
			client = incoming.getOutputStream();

			// Reading response header:
			int rdL;
			final StringBuffer header = new StringBuffer(9999);
			while (true) {
				count1++;
				rdL = server.read();
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
						+ " HTTPProxyThreadServer ::Response Header =  "
						+ currentThread() + " " + header);
			}

			if (header.toString().toLowerCase().indexOf(
					"Transfer-Encoding: chunked".toLowerCase()) != -1) {
				if (ProxyConstants.logLevel >= 2) {
					System.out
							.println(new Date()
									+ " HTTPProxyThreadServer :: It is Chunked Response   "
									+ currentThread());
				}
				count3 = HTTPChunkResponseReader.readFullyANDWriteFully(client,
						server);
			} else {
				if (ProxyConstants.logLevel >= 2) {
					System.out
							.println(new Date()
									+ " HTTPProxyThreadServer :: It is Normal Response   "
									+ currentThread());
				}
				while (true) {
					numberRead = server.read(buffer);
					count2 = count2 + numberRead;
					if (numberRead == -1) {
						outgoing.close();
						incoming.close();
						break;
					}

					if (ProxyConstants.ENCRYPTION_ENABLED) {
						client.write(SimpleEncryptDecrypt.dec(buffer,
								numberRead), 0, numberRead);
					} else {
						client.write(buffer, 0, numberRead);
					}
					if (ProxyConstants.logLevel >= 3) {
						final ByteArrayOutputStream bo = new ByteArrayOutputStream();
						bo.write(buffer, 0, numberRead);
						System.out.println("::::::Normal Read" + bo
								+ "::::::Normal Read");
					}
				}
			}
			if (ProxyConstants.logLevel >= 1) {
				System.out.println(new Date()
						+ " HTTPProxyThreadServer :: End   " + currentThread());
			}
			outgoing.close();
			incoming.close();
		} catch (final Exception e) {
			// e.printStackTrace();
		}
		synchronized (ProxyConstants.MUTEX) {
			ProxyConstants.TOTAL_TRANSFER = ProxyConstants.TOTAL_TRANSFER
					+ count1 + count2 + count3;
		}
	}

}
