package stagelist;

import apptemplate.Vector;
import netogeserver.Stage;
import netogeserver.StageGate;

public class Stage7 extends Stage{
	public Stage7(int id){
		super(id);

		gateList.add( new StageGate(300,470,false,6));
		gateList.add( new StageGate(10,10,true,8));
		em.setEnemyNum(3, 2);
	}

	@Override public void update(){
		em.update();

	}


	@Override public void moveStage(Vector p, int nextStageId){
		switch(nextStageId){
		case 6:
			p.x = 325;
			p.y = 30;
			break;
		case 8:
			p.x = 740;
			p.y = 445;
			break;
		}
	}
}
