package apptemplate;

import java.awt.event.*;

public class Textbox{
	public static final int MAX_CHAR = 100;
	private int x;
	private int y;
	private int width;
	private int height;
	private int frontColor;
	private int backColor;
	private char[] chs = new char[MAX_CHAR];
	private int chsCount = 0;
	private boolean active;
	private int time;
	private int oldChsCount;
	private int strWidth;
	private StringBuffer str;

	public Textbox(int x, int y, int width, int height, int frontColor, int backColor){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.frontColor = frontColor;
		this.backColor = backColor;
		str = new StringBuffer();
	}

	public Textbox(int x, int y, int width, int height){
		this(x,y,width, height, 0x000000, 0xffffff);
	}


	public void update(){
		oldChsCount = chsCount;
		int mx = Key.getMouseX();
		int my = Key.getMouseY();
		time++;

		boolean mouseIn = Judge.inRect(x,y,width,height,mx,my);

		if(Key.mouseCount[0] == 1){
			if(mouseIn){
				active = true;
				time = 0;
			}
			else{
				active = false;
			}
		}
		if(active == false){
			return;
		}

		//activeの時↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
		if(Key.justTyped()){
			char key = Key.getJustChar();
			if(Judge.isRomaji(key) && chsCount < MAX_CHAR){
				chs[chsCount] = key;
				str.append(key);
				chsCount++;
			}
		}
		if(Key.getKeyCount(KeyEvent.VK_BACK_SPACE) == 1){
			if(chsCount != 0){
				str.deleteCharAt(chsCount-1);
				chsCount--;

			}
		}
		else if(Key.getKeyCount(KeyEvent.VK_BACK_SPACE) >20){
			if(chsCount != 0 && time%2 == 0){
				str.deleteCharAt(chsCount-1);
				chsCount--;
			}
		}
		
		if(oldChsCount != chsCount){
			Draw.setFontSize(10);
			strWidth = Draw.fontMetrics.stringWidth(str.toString());
		}

	}

	public void draw(){
		Draw.fillRect(x,y,width,height,backColor);
		Draw.rect(x,y,width,height,frontColor);

		Draw.string(str.toString(),x+3,y+3,frontColor);
		if(active){
			if((time / 30)%2 == 0){
				Draw.line(x+strWidth+4, y, x+strWidth+4, y+15, 0x000000);
			}
		}
		
		//Draw.string(str+"123",0,30,0x000000);
	}
	
	
}