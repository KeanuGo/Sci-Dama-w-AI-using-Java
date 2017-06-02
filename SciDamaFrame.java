import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")

public class SciDamaFrame extends JPanel {
	
	public static SciDamaBoard board;
	private Thread t1;
	private AI ai;
	
	public int player = 2;
	boolean left = false, right = false;
	public static int[] co = new int[2];
	public static int ifrom, jfrom;
	public static boolean selected;
	public static boolean fondAI;
	public static boolean isAI;
	public static boolean fond;
	public static boolean a;
	
	public SciDamaFrame(boolean isAI) {
		
		if(isAI) ai = new AI();
		SciDamaFrame.isAI = isAI;
		board = new SciDamaBoard();
		setLayout(new BorderLayout());
		reset();
		board.reset();
		setBorder(SciDamaBoard.themeBorder());
		add(board, BorderLayout.CENTER);
		
	}
	
	public boolean hasMoves(int player, SciDamaBoard board) {
		
		boolean hasMove = false;
		
		if(findEat(player, board)) {
			for(int k = 0; k < 8; k++){
				for(int m = 0; m < 8; m++){
					if(board.possEat[k][m]){
						board.tiles[k][m].setBorder(null);
						board.possEat[k][m]= false;
					}
				}
			}
			return true;
		}else{			
			for(int x = 0; x < 8; x++) {
				for(int y = 0; y < 8; y++) {
					if(board.isChip[x][y]) {
						Chip c = (Chip) board.tiles[x][y].getComponent(0);
						if(c.canMove(board, x, y) &&  c.player == player) {
							hasMove = true;			
							break;
						}
					}
				}
			}
			return hasMove ? true : false;
		}
		
	}
	
	public void startGame() {
		
		if(isAI && Settings.AIfirst) {
			int[] result = ai.move(board);
			board.chipMove(result[0], result[1], result[2], result[3], player=1);
			reset();
			player = player % 2 + 1;
		}
		board.addListener(new MouseHandler());
		
	}
	
	private class MouseHandler extends MouseAdapter {
		public void mouseClicked(MouseEvent event) {
			gameBoard(event);
		}
	}
	
	private void changeTurn() {
		
		board.countChips(player);
		player = player % 2 + 1;
		
		if(!hasMoves(this.player, board) && (SciDamaBoard.counter[0] != 0 || SciDamaBoard.counter[1] != 0)) {
			String playerString = "";
			if(isAI) {
				playerString = player == 1 ? "CPU" : "You";
			}else{
				playerString = player == 1 ? "P2" : "P1";
			}
			JOptionPane.showMessageDialog(null, playerString + " : NO MOVES", "", JOptionPane.WARNING_MESSAGE);
		}
		
		reset();
		JLabel temp = new JLabel(SciDama.turn[0].getIcon());
		SciDama.turn[0].setIcon(SciDama.turn[1].getIcon());
		SciDama.turn[1].setIcon(temp.getIcon());
		
	}
	
	private static LineBorder setSelectedTileBorder() {
		return new LineBorder(Color.BLUE, 5, true);
	}
	
