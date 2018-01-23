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

public class Stage3 extends Stage{

	
	public Stage3(int id){
		super(id);
		gateList.add( new StageGate(60,100,60,150,2));
		gateList.add( new StageGate(300,470,350,470,4));
		gateList.add( new StageGate(300,20,350,20,5));
	}
	@Override public void update(){
		em.update();
		
	}

	@Override public void moveStage(Vector p, int nextStageId){
		switch(nextStageId){
		case 2:
			p.x = 740;
			p.y = 325;
			break;
		case 4:
			p.x = 325;
			p.y = 60;
			break;
		case 5:
			p.x = 55;
			p.y = 450;
			break;
		}
	}	
}
