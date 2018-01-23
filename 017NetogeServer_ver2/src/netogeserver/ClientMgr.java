package netogeserver;

import java.awt.event.KeyEvent;

import netogeserver.ClientData.PlayerData;

public class ClientMgr {
	GameProcess gp;
	
	public ClientMgr(GameProcess g){
		gp = g;
	}
	public void update(){
		for(ClientData cd: gp.clientData.values()){
			if(cd.inField){
				update(cd);
			}
		}
	}
	
	private void update(ClientData cd){
		PlayerData pd = cd.getLoginPlayer();
		Stage s = gp.sm.getStageOf(pd.stageId);
		if(s.onGate(pd.p, pd.r)){
			int nextStageId = s.getNextStageId(pd.p,pd.r);
			
			pd.nextStageId = nextStageId;
			s.moveStage(pd.nextP, nextStageId);
			pd.loadStageFlag = true;
		}
		if(s.onMoney(pd.p, pd.r)){
			pd.money += s.getMoney(pd.p, pd.r);
		}
		s.attackTo(pd);
		
		if(cd.key.getKeyCount(KeyEvent.VK_Z) == 1){
			if(s.isNPCNear(pd.p)){
				pd.talkNPC = s.getNearNPC(pd.p);
				pd.npcTalkFlag = true;
			}
		}
	}
}
