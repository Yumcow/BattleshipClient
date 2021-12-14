import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JButton;
//import JavaObjClientMain.Myaction;

public class BattleshipLogInView {

	private JFrame frame;
	private JTextField ipTextField;
	private JTextField portTextField;
	private JTextField nameTextField;
	private JTextField codeTextField;

	/**
	 * Create the application.
	 */
	public BattleshipLogInView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel titleLabel = new JLabel("Battleship Online");
		titleLabel.setFont(new Font("굴림", Font.PLAIN, 24));
		titleLabel.setBounds(125, 28, 169, 28);
		frame.getContentPane().add(titleLabel);
		
		JLabel ipLabel = new JLabel("Server IP");
		ipLabel.setFont(new Font("굴림", Font.PLAIN, 16));
		ipLabel.setBounds(45, 95, 64, 28);
		frame.getContentPane().add(ipLabel);
		
		JLabel portLabel = new JLabel("Server Port");
		portLabel.setFont(new Font("굴림", Font.PLAIN, 16));
		portLabel.setBounds(45, 133, 81, 28);
		frame.getContentPane().add(portLabel);
		
		JLabel nameLabel = new JLabel("User Name");
		nameLabel.setFont(new Font("굴림", Font.PLAIN, 16));
		nameLabel.setBounds(45, 171, 81, 28);
		frame.getContentPane().add(nameLabel);
		
		ipTextField = new JTextField();
		ipTextField.setText("127.0.0.1");
		ipTextField.setBounds(138, 97, 116, 28);
		frame.getContentPane().add(ipTextField);
		ipTextField.setColumns(10);
		
		portTextField = new JTextField();
		portTextField.setText("30000");
		portTextField.setBounds(138, 135, 116, 28);
		frame.getContentPane().add(portTextField);
		portTextField.setColumns(10);
		
		nameTextField = new JTextField();
		nameTextField.setBounds(138, 173, 116, 28);
		frame.getContentPane().add(nameTextField);
		nameTextField.setColumns(10);
		
		JButton playButton = new JButton("PLAY");
		playButton.setFont(new Font("굴림", Font.PLAIN, 14));
		playButton.setBounds(287, 163, 116, 47);
		frame.getContentPane().add(playButton);
		
		JLabel codeLabel = new JLabel("Room Code");
		codeLabel.setFont(new Font("굴림", Font.PLAIN, 16));
		codeLabel.setBounds(45, 209, 87, 28);
		frame.getContentPane().add(codeLabel);
		
		codeTextField = new JTextField();
		codeTextField.setBounds(138, 211, 116, 28);
		frame.getContentPane().add(codeTextField);
		codeTextField.setColumns(10);
		
		Myaction action = new Myaction();
		playButton.addActionListener(action);
		nameTextField.addActionListener(action);
		ipTextField.addActionListener(action);
		portTextField.addActionListener(action);
		codeTextField.addActionListener(action);
		
		frame.setVisible(true);
	}
	
	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = nameTextField.getText().trim();
			String ip_addr = ipTextField.getText().trim();
			String port_no = portTextField.getText().trim();
			String codeStr = codeTextField.getText().trim();
			try {
				int codeInt = Integer.parseInt(codeStr);
				if(codeInt < 1001 || codeInt > 1999) {
					JOptionPane tempPane=new JOptionPane();
					tempPane.showMessageDialog(null, "방 코드는 1001~1999사이의 정수를 입력해야 합니다.");
					return;
				}
			} catch(NumberFormatException e1) {
				JOptionPane tempPane=new JOptionPane();
				tempPane.showMessageDialog(null, "방 코드는 1001~1999사이의 정수를 입력해야 합니다.");
				return;
			}
			BattleshipGameroomView view = new BattleshipGameroomView(ip_addr, port_no, username, codeStr);
			frame.setVisible(false);
		}
	}
}
