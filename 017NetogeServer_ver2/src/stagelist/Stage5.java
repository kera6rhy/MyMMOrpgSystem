package stagelist;

import java.util.ArrayList;
import java.util.HashMap;

import apptemplate.Judge;
import apptemplate.MyByteBuffer;
import apptemplate.Vector;
import netogeserver.ClientData;
import netogeserver.Enemy;
import netogeserver.EnemyMgr;
import netogeserver.Stage;
import netogeserver.StageGate;
import netogeserver.ClientData.PlayerData;

public class Stage5 extends Stage{

	
	public Stage5(int id){
		super(id);
		gateList.add( new StageGate(30,470,80,470,3));
		gateList.add( new StageGate(750,30,750,80,6));
		em.setEnemyNum(2, 2);
	}
	@Override public void update(){
		em.update();
	}

	@Override public void moveStage(Vector p, int nextStageId){
		switch(nextStageId){
		case 3:
			p.x = 325;
			p.y = 40;
			break;
		case 6:
			p.x = 40;
			p.y = 225;
			break;
		}
	}
}
