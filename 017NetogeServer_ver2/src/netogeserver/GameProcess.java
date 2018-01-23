package netogeserver;

import java.util.ArrayList;
import java.util.HashMap;

import apptemplate.Judge;

public class GameProcess {
	NetogeServer ns;
	StageMgr sm = new StageMgr();
	HashMap<String,ClientData> clientData;
	ClientMgr cm = new ClientMgr(this);
	
	public GameProcess(NetogeServer n){
		ns = n;
		clientData = ns.clientData;
	}
	
	
	public void update(){
		cm.update();
		sm.update();
		sm.hantei(clientData);
	}
	
}
