import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class SciDama extends MouseAdapter implements ActionListener {
	
	public static String[] turnImage = {"images/stop.png", "images/go.png"};
	
	private static JButton[] menuButton = new JButton[6];
	public static JLabel[] noOfChips = new JLabel[2];
	private static JLabel[] curtain = new JLabel[2];
	private static JLabel[][] players = new JLabel[2][2];
	public static JLabel[] turn = new JLabel[2];
	public static JLabel[] bar = new JLabel[2];
	private static JFrame frame = new JFrame();
	public static JLabel gameTitle, rope, about;	
	public static JLabel sandglass;
	private static JButton backToMenu;
	
	private static final Insets insets = frame.getInsets();
	private static Settings settings;
	private static SciDamaFrame board;
	private SoundThread sound;
	
	public static boolean hasSound = true, isAbout;
	public static int playMode;
	
	public SciDama() {
		sound = new SoundThread("play.wav", true);
		mainMenu();
	}
	
	public static Frame getFrame() {
		return frame;
	}
	
	public static void addComponent(Component c) {
		frame.add(c);
		resetFrame();
	}
	
	public void mainMenu() {
		
		gameTitle = new JLabel(new ImageIcon("images/gameLogo.jpg"));
		gameTitle.setBounds(insets.left + 300, insets.top + 80, 800, 226);
		
		String[] butIcons = {"images/1P.jpg", "images/2P.jpg", "images/quit.jpg", "images/sound.jpg", "images/settings.jpg", "images/about.png"};
		int pointY = 350;
		
		settings = new Settings();
		settings.enable(false);
		
		for(int x = 0; x < 6; x++) {
			menuButton[x] = new JButton(new ImageIcon(butIcons[x]));
			menuButton[x].addActionListener(this);
			if(x < 2) {
				menuButton[x].setBounds(insets.left + 570, insets.top + pointY, 250, 140);
				pointY += 200;
			}else if(x == 5){
				menuButton[5].setBounds(insets.left + 50, insets.top + 650, 100, 82);
			}else{
				menuButton[x].setBounds(insets.left + 1250, insets.top + pointY - 300, 100, 82);
				pointY += 100;
			}
			frame.add(menuButton[x]);
		}
		
		about = new JLabel(new ImageIcon("images/help.png"));
		about.setBounds(insets.left + 400, insets.top + 200, 598, 382);
		about.setVisible(false);
		frame.add(about);
		frame.add(gameTitle);
		
		backToMenu = new JButton(new ImageIcon("images/backToMenu.jpg"));
		backToMenu.addActionListener(this);
		backToMenu.setBounds(insets.left + 1220, insets.top + 670, 100, 80);
		backToMenu.setVisible(false);
		frame.add(backToMenu);
		
		for(int a = 0; a < 2; a++) {
			curtain[a] = new JLabel(new ImageIcon(a == 0 ? "images/curtain1.jpg" : "images/curtain2.jpg"));
			curtain[a].setBounds(insets.left + (a == 0 ? 315 : 690), insets.top, 380, 768);
			frame.add(curtain[a]);	
		}
		
		sandglass = new JLabel(new ImageIcon("images/1.jpg"));
		sandglass.setBounds(insets.left + 1085, insets.top + 300, 130, 190);
		sandglass.setVisible(false);
		frame.add(sandglass);
		
		rope = new JLabel(new ImageIcon("images/rope.jpg"));
		rope.setBounds(insets.left, insets.top + 10, 1366, 15);
		frame.add(rope);
		frame.repaint();
		
	}
	
	private void play(int playMode) {
		
		SciDama.playMode = playMode;
		enableButtons(false);
		
		int y = 1;
		String[][] p = { {"CPU", "YOU"}, {"P2", "P1"} };
		
		for(int x = 0; x < 2; x++) {
			int ctr = 90;
			turn[x] = new JLabel(new ImageIcon(turnImage[Settings.AIfirst && playMode == 1 ? y-- : x]));
			turn[x].setBounds(insets.left + 1120, insets.top + (x == 0 ? 220 : frame.getHeight() - 130), 65, 65);
			
			noOfChips[x] = new JLabel("12", SwingConstants.CENTER);
			noOfChips[x].setFont(new Font("Impact", Font.PLAIN, 50));
			noOfChips[x].setBounds(insets.left + 1085, insets.top + (x == 0 ? 155 : frame.getHeight() - 195), 130, 40);
			for(int z = 0; z < 2; z++) {
				players[x][z] = new JLabel(p[x][z], SwingConstants.CENTER);
				players[x][z].setBounds(insets.left + 1100, insets.top + ctr, 100, 50);
				players[x][z].setFont(new Font("Impact", Font.PLAIN, 40));
				players[x][z].setBackground(new Color(0,0,0,10));
				players[x][z].setForeground(Color.WHITE);
				players[x][z].setOpaque(true);
				ctr += 420;
			}
		}
		
		board = new SciDamaFrame(playMode == 1 ? true : false);
		openCurtain();
		
		for(int a = 0; a < 2; a++) {
			bar[a] = new JLabel(new ImageIcon("images/bar.jpg"));
			bar[a].setBounds(insets.left + 1050, insets.top + (a == 0 ? 145 : frame.getHeight() - 205), 200, 60);
		}
		
		board.setBounds(insets.left + (frame.getWidth()/4) - 50, insets.top + 50, 750, frame.getHeight() - 90);
		resetFrame();
		
	}
	
	private void openCurtain() {
		
		Thread t1 = new Thread() {
			public void run() {
				try{
					int x2 = insets.left + 315;
					gameTitle.setVisible(false);
					for(int x1 = insets.left + 690; x1 < frame.getWidth()-100; x1 += 10) {
						if(x1 == 710) {
							frame.add(board);
							resetFrame();
						}
						if(x1 == 830) {
							for(int y = 0; y < 2; y++) {
								frame.add(players[playMode-1][y]);
								frame.add(noOfChips[y]);
								frame.add(bar[y]);
								frame.add(turn[y]);
							}
						}
						x2 -= 10;
						curtain[0].setBounds(x2,insets.top,380,frame.getHeight());
						curtain[1].setBounds(x1,insets.top,380,frame.getHeight());
						Thread.sleep(5);
					}
					backToMenu.setVisible(true);
					resetFrame();
					board.startGame();
				}catch(InterruptedException iex){}
			}
		};
		
		t1.start();
		
	}
	
	public static void closeCurtain() {
		
		Thread t2 = new Thread() {
			public void run() {
				try{
					int x2 = -275;
					backToMenu.setVisible(false);
					
					for(int x1 = 1260; x2 < 310; x1 -= 10) {
						if(x1 == 1070) {
							sandglass.setVisible(false);
							for(int ctr = 0; ctr < 2; ctr++) {
								frame.remove(players[playMode-1][ctr]);
								frame.remove(noOfChips[ctr]);
								frame.remove(turn[ctr]);
								frame.remove(bar[ctr]);
							}
						}
						if(x1 == 710) {
							frame.remove(board);
							frame.validate();
						}
						curtain[0].setBounds(x2,insets.top,380,frame.getHeight());
						curtain[1].setBounds(x1,insets.top,380,frame.getHeight());
						x2 += 10;
						Thread.sleep(5);
					}
					resetFrame();
					gameTitle.setVisible(true);
					enableButtons(true);
				}catch(InterruptedException iex){}
			}
		};
		
		t2.start();
		
	}
	
	public static void resetFrame() {
				
		frame.repaint();
		frame.validate();
		
	}
	
	public static void enableButtons(boolean b) {
		for(int x = 0; x < menuButton.length; x++) {
			menuButton[x].setVisible(b);
		}
	}
	
	public void actionPerformed(ActionEvent a) {
		
		if(a.getSource() == menuButton[0]) play(1);
		else if(a.getSource() == menuButton[1]) play(2);
		else if(a.getSource() == menuButton[2]) System.exit(0);
		else if(a.getSource() == menuButton[3]) {
			if(hasSound) {
				sound.mute();
				menuButton[3].setIcon(new ImageIcon("images/mute.jpg"));
				hasSound = false;
			}else{
				sound = new SoundThread("play.wav", true);
				menuButton[3].setIcon(new ImageIcon("images/sound.jpg"));
				hasSound = true;
			}
		}
		else if(a.getSource() == menuButton[4])	{
			
			settings.enable(true);
			gameTitle.setVisible(false);
			enableButtons(false);
			
		}else if(a.getSource() == menuButton[5]) {
			
			if(!isAbout) {
				isAbout = true;
				gameTitle.setVisible(false);
				enableButtons(false);
				about.setVisible(true);
				menuButton[5].setVisible(true);
			}else{
				isAbout = false;
				gameTitle.setVisible(true);
				enableButtons(true);
				about.setVisible(false);
			}
			
		}else if(a.getSource() == backToMenu) {
			
			Object[] options = {"RESUME", "END GAME"};
			int choice = JOptionPane.showOptionDialog(null, "    Abandon Game?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
												      null, options, options[0]);
			if(choice == 1)
				closeCurtain();
		}
		
	}
	
	public static void main(String[] args) {
		
		frame.setLayout(null);
		frame.setContentPane(new JLabel(new ImageIcon("images/menu.jpg")));
		frame.setUndecorated(true);
		//frame.setSize(1024, 768);
        frame.setVisible(true);
		new SciDama();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
    }
	
}