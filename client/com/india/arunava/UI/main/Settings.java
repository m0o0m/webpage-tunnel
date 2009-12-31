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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.india.arunava.network.utils.ProxyConstants;

class Settings extends JFrame implements ActionListener {
	JButton SUBMIT;

	JPanel panel;

	JLabel label1, label2, label3, label4, label5, label6, label7, label8,
			label9, label10, label11, label12, label13, label14, label15,
			label16, label17;

	final JTextField text1, text2, text3, text4, text5, text6, text7, text8,
			text9, text10, text11, text12, text13, text14, text15, text16,
			text17;

	JCheckBox chinButton;

	Settings() {
		label1 = new JLabel();
		label1.setText(" LogLevel (0-3):");
		text1 = new JTextField(35);

		label2 = new JLabel();
		label2.setText(" Max Buffer Size:");
		text2 = new JTextField(35);

		label3 = new JLabel();
		label3.setText(" HTTP Proxy Host IP :");
		text3 = new JTextField(35);

		label4 = new JLabel();
		label4.setText(" SSL Proxy Host IP :");
		text4 = new JTextField(35);

		label5 = new JLabel();
		label5.setText(" HTTPS Server_443 IP :");
		text5 = new JTextField(35);

		label6 = new JLabel();
		label6.setText(" HTTPS Server_8443 IP :");
		text6 = new JTextField(35);

		label7 = new JLabel();
		label7.setText(" HTTPS Server_443 Port :");
		text7 = new JTextField(35);

		label8 = new JLabel();
		label8.setText(" HTTPS Server_8443 Port :");

		text8 = new JTextField(35);

		label9 = new JLabel();
		label9.setText(" HTTP Proxy Port :");
		text9 = new JTextField(35);

		label10 = new JLabel();
		label10.setText(" SSL Proxy Port :");
		text10 = new JTextField(35);

		label11 = new JLabel();
		label11.setText(" (PHP) Web Server IP :");
		text11 = new JTextField(35);

		label12 = new JLabel();
		label12.setText(" (PHP) Web Server Port :");
		text12 = new JTextField(35);

		label13 = new JLabel();
		label13.setText(" (PHP) File Full URL :");
		text13 = new JTextField(115);

		label14 = new JLabel();
		label14.setText(" Organization Proxy Host :");
		text14 = new JTextField(35);

		label15 = new JLabel();
		label15.setText(" Organization Proxy Port :");
		text15 = new JTextField(35);

		label16 = new JLabel();
		label16.setText(" Organization Proxy UserName :");
		text16 = new JTextField(35);

		label17 = new JLabel();
		label17.setText(" Organization Proxy Password :");
		text17 = new JTextField(35);

		this.setLayout(new BorderLayout());
		SUBMIT = new JButton("  Save & Close");

		panel = new JPanel(new GridLayout(23, 1));
		panel.add(new JLabel());
		panel.add(new JLabel());
		panel.add(label1);
		panel.add(text1);

		panel.add(label2);
		panel.add(text2);

		panel.add(new JLabel());
		panel.add(new JLabel());

		panel.add(label3);
		panel.add(text3);

		panel.add(label4);
		panel.add(text4);

		panel.add(label5);
		panel.add(text5);

		panel.add(label6);
		panel.add(text6);

		panel.add(label7);
		panel.add(text7);

		panel.add(label8);
		panel.add(text8);

		panel.add(label9);
		panel.add(text9);

		panel.add(label10);
		panel.add(text10);

		panel.add(new JLabel());
		panel.add(new JLabel());

		panel.add(label11);
		panel.add(text11);

		panel.add(label12);
		panel.add(text12);

		panel.add(label13);
		panel.add(text13);

		panel.add(new JLabel());
		panel.add(new JLabel());

		panel.add(label14);
		panel.add(text14);

		panel.add(label15);
		panel.add(text15);

		panel.add(label16);
		panel.add(text16);

		panel.add(label17);
		panel.add(text17);

		panel.add(new JLabel());
		panel.add(new JLabel());

		chinButton = new JCheckBox("Eanble Encryption");
		chinButton.setMnemonic(KeyEvent.VK_C);
		// chinButton.setSelected(true);

		panel.add(chinButton);
		// panel.add(new JLabel());
		panel.add(SUBMIT);
		add(panel, BorderLayout.CENTER);
		SUBMIT.addActionListener(this);
		setTitle("Proxy Settings..");

		// //////////////////// Setting from Constant initially ////
		text1.setText(ProxyConstants.logLevel + "");
		text2.setText(ProxyConstants.MAX_BUFFER + "");
		text3.setText(ProxyConstants.HTTPProxyHost);
		text4.setText(ProxyConstants.SSLProxyHost);
		text5.setText(ProxyConstants.HTTPSServer_443);
		text6.setText(ProxyConstants.HTTPSServer_8443);
		text7.setText(ProxyConstants.HTTPSPort_443 + "");
		text8.setText(ProxyConstants.HTTPSPort_8443 + "");
		text9.setText(ProxyConstants.HTTPProxyPort + "");
		text10.setText(ProxyConstants.SSLProxyPort + "");
		text11.setText(ProxyConstants.webPHP_HOST_HTTP);
		text12.setText(ProxyConstants.webPHP_PORT_HTTP + "");
		text13.setText(ProxyConstants.webPHP_URL_HTTP);
		text14.setText(ProxyConstants.ORGANIZATION_HTTP_PROXY_HOST);
		text15.setText(ProxyConstants.ORGANIZATION_HTTP_PROXY_PORT + "");
		text16.setText(ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_NAME);
		text17.setText(ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_PASS);

		if (ProxyConstants.ENCRYPTION_ENABLED)
			chinButton.setSelected(true);
		if (!ProxyConstants.ENCRYPTION_ENABLED)
			chinButton.setSelected(false);

		updateField();
	}

