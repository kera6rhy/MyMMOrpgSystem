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

public class Stage2 extends Stage{
	public Stage2(int id){
		super(id);
		gateList.add( new StageGate(60,100,60,150,1));
		gateList.add( new StageGate(760,300,760,350,3));
	}
	@Override public void update(){
		em.update();
		
	}
	
	@Override public void moveStage(Vector p, int nextStageId){
		switch(nextStageId){
		case 1:
			p.x =740;
			p.y = 125;
			break;
		case 3:
			p.x = 80;
			p.y = 125;
			break;
		}
	}	

}
