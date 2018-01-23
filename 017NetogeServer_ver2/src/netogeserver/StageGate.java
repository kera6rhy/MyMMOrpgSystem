package netogeserver;

import apptemplate.Vector;

public class StageGate {
	public Vector p1 = new Vector();
	public Vector p2 = new Vector();
	public int toStageId = -1;
	
	public StageGate(int x1,int y1, int x2, int y2, int toId){
		p1.x = x1;
		p1.y = y1;
		p2.x = x2;
		p2.y = y2;
		toStageId = toId;
	}
	
	public StageGate(int x, int y,boolean tate,  int i){
		toStageId = i;
		p1.x = x;
		p1.y = y;
		if(tate){
			p2.x = p1.x;
			p2.y = p1.y+50;
		}
		else{
			p2.x = p1.x+50;
			p2.y = p1.y;
		}
	}
	
}
