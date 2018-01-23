package apptemplate;

import java.awt.image.BufferStrategy;

public class RenderThread extends Thread{
	private AppTemplate at;
	static FrameRate fr;
		
	RenderThread(AppTemplate a){
		this.at = a;
		fr =  new FrameRate();
	}

	@Override
	public void run(){
		while(true){
			at.gameRender();
			fr.update();
		}
	}
}
