import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class Settings extends MouseAdapter implements ActionListener {
	
	private String[][] chipImages = {{"images/c1_black.png", "images/c1_white.png", "images/c1_blackDama.png", "images/c1_whiteDama.png"}, 
									 {"images/c2_black.png", "images/c2_white.png", "images/c2_blackDama.png", "images/c2_whiteDama.png"}, 
									 {"images/c3_black.png", "images/c3_white.png", "images/c3_blackDama.png", "images/c3_whiteDama.png"}};
								 
	private String[] cstyle = {"images/cstyle1.jpg", "images/cstyle2.jpg", "images/cstyle3.jpg"};
	private String[] css = {"images/cs1.jpg", "images/cs2.jpg", "images/cs3.jpg"};	
	private String[] onOff = {"images/on.jpg", "images/off.jpg"};
	private static JLabel[] chipStyles = new JLabel[3];
	public static String[] cStyle = new String[4];
	private JButton backToMenu, firstTurn;
	public static JLabel bStyle;
	private JLabel styleArea;
	private Insets insets;
	
	private static boolean[] styleSelected = new boolean[3];
	public static boolean AIfirst = false;
	
	public Settings() {
		setComponents();
	}
	
	private void setComponents() {
		
		insets = SciDama.getFrame().getInsets();
		
		backToMenu = new JButton(new ImageIcon("images/back.jpg"));
		backToMenu.setBounds(insets.left + 1000, insets.top + 30, 50, 50);
		backToMenu.addActionListener(this);
		SciDama.addComponent(backToMenu);
		
		firstTurn = new JButton(new ImageIcon(onOff[1]));
		firstTurn.setBounds(insets.left + 500, insets.top + 500, 150, 84);
		firstTurn.addActionListener(this);
		SciDama.addComponent(firstTurn);
		
		styleArea = new JLabel(new ImageIcon("images/shiny.jpg"));
		styleArea.setBounds(insets.left + 400, insets.top + 300, 600, 200);
		styleArea.setLayout(new FlowLayout());
		styleArea.setBorder(SciDamaBoard.themeBorder());
		SciDama.addComponent(styleArea);
		
		for(int x = 0; x < 3; x++) {
			chipStyles[x] = new JLabel(new ImageIcon(cstyle[x]));
			
			chipStyles[x].setPreferredSize(new Dimension(185,165));
			chipStyles[x].addMouseListener(this);
			styleArea.add(chipStyles[x]);
		}
		
		chipStyles[0].setIcon(new ImageIcon(css[0]));
		cStyle = chipImages[0];
		styleSelected[0] = true;
		
	}
	
	public void enable(boolean b) {
		
		backToMenu.setVisible(b);
		firstTurn.setVisible(b);
		styleArea.setVisible(b);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == firstTurn) {
			if(!AIfirst) {
				firstTurn.setIcon(new ImageIcon(onOff[0]));
				AIfirst = true;
			}else{
				firstTurn.setIcon(new ImageIcon(onOff[1]));
				AIfirst = false;
			}
		}else if(e.getSource() == backToMenu) {
			enable(false);
			SciDama.enableButtons(true);
			SciDama.gameTitle.setVisible(true);
		}
		
	}
	
	public void mouseEntered(MouseEvent m) {
		
		String[] chover = {"images/chover1.jpg", "images/chover2.jpg", "images/chover3.jpg"};
		for(int x = 0; x < chipStyles.length; x++) {
			if(m.getSource() == chipStyles[x] && !styleSelected[x]) {
				chipStyles[x].setIcon(new ImageIcon(chover[x]));
			}
		}
		
	}
	
	public void mouseExited(MouseEvent m) {
		
		for(int x = 0; x < 3; x++) {
			if(m.getSource() == chipStyles[x] && !styleSelected[x]) {
				chipStyles[x].setIcon(new ImageIcon(cstyle[x]));
			}
		}
		
	}
	
	public void mouseClicked(MouseEvent m) {
	
		for(int x = 0; x < chipStyles.length; x++) {
			if(m.getSource() == chipStyles[x]) {
				chipStyles[x].setIcon(new ImageIcon(css[x]));
				cStyle = chipImages[x];
				styleSelected[x] = true;
			}
		}
		
		for(int y = 0; y < 3; y++) {
			if(m.getSource() != chipStyles[y]) {
				chipStyles[y].setIcon(new ImageIcon(cstyle[y]));
				styleSelected[y] = false;
			}
		}
		
	}
	
}