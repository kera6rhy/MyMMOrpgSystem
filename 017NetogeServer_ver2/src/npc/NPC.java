package npc;

import apptemplate.MyByteBuffer;
import apptemplate.Vector;

abstract public class NPC {
	Vector p = new Vector();
	Vector v = new Vector();
	double r = 10;
	int color = 0xaaaaaa;
	NPCType type;
	
	
	
	public NPC(int x, int y, NPCType t){
		p.x = x; p.y = y;
		type =t;
	}

	abstract public void talk(MyByteBuffer bb);
	abstract public void talk(MyByteBuffer bb, int i);
	
	enum NPCType{
		SHOP
	}
}
