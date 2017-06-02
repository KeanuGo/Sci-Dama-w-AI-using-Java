import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundThread extends Thread {

	private String resource;
	private boolean loop;
	private Clip clip;
	
	public SoundThread(String resource, boolean loop) {
		
		this.resource = resource;
		this.loop = loop;
		this.start();
		
    }

    public void run() {
    	
		try{
			 File file = new File(resource);
			 clip = AudioSystem.getClip();
			 AudioInputStream Audio = AudioSystem.getAudioInputStream(file);
			 clip.open(Audio);
			 
			 if(loop) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			 }else{
				 clip.start();
			 }
			 Thread.sleep(6000);
		}catch(Exception e){}
		
    }
	
	public void mute() {
		clip.stop();
	}
	
}