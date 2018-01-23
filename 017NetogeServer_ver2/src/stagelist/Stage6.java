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

public class Stage6 extends Stage {

	
	public Stage6(int id){
		super(id);
		gateList.add( new StageGate(20,200,20,250,5));
		gateList.add( new StageGate(300,10,false,7));
	}
	@Override public void update(){
		em.update();
		
	}


	@Override public void moveStage(Vector p, int nextStageId){
		switch(nextStageId){
		case 5:
			p.x = 730;
			p.y = 55;
			break;
		case 7:
			p.x = 325;
			p.y = 450;
			break;
		}
	}

}
