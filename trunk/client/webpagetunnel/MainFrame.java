package webpagetunnel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
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

	private static final int FRAME_WIDTH = 520;
	private static final int FRAME_HEIGHT = 460;
	private static final String FRAME_TITLE = "Web page tunnel v0.1";
	private static final int GAP_SIZE = 3;
	private static final int BORDER_SIZE = 6;
	private static final String PROCOTOL_PREFIX = "http://";

	private JTextField proxyPageTextField, portTextField;
	private JTextArea logTextArea;
	private JCheckBox useEncryptCheckBox;
	private JButton testButton, settingButton, runButton, aboutButton,
			exitButton;
	private JPanel contentPanel;

	private ButtonMouseAdapter buttonMouseAdapter;

	public MainFrame() {
		initComponents();
		setupGUI();
		setUpEventListener();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Common.initCertificate();
		Common.initSettings();
		MainFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);
	}

	private void initComponents() {
		proxyPageTextField = new JTextField(ProxyConstants.webPHP_URL_HTTP);
		portTextField = new JTextField(String
				.valueOf(ProxyConstants.HTTPProxyPort));
		logTextArea = new JTextArea();
		useEncryptCheckBox = new JCheckBox("使用加密传输",
				ProxyConstants.ENCRYPTION_ENABLED);
		testButton = new JButton("测试");
		settingButton = new JButton("高级设置");
		runButton = new JButton("启动");
		aboutButton = new JButton("关于");
		exitButton = new JButton("退出");
		contentPanel = new JPanel();
		buttonMouseAdapter = new ButtonMouseAdapter();
		System.setOut(new PrintStream(new TextAreaOutputStream(logTextArea)));
	}

	private void setupGUI() {
		setupMainWindow();
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

		Image icon = getToolkit().getImage(
				this.getClass().getClassLoader().getResource("res/icon.png"));
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(icon, 0);
		try {
			mt.waitForID(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setIconImage(icon);
	}

	private void setupContentPanel() {

		JPanel north_panel = new JPanel();
		north_panel.setBorder(new CompoundBorder(new TitledBorder("选项"),
				new EmptyBorder(0, GAP_SIZE, GAP_SIZE, GAP_SIZE)));
		north_panel.setLayout(new GridLayout(2, 0, GAP_SIZE, GAP_SIZE));

		JPanel north_panel_child0 = new JPanel();
		north_panel_child0.setLayout(new BoxLayout(north_panel_child0,
				BoxLayout.X_AXIS));
		north_panel_child0.add(new JLabel("代理网页地址: "));
		north_panel_child0.add(Box.createHorizontalStrut(GAP_SIZE));
		north_panel_child0.add(proxyPageTextField);
		north_panel_child0.add(Box.createHorizontalStrut(GAP_SIZE));
		// north_panel_child0.add(testButton);
		north_panel_child0.add(runButton);

		JPanel north_panel_child1 = new JPanel();
		north_panel_child1.setLayout(new BoxLayout(north_panel_child1,
				BoxLayout.X_AXIS));
		north_panel_child1.add(new JLabel("本地代理端口: "));
		north_panel_child1.add(Box.createHorizontalStrut(GAP_SIZE));
		north_panel_child1.add(portTextField);
		north_panel_child1.add(Box.createHorizontalStrut(GAP_SIZE));
		north_panel_child1.add(useEncryptCheckBox);
		north_panel_child1.add(Box.createHorizontalStrut(GAP_SIZE));
		north_panel_child1.add(settingButton);
		// north_panel_child1.add(Box.createHorizontalStrut(GAP_SIZE));
		// north_panel_child1.add(runButton);

		north_panel.add(north_panel_child0);
		north_panel.add(north_panel_child1);

		JPanel center_panel = new JPanel();
		center_panel.setBorder(new CompoundBorder(new TitledBorder("日志"),
				new EmptyBorder(0, GAP_SIZE, GAP_SIZE, GAP_SIZE)));
		center_panel.setLayout(new BorderLayout());
		center_panel.add(new JScrollPane(logTextArea), BorderLayout.CENTER);

		JPanel south_panel = new JPanel();
		south_panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		south_panel.add(aboutButton);
		south_panel.add(exitButton);

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
			message_text = "未输入代理网页。";
			JOptionPane.showMessageDialog(null, message_text, "错误",
					JOptionPane.ERROR_MESSAGE);
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
			message_text = "不是一个合法的http地址！";
			JOptionPane.showMessageDialog(null, message_text, "错误",
					JOptionPane.ERROR_MESSAGE);
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
					message_text = "代理网页是有效的。";
				} else {
					message_text = "代理网页有回应，但可能不是一个有效的代理网页。";
				}
			} else {
				message_text = "代理网页是无效的。";
			}
			JOptionPane.showMessageDialog(null, message_text, "信息",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			message_text = "访问代理网页出现错误。";
			JOptionPane.showMessageDialog(null, message_text, "错误",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void runProxyServer() {
		updateSettings();

		ProxyRunner.runServers();
		while (HTTPProxyServerStarter.started == 0
				|| HttpsServerStarter_443.started == 0
				|| HttpsServerStarter_8443.started == 0) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
			}
		}
		if (HTTPProxyServerStarter.started == 2
				|| HttpsServerStarter_443.started == 2
				|| HttpsServerStarter_8443.started == 2) {
			System.exit(1);
		}
		runButton.setEnabled(false);
	}

	private void updateSettings() {
		int port = Integer.parseInt(portTextField.getText().trim());
		String url = proxyPageTextField.getText().trim();
		int index = url.indexOf("/", PROCOTOL_PREFIX.length());
		String host = url.substring(PROCOTOL_PREFIX.length(), index);
		ProxyConstants.webPHP_HOST_HTTP = host;
		ProxyConstants.HTTPProxyPort = port;
		ProxyConstants.webPHP_URL_HTTP = url;
		ProxyConstants.ENCRYPTION_ENABLED = useEncryptCheckBox.isSelected();
		Common.saveSettings();
	}

	private void showSettingDialog() {
		SettingDialog settingDialog = new SettingDialog();
		settingDialog.setVisible(true);
	}

	private void showAboutDialog() {
		String aboutText;
		aboutText = "Web page tunnel v0.1\nunder GPLv3 write by muzuiget\n"
				+ "base on India Web Proxy v1.0\nwrite by Arunava  Bhowmick";
		JOptionPane.showMessageDialog(null, aboutText, "关于",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void setUpEventListener() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		testButton.addMouseListener(buttonMouseAdapter);
		settingButton.addMouseListener(buttonMouseAdapter);
		runButton.addMouseListener(buttonMouseAdapter);
		aboutButton.addMouseListener(buttonMouseAdapter);
		exitButton.addMouseListener(buttonMouseAdapter);
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
				if (runButton.isEnabled()) {
					runProxyServer();
				}
				return;
			}
			if (source == aboutButton) {
				showAboutDialog();
				return;
			}
			if (source == exitButton) {
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