	// ////////////////////////////////////////////
	void updateField() {
		try {
			final String currentDir = new File("").getAbsolutePath();
			final File file = new File(currentDir + File.separator
					+ "ProxyConfig.dat");
			if (file.exists() && file.canRead()) {
				// Deserialize it
				final FileInputStream fin = new FileInputStream(file);
				final ObjectInputStream in = new ObjectInputStream(fin);
				final HashMap store = (HashMap) in.readObject();

				text1.setText((String) store.get("ProxyConstants.logLevel"));
				text2.setText((String) store.get("ProxyConstants.MAX_BUFFER"));
				text3.setText((String) store
						.get("ProxyConstants.HTTPProxyHost"));
				text4
						.setText((String) store
								.get("ProxyConstants.SSLProxyHost"));
				text5.setText((String) store
						.get("ProxyConstants.HTTPSServer_443"));
				text6.setText((String) store
						.get("ProxyConstants.HTTPSServer_8443"));
				text7.setText((String) store
						.get("ProxyConstants.HTTPSPort_443"));
				text8.setText((String) store
						.get("ProxyConstants.HTTPSPort_8443"));
				text9.setText((String) store
						.get("ProxyConstants.HTTPProxyPort"));
				text10.setText((String) store
						.get("ProxyConstants.SSLProxyPort"));
				text11.setText((String) store
						.get("ProxyConstants.webPHP_HOST_HTTP"));
				text12.setText((String) store
						.get("ProxyConstants.webPHP_PORT_HTTP"));
				text13.setText((String) store
						.get("ProxyConstants.webPHP_URL_HTTP"));
				text14.setText((String) store
						.get("ProxyConstants.ORGANIZATION_HTTP_PROXY_HOST"));
				text15.setText((String) store
						.get("ProxyConstants.ORGANIZATION_HTTP_PROXY_PORT"));
				text16
						.setText((String) store
								.get("ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_NAME"));
				text17
						.setText((String) store
								.get("ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_PASS"));

				String ence = (String) store
						.get("ProxyConstants.ENCRYPTION_ENABLED");
				if (ence != null) {
					if (ence.equals("Y"))
						chinButton.setSelected(true);
					else
						chinButton.setSelected(false);
				}

				// Now update the ProxyConstant file .
				String tmpStr;
				try {// 1
					ProxyConstants.logLevel = new Integer((String) store
							.get("ProxyConstants.logLevel")).intValue();
				} catch (Exception e1) {
				}
				try {// 2
					ProxyConstants.MAX_BUFFER = new Integer((String) store
							.get("ProxyConstants.MAX_BUFFER")).intValue();
				} catch (Exception e1) {
				}
				try {// 3
					tmpStr = (String) store.get("ProxyConstants.HTTPProxyHost");
					if (null != tmpStr)
						ProxyConstants.HTTPProxyHost = tmpStr;
				} catch (Exception e1) {
				}
				try {// 4
					tmpStr = (String) store.get("ProxyConstants.SSLProxyHost");
					if (null != tmpStr)
						ProxyConstants.SSLProxyHost = tmpStr;
				} catch (Exception e1) {
				}
				try {// 5
					tmpStr = (String) store
							.get("ProxyConstants.HTTPSServer_443");
					if (null != tmpStr)
						ProxyConstants.HTTPSServer_443 = tmpStr;
				} catch (Exception e1) {
				}
				try {// 6
					tmpStr = (String) store
							.get("ProxyConstants.HTTPSServer_8443");
					if (null != tmpStr)
						ProxyConstants.HTTPSServer_8443 = tmpStr;
				} catch (Exception e1) {
				}
				try {// 7

					ProxyConstants.HTTPSPort_443 = new Integer((String) store
							.get("ProxyConstants.HTTPSPort_443")).intValue();
				} catch (Exception e1) {
				}
				try {// 8
					ProxyConstants.HTTPSPort_8443 = new Integer((String) store
							.get("ProxyConstants.HTTPSPort_8443")).intValue();
				} catch (Exception e1) {
				}
				try {// 9
					ProxyConstants.HTTPProxyPort = new Integer((String) store
							.get("ProxyConstants.HTTPProxyPort")).intValue();
				} catch (Exception e1) {
				}
				try {// 10
					ProxyConstants.SSLProxyPort = new Integer((String) store
							.get("ProxyConstants.SSLProxyPort")).intValue();
				} catch (Exception e1) {
				}
				try {// 11
					tmpStr = (String) store
							.get("ProxyConstants.webPHP_HOST_HTTP");
					if (null != tmpStr)
						ProxyConstants.webPHP_HOST_HTTP = tmpStr;
				} catch (Exception e1) {
				}
				try {// 12
					ProxyConstants.webPHP_PORT_HTTP = new Integer(
							(String) store
									.get("ProxyConstants.webPHP_PORT_HTTP"))
							.intValue();
				} catch (Exception e1) {
				}
				try {// 13
					tmpStr = (String) store
							.get("ProxyConstants.webPHP_URL_HTTP");
					if (null != tmpStr)
						ProxyConstants.webPHP_URL_HTTP = tmpStr;
				} catch (Exception e1) {
				}
				try {// 14
					tmpStr = (String) store
							.get("ProxyConstants.ORGANIZATION_HTTP_PROXY_HOST");
					if (null != tmpStr)
						ProxyConstants.ORGANIZATION_HTTP_PROXY_HOST = tmpStr;
				} catch (Exception e1) {
				}
				try {// 15
					ProxyConstants.ORGANIZATION_HTTP_PROXY_PORT = new Integer(
							(String) store
									.get("ProxyConstants.ORGANIZATION_HTTP_PROXY_PORT"))
							.intValue();
				} catch (Exception e1) {
				}
				try {// 16
					tmpStr = (String) store
							.get("ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_NAME");
					if (null != tmpStr)
						ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_NAME = tmpStr;
				} catch (Exception e1) {
				}
				try {// 17
					tmpStr = (String) store
							.get("ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_PASS");
					if (null != tmpStr)
						ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_PASS = tmpStr;
				} catch (Exception e1) {
				}

				String ence1 = (String) store
						.get("ProxyConstants.ENCRYPTION_ENABLED");
				if (ence1 != null) {
					if (ence1.equals("Y"))
						ProxyConstants.ENCRYPTION_ENABLED = true;
					else
						ProxyConstants.ENCRYPTION_ENABLED = false;
				}

				// ///////////////////////////////////
				in.close();
				fin.close();
			}
		} catch (final Exception e) {
			System.out.println(e.getMessage());
			// e.printStackTrace();
		}
	}

