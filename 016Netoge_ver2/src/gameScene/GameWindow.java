package gameScene;

import gameScene.Menu.Goods;
import apptemplate.Draw;
import apptemplate.Judge;
import apptemplate.Key;

public class GameWindow {
	boolean showState = false;
	int drawX = 0;
	int drawY = 0;
	int height = 300;
	int width = 500;
	int color = 0xffffff;
	int barColor = 0xaaaaff;
	
	private boolean isGrab = false;
	private int grabPointX;
	private int grabPointY;
	private int beforeX;
	private int beforeY;
	
	public GameWindow(int w, int h){
		width = w;
		height = h;
	}
	
	
	public void update(){
		if(showState == false)return;
		int mx = Key.getMouseX();
		int my = Key.getMouseY();
		if(Key.getMouseCount() == 1){
			if(Judge.inRect(drawX, drawY, width, 20, mx, my)){
				isGrab = true;
				grabPointX = mx;
				grabPointY = my;
				beforeX = drawX;
				beforeY = drawY;
			}
			if(Judge.inRect(drawX+width-20, drawY, 20, 20, mx, my)){
				showState = false;
				isGrab = false;
			}
		}
		if(isGrab){
			drawX = mx-grabPointX + beforeX;
			drawY = my - grabPointY + beforeY;
			if(Key.getMouseCount() == 0){
				isGrab = false;
			}
		}
	}

	public void draw(){
		if(showState == false)return;
		Draw.fillRect(drawX, drawY, width, height, color);
		Draw.fillRect(drawX, drawY, width, 20, barColor);
		Draw.fillRect(drawX+width - 20, drawY, 20, 20, 0xffaaaa);
		Draw.rect(drawX, drawY, width, height, 0xaaaaaa);
		
	}
}
