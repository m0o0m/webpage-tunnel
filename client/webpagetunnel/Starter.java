package webpagetunnel;

public class Starter {

	public static void main(String[] args) {
		Common.initCertificate();
		Common.initSettings();

		if (args.length == 1 && args[0].equals("-c")) {
			new MainConsole();
		} else {
			new MainFrame();
		}
	}

}
