package stagelist;

import java.util.ArrayList;
import java.util.HashMap;

import netogeserver.ClientData;
import netogeserver.ClientData.PlayerData;
import netogeserver.Enemy;
import netogeserver.EnemyMgr;
import netogeserver.Stage;
import netogeserver.StageGate;
import npc.NPCMgr;
import apptemplate.Judge;
import apptemplate.MyByteBuffer;
import apptemplate.Vector;

public class Stage1 extends Stage{
	String stageName = "Stage1";


	
	public Stage1(int id){
		super(id);
		gateList.add( new StageGate(760,100,760,150,2));
		nm.addNPC(300, 100, NPCMgr.Type.SHOP);
	}
	
	@Override public void update(){
		//em.update();
	}
	
	
	@Override public void moveStage(Vector p, int nextStageId){
		switch(nextStageId){
		case 2:
			p.x = 80;
			p.y = 125;
			break;
		}
	}	
	
	@Override public void attackTo(PlayerData pd){
		/*
		if(pd.damaged)return;
		for(Enemy e: em.enemys){
			if(Judge.circleInCircle(pd.p, pd.r, e.p, e.r)){
				pd.attack(e.STR);
				break;
			}
		}*/
	}
}
