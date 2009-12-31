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

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import com.india.arunava.network.utils.ProxyConstants;

/**
 * Read from external server and write to browser through tunnel socket..
 * 
 * @author http://arunava.in
 * 
 */
class HttpsServerOutgoingThread extends Thread {
	private Socket incoming;

	private Socket outgoing;

	HttpsServerOutgoingThread(final Socket in, final Socket out) {
		incoming = in;
		outgoing = out;
	}

	@Override
	public void run() {
		if (ProxyConstants.logLevel >= 1) {
			System.out.println(new Date()
					+ " HttpsServerOutgoingThread Started :: "
					+ currentThread());
		}

		final byte[] buffer = new byte[ProxyConstants.MAX_BUFFER];
		int numberRead = 0;
		OutputStream ToClient;
		InputStream FromClient;

		try {
			ToClient = outgoing.getOutputStream();
			FromClient = incoming.getInputStream();
			while (true) {
				numberRead = FromClient.read(buffer);
				if (numberRead == -1) {
					outgoing.close();
					incoming.close();
					break;
				}
				ToClient.write(buffer, 0, numberRead);
			}

			if (ProxyConstants.logLevel >= 1) {
				System.out.println(new Date()
						+ " HttpsServerOutgoingThread Finish :: "
						+ currentThread());
			}

		} catch (final Exception e) {

		}
	}
}