	public void gameBoard(MouseEvent event) {
		
		new SoundThread("move.wav", false);
		
		t1 = new Thread() {
			public void run() {
				try{
					for(int i = 0; i < 8; i++ ){
						for(int j = 0; j < 8; j++) {
							if(event.getSource() == board.tiles[i][j]) { 
								if(player == 1 && isAI) {
									selected = true;
								}
								if(!selected) {
									if(board.isChip[i][j]) {
										Chip chipSelected = (Chip)board.tiles[i][j].getComponent(0);
										
										if(chipSelected.player == player){
											if(fond){
												eatAll(chipSelected, i, j, board);
											}else if(chipSelected.canMove(board, i, j)){
												if(chipSelected.ddr){
													int k= i, m= j;
													board.tiles[i][j].setBorder(setSelectedTileBorder());
													ifrom= i; jfrom= j;
													k++;
													m++;
													while(!(k >= 8 || m >= 8)){
														if(board.isChip[k][m]){
															break;
														}
														board.tiles[k][m].setBorder(setSelectedTileBorder());
														board.possMoves[k][m]= true;
														k++;
														m++;
													}
													selected= true;
												}
												if(chipSelected.ddl){
													int k= i, m= j;
													board.tiles[i][j].setBorder(setSelectedTileBorder());
													ifrom= i; jfrom= j;
													k++;
													m--;
													while(!(k >= 8 || m < 0)){
														if(board.isChip[k][m]){
															break;
														}
														board.tiles[k][m].setBorder(setSelectedTileBorder());
														board.possMoves[k][m]= true;
														k++;
														m--;
													}
													selected= true;
												}
												if(chipSelected.dur){
													int k= i, m= j;
													board.tiles[i][j].setBorder(setSelectedTileBorder());
													ifrom= i; jfrom= j;
													k--;
													m++;
													while(!(k < 0 || m >= 8)){
														if(board.isChip[k][m]){
															break;
														}
														board.tiles[k][m].setBorder(setSelectedTileBorder());
														board.possMoves[k][m]= true;
														k--;
														m++;
													}
													selected= true;
												}
												if(chipSelected.dul){
													int k= i, m= j;
													board.tiles[i][j].setBorder(setSelectedTileBorder());
													ifrom= i; jfrom= j;
													k--;
													m--;
													while(!(k < 0 || m < 0)){
														if(board.isChip[k][m]){
															break;
														}
														board.tiles[k][m].setBorder(setSelectedTileBorder());
														board.possMoves[k][m]= true;
														k--;
														m--;
													}
													selected= true;
												}
												if(chipSelected.left){
													board.tiles[i][j].setBorder(setSelectedTileBorder());
													board.tiles[i+(player==2?-1:1)][j-1].setBorder(setSelectedTileBorder());
													board.possMoves[i+(player==2?-1:1)][j-1]= true;
													ifrom= i; jfrom= j;
													selected= true;
												}
												if(chipSelected.right){
													board.tiles[i][j].setBorder(setSelectedTileBorder());
													board.tiles[i+(player==2?-1:1)][j+1].setBorder(setSelectedTileBorder());
													board.possMoves[i+(player==2?-1:1)][j+1]= true;
													ifrom= i; jfrom= j;
													selected= true;
												}
											}
										}
									}
								}else if(selected){
									for(int k = 0; k < 8; k++){
										for(int m = 0; m < 8; m++){
											if(board.possEat[k][m]){
												board.tiles[k][m].setBorder(null);
												board.possEat[k][m]= false;
											}
										}
									}
									if(fond) {
										if(board.possMoves[i][j]) {
											left= false; right= false;
											if(co[0]==j){
												left= true;
											}
											if(co[1]==j){
												right= true;
											}
											board.chipEat(ifrom, jfrom, i, j, player, left, right);
											Chip chipE = (Chip)board.tiles[i][j].getComponent(0);
											fond = false;
											if(chipE.canEat(board, i, j)) {  
												board.tiles[i][j].setBorder(setSelectedTileBorder());	
												board.possEat[i][j] = true;
												fond = true;
												eatAll(chipE, i, j, board);
											}else{
												if((i == 0 && player == 2) || (i == 7 && player == 1))  {
													chipE.isDama = true;
													chipE.setIcon(chipE.damaIcon);
												}
												changeTurn();
												if(player == 1 && isAI) {
													fondAI = false;
													a = false;
													while(findEat(player, board) && !a) {
														fondAI = true;
														boolean p = false;
														for(int k = 0; k < 8 && !p; k++) {
															for(int m = 0; m < 8; m++) {
																if(board.possEat[k][m]) {
																	Chip chipSelected = (Chip)board.tiles[k][m].getComponent(0);
																	eatAll(chipSelected, k, m, board);
																	ifrom = k; jfrom = m;
																	p = true;
																	break;
																}
															}
														}
														for(int k = 0; k < 8; k++){
															for(int m = 0; m < 8; m++){
																if(board.possEat[k][m]){
																	board.tiles[k][m].setBorder(null);
																	board.possEat[k][m]= false;
																}
															}
														}
														p = false;
														for(int k = 0; k < 8 && !p; k++){
															for(int m = 0; m < 8; m++){
																if(board.possMoves[k][m]){
																	left= false; right= false;
																	Thread.sleep(500);
																	if(co[0]==k){
																		left= true;
																	}
																	if(co[1]==m){
																		right= true;
																	}
																	board.chipEat(ifrom, jfrom, k, m, player, left, right);
																	new SoundThread("move.wav", false);
																	Chip chipSelected = (Chip)board.tiles[k][m].getComponent(0);
																	
																	board.possMoves[k][m]= false;
																	
																	if(chipSelected.canEat(board, k, m)){
																		a= false;
																		eatAll(chipSelected, k, m, board);
																	}else{
																		a= true;
																		if((k==0 && player==2) || (k==7 && player==1)){
																			chipSelected.isDama= true;
																			chipSelected.setIcon(chipSelected.damaIcon);
																		}
																	}
																	reset();
																	p= true;
																	break;
																}
															}
														}
													}
													if(!fondAI && isAI){
														int[] result = ai.move(board);
														board.chipMove(result[0], result[1], result[2], result[3], player);
													}
													changeTurn();
												}
											}
										}else {
											reset();
											
										}
									}
									else if(board.possMoves[i][j]){
										board.chipMove(ifrom, jfrom, i, j, player);
										changeTurn();
										
										if(player==1 && isAI){
											fondAI= false;
											a= false;
											while(findEat(player, board) && !a){
												fondAI= true;
												boolean p= false;
												for(int k = 0; k < 8 && !p; k++){
													for(int m = 0; m < 8; m++){
														if(board.possEat[k][m]){
															Chip chipSelected = (Chip)board.tiles[k][m].getComponent(0);
															eatAll(chipSelected, k, m, board);
															ifrom=k; jfrom= m;
															p= true;
															break;
														}
													}
												}
												for(int k = 0; k < 8; k++){
													for(int m = 0; m < 8; m++){
														if(board.possEat[k][m]){
															board.tiles[k][m].setBorder(null);
															board.possEat[k][m]= false;
														}
													}
												}
												p= false;
												for(int k = 0; k < 8 && !p; k++){
													for(int m = 0; m < 8; m++){
														if(board.possMoves[k][m]){
															Thread.sleep(500);
															left= false; right= false;
															if(co[0]==k){
																left= true;
															}
															if(co[1]==m){
																right= true;
															}
															board.chipEat(ifrom, jfrom, k, m, player, left, right);
															new SoundThread("move.wav", false);
															board.possMoves[k][m]= false;
															Chip chipSelected = (Chip)board.tiles[k][m].getComponent(0);
															
															if(chipSelected.canEat(board, k, m)){
																a= false;
																eatAll(chipSelected, k, m, board);
															}else{
																a= true;
																if((k==0 && player==2) || (k==7 && player==1)){
																	chipSelected.isDama= true;
																	chipSelected.setIcon(chipSelected.damaIcon);
																}
															}
															reset();
															p= true;
															break;
														}
													}
												}
											}
											if(!fondAI && isAI){
												int[] result = ai.move(board);
												board.chipMove(result[0], result[1], result[2], result[3], player);
											}
										changeTurn();
										}
									}else{
										reset();
									}
									if(findEat(player, board)){
										fond= true;
									}else
										fond= false;
								}
							}
						}
					}
				}catch(InterruptedException iex){}
			}
		};
		
		t1.start();
		
	}
	
	
	public void reset() {
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				board.tiles[i][j].setBorder(null);
				board.possMoves[i][j] = false;
			}
		}
		selected = false;
		
	}
	
	public static void eatAll(Chip chipSelected, int i, int j, SciDamaBoard board) {	
	
		if(chipSelected.ul){
			ifrom= i; jfrom= j;
			if(chipSelected.isDama){
				i=chipSelected.rw[3]; j=chipSelected.col[3];
			}
			board.tiles[i-2][j-2].setBorder(setSelectedTileBorder());
			board.possMoves[i-2][j-2]= true;
			co[0]=j-2;
			selected= true;
		}
		if(chipSelected.ur){
			ifrom= i; jfrom= j;
			if(chipSelected.isDama){
				i=chipSelected.rw[2]; j=chipSelected.col[2];
			}
			board.tiles[i-2][j+2].setBorder(setSelectedTileBorder());
			board.possMoves[i-2][j+2]= true;
			co[1]=j+2;
			selected= true;
		}
		if(chipSelected.dl){
			ifrom= i; jfrom= j;
			if(chipSelected.isDama){
				i=chipSelected.rw[1]; j=chipSelected.col[1];
			}
			board.tiles[i+2][j-2].setBorder(setSelectedTileBorder());
			board.possMoves[i+2][j-2]= true;
			co[0]=j-2;
			selected= true;
		}
		if(chipSelected.dr){
			ifrom= i; jfrom= j;
			if(chipSelected.isDama){
				i=chipSelected.rw[0]; j=chipSelected.col[0];
			}
			board.tiles[i+2][j+2].setBorder(setSelectedTileBorder());
			board.possMoves[i+2][j+2]= true;
			co[1]=j+2;
			selected= true;
		}
		
	}
	
	public static boolean findEat(int player, SciDamaBoard board) {
		
		boolean found = false;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board.isChip[i][j]) {
					Chip chipSelected = (Chip)board.tiles[i][j].getComponent(0);
					if(chipSelected.canEat(board, i, j) && chipSelected.player == player) {
						board.tiles[i][j].setBorder(setSelectedTileBorder());	
						board.possEat[i][j] = true;
						found = true;
					}
				}
			}
		}
		
		return found;
		
	}
	
}