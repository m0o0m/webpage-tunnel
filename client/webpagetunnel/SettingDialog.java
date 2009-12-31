package webpagetunnel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.india.arunava.network.utils.ProxyConstants;

@SuppressWarnings("serial")
public class SettingDialog extends JDialog implements MouseListener {

	private static final int DIALOG_WIDTH = 420;
	private static final int DIALOG_HEIGHT = 420;
	private static final String DIALOG_TITLE = "高级设置";
	private static final int GAP_SIZE = 3;
	private static final int BORDER_SIZE = 6;

	public static final int RESPONSE_OK = 0;
	public static final int RESPONSE_CANCEL = 1;

	private JPanel contentPanel;
	private JCheckBox useProxyCheckBox;
	private JButton okayButton, cancelButton;
	private JComboBox logLevelComboBox;
	private JTextField bufferSizeTextField, proxyHostTextField,
			proxyPortTextField, proxyUsernameTextField, sslHost443TextField,
			sslPort443TextField, sslHost8443TextField, sslPort8443TextField;
	private JPasswordField proxyPasswordTextField;

	private String[] logLevelItem = { "简洁", "一般", "详细", "调试" };

	public SettingDialog() {
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
		SettingDialog settingDialog = new SettingDialog();
		settingDialog.setVisible(true);
	}

	private void initComponents() {
		contentPanel = new JPanel();
		useProxyCheckBox = new JCheckBox("本机通过其它代理连接到互联网",
				ProxyConstants.ORGANIZATION_HTTP_PROXY_ENABLED);
		logLevelComboBox = new JComboBox(logLevelItem);
		bufferSizeTextField = new JTextField(String
				.valueOf(ProxyConstants.MAX_BUFFER));
		proxyHostTextField = new JTextField(String
				.valueOf(ProxyConstants.ORGANIZATION_HTTP_PROXY_HOST));
		proxyPortTextField = new JTextField(String
				.valueOf(ProxyConstants.ORGANIZATION_HTTP_PROXY_PORT));
		proxyUsernameTextField = new JTextField(String
				.valueOf(ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_NAME));
		proxyPasswordTextField = new JPasswordField(String
				.valueOf(ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_PASS));
		sslHost443TextField = new JTextField(String
				.valueOf(ProxyConstants.HTTPSServer_443));
		sslPort443TextField = new JTextField(String
				.valueOf(ProxyConstants.HTTPSPort_443));
		sslHost8443TextField = new JTextField(String
				.valueOf(ProxyConstants.HTTPSServer_8443));
		sslPort8443TextField = new JTextField(String
				.valueOf(ProxyConstants.HTTPSPort_8443));
		okayButton = new JButton("确定");
		cancelButton = new JButton("取消");
		proxyHostTextField
				.setEnabled(ProxyConstants.ORGANIZATION_HTTP_PROXY_ENABLED);
		proxyPortTextField
				.setEnabled(ProxyConstants.ORGANIZATION_HTTP_PROXY_ENABLED);
		proxyUsernameTextField
				.setEnabled(ProxyConstants.ORGANIZATION_HTTP_PROXY_ENABLED);
		proxyPasswordTextField
				.setEnabled(ProxyConstants.ORGANIZATION_HTTP_PROXY_ENABLED);
		if (ProxyConstants.ORGANIZATION_HTTP_PROXY_PORT == 0) {
			proxyPortTextField.setText("");
		}
		logLevelComboBox.setSelectedIndex(ProxyConstants.logLevel);
	}

	private void setupGUI() {
		setupMainWindow();
		setupContentPanel();
		this.getContentPane().add(contentPanel);
	}

	private void setupMainWindow() {
		setModal(true);
		setTitle(DIALOG_TITLE);
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		// setResizable(false);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = getSize();
		setLocation((screenSize.width - windowSize.width) / 2,
				(screenSize.height - windowSize.height) / 2);
	}

