package webpagetunnel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Properties;

import com.india.arunava.network.utils.ProxyConstants;

public class Common {

	public static String runFileDirectory;
	private static File file;

	public static void initCertificate() {
		ClassLoader classLoader = Common.class.getClassLoader();
		try {
			Common.runFileDirectory = classLoader.getResource(".").toURI()
					.getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		File outerCertFile = new File(Common.runFileDirectory,
				"ProxyCertificate.ser");
		if (!outerCertFile.exists()) {
			try {
				outerCertFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(outerCertFile);
				InputStream fis = classLoader
						.getResourceAsStream("res/ProxyCertificate.ser");
				byte[] buffer = new byte[1024];
				int length = 0;
				while ((length = fis.read(buffer)) != -1) {
					fos.write(buffer, 0, length);
				}
				fos.flush();
				fos.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(2);
			}
		}
	}

	public static void initSettings() {
		file = new File(Common.runFileDirectory, "webpage-tunnel.cfg");
		if (file.exists() && file.canRead()) {
			loadSettings();
		} else {
			saveSettings();
		}
	}

	public static void loadSettings() {
		try {
			FileInputStream fin = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fin);

			ProxyConstants.webPHP_HOST_HTTP = properties
					.getProperty("server.host");
			ProxyConstants.webPHP_PORT_HTTP = Integer.parseInt(properties
					.getProperty("server.port"));
			ProxyConstants.webPHP_URL_HTTP = properties
					.getProperty("server.url");

			ProxyConstants.HTTPProxyHost = properties
					.getProperty("client.http.host");
			ProxyConstants.HTTPProxyPort = Integer.parseInt(properties
					.getProperty("client.http.port"));
			ProxyConstants.SSLProxyHost = properties
					.getProperty("client.https.host");
			ProxyConstants.SSLProxyPort = Integer.parseInt(properties
					.getProperty("client.https.port"));
			ProxyConstants.HTTPSServer_443 = properties
					.getProperty("client.ssl443.host");
			ProxyConstants.HTTPSPort_443 = Integer.parseInt(properties
					.getProperty("client.ssl443.port"));
			ProxyConstants.HTTPSServer_8443 = properties
					.getProperty("client.ssl8443.host");
			ProxyConstants.HTTPSPort_8443 = Integer.parseInt(properties
					.getProperty("client.ssl8443.port"));

			ProxyConstants.ENCRYPTION_ENABLED = Boolean.parseBoolean(properties
					.getProperty("general.encryption"));
			ProxyConstants.AUTO_START_AND_HIDE = Boolean
					.parseBoolean(properties.getProperty("general.autoHide"));
			ProxyConstants.logLevel = Integer.parseInt(properties
					.getProperty("general.logLevel"));
			ProxyConstants.MAX_BUFFER = Integer.parseInt(properties
					.getProperty("general.maxBuffer"));

			ProxyConstants.ORGANIZATION_HTTP_PROXY_ENABLED = Boolean
					.parseBoolean(properties.getProperty("proxy.enabled"));
			ProxyConstants.ORGANIZATION_HTTP_PROXY_HOST = properties
					.getProperty("proxy.host");
			ProxyConstants.ORGANIZATION_HTTP_PROXY_PORT = Integer
					.parseInt(properties.getProperty("proxy.port"));
			ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_NAME = properties
					.getProperty("proxy.username");
			ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_PASS = properties
					.getProperty("proxy.password");

			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveSettings() {
		try {
			Properties properties = new Properties();

			properties.setProperty("server.host",
					ProxyConstants.webPHP_HOST_HTTP);
			properties.setProperty("server.port", String
					.valueOf(ProxyConstants.webPHP_PORT_HTTP));
			properties
					.setProperty("server.url", ProxyConstants.webPHP_URL_HTTP);

			properties.setProperty("client.http.host",
					ProxyConstants.HTTPProxyHost);
			properties.setProperty("client.http.port", String
					.valueOf(ProxyConstants.HTTPProxyPort));
			properties.setProperty("client.https.host",
					ProxyConstants.SSLProxyHost);
			properties.setProperty("client.https.port", String
					.valueOf(ProxyConstants.SSLProxyPort));
			properties.setProperty("client.ssl443.host",
					ProxyConstants.HTTPSServer_443);
			properties.setProperty("client.ssl443.port", String
					.valueOf(ProxyConstants.HTTPSPort_443));
			properties.setProperty("client.ssl8443.host",
					ProxyConstants.HTTPSServer_8443);
			properties.setProperty("client.ssl8443.port", String
					.valueOf(ProxyConstants.HTTPSPort_8443));

			properties.setProperty("general.encryption", String
					.valueOf(ProxyConstants.ENCRYPTION_ENABLED));
			properties.setProperty("general.autoHide", String
					.valueOf(ProxyConstants.AUTO_START_AND_HIDE));
			properties.setProperty("general.logLevel", String
					.valueOf(ProxyConstants.logLevel));
			properties.setProperty("general.maxBuffer", String
					.valueOf(ProxyConstants.MAX_BUFFER));

			properties.setProperty("proxy.enabled", String
					.valueOf(ProxyConstants.ORGANIZATION_HTTP_PROXY_ENABLED));
			properties.setProperty("proxy.host",
					ProxyConstants.ORGANIZATION_HTTP_PROXY_HOST);
			properties.setProperty("proxy.port", String
					.valueOf(ProxyConstants.ORGANIZATION_HTTP_PROXY_PORT));
			properties.setProperty("proxy.username",
					ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_NAME);
			properties.setProperty("proxy.password",
					ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_PASS);

			FileOutputStream fout = new FileOutputStream(file);
			properties.store(fout, "webpage-tunnel config file");
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
