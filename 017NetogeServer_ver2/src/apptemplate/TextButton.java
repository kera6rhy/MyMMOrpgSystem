package apptemplate;


import java.awt.FontMetrics;
import java.awt.event.ActionListener;

public class TextButton extends Part{
	private String str;
	private Vector p = new Vector();
	private int dx;
	private int dy;
	private int backColor;
	private int pressedBackColor;
	private boolean isPressed;
	private int frontColor;
	private boolean isInside;
	private int size;
	private int strWidth;
	private int strHeight;
	private int pushPhase;
	private boolean clicked;
	private boolean glass;
	private MyAction action;


	public TextButton(String s, int x, int y, int size, int frontColor, int c,boolean flag){
		str = s;
		p.x = x;
		p.y = y;
		backColor = c;

		int r = backColor / 0x10000;
		int g = (backColor / 0x100)%0x100;
		int b = (backColor) %0x100;
		r *= 0.7;
		g *= 0.7;
		b *= 0.7;
		pressedBackColor = r * 0x10000 + g * 0x100 + b;
		this.frontColor = frontColor;
		this.size = size;
		Draw.setFontSize(size);
		FontMetrics fo = Draw.g.getFontMetrics() ;
        strWidth = fo.stringWidth(str);
        strHeight = fo.getHeight();
        glass = flag;
	}
	public TextButton(String s, int x, int y, int size,int c){
		this(s, x, y, size,0x000000, c,false);
	}

	public void setAction(MyAction a){
		action = a;
	}
	
	public void update(){
		
		int mx = Key.getMouseX();
		int my = Key.getMouseY();
		
		if(mx>p.x && mx<=p.x+strWidth && my>(int)p.y && my<=(int)p.y+strHeight){
			isInside = true;
		}
		else{
			isInside = false;
			pushPhase = 0; //範囲外に出たらすぐキャンセル
			isPressed = false;
		}

		if(Key.getMouseCount() >= 1){
			if(isInside && pushPhase == 0){
				pushPhase = 1;
				isPressed = true;
			}
		}
		else{
			isPressed = false;
			if(pushPhase == 1){//一度もキャンセルされずに最後まで行ったら
				action.doAction();
				pushPhase = 0;
			}
		}
	}

	public void draw(){

		Draw.rect((int)p.x, (int)p.y, strWidth, strHeight, 0x999999);
		
		if(isPressed == true){

			Draw.fillRect(p.x, p.y, strWidth, strHeight, pressedBackColor);
		}
		else{

			Draw.fillRect(p.x, p.y, strWidth, strHeight, backColor);	
		}
		if(isInside == true){
		}
		Draw.string(str,(int)p.x,(int)p.y,frontColor,size);
	}
	public void debugDraw(){
		Draw.string("mx="+Key.getMouseX()+"my="+Key.getMouseY()+" py="+(int)p.y,0,30,0x000000);
	}
}