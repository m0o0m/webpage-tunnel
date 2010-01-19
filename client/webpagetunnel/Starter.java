package webpagetunnel;

public class Starter {

	public static void main(String[] args) {

		boolean gui = true;
		boolean https = true;
		if (args.length != 0) {
			int i;
			for (i = 0; i < args.length; i++) {
				if (args[i].equals("-c")) {
					gui = false;
					continue;
				}
				if (args[i].equals("-n")) {
					https = false;
					continue;
				}
			}
		}

		Common.initResourcesFolder();
		Common.initSettings();

		if (https) {
			Common.initCertificate();
		} else {
			Common.changeSetting("https", false);
		}

		if (gui) {
			new MainFrame();
		} else {
			new MainConsole();
		}
	}
}