	private void setupContentPanel() {

		GridBagConstraints constraintLabel = new GridBagConstraints();
		constraintLabel.anchor = GridBagConstraints.WEST;
		constraintLabel.insets = new Insets(GAP_SIZE, GAP_SIZE, GAP_SIZE,
				GAP_SIZE);

		GridBagConstraints constraintFill = new GridBagConstraints();
		constraintFill.fill = GridBagConstraints.HORIZONTAL;
		constraintFill.weightx = 1.0;
		constraintFill.insets = new Insets(GAP_SIZE, GAP_SIZE, GAP_SIZE,
				GAP_SIZE);

		GridBagConstraints constraintEnd = new GridBagConstraints();
		constraintEnd.gridwidth = GridBagConstraints.REMAINDER;

		GridBagConstraints constraintFillEnd = new GridBagConstraints();
		constraintFillEnd.fill = GridBagConstraints.HORIZONTAL;
		constraintFillEnd.weightx = 1.0;
		constraintFillEnd.gridwidth = GridBagConstraints.REMAINDER;
		constraintFill.insets = new Insets(GAP_SIZE, GAP_SIZE, GAP_SIZE,
				GAP_SIZE);

		JPanel center_panel = new JPanel();
		center_panel.setLayout(new BoxLayout(center_panel, BoxLayout.Y_AXIS));

		JPanel general_panel = new JPanel();
		general_panel.setBorder(new CompoundBorder(new TitledBorder("常规"),
				new EmptyBorder(0, GAP_SIZE, GAP_SIZE, GAP_SIZE)));
		general_panel.setLayout(new GridBagLayout());
		general_panel.add(new JLabel("日志详细程度: "), constraintLabel);
		general_panel.add(logLevelComboBox, constraintFill);
		general_panel.add(new JLabel("缓存大小: "), constraintLabel);
		general_panel.add(bufferSizeTextField, constraintFillEnd);
		general_panel.add(new JLabel());

		JPanel proxy_panel = new JPanel();
		proxy_panel.setBorder(new CompoundBorder(new TitledBorder("代理"),
				new EmptyBorder(0, GAP_SIZE, GAP_SIZE, GAP_SIZE)));
		proxy_panel.setLayout(new GridBagLayout());
		proxy_panel.add(useProxyCheckBox, constraintFillEnd);
		proxy_panel.add(new JLabel("主机 IP: "), constraintLabel);
		proxy_panel.add(proxyHostTextField, constraintFill);
		proxy_panel.add(new JLabel("端口: "), constraintLabel);
		proxy_panel.add(proxyPortTextField, constraintFillEnd);
		proxy_panel.add(new JLabel("用户名: "), constraintLabel);
		proxy_panel.add(proxyUsernameTextField, constraintFill);
		proxy_panel.add(new JLabel("密码: "), constraintLabel);
		proxy_panel.add(proxyPasswordTextField, constraintFillEnd);

		JPanel https_panel = new JPanel();
		https_panel.setBorder(new CompoundBorder(new TitledBorder("HTTPS"),
				new EmptyBorder(0, GAP_SIZE, GAP_SIZE, GAP_SIZE)));
		https_panel.setLayout(new GridBagLayout());
		https_panel.add(new JLabel("远程 SSL 端口为 443"), constraintFillEnd);
		https_panel.add(new JLabel("主机 IP: "), constraintLabel);
		https_panel.add(sslHost443TextField, constraintFill);
		https_panel.add(new JLabel("端口: "), constraintLabel);
		https_panel.add(sslPort443TextField, constraintFillEnd);
		https_panel.add(new JLabel("远程 SSL 端口为 8443"), constraintFillEnd);
		https_panel.add(new JLabel("主机 IP: "), constraintLabel);
		https_panel.add(sslHost8443TextField, constraintFill);
		https_panel.add(new JLabel("端口: "), constraintLabel);
		https_panel.add(sslPort8443TextField, constraintFillEnd);

		center_panel.add(general_panel);
		center_panel.add(proxy_panel);
		center_panel.add(https_panel);

		JPanel sourth_panel = new JPanel();
		sourth_panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		sourth_panel.add(okayButton);
		sourth_panel.add(cancelButton);

		contentPanel.setBorder(new EmptyBorder(BORDER_SIZE, BORDER_SIZE,
				BORDER_SIZE, BORDER_SIZE));
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(center_panel, BorderLayout.CENTER);
		contentPanel.add(sourth_panel, BorderLayout.SOUTH);
	}

	private void updateSettings() {
		String text;
		text = bufferSizeTextField.getText().trim();
		if (!text.isEmpty()) {
			ProxyConstants.MAX_BUFFER = Integer.parseInt(text);
		}
		text = proxyPortTextField.getText().trim();
		if (text.isEmpty() || text.equals("0")) {
			ProxyConstants.ORGANIZATION_HTTP_PROXY_PORT = 0;
		} else {
			ProxyConstants.ORGANIZATION_HTTP_PROXY_PORT = Integer
					.parseInt(text);
		}
		text = sslPort443TextField.getText().trim();
		if (!text.isEmpty()) {
			ProxyConstants.HTTPSPort_443 = Integer.parseInt(text);
		}
		text = sslPort8443TextField.getText().trim();
		if (!text.isEmpty()) {
			ProxyConstants.HTTPSPort_8443 = Integer.parseInt(text);
		}
		ProxyConstants.logLevel = logLevelComboBox.getSelectedIndex();
		ProxyConstants.ORGANIZATION_HTTP_PROXY_ENABLED = useProxyCheckBox
				.isSelected();
		ProxyConstants.ORGANIZATION_HTTP_PROXY_HOST = proxyHostTextField
				.getText().trim();
		ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_NAME = proxyUsernameTextField
				.getText().trim();
		ProxyConstants.ORGANIZATION_HTTP_PROXY_USER_PASS = new String(
				proxyPasswordTextField.getPassword());
		ProxyConstants.HTTPSServer_443 = sslHost443TextField.getText().trim();
		ProxyConstants.HTTPSServer_8443 = sslHost8443TextField.getText().trim();
		Common.saveSettings();
	}

	private void setUpEventListener() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		useProxyCheckBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean status = useProxyCheckBox.isSelected();
				proxyHostTextField.setEnabled(status);
				proxyPortTextField.setEnabled(status);
				proxyUsernameTextField.setEnabled(status);
				proxyPasswordTextField.setEnabled(status);
			}

		});
		okayButton.addMouseListener(this);
		cancelButton.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Object source = e.getSource();
		if (source == okayButton) {
			updateSettings();
			this.dispose();
		}
		if (source == cancelButton) {
			this.dispose();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
