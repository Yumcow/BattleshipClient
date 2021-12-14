import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

public class BattleshipWaitingroomView {

	private JFrame frame;
	private JTextField chatTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BattleshipWaitingroomView window = new BattleshipWaitingroomView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BattleshipWaitingroomView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1100, 650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel roomPane = new JPanel();
		roomPane.setBounds(12, 10, 800, 400);
		frame.getContentPane().add(roomPane);
		
		JTextArea userList = new JTextArea();
		userList.setBounds(824, 10, 248, 591);
		userList.setEnabled(false); // 사용자 수정 불가
		frame.getContentPane().add(userList);
		
		chatTextField = new JTextField();
		chatTextField.setBounds(12, 580, 420, 24);
		frame.getContentPane().add(chatTextField);
		chatTextField.setColumns(10);
		
		JButton sendButton = new JButton("Send");
		sendButton.setBounds(437, 579, 97, 24);
		frame.getContentPane().add(sendButton);
		
		JTextArea chatList = new JTextArea();
		chatList.setBounds(12, 420, 800, 150);
		chatList.setEnabled(false); // 사용자 수정 불가
		frame.getContentPane().add(chatList);
	}

}
