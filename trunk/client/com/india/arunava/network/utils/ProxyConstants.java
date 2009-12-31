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

package com.india.arunava.network.utils;

import java.io.File;

import webpagetunnel.Common;

/**
 * ProxyConstants. Contains Server address & Port.
 * 
 */
public class ProxyConstants {

	// General Info ===========================================
	public static int logLevel = 0;

	public static int MAX_BUFFER = 30000;

	// Certificate Info ==================================

	public static String KEYSTORE_PATH = Common.runFileDirectory
			+ File.separator + "ProxyCertificate.ser";

	public static String KEYSTORE_PASSWORD = "password";

	// Local Proxy configuration ==========================

	public static String HTTPProxyHost = "localhost";

	public static String SSLProxyHost = "localhost";

	public static String HTTPSServer_443 = "localhost";

	public static String HTTPSServer_8443 = "localhost";

	public static int HTTPSPort_443 = 6053;

	public static int HTTPSPort_8443 = 6054;

	// Following port numbers you need to set in Browser =========

	public static int HTTPProxyPort = 6050;

	public static int SSLProxyPort = 6051;

	// WEB Server Information =====================================

	public static String webPHP_HOST_HTTP = "www.example.com";

	public static int webPHP_PORT_HTTP = 80;

	public static String webPHP_URL_HTTP = "http://www.example.com/proxy.php";

	// Organization Proxy Information ==============================

	public static boolean ORGANIZATION_HTTP_PROXY_ENABLED = false;

	public static String ORGANIZATION_HTTP_PROXY_HOST = "";

	public static int ORGANIZATION_HTTP_PROXY_PORT = 0;

	public static String ORGANIZATION_HTTP_PROXY_USER_NAME = "";

	public static String ORGANIZATION_HTTP_PROXY_USER_PASS = "";

	// Log File ===================
	public static String LOG_FILE = new File("").getAbsolutePath()
			+ File.separator + "ProxyLog.txt";

	// Enabled encryption will encrtpy all trafic hence there is no way to
	// monitor which target server your are accessing.
	public static boolean ENCRYPTION_ENABLED = true;

	// Misc ===================
	public static long TOTAL_TRANSFER = 0;

	public static Object MUTEX = new Object();

	// SHOULD be same as defined in PHP file.
	public static int ENCKEY = 20;

}
