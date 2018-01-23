package netogeserver;

import java.util.Random;

import apptemplate.Global;
import apptemplate.Vector;

public class Enemy {
	private enum State{
		STOP,UP,DOWN,LEFT,RIGHT
	}
	
	public Vector p = new Vector();
	Vector v = new Vector();
	boolean exist;
	public double r = 8;
	protected int hp = 10;
	int time =0;
	public int STR = 3;
	protected double speed = 0.1;
	public int exp = 1;
	protected int color = 0xcc7777;
	protected int money = 2;
	State state = State.STOP;
	protected int updateInterval = 30;
	protected double resistKnockback = 1;
	
	public Enemy(){
		Random rand = Global.rand;
		p.x = rand.nextDouble() * 760;
		p.y = rand.nextDouble() * 480;
		exist = true;
	}
	
	public void attack(int a,int kb, Vector blp){
		hp -= a;
		Vector tv = new Vector();
		tv.copyFrom(p);;
		tv.subV(blp);
		tv.transEV();
		tv.mulV(kb / resistKnockback);
		v.addV(tv);;
	}
	
	public void update(){
		if(exist==false)return;
		time++;
		Random rand = Global.rand;
		
		if(time%updateInterval == 0){
			switch(rand.nextInt(20)){
			default:
				break;
			case 5:
			case 4:
			case 3:
			case 2:
				state = State.STOP;
				break;
			case 6:
				state = State.UP;
				break;
			case 7:
				state = State.DOWN;
				break;
			case 8:
				state = State.LEFT;
				break;
			case 9:
				state = State.RIGHT;
				break;
			}
		}
		switch(state){
		case STOP:
			//v.x = 0;
			//v.y = 0;
			break;
		case UP:
			//v.x=0;
			v.y -=speed;
			break;
		case DOWN:
			//v.x = 0;
			v.y += speed;
			break;
		case LEFT:
			v.x -= speed;
			//v.y = 0;
			break;
		case RIGHT:
			v.x += speed;
			//v.y = 0;
			break;
			
		}
		

		p.addV(v);
		v.mulV(0.6);

		if(p.x <0)p.x =0;
		if(p.x > 768)p.x=768;
		if(p.y<0)p.y=0;
		if(p.y>480)p.y=480;
		
		
	}
	
}
