package enemy;

import netogeserver.Enemy;

public class Enemy4 extends Enemy{
	public Enemy4(){
		super();
		r = 20;
		hp = 60;
		STR = 10;
		speed = 0.2;
		exp = 15;
		color = 0x777777;
		money = 12;
		updateInterval = 10;
		resistKnockback = 10;
	}
}
