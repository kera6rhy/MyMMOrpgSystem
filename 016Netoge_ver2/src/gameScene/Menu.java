package gameScene;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import apptemplate.Draw;
import apptemplate.Judge;
import apptemplate.Key;
import apptemplate.MyByteBuffer;

public class Menu {
	ArrayList<MenuItem> list = new ArrayList<MenuItem>();
	int showState = 0;
	ItemMenu im = new ItemMenu();
	ShopMenu sm = new ShopMenu();
	Game game;
	BuyOrder bo = new BuyOrder();
	ItemUseOrder io = new ItemUseOrder();
	ArrayList<Item> itemList = new ArrayList<Item>();
	
	StatusWindow sw = new StatusWindow();
	
	
	class Item{
		int id;
		int num;
		public Item(int i, int n){
			id  = i;
			num = n;
		}
		
	}
	public String getItemStr(int id){
		String str = null;
		switch(id){
		case 0:
			str = "赤い薬";
			break;
		}
		return str;
	}
	
	
	public Menu(Game g){
		game = g;
		list.add(new MenuItem("アイテム",0));
		list.add(new MenuItem("ステータス",1));
	}
	
	public void update(){
		im.update();
		sm.update();
		sw.update();
		
		if(Key.getKeyCount(KeyEvent.VK_ESCAPE) == 1){
			showState++;
			showState %= 2;
		}
		
		
		if(showState == 0)return ;
		int mx = Key.getMouseX();
		int my = Key.getMouseY();
		
		if(Key.getMouseCount() == 1){
			for(MenuItem m: list){
				if(m.inRect(mx, my)){
					m.click();
					break;
				}
			}
		}
		
	}
	
	
	public void draw(){
		im.draw();
		sm.draw();
		sw.draw();
		
		if(showState == 0)return;
		for(MenuItem mi: list){
			mi.draw();
		}
	}
	
	public void shopTalk(MyByteBuffer bb){
		if(bo.sendFlag){
			bb.putInt(2);
			bb.putInt(bo.id);
			bb.putInt(bo.shopId);
			bo.sendFlag = false;
		}
	}
	public void moneyTalk(MyByteBuffer bb){
		if(im.showState || sm.showState){
			bb.putInt(1);
			//bb.putInt(1);
		}
	}
	public void useItemTalk(MyByteBuffer bb){
		if(io.sendFlag == true){
			bb.putInt(3);
			bb.putInt(io.id);
			io.sendFlag = false;
		}
	}
	public void statusTalk(MyByteBuffer bb){
		if(sw.showState == true){
			bb.putInt(4);
		}
	}
	public void talk(MyByteBuffer bb){
		moneyTalk(bb);
		useItemTalk(bb);
		shopTalk(bb);
		statusTalk(bb);
		sw.abilityUpTalk(bb);
	}
	public void shopTalkRes(MyByteBuffer bb){
		sm.list.clear();
		int shopId = bb.getInt();
		int len = bb.getInt();
		
		for(int i=0; i<len;i++){
			int id = bb.getInt();
			int v = bb.getInt();
			sm.list.add(new Goods(id,v,shopId));
		}
		sm.showState = true;
	}
	
	public void talkRes(MyByteBuffer bb){
		itemList.clear();
		int itemLen = bb.getInt();
		for(int i=0;i<itemLen;i++){
			itemList.add(new Item(bb.getInt(),bb.getInt()) );
		}

	}
	
	class BuyOrder{
		boolean sendFlag = false;
		int id;
		int shopId;
	}
	
	class ItemUseOrder{
		boolean sendFlag = false;
		int id;
	}
	class ShopMenu{
		boolean showState = false;
		ArrayList<Goods> list = new ArrayList<Goods>();
		int drawX = 0;
		int drawY = 0;
		int height = 300;
		int width = 500;
		int color = 0xffffff;
		int barColor = 0xaaaaff;
		boolean isGrab = false;
		int grabPointX;
		int grabPointY;
		int beforeX;
		int beforeY;
		
		public ShopMenu(){
			
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
				int i=0;
				for(Goods g:list){
					if(Judge.inRect(drawX+10, drawY+30+50*i, 200, 30, mx, my)){
						bo.id = g.id;
						bo.shopId = g.shopId;
						bo.sendFlag = true;
						System.out.println("buy");
					}
					i++;
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
			int i=0 ;
			for(Goods g:list){
				String str = getItemStr(g.id);
				
				Draw.rect(drawX+10, drawY+30+50*i,  200, 30, 0xaaaaaa);
				Draw.string(str+"     "+g.val+"G", drawX+35, drawY+35+50*i, 0);
				i++;
			}
			Draw.string("money="+game.oriPl.money, drawX,drawY+height-20,0x555555);
		}
	}
		
	class Goods{
		int id;
		int val;
		int shopId;
		public Goods(int i,int v,int s){
			id = i;
			val = v;
			shopId = s;
		}
	}
	
	class MenuItem{
		String name;
		int drawX = 650;
		int drawY = 200;
		int intervalY = 40;
		int height = 20;
		int width = 100;
		int color = 0xffffff;
		int id;
		
		MenuItem(String n, int i){
			name = n;
			id = i;
			drawY += intervalY * id;
		}
		
		public void click(){
			switch(id){
			case 0:
				showState = 0;
				im.showState = true;
				
				break;
				
			case 1:
				showState = 0;
				sw.pl = game.oriPl;
				sw.showState = true;
				
				break;
			}
		}
		
		
		public boolean inRect(int x, int y){
			if(drawX<=x && drawX+width>=x && drawY<=y && drawY+height>=y){
				return true;
			}
			return false;
		}
		public void draw(){
			Draw.fillRect(drawX, drawY, width, height, color);
			Draw.rect(drawX, drawY, width, height, 0xaaaaaa);
			Draw.string(name, drawX+10, drawY, 0);
		}
	}
	
	class ItemMenu{
		boolean showState = false;
		int drawX = 0;
		int drawY = 0;
		int height = 300;
		int width = 200;
		int color = 0xffffff;
		int barColor = 0xaaaaff;
		boolean isGrab = false;
		int grabPointX;
		int grabPointY;
		int beforeX;
		int beforeY;
		
		public ItemMenu(){
			
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
				int n=0;
				for(Item i: itemList){
					if(Judge.inRect(drawX+5, drawY+35+n*30, 150, 30, mx, my)){
						io.sendFlag = true;
						io.id = i.id;
					}
					n++;
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
			int n=0;
			for(Item i: itemList){
				Draw.string(getItemStr(i.id)+"    ×"+i.num, drawX+10, drawY+40 + n*30, 0);
				Draw.rect(drawX+5, drawY+35+n*30, 150, 30, 0xaaaaaa);
				n++;
			}
			Draw.string("money="+game.oriPl.money, drawX,drawY+height-20,0x555555);
		}
	}

}	
