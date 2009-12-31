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

package com.india.arunava.UI.main;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.india.arunava.console.main.ProxyRunner;
import com.india.arunava.network.httpProxy.HTTPProxyServerStarter;
import com.india.arunava.network.httpsServer.HttpsServerStarter_443;
import com.india.arunava.network.httpsServer.HttpsServerStarter_8443;
import com.india.arunava.network.utils.ProxyConstants;

public class ProxyUIStarter extends JFrame {

	public void copyFile(final ProxyUIStarter proxyUIStarter,
			final InputStream in) {
		final String currentDir = new File("").getAbsolutePath();
		try {
			final File f2 = new File(currentDir + File.separator
					+ "ProxyCertificate.ser");
			final OutputStream out = new FileOutputStream(f2);
			final byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (final Exception ex) {

			JOptionPane.showMessageDialog(proxyUIStarter, ex
					+ "Could not create SSL Certificate file in  : "
					+ currentDir + File.separator + "ProxyCertificate.ser",
					"Message", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	public static void main(final String args[]) {
		// copy certificate file from jar.
		final ProxyUIStarter proxyUIStarter = new ProxyUIStarter();
		final InputStream is = proxyUIStarter.getClass().getResourceAsStream(
				"/ProxyCertificate.ser");
		// proxyUIStarter.copyFile(proxyUIStarter, is);

		final String currentDir = new File("").getAbsolutePath();
		final String cerfile = currentDir + File.separator
				+ "ProxyCertificate.ser";

		try {
			final PrintStream stdout = new PrintStream(new FileOutputStream(
					ProxyConstants.LOG_FILE));
			System.setOut(stdout);
		} catch (final FileNotFoundException e) {
			final String currentDir1 = new File("").getAbsolutePath();
			JOptionPane.showMessageDialog(null,
					"Could not access LOG file in  : " + currentDir1
							+ File.separator + "ProxyLog.txt", "Message",
					JOptionPane.ERROR_MESSAGE);
		}

		final JFrame jd = new JFrame();
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - jd.getWidth()) / 2;
		final int y = (screenSize.height - jd.getHeight()) / 2;

		final JPanel panel = new JPanel(new GridLayout(5, 1));
		jd.addWindowListener(new MyWindowListener());

		jd.setTitle("India Web Proxy v1.0");
		jd.setLocation(100, 100);
		jd.setSize(300, 200);
		final JButton jb1 = new JButton("Start");
		final JButton jb2 = new JButton("Settings");
		final JButton jb3 = new JButton("Stop");
		final JButton jb4 = new JButton("About");
		final JButton jb5 = new JButton();

		jb1.addActionListener(new ButtonListener(jb1, jd, jb5, jb2));
		jb2.addActionListener(new ButtonListener(jb2, jd, jb5, jb2));
		jb3.addActionListener(new ButtonListener(jb3, jd, jb5, jb2));
		jb4.addActionListener(new ButtonListener(jb4, jd, jb5, jb2));

		panel.add(jb1);
		panel.add(jb2);
		panel.add(jb3);
		panel.add(jb4);
		panel.add(jb5);
		jd.add(panel);
		jd.show();

	}

	public static void showSettings() {

		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final Settings frame = new Settings();
		final int x = (screenSize.width - frame.getWidth()) / 2;
		final int y = (screenSize.height - frame.getHeight()) / 2;
		frame.setLocation(100, 100);
		frame.setSize(500, 500);

		frame.show();

	}
}

class ButtonListener implements ActionListener {
	JButton jb;

	JButton jb5;

	JButton jb2;

	JFrame jf;

	ButtonListener(final JButton jb, JFrame jf, JButton jb5, JButton jb2) {
		this.jb = jb;
		this.jf = jf;
		this.jb5 = jb5;
		this.jb2 = jb2;
	}

	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals("Settings")) {
			ProxyUIStarter.showSettings();
		}
		if (e.getActionCommand().equals("Stop")) {
			System.exit(1);
		}
		if (e.getActionCommand().equals("About")) {
			JOptionPane
					.showMessageDialog(
							null,
							"visit : http://arunava.in/Proxy.html\r\nhttp://webproxytunnel.sourceforge.net/\r\n@copyright Arunava  Bhowmick ( 2009-2010)",
							"India Web Proxy v1.0", JOptionPane.PLAIN_MESSAGE);
		}
		if (e.getActionCommand().equals("Start")) {
			ProxyRunner.runServers();
			// Wait till all server started
			while (HTTPProxyServerStarter.started == 0
					// || SSLProxyStarter.started == 0
					|| HttpsServerStarter_443.started == 0
					|| HttpsServerStarter_8443.started == 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}

			}
			// Check all server started successfully.
			if (HTTPProxyServerStarter.started == 2
					// || SSLProxyStarter.started == 2
					|| HttpsServerStarter_443.started == 2
					|| HttpsServerStarter_8443.started == 2) {
				final String currentDir = new File("").getAbsolutePath();
				JOptionPane.showMessageDialog(null,
						"Proxy Not Started.( Probably Port is in use ) Pls. refer the log file in : "
								+ currentDir + File.separator + "ProxyLog.txt",
						"Starting Failed", JOptionPane.ERROR_MESSAGE);

				System.exit(1);
			}

			jb.setEnabled(false);
			jb2.setEnabled(false);

			new DataTransferMonitor(jb5, jf).start();

			final String currentDir = new File("").getAbsolutePath();
			JOptionPane.showMessageDialog(null,
					"Proxy Started.\r\nPls. refer the log file in : "
							+ currentDir + File.separator + "ProxyLog.txt",
					"Starting successfully", JOptionPane.INFORMATION_MESSAGE);
		}

	}
}

class MyWindowListener implements WindowListener {

	public void windowClosing(final WindowEvent arg0) {
		System.exit(0);
	}

	public void windowOpened(final WindowEvent arg0) {
	}

	public void windowClosed(final WindowEvent arg0) {
	}

	public void windowIconified(final WindowEvent arg0) {
	}

	public void windowDeiconified(final WindowEvent arg0) {
	}

	public void windowActivated(final WindowEvent arg0) {
	}

	public void windowDeactivated(final WindowEvent arg0) {
	}

}

class DataTransferMonitor extends Thread {
	JButton jb;

	JFrame jd;

	public DataTransferMonitor(JButton jb, JFrame jd) {
		this.jb = jb;
		this.jd = jd;
	}

	@Override
	public void run() {
		long transfer = 0;
		try {
			while (true) {
				Thread.sleep(3000);
				transfer = ProxyConstants.TOTAL_TRANSFER;
				if (transfer > 0) {
					jb.setText("Total Transfer : " + transfer / 1000 + " KB");
					jd.setTitle(transfer / 1000 + " KB " + " : Total Transfer");
				}
			}
		} catch (InterruptedException e) {
		}
	}
}