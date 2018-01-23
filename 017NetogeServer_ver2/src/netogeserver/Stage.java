package netogeserver;

import java.util.ArrayList;
import java.util.HashMap;

import netogeserver.ClientData.Bullet;
import netogeserver.ClientData.PlayerData;
import npc.NPC;
import npc.NPCMgr;
import apptemplate.Judge;
import apptemplate.MyByteBuffer;
import apptemplate.Vector;

public abstract class Stage {
	protected EnemyMgr em = new EnemyMgr();;
	protected NPCMgr nm = new NPCMgr();
	public ArrayList<StageGate> gateList = new ArrayList<StageGate>();
	protected ArrayList<Money> moneyList = new ArrayList<Money>();
	private ArrayList<Money> removeMoneyList = new ArrayList<Money>();
	boolean removeMoneyLock = false;
	protected int stageId;
	
	public Stage(int i){
		stageId = i;
	}
	
	public boolean isNPCNear(Vector p){
		return nm.isNPCNear(p);
	}
	
	public NPC getNearNPC(Vector p){
		return nm.getNearNPC(p);
	}
	
	public boolean onGate(Vector p, double r){
		for(StageGate sg : gateList){
			if(Judge.lineInCircle(sg.p1, sg.p2, p, r)){
				
				return true;
			}
		}
		return false;
	}
	
	public int getNextStageId(Vector p, double r){
		for(StageGate sg : gateList){
			if(Judge.lineInCircle(sg.p1, sg.p2, p, r)){
				return sg.toStageId;
			}
		}
		return -1;
	}

	public void attackTo(PlayerData pd){
		if(pd.damaged)return;
		for(Enemy e: em.enemys){
			if(Judge.circleInCircle(pd.p, pd.r, e.p, e.r)){
				pd.attack(e.STR,e.p);
				break;
			}
		}
	}

	public void hantei(HashMap<String,ClientData> clientData){

		ArrayList<Enemy> killList = new ArrayList<Enemy>();
		for(Enemy e: em.enemys){
			for(ClientData cd: clientData.values()){
				PlayerData pd  =cd.getLoginPlayer();
				if(cd.getLoginPlayer().stageId != stageId)continue;
				for(Bullet b: pd.bl){
					if(b.exist == 0)continue;
					if(Judge.circleInCircle(e.p,e.r, b.p, b.r)){
						e.attack(pd.STR, pd.knockback, b.p);
						b.bulletHit();
						if(e.hp <= 0){
							killList.add(e);
							cd.getLoginPlayer().giveExp(e.exp);
							moneyList.add(new Money(e.p.x, e.p.y, e.money));
						}
						break;
						
					}
				}
			}
		}
		for(Enemy e: killList){
			
			em.enemys.remove(e);
			
			em.aliveEnemyNum--;
		}
		
		killList.clear();
	}

	public boolean onMoney(Vector p, double r){
		for(Money m: moneyList){
			if( Judge.circleInCircle(p,r, m.p, 5)){
				return true;
			}
		}
		return false;
	}
	public int getMoney(Vector p, double r){
		
		while(removeMoneyLock){
			try{Thread.sleep(1);}catch(Exception e){}
		}
		for(Money m: moneyList){
			if( Judge.circleInCircle(p,r, m.p, 5)){
				int ret = m.val;
				removeMoneyList.add(m);
				return ret;
			}
		}
		return 0;
	}
	public void moneyTalk(MyByteBuffer bb){
		int len = moneyList.size();
		bb.putInt(len);
		for(Money m: moneyList){
			bb.putDouble(m.p.x).putDouble(m.p.y).putInt(m.val);
		}
		removeMoneyLock = true;
		for(Money m: removeMoneyList){
			moneyList.remove(m);
		}
		removeMoneyList.clear();
		removeMoneyLock = false;
		
	}
	
	public void talk(MyByteBuffer bb ,ClientData cd){
		em.talk(bb);
		moneyTalk(bb);
		
		PlayerData pd = cd.getLoginPlayer();
		if(pd.loadStageFlag){
			pd.loadStageFlag = false;
			bb.putInt(1);
			bb.putInt(gateList.size());//Gate num;
			for(StageGate sg: gateList){
				bb.putDouble(sg.p1.x).putDouble(sg.p1.y);
				bb.putDouble(sg.p2.x).putDouble(sg.p2.y);
			}
			bb.putInt(stageId);//stage id
			bb.putDouble(pd.nextP.x);
			bb.putDouble(pd.nextP.y);
			pd.stageId = pd.nextStageId;
			pd.p.copyFrom(pd.nextP);

			nm.talk(bb);
		}
		else{
			bb.putInt(0);
		}
	}
	abstract public void update();
	abstract public void moveStage(Vector p, int nextStageId);
}
