package netogeserver;

import java.util.HashMap;

import stagelist.Stage1;
import stagelist.Stage2;
import stagelist.Stage3;
import stagelist.Stage4;
import stagelist.Stage5;
import stagelist.Stage6;
import stagelist.Stage7;
import stagelist.Stage8;

public class StageMgr {
	Stage stageList[] = new Stage[10];
	int tmp = 0;
	
	public StageMgr(){
		stageList[1] = new Stage1(1);tmp++;
		stageList[2] = new Stage2(2);tmp++;
		stageList[3] = new Stage3(3);tmp++;
		stageList[4] = new Stage4(4);tmp++;
		stageList[5] = new Stage5(5);tmp++;
		stageList[6] = new Stage6(6);tmp++;
		stageList[7] = new Stage7(7);tmp++;
		stageList[8] = new Stage8(8);tmp++;
	}
	
	
	public void update(){
		for(int i=1;i<=tmp;i++){
			stageList[i].update();
		}
	}
	
	public Stage getStageOf(int id){
		return stageList[id];
	}

	public void hantei(	HashMap<String,ClientData> clientData){
		for(int i=1;i<=tmp;i++){
			stageList[i].hantei(clientData);
		}
		
	}
}
