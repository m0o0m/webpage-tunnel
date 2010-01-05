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
		file = new File(Common.runFileDirectory, "ProxyConfig.dat");
		if (file.exists() && file.canRead()) {
			loadSettings();
		} else {
			saveSettings();
		}
	}

	@SuppressWarnings("unchecked")
	public static void loadSettings() {
		try {
			FileInputStream fin = new FileInputStream(file);
			ObjectInputStream oin = new ObjectInputStream(fin);
			HashMap<String, String> configMap = (HashMap<String, String>) oin
					.readObject();
			ProxyConstants.HTTPProxyPort = Integer.parseInt(configMap
					.get("ProxyConstants.HTTPProxyPort"));
			ProxyConstants.webPHP_PORT_HTTP = Integer.parseInt(configMap
					.get("ProxyConstants.webPHP_PORT_HTTP"));
			ProxyConstants.ENCRYPTION_ENABLED = Boolean.parseBoolean(configMap
					.get("ProxyConstants.ENCRYPTION_ENABLED"));
			ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_PASS = configMap
					.get("ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_PASS");
			ProxyConstants.HTTPSPort_8443 = Integer.parseInt(configMap
					.get("ProxyConstants.HTTPSPort_8443"));
			ProxyConstants.webPHP_URL_HTTP = configMap
					.get("ProxyConstants.webPHP_URL_HTTP");
			ProxyConstants.logLevel = Integer.parseInt(configMap
					.get("ProxyConstants.logLevel"));
			ProxyConstants.MAX_BUFFER = Integer.parseInt(configMap
					.get("ProxyConstants.MAX_BUFFER"));
			ProxyConstants.HTTPProxyHost = configMap
					.get("ProxyConstants.HTTPProxyHost");
			ProxyConstants.webPHP_HOST_HTTP = configMap
					.get("ProxyConstants.webPHP_HOST_HTTP");
			ProxyConstants.SSLProxyPort = Integer.parseInt(configMap
					.get("ProxyConstants.SSLProxyPort"));
			ProxyConstants.HTTPSPort_443 = Integer.parseInt(configMap
					.get("ProxyConstants.HTTPSPort_443"));
			ProxyConstants.HTTPSServer_8443 = configMap
					.get("ProxyConstants.HTTPSServer_8443");
			ProxyConstants.ORGANIZATION_HTTP_PROXY_PORT = Integer
					.parseInt(configMap
							.get("ProxyConstants.ORGANIZATION_HTTP_PROXY_PORT"));
			ProxyConstants.HTTPSServer_443 = configMap
					.get("ProxyConstants.HTTPSServer_443");
			ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_NAME = configMap
					.get("ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_NAME");
			ProxyConstants.SSLProxyHost = configMap
					.get("ProxyConstants.SSLProxyHost");
			ProxyConstants.ORGANIZATION_HTTP_PROXY_HOST = configMap
					.get("ProxyConstants.ORGANIZATION_HTTP_PROXY_HOST");
			ProxyConstants.ORGANIZATION_HTTP_PROXY_ENABLED = Boolean
					.parseBoolean(configMap
							.get("ProxyConstants.ORGANIZATION_HTTP_PROXY_ENABLED"));
			ProxyConstants.AUTO_START_AND_HIDE = Boolean.parseBoolean(configMap
					.get("ProxyConstants.AUTO_START_AND_HIDE"));
			oin.close();
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveSettings() {
		try {
			FileOutputStream fout = new FileOutputStream(file);
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			HashMap<String, String> configMap = new HashMap<String, String>();
			configMap.put("ProxyConstants.HTTPProxyPort", String
					.valueOf(ProxyConstants.HTTPProxyPort));
			configMap.put("ProxyConstants.webPHP_PORT_HTTP", String
					.valueOf(ProxyConstants.webPHP_PORT_HTTP));
			configMap.put("ProxyConstants.ENCRYPTION_ENABLED", String
					.valueOf(ProxyConstants.ENCRYPTION_ENABLED));
			configMap.put("ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_PASS",
					ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_PASS);
			configMap.put("ProxyConstants.HTTPSPort_8443", String
					.valueOf(ProxyConstants.HTTPSPort_8443));
			configMap.put("ProxyConstants.webPHP_URL_HTTP",
					ProxyConstants.webPHP_URL_HTTP);
			configMap.put("ProxyConstants.logLevel", String
					.valueOf(ProxyConstants.logLevel));
			configMap.put("ProxyConstants.MAX_BUFFER", String
					.valueOf(ProxyConstants.MAX_BUFFER));
			configMap.put("ProxyConstants.HTTPProxyHost",
					ProxyConstants.HTTPProxyHost);
			configMap.put("ProxyConstants.webPHP_HOST_HTTP",
					ProxyConstants.webPHP_HOST_HTTP);
			configMap.put("ProxyConstants.SSLProxyPort", String
					.valueOf(ProxyConstants.SSLProxyPort));
			configMap.put("ProxyConstants.HTTPSPort_443", String
					.valueOf(ProxyConstants.HTTPSPort_443));
			configMap.put("ProxyConstants.HTTPSServer_8443",
					ProxyConstants.HTTPSServer_8443);
			configMap.put("ProxyConstants.ORGANIZATION_HTTP_PROXY_PORT", String
					.valueOf(ProxyConstants.ORGANIZATION_HTTP_PROXY_PORT));
			configMap.put("ProxyConstants.HTTPSServer_443",
					ProxyConstants.HTTPSServer_443);
			configMap.put("ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_NAME",
					ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_NAME);
			configMap.put("ProxyConstants.SSLProxyHost",
					ProxyConstants.SSLProxyHost);
			configMap.put("ProxyConstants.ORGANIZATION_HTTP_PROXY_HOST",
					ProxyConstants.ORGANIZATION_HTTP_PROXY_HOST);
			configMap
					.put(
							"ProxyConstants.ORGANIZATION_HTTP_PROXY_ENABLED",
							String
									.valueOf(ProxyConstants.ORGANIZATION_HTTP_PROXY_ENABLED));
			configMap.put("ProxyConstants.AUTO_START_AND_HIDE", String
					.valueOf(ProxyConstants.AUTO_START_AND_HIDE));
			oout.writeObject(configMap);
			oout.flush();
			oout.close();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
