package gameScene;

import apptemplate.Draw;
import apptemplate.Vector;

public class Enemy {
	Vector p = new Vector();
	Vector v = new Vector();
	int color;
	double r;
	
	
	public void draw(){
		Draw.fillCircle(p, r, color);
	}
}