	public void actionPerformed(final ActionEvent ae) {
		final String currentDir = new File("").getAbsolutePath();
		try {
			final File file = new File(currentDir + File.separator
					+ "ProxyConfig.dat");
			final FileOutputStream fout = new FileOutputStream(file);
			final ObjectOutputStream obj = new ObjectOutputStream(fout);

			final HashMap store = new HashMap();
			store.put("ProxyConstants.logLevel", text1.getText().trim());
			store.put("ProxyConstants.MAX_BUFFER", text2.getText().trim());

			store.put("ProxyConstants.HTTPProxyHost", text3.getText().trim());
			store.put("ProxyConstants.SSLProxyHost", text4.getText().trim());
			store.put("ProxyConstants.HTTPSServer_443", text5.getText().trim());
			store
					.put("ProxyConstants.HTTPSServer_8443", text6.getText()
							.trim());
			store.put("ProxyConstants.HTTPSPort_443", text7.getText().trim());
			store.put("ProxyConstants.HTTPSPort_8443", text8.getText().trim());
			store.put("ProxyConstants.HTTPProxyPort", text9.getText().trim());
			store.put("ProxyConstants.SSLProxyPort", text10.getText().trim());

			store.put("ProxyConstants.webPHP_HOST_HTTP", text11.getText()
					.trim());
			store.put("ProxyConstants.webPHP_PORT_HTTP", text12.getText()
					.trim());
			store
					.put("ProxyConstants.webPHP_URL_HTTP", text13.getText()
							.trim());

			store.put("ProxyConstants.ORGANIZATION_HTTP_PROXY_HOST", text14
					.getText().trim());
			store.put("ProxyConstants.ORGANIZATION_HTTP_PROXY_PORT", text15
					.getText().trim());
			store.put("ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_NAME",
					text16.getText().trim());
			store.put("ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_PASS",
					text17.getText().trim());

			if (chinButton.isSelected())
				store.put("ProxyConstants.ENCRYPTION_ENABLED", "Y");
			if (!chinButton.isSelected())
				store.put("ProxyConstants.ENCRYPTION_ENABLED", "N");

			obj.writeObject(store);
			obj.flush();
			obj.close();
			fout.close();

			updateField();

		} catch (final Exception e) {
			JOptionPane.showMessageDialog(null, "Cant save configuration in : "
					+ currentDir + "ProxyConfig.dat", "Message",
					JOptionPane.ERROR_MESSAGE);
		}
		this.dispose();
	}
}
