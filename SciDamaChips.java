
public class SciDamaChips {
	
	public final Chip[] chip = new Chip[12];
	
	public SciDamaChips(int color) {
		for(int i = 0; i < 12; i++) {
			chip[i] = new Chip(color);
		}
	}
	
}