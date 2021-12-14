import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;

public class BattleshipGameroomView {
	private JFrame frame; 
	private JPanel p1Pane;
	private JPanel p2Pane;
	private JTextField chatTextField; // ä�� Field
	private JTextPane chatList; // ä�� area
	private JTextPane logList; // log area
	private JTextPane observerList; // observer area
	private JButton sendChatButton;
	private JLabel p1Label; // p1 �̸� ǥ���� Label
	private JLabel p2Label; // p2 �̸� ǥ���� Label
	private int p1BoardCells[][] = new int[10][10];
	private int p2BoardCells[][] = new int[10][10];
	//private JLabel[][] p1BoardLabel;
	//private JLabel[][] p2BoardLabel;
	private JLabel codeLabel; // �� �ڵ带 ǥ���� Label
	
	private int roomCode;
	private String p1Name;
	private String p2Name;
	private String userName;
	
	private Socket socket; // ���� ����
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	private ImageIcon background = new ImageIcon("images/background.png");
	private ImageIcon fog = new ImageIcon("images/fog.png");
	private ImageIcon shot1 = new ImageIcon("images/shot1.png");
	private ImageIcon shot2 = new ImageIcon("images/shot2.png");
	private ImageIcon[] dd;
	private ImageIcon[] cl;
	private ImageIcon[] bb;
	private ImageIcon[] cv;

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the application.
	 */
	public BattleshipGameroomView(String ip_addr, String port_no, String username, String roomCode) {
		//������ ��ġ
		dd = new ImageIcon[4];
		cl = new ImageIcon[6];
		bb = new ImageIcon[8];
		cv = new ImageIcon[10];
		for(int i = 0; i < 4; i++) dd[i] = new ImageIcon("images/dd" + i + ".png");
		for(int i = 0; i < 6; i++) cl[i] = new ImageIcon("images/cl" + i + ".png");
		for(int i = 0; i < 8; i++) bb[i] = new ImageIcon("images/bb" + i + ".png");
		for(int i = 0; i < 10; i++) cv[i] = new ImageIcon("images/cv" + i + ".png");
		//cells �ʱ�ȭ
		resetCells();
		initialize(); // ������ ����
		codeLabel.setText("�� �ڵ� : " + roomCode);
		codeLabel.repaint();
		try {
			userName = username;
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());
			ListenNetwork net = new ListenNetwork();
			net.start();
			//�α��� �۾�
			GameSignal loginGs = new GameSignal(username, "101", roomCode);
			SendGameSignal(loginGs);
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
			AppendChatText("connect error");
		}
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				SendGameSignal(new GameSignal(userName, "321"));
				System.exit(0);
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	// ���� �г� �� �� ��ġ
	// cells ����
	private void resetCells() {
		for(int i = 0; i < 10; i++)
			for(int j = 0; j < 10; j++) {
				p1BoardCells[i][j] = 0;
				p2BoardCells[i][j] = 0;
			}
	}
	// ������
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1160, 670);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		p1Pane = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(background.getImage(), 0, 0, null);
				
				for(int i = 0; i < 10; i++) {
					for(int j = 0; j < 10; j++) {
						int x = 40 * (i + 1) + 1;
						int y = 40 * (j + 1) + 1;
						int shotted;
						if (p1BoardCells[i][j] == 0) { // ����
							continue;
						} else if (p1BoardCells[i][j] == 1) { // ǳ��
							g.drawImage(shot1.getImage(), x, y, null);
						} else if (p1BoardCells[i][j] == 2) { // ��Ȯ��
							g.drawImage(fog.getImage(), x, y, null);
						} else if (p1BoardCells[i][j] == 3) { // ���� ���� ��Ȯ��
							g.drawImage(shot2.getImage(), x, y, null);
						} else {
							shotted = (p1BoardCells[i][j] / 10) % 10;
							switch(p1BoardCells[i][j] / 100) {
							case 3: // dd
								g.drawImage(dd[p1BoardCells[i][j] % 10].getImage(), x, y, null);
								if(shotted == 1) g.drawImage(shot2.getImage(), x, y, null);
								break;
							case 4: // cl
								g.drawImage(cl[p1BoardCells[i][j] % 10].getImage(), x, y, null);
								if(shotted == 1) g.drawImage(shot2.getImage(), x, y, null);
								break;
							case 5: // bb
								g.drawImage(bb[p1BoardCells[i][j] % 10].getImage(), x, y, null);
								if(shotted == 1) g.drawImage(shot2.getImage(), x, y, null);
								break;
							case 6: // cv
								g.drawImage(cv[p1BoardCells[i][j] % 10].getImage(), x, y, null);
								if(shotted == 1) g.drawImage(shot2.getImage(), x, y, null);
								break;
							}
						}
					}
				}
			}
		};
		p1Pane.setBounds(12, 10, 440, 440);
		frame.getContentPane().add(p1Pane);
		p1Pane.setLayout(null);
		
		p2Pane = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(background.getImage(), 0, 0, null);
				
				for(int i = 0; i < 10; i++) {
					for(int j = 0; j < 10; j++) {
						int x = 40 * (i + 1) + 1;
						int y = 40 * (j + 1) + 1;
						int shotted;
						if (p2BoardCells[i][j] == 0) { // ����
							continue;
						} else if (p2BoardCells[i][j] == 1) { // ǳ��
							g.drawImage(shot1.getImage(), x, y, null);
						} else if (p2BoardCells[i][j] == 2) { // ��Ȯ��
							g.drawImage(fog.getImage(), x, y, null);
						} else if (p2BoardCells[i][j] == 3) { // ���� ���� ��Ȯ��
							g.drawImage(shot2.getImage(), x, y, null);
						} else {
							shotted = (p2BoardCells[i][j] / 10) % 10;
							switch(p2BoardCells[i][j] / 100) {
							case 3: // dd
								g.drawImage(dd[p2BoardCells[i][j] % 10].getImage(), x, y, null);
								if(shotted == 1) g.drawImage(shot2.getImage(), x, y, null);
								break;
							case 4: // cl
								g.drawImage(cl[p2BoardCells[i][j] % 10].getImage(), x, y, null);
								if(shotted == 1) g.drawImage(shot2.getImage(), x, y, null);
								break;
							case 5: // bb
								g.drawImage(bb[p2BoardCells[i][j] % 10].getImage(), x, y, null);
								if(shotted == 1) g.drawImage(shot2.getImage(), x, y, null);
								break;
							case 6: // cv
								g.drawImage(cv[p2BoardCells[i][j] % 10].getImage(), x, y, null);
								if(shotted == 1) g.drawImage(shot2.getImage(), x, y, null);
								break;
							}
						}
					}
				}
			}
		};
		p2Pane.setBounds(464, 10, 440, 440);
		frame.getContentPane().add(p2Pane);
		p2Pane.setLayout(null);
		
		p1Label = new JLabel("Player 1 :");
		p1Label.setFont(new Font("����", Font.PLAIN, 14));
		p1Label.setBounds(916, 70, 216, 50);
		frame.getContentPane().add(p1Label);
		
		p2Label = new JLabel("Player 2 :");
		p2Label.setFont(new Font("����", Font.PLAIN, 14));
		p2Label.setBounds(916, 130, 216, 50);
		frame.getContentPane().add(p2Label);
		
		/*
		chatList = new JTextArea();
		chatList.setForeground(Color.BLACK);
		chatList.setFont(new Font("����", Font.PLAIN, 14));
		chatList.setBounds(0, 21, 440, 102);
		chatList.setEnabled(false); // ����� ���� �Ұ�
		chatPanel.add(chatList);
		*/
		
		chatTextField = new JTextField();
		chatTextField.setColumns(10);
		chatTextField.setBounds(12, 594, 362, 28);
		frame.getContentPane().add(chatTextField);
		
		sendChatButton = new JButton("Send");
		sendChatButton.setBounds(386, 593, 66, 28);
		frame.getContentPane().add(sendChatButton);
		
		JLabel chatLabel = new JLabel("\uCC44\uD305");
		chatLabel.setBounds(208, 460, 26, 15);
		frame.getContentPane().add(chatLabel);
		
		JScrollPane scrollChatPane = new JScrollPane();
		scrollChatPane.setBounds(12, 481, 440, 103);
		frame.getContentPane().add(scrollChatPane);
		
		chatList = new JTextPane();
		chatList.setFont(new Font("����ü", Font.PLAIN, 14));
		scrollChatPane.setViewportView(chatList);
		
		JLabel logLabel = new JLabel("\uB85C\uADF8");
		logLabel.setBounds(672, 460, 30, 15);
		frame.getContentPane().add(logLabel);
		
		JButton absButton = new JButton("\uAE30\uAD8C");
		absButton.setFont(new Font("����", Font.PLAIN, 16));
		absButton.setBounds(1038, 481, 94, 59);
		frame.getContentPane().add(absButton);
		absButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SendGameSignal(new GameSignal(userName, "314"));
			}
		});
		
		JButton exitButton = new JButton("\uB098\uAC00\uAE30");
		exitButton.setFont(new Font("����", Font.PLAIN, 16));
		exitButton.setBounds(916, 550, 216, 71);
		frame.getContentPane().add(exitButton);
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SendGameSignal(new GameSignal(userName, "321"));
				System.exit(0);
			}
		});
		
		codeLabel = new JLabel("\uBC29 \uCF54\uB4DC : ");
		codeLabel.setFont(new Font("����", Font.PLAIN, 20));
		codeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		codeLabel.setBounds(916, 10, 216, 50);
		frame.getContentPane().add(codeLabel);
		
		JScrollPane scrollLogPane = new JScrollPane();
		scrollLogPane.setBounds(464, 481, 440, 140);
		frame.getContentPane().add(scrollLogPane);
		
		logList = new JTextPane();
		logList.setForeground(new Color(0, 128, 0));
		logList.setFont(new Font("����ü", Font.PLAIN, 14));
		scrollLogPane.setViewportView(logList);
		
		JScrollPane scrollObserverPane = new JScrollPane();
		scrollObserverPane.setBounds(916, 190, 216, 260);
		frame.getContentPane().add(scrollObserverPane);
		
		observerList = new JTextPane();
		observerList.setFont(new Font("����ü", Font.PLAIN, 14));
		scrollObserverPane.setViewportView(observerList);
		
		JButton startButton = new JButton("\uC2DC\uC791");
		startButton.setFont(new Font("����", Font.PLAIN, 16));
		startButton.setBounds(916, 481, 94, 59);
		frame.getContentPane().add(startButton);
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SendGameSignal(new GameSignal(userName, "313"));
			}
		});
		
		p1Pane.addMouseListener(new MouseAdapter() { // p1�г� �� ������ 2p ���� (312) ����
			int x, y;
			public void mouseClicked(MouseEvent e) {
				GameSignal gs = new GameSignal(userName, "312");
				x = (e.getX() - 40) / 40;
				y = (e.getY() - 40) / 40;
				gs.data = Integer.toString(x);
				gs.data = gs.data + "/";
				gs.data = gs.data + Integer.toString(y);
				SendGameSignal(gs);
		    }
		});
		p2Pane.addMouseListener(new MouseAdapter() { // p2�г� �� ������ 1p ���� (311) ����
			int x, y;
			public void mouseClicked(MouseEvent e) {
				GameSignal gs = new GameSignal(userName, "311");
				x = (e.getX() - 40) / 40;
				y = (e.getY() - 40) / 40;
				gs.data = Integer.toString(x);
				gs.data = gs.data + "/";
				gs.data = gs.data + Integer.toString(y);
				SendGameSignal(gs);
		    }
		});
		TextSendAction action = new TextSendAction();
		sendChatButton.addActionListener(action);
		chatTextField.addActionListener(action);
		chatTextField.requestFocus();
		
		frame.setVisible(true);
	}
	// keyboard enter key ġ�� ������ ����
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button�� �����ų� �޽��� �Է��ϰ� Enter key ġ��
			if (e.getSource() == sendChatButton || e.getSource() == chatTextField) {
				String msg = null;
				// msg = String.format("[%s] %s\n", UserName, txtInput.getText());
				msg = chatTextField.getText();
				SendGameSignal(new GameSignal(userName, "301", msg));
				chatTextField.setText(""); // �޼����� ������ ���� �޼��� ����â�� ����.
				chatTextField.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��
				if (msg.contains("/exit")) // ���� ó��
					System.exit(0);
			}
		}
	}
	// GameSignal ������Ʈ �д� �޼ҵ�
	public GameSignal ReadGameSignal() {
		Object obj = null;
		String msg = null;
		GameSignal gs = new GameSignal("", "", "");
		
		try {
			obj = ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			AppendChatText("ReadChatMsg Error");
			e.printStackTrace();
			try {
				oos.close();
				socket.close();
				ois.close();
				socket = null;
				return null;
			} catch (IOException e1) {
				e1.printStackTrace();
				try {
					oos.close();
					socket.close();
					ois.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}

				socket = null;
				return null;
			}
		}
		if(obj instanceof GameSignal) {
			gs = (GameSignal) obj;
			return gs;
		}
		else return null;
	}
	// GameSignal ������Ʈ�� ������ �޼ҵ�
	public void SendGameSignal(GameSignal obj) {
		try {
			oos.writeObject(obj);
			oos.flush();
		} catch (IOException e) {
			AppendChatText("SendChatMsg Error");
			e.printStackTrace();
			try {
				oos.close();
				socket.close();
				ois.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	// �����κ��� GameSignal �޴� ������
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				GameSignal gs = ReadGameSignal();
				if (gs==null)
					break;
				if (socket == null)
					break;
				String msg;
				switch (gs.code) {
				case "301": // chat message
					msg = String.format("[%s] %s", gs.UserName, gs.data);
					AppendChatText(msg);
					break;
				case "304": // log message
					msg = gs.data;
					AppendLogText(msg);
					break;
				case "331": // ��ü redraw
					redrawUser(gs.data);
					break;
				case "332": // ��ü redraw
					redrawBoard(gs);
					break;
				}
			}
		}
	}
	// ä��â�� ����ϴ� �޼ҵ�
	public void AppendChatText(String msg) {
		
		msg = msg.trim(); // �յ� blank�� \n�� �����Ѵ�.
		int len = chatList.getDocument().getLength();
		// ������ �̵�
		chatList.setCaretPosition(len);
		chatList.replaceSelection(msg + "\n");
	}
	// �α�â�� ����ϴ� �޼ҵ�
	public void AppendLogText(String msg) {
		// textArea.append(msg + "\n");
		// AppendIcon(icon1);
		msg = msg.trim(); // �յ� blank�� \n�� �����Ѵ�.
		int len = logList.getDocument().getLength();
		// ������ �̵�
		logList.setCaretPosition(len);
		logList.replaceSelection(msg + "\n");
	}
	// p1, p2, observers ��� refresh �ϴ� �޼ҵ�
	public void redrawUser(String data) {
		data = data.trim();
		String[] users = data.split("/");
		
		if(users[0].equals("*")) { p1Label.setText("Player 1 :"); }
		else {
			if(users[0].equals(userName)) p1Label.setText("Player 1 : " + users[0] + " (��)");
			else p1Label.setText("Player 1 : " + users[0]);
		}
		if(users[1].equals("*")) { p2Label.setText("Player 2 :"); }
		else {
			if(users[1].equals(userName)) p2Label.setText("Player 2 : " + users[1] + " (��)");
			else p2Label.setText("Player 2 : " + users[1]);
		}
		
		observerList.setText("");
		for(int i = 2; i < users.length; i++) {
			int len = observerList.getDocument().getLength();
			// ������ �̵�
			observerList.setCaretPosition(len);
			if(users[i].equals(userName)) observerList.replaceSelection(users[i] + " (��)" + "\n");
			else observerList.replaceSelection(users[i] + "\n");
		}
		
		p1Label.repaint();
		p2Label.repaint();
		observerList.repaint();
	}
	// ��ü(board ����) refresh �ϴ� �޼ҵ�
	public void redrawBoard(GameSignal gs) {
		redrawUser(gs.data);
		for(int i = 0; i < 10; i++) { // p1 pane refresh
			for(int j = 0; j < 10; j++) {
				p1BoardCells[i][j] = gs.p1Data[i][j];
				p2BoardCells[i][j] = gs.p2Data[i][j];
			}
		}
		p1Pane.repaint();
		p2Pane.repaint();
	}
}
