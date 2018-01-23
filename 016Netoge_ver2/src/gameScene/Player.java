package gameScene;

import java.awt.event.KeyEvent;

import apptemplate.Draw;
import apptemplate.Key;
import apptemplate.MyByteBuffer;
import apptemplate.Vector;

public class Player extends Unit{
	String id;
	String chatStr="";
	String talkStr = "";
	String name = "";
	int color;
	int r;
	int chatStrCount;
	double speed = 1;
	boolean isJustExist;
	int attackCount = 0;
	int hp = 1;
	int maxHp = 30;
	boolean damaged;
	int damageCount = 0;
	int exp;
	int nextExp = 1;
	int level;
	int money;
	int STR;
	int DEF;
	int abilityPoint = 0;
	Bullet bl[] = new Bullet[10];
	
	
	public Player(){
		point.x = 200;
		point.y = 100;
		for(int i=0;i<bl.length;i++){
			bl[i] = new Bullet();
		}
	}
	
	public Player(String str){
		this();
		id = str;
	}
	
	public void input(){
		if(Key.getKeyCount(KeyEvent.VK_UP) >= 1){
			a.y-=speed;
		}
		if(Key.getKeyCount(KeyEvent.VK_DOWN) >= 1){
			a.y+=speed;
		}
		if(Key.getKeyCount(KeyEvent.VK_LEFT) >= 1){
			a.x-=speed;
		}
		if(Key.getKeyCount(KeyEvent.VK_RIGHT) >= 1){
			a.x+=speed;
		}
		/*
		if(Key.getKeyCount(KeyEvent.VK_X) == 1){
			if(attackCount == 0){
				attackCount = 1;
				bl.p.x = point.x;
				bl.p.y = point.y;
				bl.v = Vector.getEV(a);
				bl.v.mulV(10+a.length());
				bl.exist  = true;
				bl.time = 0;
			}
		}*/
		
	}
	
	public void otherPlayerUpdate(){

		point.addV(a);

		for(Bullet b : bl){
			b.update();
		}
		chatStrCount++;
		if(chatStrCount >= 300){
			chatStr = "";
		}

		if(damaged){
			damageCount++;
		}
	}
	public void update(){
		input();
		point.addV(a);
		a.mulV(0.6);
		
		if(attackCount >= 1){
			attackCount++;
			if(attackCount ==10){
				attackCount = 0;
			}
		}
		
		for(Bullet b:bl){
			b.update();
		}
		chatStrCount++;
		if(chatStrCount >= 300){
			chatStr = "";
		}
		
	}
	
	public void draw(){
		if(hp > 0){
			if(damaged == false || (damageCount/5)%2 == 0){		
				Draw.fillOval(point.x, point.y, r,color);
			}
			Draw.string(name, point.x-10, point.y-20, 0x00cc00);
			Draw.string(chatStr, point.x-30, point.y - 50,0x000000);
			Draw.fillRect(point.x-25, point.y-25, 50*hp/maxHp, 5, 0xaa0000);
			Draw.rect(point.x-25, point.y-25, 50, 5, 0x555555);

			Draw.fillRect(point.x-25, point.y-20, 50, 3, 0x999999);
			Draw.fillRect(point.x-25, point.y-20, 50*exp/nextExp, 3, 0xffff44);
			Draw.rect(point.x-25, point.y-20, 50, 3, 0x555555);
			Draw.string("Lv."+level, point.x-27, point.y-40, 0x000000);
		}
		//Draw.string("damaged="+damageCount, 0, 40, 0);
		for(Bullet b: bl){
			b.draw();
		}
	}
	
