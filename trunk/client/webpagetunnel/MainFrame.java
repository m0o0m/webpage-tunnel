package webpagetunnel;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.india.arunava.console.main.ProxyRunner;
import com.india.arunava.network.httpProxy.HTTPProxyServerStarter;
import com.india.arunava.network.httpsServer.HttpsServerStarter_443;
import com.india.arunava.network.httpsServer.HttpsServerStarter_8443;
import com.india.arunava.network.utils.ProxyConstants;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private static final int FRAME_WIDTH = 560;
	private static final int FRAME_HEIGHT = 420;
	private static final String FRAME_TITLE = "Webpage tunnel v0.1.3";
	private static final int GAP_SIZE = 3;
	private static final int BORDER_SIZE = 6;
	private static final String PROCOTOL_PREFIX = "http://";

	private JTextField proxyPageTextField, portTextField;
	private JTextArea logTextArea;
	private JCheckBox autoHideCheckBox, useEncryptCheckBox;
	private JButton testButton, settingButton, runButton, hideButton,
			aboutButton, exitButton;
	private JPanel contentPanel;
	private Image iconImage;
	private TrayIcon trayIcon;
	private MenuItem displayItem, exitItem;
	private JLabel transferLabel;
	private Timer timer;

	private ButtonMouseAdapter buttonMouseAdapter;
	private ResourceBundle rb;

	public MainFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		initComponents();
		setupGUI();
		setUpEventListener();

		initRunStatus();
	}

	public void initRunStatus() {
		if (ProxyConstants.AUTO_START_AND_HIDE) {
			runProxyServer();
			setVisible(false);
		} else {
			setVisible(true);
		}
	}

	private void initComponents() {
		try {
			rb = ResourceBundle.getBundle("webpagetunnel.MainFrame",
					Locale.getDefault());
		} catch (Exception e) {
			rb = ResourceBundle.getBundle("webpagetunnel.MainFrame_en_US");
		}
		proxyPageTextField = new JTextField(ProxyConstants.HTTP_FULL_URL);
		portTextField = new JTextField(
				String.valueOf(ProxyConstants.HTTPProxyPort));
		logTextArea = new JTextArea();
		autoHideCheckBox = new JCheckBox(
				rb.getString("AutoStartupAndMinimize"),
				ProxyConstants.AUTO_START_AND_HIDE);
		useEncryptCheckBox = new JCheckBox(rb.getString("EnableEncryption"),
				ProxyConstants.ENCRYPTION_ENABLED);
		testButton = new JButton(rb.getString("Test"));
		settingButton = new JButton(rb.getString("Advance"));
		runButton = new JButton(rb.getString("Startup"));
		hideButton = new JButton(rb.getString("Minimize"));
		aboutButton = new JButton(rb.getString("About"));
		exitButton = new JButton(rb.getString("Exit"));
		displayItem = new MenuItem(rb.getString("Show"));
		exitItem = new MenuItem(rb.getString("Exit"));
		transferLabel = new JLabel("0.00 KB");
		timer = new Timer(3000, null);
		contentPanel = new JPanel();
		buttonMouseAdapter = new ButtonMouseAdapter();
		System.setOut(new PrintStream(new TextAreaOutputStream(logTextArea)));
	}

	private void setupGUI() {
		setupMainWindow();
		setupSystemTray();
		setupContentPanel();
		this.getContentPane().add(contentPanel);
	}

	private void setupMainWindow() {
		setTitle(FRAME_TITLE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = getSize();
		setLocation((screenSize.width - windowSize.width) / 2,
				(screenSize.height - windowSize.height) / 2);

		iconImage = getToolkit().getImage(
				this.getClass().getClassLoader().getResource("res/icon.png"));
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(iconImage, 0);
		try {
			mt.waitForID(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setIconImage(iconImage);
	}

	private void setupSystemTray() {
		if (SystemTray.isSupported()) {
			PopupMenu popup = new PopupMenu();
			SystemTray tray = SystemTray.getSystemTray();
			Dimension iconDimension = tray.getTrayIconSize();
			trayIcon = new TrayIcon(iconImage.getScaledInstance(
					iconDimension.width, iconDimension.height,
					Image.SCALE_SMOOTH), rb.getString("TransferedDataSize")
					+ ": 0.00 KB");
			popup.add(displayItem);
			popup.add(exitItem);
			trayIcon.setPopupMenu(popup);
			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				e.printStackTrace();
			}
			return;
		} else {
			String message_text = rb.getString("YouSystemNotSupportMinimize");
			JOptionPane.showMessageDialog(null, message_text,
					rb.getString("Error"), JOptionPane.ERROR_MESSAGE);
		}
	}

	private void setupContentPanel() {

		JPanel north_panel = new JPanel();
		north_panel.setBorder(new CompoundBorder(new TitledBorder(rb
				.getString("Option")), new EmptyBorder(0, GAP_SIZE, GAP_SIZE,
				GAP_SIZE)));
		north_panel.setLayout(new GridLayout(2, 0, GAP_SIZE, GAP_SIZE));

		JPanel north_panel_child0 = new JPanel();
		north_panel_child0.setLayout(new BoxLayout(north_panel_child0,
				BoxLayout.X_AXIS));
		north_panel_child0.add(new JLabel(rb.getString("ServerUrl")));
		north_panel_child0.add(Box.createHorizontalStrut(GAP_SIZE));
		north_panel_child0.add(proxyPageTextField);
		north_panel_child0.add(Box.createHorizontalStrut(GAP_SIZE));
		// north_panel_child0.add(testButton);
		north_panel_child0.add(runButton);

		JPanel north_panel_child1 = new JPanel();
		north_panel_child1.setLayout(new BoxLayout(north_panel_child1,
				BoxLayout.X_AXIS));
		north_panel_child1.add(new JLabel(rb.getString("ClientPort")));
		north_panel_child1.add(Box.createHorizontalStrut(GAP_SIZE));
		north_panel_child1.add(portTextField);
		north_panel_child1.add(Box.createHorizontalStrut(GAP_SIZE));
		north_panel_child1.add(autoHideCheckBox);
		north_panel_child1.add(Box.createHorizontalStrut(GAP_SIZE));
		north_panel_child1.add(useEncryptCheckBox);
		north_panel_child1.add(Box.createHorizontalStrut(GAP_SIZE));
		north_panel_child1.add(settingButton);
		// north_panel_child1.add(Box.createHorizontalStrut(GAP_SIZE));
		// north_panel_child1.add(runButton);

		north_panel.add(north_panel_child0);
		north_panel.add(north_panel_child1);

		JPanel center_panel = new JPanel();
		center_panel.setBorder(new CompoundBorder(new TitledBorder(rb
				.getString("Log")), new EmptyBorder(0, GAP_SIZE, GAP_SIZE,
				GAP_SIZE)));
		center_panel.setLayout(new BorderLayout());
		center_panel.add(new JScrollPane(logTextArea), BorderLayout.CENTER);

		JPanel south_panel = new JPanel();
		south_panel.setLayout(new BorderLayout());

		JPanel south_panel_child0 = new JPanel();
		south_panel_child0.setLayout(new FlowLayout(FlowLayout.LEFT));
		south_panel_child0.add(new JLabel(rb.getString("TransferedDataSize")
				+ ": "));
		south_panel_child0.add(transferLabel);

		JPanel south_panel_child1 = new JPanel();
		south_panel_child1.setLayout(new FlowLayout(FlowLayout.RIGHT));
		south_panel_child1.add(hideButton);
		south_panel_child1.add(aboutButton);
		south_panel_child1.add(exitButton);

		south_panel.add(south_panel_child0, BorderLayout.WEST);
		south_panel.add(south_panel_child1, BorderLayout.EAST);

		contentPanel.setBorder(new EmptyBorder(BORDER_SIZE, BORDER_SIZE,
				BORDER_SIZE, BORDER_SIZE));
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(north_panel, BorderLayout.NORTH);
		contentPanel.add(center_panel, BorderLayout.CENTER);
		contentPanel.add(south_panel, BorderLayout.SOUTH);
	}

	private void testProxyPage() {
		String message_text;
		String urlText = proxyPageTextField.getText().trim();
		if (urlText.isEmpty()) {
			message_text = rb.getString("NotInputServerUrl");
			JOptionPane.showMessageDialog(null, message_text,
					rb.getString("Error"), JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!urlText.startsWith(PROCOTOL_PREFIX)) {
			urlText = PROCOTOL_PREFIX + urlText;
			proxyPageTextField.setText(urlText);
		}
		if (urlText.indexOf("/", PROCOTOL_PREFIX.length()) == -1) {
			urlText += "/";
		}

		URL server;
		try {
			server = new URL(urlText + "?test=1");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			message_text = rb.getString("NotAvailableHttpUrl");
			JOptionPane.showMessageDialog(null, message_text,
					rb.getString("Error"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) server.openConnection();
			connection.setDoOutput(true);
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				String response_text = reader.readLine();
				boolean isAvaiable = response_text.contains("Test Passed");
				if (isAvaiable) {
					message_text = rb.getString("ServerUrlIsAvailable");
				} else {
					message_text = rb.getString("ServerUrlMayAvailable");
				}
			} else {
				message_text = rb.getString("ServerUrlIsInvalid");
			}
			JOptionPane.showMessageDialog(null, message_text,
					rb.getString("Info"), JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			message_text = rb.getString("AccessProxyUrlWrong");
			JOptionPane.showMessageDialog(null, message_text,
					rb.getString("Error"), JOptionPane.ERROR_MESSAGE);
		}
	}

	private void runProxyServer() {
		if (!runButton.isEnabled()) {
			return;
		}

		updateSettings();
		ProxyRunner.runServers();
		while (HTTPProxyServerStarter.started == 0
				|| HttpsServerStarter_443.started == 0
				|| HttpsServerStarter_8443.started == 0) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e1) {
			}
		}
		if (HTTPProxyServerStarter.started == 2
				|| HttpsServerStarter_443.started == 2
				|| HttpsServerStarter_8443.started == 2) {
			System.exit(1);
		}
		runButton.setEnabled(false);
		settingButton.setEnabled(false);
		timer.start();
	}

	private void updateSettings() {
		ProxyConstants.HTTP_FULL_URL = proxyPageTextField.getText().trim();
		ProxyConstants.HTTPProxyPort = Integer.parseInt(portTextField.getText()
				.trim());
		ProxyConstants.ENCRYPTION_ENABLED = useEncryptCheckBox.isSelected();
		ProxyConstants.AUTO_START_AND_HIDE = autoHideCheckBox.isSelected();
		Common.parseFullUrl();
		Common.saveSettings();
	}

	private void updateTransferText() {
		String transferText;
		float transfer = (float) ProxyConstants.TOTAL_TRANSFER / 1024;
		if (transfer < 1024) {
			transferText = String.format("%.2f KB", transfer);
		} else {
			transferText = String.format("%.2f MB", transfer / 1024);
		}
		transferLabel.setText(transferText);
		trayIcon.setToolTip(rb.getString("TransferedDataSize") + ": "
				+ transferText);
	}

	private void showSettingDialog() {
		SettingDialog settingDialog = new SettingDialog();
		settingDialog.setVisible(true);
	}

	private void hideToSystemTray(boolean hide) {
		setVisible(!hide);
	}

	private void showAboutDialog() {
		String aboutText;
		aboutText = "Webpage tunnel v0.1.3\n"
				+ "under GPLv3 write by muzuiget\n"
				+ "http://code.google.com/p/webpage-tunnel/\n"
				+ "base on India Web Proxy v1.0\n"
				+ "write by Arunava  Bhowmick\n"
				+ "http://webproxytunnel.sourceforge.net/";
		JOptionPane.showMessageDialog(null, aboutText, "关于",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void setUpEventListener() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		testButton.addMouseListener(buttonMouseAdapter);
		settingButton.addMouseListener(buttonMouseAdapter);
		runButton.addMouseListener(buttonMouseAdapter);
		hideButton.addMouseListener(buttonMouseAdapter);
		aboutButton.addMouseListener(buttonMouseAdapter);
		exitButton.addMouseListener(buttonMouseAdapter);
		trayIcon.addMouseListener(buttonMouseAdapter);
		displayItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideToSystemTray(false);
			}
		});
		exitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		timer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateTransferText();
			}
		});
	}

	private class ButtonMouseAdapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			Object source = e.getSource();
			if (source == testButton) {
				testProxyPage();
				return;
			}
			if (source == settingButton) {
				showSettingDialog();
				return;
			}
			if (source == runButton) {
				runProxyServer();
				return;
			}
			if (source == hideButton) {
				hideToSystemTray(true);
				return;
			}
			if (source == trayIcon) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					hideToSystemTray(false);
				}
				return;
			}
			if (source == aboutButton) {
				showAboutDialog();
				return;
			}
			if (source == exitButton) {
				updateSettings();
				System.exit(0);
				return;
			}
		}
	}

	private class TextAreaOutputStream extends OutputStream {

		private JTextArea textarea;

		public TextAreaOutputStream(JTextArea textarea) {
			this.textarea = textarea;
		}

		@Override
		public void write(int b) throws IOException {
			textarea.append(String.valueOf((char) b));
		}
	}
}
