package netogeserver;

import apptemplate.Vector;

public class Money {
	int val;
	Vector p;
	public Money(double x, double y, int v){
		p = new Vector(x,y);
		val = v;
	}
}
