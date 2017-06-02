import javax.swing.*;

public class AI {
	
	private boolean evaluating;
	
	public int[] move(SciDamaBoard board) {
		
		evaluating = true;
		Thread animation = new Thread() {
			public void run() {
				String[] sglass = {"images/1.jpg", "images/2.jpg", "images/3.jpg", "images/4.jpg", "images/5.jpg"};
				
				SciDama.sandglass.setVisible(true);
				while(evaluating) {
					try{
						Thread.sleep(1);
						for(int x = 0; x < 5; x++) {
							SciDama.sandglass.setIcon(new ImageIcon(sglass[x]));
							Thread.sleep(500);
						}
					}catch(InterruptedException iex){}
				}
			}
		};
		
		animation.start();
		
		int[] result = minimax(board, true, 5, Integer.MIN_VALUE, Integer.MAX_VALUE);
		SciDama.sandglass.setVisible(false);
		new SoundThread("move.wav", false);
		evaluating = false;
		
		return new int[] {result[1], result[2], result[3], result[4]}; 
		
	}
	
	public int[] minimax(SciDamaBoard board, boolean myTurn, int depth, int alpha, int beta) {
		
		SciDamaBoard b = new SciDamaBoard(board);
		
		int best = (myTurn) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		int ifromBest = -1, jfromBest = -1, iBest = -1, jBest = -1;
		int[] xfrom = new int[200], yfrom = new int[200];
		int[] x = new int[200], y = new int[200];
		boolean ea[] = new boolean[200];
		boolean fond = false;
		int player = 0;
		int index = 0;
		
		b.countAIChips();
		player = myTurn ? 1 : 2;
		
		if(depth == 0 || b.finish)
			return new int[] {best = b.evaluate(), ifromBest, jfromBest, iBest, jBest};	
		
		if(SciDamaFrame.findEat(player, b)) {
			fond = true;
		}else
			fond = false;
		
		for(int k = 0; k < 8; k++) {
			for(int m = 0; m < 8; m++) {
				if(b.possEat[k][m]) {
					b.tiles[k][m].setBorder(null);
					b.possEat[k][m] = false;
				}
			}
		}
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(b.isChip[i][j]) {
					Chip chipSelected = (Chip)b.tiles[i][j].getComponent(0);
					
					if(chipSelected.canEat(b, i, j) && chipSelected.player == player) {
						SciDamaFrame.eatAll(chipSelected, i, j, b);
						
						for(int k = 0; k < 8; k++) {
							for(int m = 0; m < 8; m++) {
								if(b.possEat[k][m]) {
									b.tiles[k][m].setBorder(null);
									b.possEat[k][m]= false;
								}
							}
						}
						
						for(int k = 0; k < 8; k++) {
							for(int m = 0; m < 8; m++) {
								if(b.possMoves[k][m]) {
									ea[index] = true;
									xfrom[index] = i; yfrom[index] = j; x[index] = k; y[index] = m;
									b.possMoves[k][m] = false;
									index++;
								}
							}
						}
						
					}else if(chipSelected.canMove(b, i, j) && chipSelected.player == player && !SciDamaFrame.fondAI && !fond) {
						if(chipSelected.ddr) {
							int k = i, m = j;
							ea[index] = false;
							xfrom[index] = i; yfrom[index] = j;
							x[index] = k; y[index] = m;
							index++;
							k++;
							m++;
							
							while(!(k >= 8 || m >= 8)) {
								if(board.isChip[k][m]) {
									break;
								}
								ea[index] = false;
								xfrom[index] = i; yfrom[index] = j;
								x[index] = k; y[index] = m;
								index++;
								k++;
								m++;
							}
						}
						
						if(chipSelected.ddl) {
							int k = i, m = j;
							ea[index] = false;
							xfrom[index] = i; yfrom[index] = j;
							x[index] = k; y[index] = m;
							index++;
							k++;
							m--;
							
							while(!(k >= 8 || m < 0)) {
								if(board.isChip[k][m]) {
									break;
								}
								xfrom[index] = i; yfrom[index] = j;
								x[index] = k; y[index] = m;
								index++;
								k++;
								m--;
							}
						}
						
						if(chipSelected.dur) {
							int k = i, m = j;
							ea[index] = false;
							xfrom[index] = i; yfrom[index] = j;
							x[index] = k; y[index] = m;
							index++;
							k--;
							m++;
							
							while(!(k < 0 || m >= 8)) {
								if(board.isChip[k][m]) {
									break;
								}
								ea[index] = false;
								xfrom[index] = i; yfrom[index] = j;
								x[index] = k; y[index] = m;
								index++;
								k--;
								m++;
							}
						}
						
						if(chipSelected.dul) {
							int k = i, m = j;
							ea[index] = false;
							xfrom[index] = i; yfrom[index] = j;
							x[index] = k; y[index] = m;
							index++;
							k--;
							m--;
							
							while(!(k < 0 || m < 0)) {
								if(board.isChip[k][m]) {
									break;
								}
								ea[index] = false;
								xfrom[index] = i; yfrom[index] = j;
								x[index] = k; y[index] = m;
								index++;
								k--;
								m--;
							}
						}
						
						if(chipSelected.left) {
							ea[index] = false;
							xfrom[index] = i; yfrom[index] = j; x[index] = i + (player == 2 ? -1 : 1); y[index] = j - 1;
							index++;
						}
						
						if(chipSelected.right) {
							ea[index] = false;
							xfrom[index] = i; yfrom[index] = j; x[index] = i + (player == 2 ? -1 : 1); y[index] = j + 1;
							index++;
						}
						
					}
				}
			}
		}
		
		for(int i = 0; i < index; i++) {
			if(myTurn) {
				best = minimax(ea[i] ? new SciDamaBoard(b).chipEat(xfrom[i], yfrom[i], x[i], y[i], player, true, false) : new SciDamaBoard(b).chipMove(xfrom[i], yfrom[i], x[i], y[i], player), !myTurn, depth-1, alpha, beta)[0];
				if(best > alpha) {
					alpha = best;
					ifromBest = xfrom[i];
					jfromBest = yfrom[i];
					iBest = x[i];
					jBest = y[i];
				}
			}else{
				best = minimax(ea[i] ? new SciDamaBoard(b).chipEat(xfrom[i], yfrom[i], x[i], y[i], player, true, false) : new SciDamaBoard(b).chipMove(xfrom[i], yfrom[i], x[i], y[i], player), !myTurn, depth-1, alpha, beta)[0];
				if(best < beta) {
					beta = best;
					ifromBest = xfrom[i];
					jfromBest = yfrom[i];
					iBest = x[i];
					jBest = y[i];
				}
			}
			if (alpha >= beta) break;
		}
		
		return new int[] {(myTurn) ? alpha : beta, ifromBest, jfromBest, iBest, jBest};
		
	}
	
}