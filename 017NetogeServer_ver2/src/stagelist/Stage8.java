package stagelist;

import netogeserver.Stage;
import netogeserver.StageGate;
import apptemplate.Vector;

public class Stage8 extends Stage{

	public Stage8(int id){
		super(id);
		gateList.add( new StageGate(760,450,true,7));
		em.setEnemyNum(4, 1);
		em.setEnemyNum(3, 2);
	}
	@Override public void update(){
		em.update();
		
	}


	@Override public void moveStage(Vector p, int nextStageId){
		switch(nextStageId){
		case 7:
			p.x = 30;
			p.y = 35;
			break;
		}
	}
}
