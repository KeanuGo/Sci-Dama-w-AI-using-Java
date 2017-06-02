import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")

public class SciDamaBoard extends JPanel {
	
	public JPanel[][] tiles = new JPanel[8][8];
	public SciDamaChips[] ch = new SciDamaChips[2];
	public boolean[][] possMoves = new boolean[8][8];
	public boolean[][] possEat = new boolean[8][8];
	public boolean[][] isChip = new boolean[8][8];
	public static int[] counter;
	public boolean finish;
	
	public SciDamaBoard() {
		
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.put("OptionPane.messageFont", new Font("Impact", Font.PLAIN, 20));
		}catch(Exception e){}	
		
		counter = new int[] {0,0};
		setLayout(new GridLayout(8,8));
		ch[0] = new SciDamaChips(1);
		ch[1] = new SciDamaChips(2);

		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				tiles[i][j] = new JPanel();

				if(i % 2 == 0) {
					if(j % 2 == 0)
						tiles[i][j].setBackground(Color.WHITE);
					else{
						tiles[i][j].setBackground(Color.BLACK);
						tiles[i][j].add(new JLabel(new ImageIcon("images/black_tile1.jpg")));
					}
				} else if(i % 2 == 1) {
					if(j % 2 == 1)
						tiles[i][j].setBackground(Color.WHITE);
					else{
						tiles[i][j].setBackground(Color.BLACK);
						tiles[i][j].add(new JLabel(new ImageIcon("images/black_tile1.jpg")));
					} 
				}
				add(tiles[i][j]);
			}
		}
		
	}
	
	public SciDamaBoard(SciDamaBoard board) { 
	
		this();
		for(int i = 0; i < 8; i++){
			for(int j = 0 ; j < 8; j++){
				this.isChip[i][j] = board.isChip[i][j];
				this.possMoves[i][j] = board.possMoves[i][j];
				this.possEat[i][j] = board.possEat[i][j];
				if(board.isChip[i][j]){
					Chip bufferChip = new Chip(((Chip)(board.tiles[i][j].getComponent(0))).player);
					this.tiles[i][j].add(bufferChip);
				}
			}
		}
		
	}
	
	public static Border themeBorder() {
		
		Border line1, line2, compound;
		line1 = BorderFactory.createLineBorder(Color.YELLOW, 5, true);
		line2 = BorderFactory.createLineBorder(Color.BLACK, 7, true);
		compound = BorderFactory.createCompoundBorder(line1, line2);
		return compound;
		
	}	
	
	public SciDamaBoard chipMove(int ifrom, int jfrom, int ito, int jto, int player) {
		
		isChip[ifrom][jfrom] = false;
		isChip[ito][jto] = true;
		tiles[ifrom][jfrom].setBorder(null);
		Chip c = (Chip)tiles[ifrom][jfrom].getComponent(0);

		tiles[ifrom][jfrom].remove(c);
		tiles[ifrom][jfrom].repaint();
		tiles[ifrom][jfrom].validate();
		
		if((ito == 0 && player == 2) || (ito == 7 && player == 1)){
			c.isDama = true;
			c.setIcon(c.damaIcon);
		}
		tiles[ito][jto].add(c);
		tiles[ito][jto].repaint();
		tiles[ito][jto].validate();
		
		return this;
		
	}
	
	public void countAIChips() {
		
		finish = false;
		int[] AIcounter = {0,0};
		
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				if(isChip[x][y]) {
					Chip c = (Chip)tiles[x][y].getComponent(0);
					if(c.player == 1) {
						AIcounter[0]++;
					}else if(c.player == 2){
						AIcounter[1]++;
					}
				}
			}
		}
		
		if(AIcounter[0] == 0 || AIcounter[1] == 0) {
			finish = true;
		}
		
	}
	
	public void countChips(int player) {
		
		counter[0] = 0;
		counter[1] = 0;
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				if(isChip[x][y]) {
					Chip c = (Chip)tiles[x][y].getComponent(0);
					if(c.player == 1) {
						counter[0]++;
					}else if(c.player == 2){
						counter[1]++;
					}
				}
			}
		}
		
		for(int z = 0; z < 2; z++) {
			SciDama.noOfChips[z].setText(String.valueOf(counter[z]));
		}		
		
		if(counter[0] == 0 || counter[1] == 0) {
			ImageIcon trophy = new ImageIcon("images/trophy.png");
			String string = "";
			
			if(SciDamaFrame.isAI) {
				string = (player == 1 ? "YOU WIN" : "CPU WINS");
			}else{
				string = (player == 1 ? "PLAYER 1 WINS" : "PLAYER 2 WINS");
			}
			JOptionPane.showMessageDialog(null, string, "", JOptionPane.INFORMATION_MESSAGE, trophy);
			
			Object[] options = {"PLAY AGAIN", "END GAME"};
			int choice = JOptionPane.showOptionDialog(null, "        Play Again?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
												      null, options, options[0]);
			
			if(choice == 0) {
				
			}else if(choice == 1) {
				
			}
			
			SciDama.turn[0].setIcon(new ImageIcon(SciDama.turnImage[0]));
			SciDama.turn[1].setIcon(new ImageIcon(SciDama.turnImage[0]));
		}
		
	}
	
	public SciDamaBoard chipEat(int ifrom, int jfrom, int ito, int jto, int player, boolean left, boolean right) {
		
		Chip c = (Chip) tiles[ifrom][jfrom].getComponent(0);
		int dei = ito, dej = jto;
		if(c.ur || c.ul) {
			dei += 1;
		}
		if(c.dr || c.dl) {
			dei -= 1;
		}
		if((c.dl && c.dr)||(c.ul && c.ur)){
			if(left){
				dej += 1;
			}else if(right){
				dej -= 1;
			}
		}else{
		if(c.ul || 	c.dl) {
			dej += 1;
		}if(c.ur || c.dr) {
			dej -= 1;
		}
		}
		isChip[ifrom][jfrom] = false;
		isChip[ito][jto] = true;

		Chip c1;
		
		if(c.isDama)
			c1 = (Chip) tiles[dei][dej].getComponent(0);
		else 
			c1 = (Chip) tiles[dei = (ifrom + ito) / 2][dej = (jfrom + jto) / 2].getComponent(0);
		
		isChip[dei][dej] = false;
		tiles[ifrom][jfrom].remove(c);
		tiles[ifrom][jfrom].validate();
		tiles[ifrom][jfrom].repaint();
		
		tiles[dei][dej].remove(c1);
		tiles[dei][dej].validate();
		tiles[dei][dej].repaint();

		tiles[ito][jto].add(c);
		tiles[ito][jto].validate();
		tiles[ito][jto].repaint();

		return this;
		
	}

	public void reset() {
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				isChip[i][j] = false;
			}
		}
		
		int ind;
		for(int p = 0; p < 2; p++) {
			ind = 0;
			for(int i = 0 + p * 5; i < (p*5) + 3; i++) {
				for(int j = 0; j < 4; j++) {
					isChip[i][(i % 2) + (j)*2] = true;
					tiles[i][(i % 2) + (j) * 2].add(ch[p].chip[ind]);
					ind++;
				}
			}
		}
		
	}
	
	public int evaluate() {
		
		int rateP = 0, rateAI = 0;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(isChip[i][j]) {
					if(isChip[i][j]) {
						Chip c = (Chip)tiles[i][j].getComponent(0);
						if(c.player == 2) {
							rateP++;
							if(c.isDama)
								rateAI -= 3;
							if(i == 5 && j == 7)
								rateAI -= 3;
						}else{
							rateAI++;
							if(c.isDama)
								rateP -= 3;
							if(i == 2 && j == 0)
								rateP -= 3;
						}
					}
				}
			}
		}
		
		return rateP - rateAI;
		
	}
	
	public void addListener(MouseAdapter handler) {
		
		this.addMouseListener(handler);
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				tiles[i][j].addMouseListener(handler);
			}
		}
		
	}

}