	public void talk(Game g, MyByteBuffer bb){
		if(talkStr.equals("")==false){
			//gClient.setTalkString("3 0 "+netoge.loginId+" "+(int)pl.point.x+" "+(int)pl.point.y + " "+(int)(pl.a.x*100)+ " "+(int)(pl.a.y*100)+" "+ (int)(pl.bl.p.x)+" "+ (int)(pl.bl.p.y)+ " "+ (int)(pl.bl.v.x*100)+" "+ (int)(pl.bl.v.y*100)+" "+ ((pl.bl.exist)?1:0)+" a"+pl.talkStr);
			
			bb.clear();
			bb.putInt(3).putInt(0).putString(g.netoge.loginId);
			bb.putInt(KeyEvent.VK_UP).putInt(Key.getKeyCount(KeyEvent.VK_UP));
			bb.putInt(KeyEvent.VK_DOWN).putInt(Key.getKeyCount(KeyEvent.VK_DOWN));
			bb.putInt(KeyEvent.VK_LEFT).putInt(Key.getKeyCount(KeyEvent.VK_LEFT));
			bb.putInt(KeyEvent.VK_RIGHT).putInt(Key.getKeyCount(KeyEvent.VK_RIGHT));
			bb.putInt(KeyEvent.VK_X).putInt(Key.getKeyCount(KeyEvent.VK_X));
			bb.putInt(KeyEvent.VK_Z).putInt(Key.getKeyCount(KeyEvent.VK_Z));
			//bb.putInt((int)point.x).putInt((int)point.y).putDouble(a.x).putDouble(a.y);
			//bb.putInt((int)bl.p.x).putInt((int)bl.p.y).putDouble(bl.v.x).putDouble(bl.v.y).putInt((bl.exist)?1:0);
			bb.putInt(1).putString(talkStr);
			//g.gClient.setTalkBuf(bb);
			talkStr = "";
		}
		else{
			if(g.chatStrStack.size() == 0){
				if(g.sendIntervalCount % g.sendInterval == 0){
					//gClient.setTalkString("3 0 "+netoge.loginId+" "+(int)pl.point.x+" "+(int)pl.point.y + " "+(int)(pl.a.x*100)+ " "+(int)(pl.a.y*100)+" "+ (int)(pl.bl.p.x)+" "+ (int)(pl.bl.p.y)+ " "+ (int)(pl.bl.v.x*100)+" "+ (int)(pl.bl.v.y*100)+" "+ ((pl.bl.exist)?1:0)+" a");
					bb.clear();
					bb.putInt(3).putInt(0).putString(g.netoge.loginId);
					bb.putInt(KeyEvent.VK_UP).putInt(Key.getKeyCount(KeyEvent.VK_UP));
					bb.putInt(KeyEvent.VK_DOWN).putInt(Key.getKeyCount(KeyEvent.VK_DOWN));
					bb.putInt(KeyEvent.VK_LEFT).putInt(Key.getKeyCount(KeyEvent.VK_LEFT));
					bb.putInt(KeyEvent.VK_RIGHT).putInt(Key.getKeyCount(KeyEvent.VK_RIGHT));
					bb.putInt(KeyEvent.VK_X).putInt(Key.getKeyCount(KeyEvent.VK_X));
					bb.putInt(KeyEvent.VK_Z).putInt(Key.getKeyCount(KeyEvent.VK_Z));
					//bb.putInt((int)point.x).putInt((int)point.y).putDouble(a.x).putDouble(a.y);
					//bb.putInt((int)bl.p.x).putInt((int)bl.p.y).putDouble(bl.v.x).putDouble(bl.v.y).putInt((bl.exist)?1:0);
					bb.putInt(0);
					//g.gClient.setTalkBuf(bb);
					//MyByteBuffer r = new MyByteBuffer();
					//r.setArray(array);
					//System.out.println(r.getInt()+" "+r.getInt()+" "+r.getString()+" "+r.getInt()+" "+r.getInt()+" "+r.getDouble()+" "+r.getDouble()+" ");

				}
				g.sendIntervalCount++;
			}
		}
	}
	
	
	class Bullet{
		Vector p = new Vector();
		Vector v = new Vector();
		boolean exist = false;
		int time = 0;
		void update(){
			if(exist == false)return;
			time++;
			p.addV(v);
			if(time == 10){
				exist = false;
			}
		}
		
		
		void draw(){
			if(exist == false)return;
			Draw.fillCircle(p,3,0x00ff00);
		}
		
	
	}
}
