import javax.swing.*;

@SuppressWarnings("serial")

public class Chip extends JLabel {
	
	public Icon chipIcon, damaIcon;
	public boolean left, right;
	public boolean dul, dur;
	public boolean ddl, ddr;
	public boolean ul, ur;
	public boolean dl, dr;
	public boolean isDama;
	
	public int[] rw = new int[4], col = new int[4];
	public int player;
	
	public Chip(int player) {
		
		if(player == 1) {
			chipIcon = new ImageIcon(Settings.cStyle[0]);
			damaIcon = new ImageIcon(Settings.cStyle[2]);
		} else if(player == 2) {
			chipIcon = new ImageIcon(Settings.cStyle[1]);
			damaIcon = new ImageIcon(Settings.cStyle[3]);
		}
		
		this.player = player;
		this.setIcon(chipIcon);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
	}
	
	public boolean canMove(SciDamaBoard board, int i, int j) {
		
		ddr = false; ddl = false; dur = false; dul = false;
		right = false; left = false;
		boolean can = false;
		col = new int[4];
		rw = new int[4]; 
		
		if(isDama) {
			
			if(!(i+1 >= 8 || j+1 >= 8)) {
				if(!board.isChip[i+1][j+1]) {
					ddr = true;
					can = true;
				}
			}
			if(!(i+1 >= 8 || j-1 < 0)) {
				if(!board.isChip[i+1][j-1]) {
					ddl = true;
					can = true;
				}
			}
			if(!(i-1 < 0 || j+1 >= 8)) {
				if(!board.isChip[i-1][j+1]) {
					dur = true;
					can = true;
				}
			}
			if(!(i-1 < 0 || j-1 < 0)) {
				if(!board.isChip[i-1][j-1]) {
					dul = true;
					can = true;
				}
			}
			return can;
			
		}else{
			
			if(player == 1) {
				if(!(i+1 >= 8 || j+1 >= 8)) {
					if(!board.isChip[i+1][j+1]) {
						right = true;
						can = true;
					}
				}
				if(!(i+1 >= 8 || j-1 < 0)) {
					if(!board.isChip[i+1][j-1]) {
						left = true;
						can = true;
					}
				}
			}else if(player == 2) {
				if(!(i-1 < 0 || j+1 >= 8)) {
					if(!board.isChip[i-1][j+1]) {
						right = true;
						can = true;
					}
				}
				if(!(i-1 < 0 || j-1 < 0)) {
					if(!board.isChip[i-1][j-1]) {
						left = true;
						can = true;
					}
				}
			}
			
		return can;
		
		}
		
	}
	
	public boolean canEat(SciDamaBoard board, int i, int j) {
		
		Chip[] chip = new Chip[4];
		ur = false; ul = false; dr = false; dl = false;
		boolean can = false;
		int k = i, m = j;
		
		if(isDama) {
			while((!(k+2 >= 8 || m+2 >= 8 || k+1 >= 8 ||  m+1 >= 8 ))) {
				if(board.isChip[k+1][m+1]) {
					break;
				}
				k++; m++;
			}
			rw[0] = k; 
			col[0] = m;
			
			if(!(k+2 >= 8 || m+2 >= 8 || k+1 >= 8 ||  m+1 >= 8 )) {
				if(board.isChip[k+1][m+1]) {
					chip[0] = (Chip)board.tiles[k+1][m+1].getComponent(0);
					if(chip[0].player != player && !board.isChip[k+2][m+2]) {
						dr = true;
						can = true;
					}
				}
			}
			
			k = i; m = j;
			
			while((!(k+2 >= 8 || m-2 < 0 || k+1 >= 8 ||  m-1 < 0 ))) {
				if(board.isChip[k+1][m-1]) {
					break;
				}
				k++; m--;
			}
			
			rw[1] = k;
			col[1] = m;
			
			if(!(k+2 >= 8 || m-2 < 0 || k+1 >= 8 ||  m-1 < 0 )) {
				if(board.isChip[k+1][m-1]) {
					chip[1] = (Chip)board.tiles[k+1][m-1].getComponent(0);
					if(chip[1].player!=player && !board.isChip[k+2][m-2]) {
						dl = true;
						can = true;
					}
				}
			}
			
			k = i; m = j;
			while((!(k-2 < 0 || m+2 >= 8 || k-1 < 0 ||  m+1 >= 8 ))) {
				if(board.isChip[k-1][m+1]) {
					break;
				}
				k--; m++;
			}
			
			rw[2] = k;
			col[2] = m;
			
			if(!(k-2 < 0 || m+2 >= 8 || k-1 < 0 ||  m+1 >= 8 )) {
				if(board.isChip[k-1][m+1]) {
					chip[2] = (Chip)board.tiles[k-1][m+1].getComponent(0);
					if(chip[2].player != player && !board.isChip[k-2][m+2]) {
						ur = true;
						can = true;
					}
				}
			}
			
			k = i; m = j;
			
			while((!(k-2 < 0 || m-2 < 0 || k-1 < 0 ||  m-1 < 0 ))) {
				if(board.isChip[k-1][m-1]) {
					break;
				}
				k--; m--;
			}
			
			rw[3] = k;
			col[3] = m;
			
			if(!(k-2 < 0 || m-2 < 0 || k-1 < 0 ||  m-1 < 0 )) {
				if(board.isChip[k-1][m-1]) {
					chip[3] = (Chip)board.tiles[k-1][m-1].getComponent(0);
					if(chip[3].player != player && !board.isChip[k-2][m-2]) {
						ul = true;
						can = true;
					}
				}
			}
			
		}else{
			
			if(!(i+2 >= 8 || j+2 >= 8 || i+1 >= 8 ||  j+1 >= 8 )) {
				if(board.isChip[i+1][j+1]) {
					chip[0] = (Chip)board.tiles[i+1][j+1].getComponent(0);
					if(chip[0].player != player && !board.isChip[i+2][j+2]) {
						dr = true;
						can = true;
					}
				}
			}
			
			if(!(i+2 >= 8 || j-2 < 0 || i+1 >= 8 ||  j-1 < 0 )) {
				if(board.isChip[i+1][j-1]) {
					chip[1] = (Chip)board.tiles[i+1][j-1].getComponent(0);
					if(chip[1].player != player && !board.isChip[i+2][j-2]) {
						dl = true;
						can = true;
					}
				}
			}
			
			if(!(i-2 < 0 || j+2 >= 8 || i-1 < 0 ||  j+1 >= 8)) {
				if(board.isChip[i-1][j+1]) {
					chip[2] = (Chip)board.tiles[i-1][j+1].getComponent(0);
					if(chip[2].player != player && !board.isChip[i-2][j+2]) {
						ur = true;
						can = true;
					}
				}
			}
			
			if(!(i-2 < 0 ||  j-2 < 0 || i-1 < 0 ||  j-1 < 0)) {
				if(board.isChip[i-1][j-1]) {
					chip[3]= (Chip)board.tiles[i-1][j-1].getComponent(0);
					if(chip[3].player != player && !board.isChip[i-2][j-2]) {
						ul = true;
						can = true;
					}
				}
			}
		}
		
		return can;
		
	}
	
}