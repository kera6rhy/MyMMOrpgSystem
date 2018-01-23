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

public class Stage4 extends Stage{

	
	public Stage4(int id){
		super(id);
		gateList.add( new StageGate(300,30,350,30,3));
		em.setEnemyNum(1,1);
	}
	@Override public void update(){
		em.update();
		
	}

	@Override public void moveStage(Vector p, int nextStageId){
		switch(nextStageId){
		case 3:
			p.x = 325;
			p.y = 450;
			break;
		}
	}	
}
