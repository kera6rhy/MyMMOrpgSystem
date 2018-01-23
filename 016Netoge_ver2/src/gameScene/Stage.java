package gameScene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import apptemplate.Draw;
import apptemplate.MyByteBuffer;
import apptemplate.Vector;

public class Stage {
	ArrayList<Gate> gateList = new ArrayList<Gate>();
	List<NPC> npcList = Collections.synchronizedList(new ArrayList<NPC>());
	ArrayList<Money> moneyList = new ArrayList<Money>();
	ArrayList<Money> addMoneyList = new ArrayList<Money>();
	
	int stageId;
	
	public Stage(){
		
	}
	
	
	
	public void update(){
		
	}
	
	
	public void draw(){
		Draw.fillRect(0,0,768,480,0xbbcc80);
		for(Gate g:gateList){
			Draw.line(g.p1.x, g.p1.y, g.p2.x, g.p2.y, 0x0000ff);
		}
	//	Draw.string("stageId="+stageId+"  gateNum="+gateList.size(), 0, 20, 0);
		synchronized(npcList){
			for(NPC n: npcList){
				Draw.fillCircle(n.p, n.r, n.color);
			}
			
			moneyList = addMoneyList;
			for(Money m: moneyList){
				Draw.fillCircle(m.p, 5, 0xffff33);
			}
		}
	}
	
	public void moneyTalk(MyByteBuffer bb){
		int len = bb.getInt();
		ArrayList<Money> addMoneyListTmp = new ArrayList<Money>();
		for(int i=0 ;i<len;i++){
			Money m = new Money(bb.getDouble(), bb.getDouble(), bb.getInt());
			addMoneyListTmp.add(m);
		}
		addMoneyList = addMoneyListTmp;
	}
	
	public void talk(MyByteBuffer bb, Player pl){
		moneyTalk(bb);
		int num3 = bb.getInt();
		if(num3 == 1){
			gateList.clear();
			int gateNum = bb.getInt();
			for(int i=0; i<gateNum;i++){
				Gate g = new Gate();
				g.p1.x = bb.getDouble();
				g.p1.y = bb.getDouble();
				g.p2.x = bb.getDouble();
				g.p2.y = bb.getDouble();
				gateList.add(g);
			}
			stageId = bb.getInt();
			pl.point.x = bb.getDouble();
			pl.point.y = bb.getDouble();
			npcList.clear();
			int len = bb.getInt();
			for(int i=0; i<len;i++){
				NPC n = new NPC();
				int npcType = bb.getInt();
				n.p.x = bb.getDouble();
				n.p.y = bb.getDouble();
				n.r = bb.getDouble();
				n.color = bb.getInt();
				npcList.add(n);
			}
		}
		else{
			
		}
	}
	class Gate{
		Vector p1 = new Vector();
		Vector p2 = new Vector();
		
	}
}
