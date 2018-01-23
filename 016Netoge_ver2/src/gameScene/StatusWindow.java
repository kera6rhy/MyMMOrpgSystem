package gameScene;

import java.util.ArrayList;

import apptemplate.Draw;
import apptemplate.Judge;
import apptemplate.Key;
import apptemplate.MyByteBuffer;

public class StatusWindow extends GameWindow{
	Player pl;
	ArrayList<SimpleButton> buttonList = new ArrayList<SimpleButton>();
	AbilityUpOrder ao = new AbilityUpOrder();
	
	public StatusWindow(){
		super(200,300);
		for(int i=0;i<4;i++){
			
			buttonList.add(new SimpleButton(17,17,i));
		}
	}
	
	public void update(){
		if(showState == false)return;
		super.update();
		int i = 0;
		for(SimpleButton b: buttonList){
			b.update();
			if(b.clicked == true){
				ao.sendFlag = true;
				ao.id = i;
			}
			i++;
		}
	}
	
	
	public void abilityUpTalk(MyByteBuffer bb){
		if(ao.sendFlag == true){
			bb.putInt(5);
			bb.putInt(ao.id);
			ao.sendFlag = false;
		}
	}
	public void draw(){
		if(showState == false)return;
		super.draw();
		int tmp = 50;

		Draw.string("Lv", drawX+10,drawY+tmp, 0);tmp+=20;
		Draw.string("経験値", drawX+10,drawY+tmp, 0);tmp+=20;
		Draw.string("HP", drawX+10,drawY+tmp, 0);tmp+=20;
		Draw.string("攻撃力", drawX+10,drawY+tmp, 0);tmp+=20;
		Draw.string("防御力", drawX+10,drawY+tmp, 0);tmp+=20;
		Draw.string("移動速度", drawX+10,drawY+tmp, 0);tmp+=20;
		tmp = 50;
		Draw.string(":"+pl.level, drawX+100,drawY+tmp, 0);tmp+=20;
		Draw.string(":"+pl.exp+"/"+pl.nextExp, drawX+100,drawY+tmp, 0);tmp+=20;
		Draw.string(":"+pl.hp+"/"+pl.maxHp, drawX+100,drawY+tmp, 0);tmp+=20;
		Draw.string(":"+pl.STR, drawX+100,drawY+tmp, 0);tmp+=20;
		Draw.string(":"+pl.DEF, drawX+100,drawY+tmp, 0);tmp+=20;
		Draw.string(":"+pl.speed, drawX+100,drawY+tmp, 0);tmp+=20;
		for(SimpleButton b: buttonList){
			b.draw();
		}
		
		Draw.string("AP:"+pl.abilityPoint, drawX+10, drawY+height-20, 0);
	}
	
	class AbilityUpOrder{
		boolean sendFlag = false;
		int id;
	}
	
	class SimpleButton{
		int x=0;
		int y=0;
		int w = 20;
		int h = 20;
		int color = 0xffffff;
		int edgeColor = 0xaaaaaa;
		int id;
		boolean clicked = false;
		
		
		public SimpleButton(int aw, int ah, int i){
			id = i;
			w = aw;
			h = ah;
		}
		
		
		public void update(){
			clicked = false;
			int mx = Key.getMouseX();
			int my = Key.getMouseY();
			if(Key.getMouseCount() == 1){
				if(Judge.inRect(x, y, w, h, mx, my)){
					clicked = true;
				}
			}
			x = drawX + 180;
			y = drawY + 90+id*20;
		}
		
		
		public void draw(){
			Draw.fillRect(x,y,w,h,color);
			Draw.rect(x, y, w, h, edgeColor);
		}
	}
}
