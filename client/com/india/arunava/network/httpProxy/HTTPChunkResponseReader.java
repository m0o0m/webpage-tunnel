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
import java.util.Date;

import com.india.arunava.network.utils.ProxyConstants;
import com.india.arunava.network.utils.SimpleEncryptDecrypt;

/**
 * Read Chunk HTTP Response Data. Does not read any header after chunk data.
 * 
 * @author http://arunava.in
 * 
 */
public class HTTPChunkResponseReader {

	public static long readFullyANDWriteFullyHelper(final OutputStream client,
			final InputStream server, final int size) {
		long count3 = 0;
		final int bufSize = ProxyConstants.MAX_BUFFER;
		int read;
		int totalRead = 0;
		int toRead = bufSize;

		if (toRead > size) {
			toRead = size;
		}
		final byte[] by = new byte[toRead];

		try {
			while (true) {
				read = server.read(by, 0, toRead);
				count3 = count3 + read;
				if (ProxyConstants.ENCRYPTION_ENABLED) {
					client.write(SimpleEncryptDecrypt.dec(by, read), 0, read);
				} else {
					client.write(by, 0, read);
				}
				if (ProxyConstants.logLevel >= 3) {
					System.out
							.println(new Date()
									+ " HTTPChunkResponseReader :: Read chunked data size =  "
									+ read);
				}

				if (ProxyConstants.logLevel >= 3) {
					final ByteArrayOutputStream bo = new ByteArrayOutputStream();
					bo.write(by, 0, read);
					System.out.println("::Chunked read::" + bo);
				}

				totalRead = totalRead + read;
				if (size - totalRead < toRead) {
					toRead = size - totalRead;
				}
				if (totalRead == size) {
					break;
				}
			}
			client.flush();
		} catch (final Exception e) {
			if (ProxyConstants.logLevel >= 2) {
				e.printStackTrace();
			}
		}
		return count3;
	}

	public static long readFullyANDWriteFully(final OutputStream client,
			final InputStream server) {
		long count3 = 0;
		try {
			int chunkSize = Integer.parseInt(readChunkSize(server), 16);

			while (chunkSize != 0) {
				count3 = count3
						+ readFullyANDWriteFullyHelper(client, server,
								chunkSize);
				String sizeStr = readChunkSize(server);
				if ("".equals(sizeStr)) {
					sizeStr = readChunkSize(server);
				}
				if ("".equals(sizeStr)) {
					break;
				}
				chunkSize = Integer.parseInt(sizeStr, 16);
			}

		} catch (final Exception e) {
			if (ProxyConstants.logLevel >= 2) {
				e.printStackTrace();
			}
		}
		return count3;
	}

	private static String readChunkSize(final InputStream server) {
		int rdL;
		String format = "";
		final StringBuffer line = new StringBuffer(1111);
		try {
			while (true) {
				rdL = server.read();
				if (rdL == -1) {
					break;
				}
				line.append((char) rdL);
				if (line.indexOf("\r\n") != -1) {
					break;
				}
			}
		} catch (final Exception e) {
			if (ProxyConstants.logLevel >= 2) {
				e.printStackTrace();
			}
		}
		format = line.toString().trim();
		if (!format.equals("")) {
			// Generally some chunk data size comes with extra data Like :
			// 222;ignore this
			final int separator = format.indexOf(";");
			if (separator != -1) {
				format = format.substring(0, separator - 1);
			}
		}
		return format;
	}

}
