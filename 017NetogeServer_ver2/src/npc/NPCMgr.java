package npc;

import java.util.ArrayList;

import netogeserver.ClientData.PlayerData;
import npc.NPC.NPCType;
import apptemplate.MyByteBuffer;
import apptemplate.Vector;

public class NPCMgr {
	public enum Type{
		SHOP
	}
	private ArrayList<NPC>list = new ArrayList<NPC>();
	public NPCMgr(){
		
	}
	
	public void addNPC(int x, int y, Type t){
		NPC n = null;
		switch(t){
		case SHOP:
			n = new ShopNPC(x,y,1);
			break;
		}
		list.add(n);
	}
	
	public void shoping(int buyId, int shopId, PlayerData pd){
		for(NPC n: list){
			if( n.type == NPCType.SHOP){
				ShopNPC s = (ShopNPC)n;
				if(s.shopId == shopId){
					int val = s.getValOf(buyId);
					if(pd.money >= val){
						pd.buy(buyId, val);
						break;
					}
				}
			}
		}
	}

	public boolean isNPCNear(Vector p){
		for(NPC n: list){
			double len = p.distanceNijo(n.p);
			if(len <= 2500){
				return true;
			}
			
		}
		return false;
	}
	public NPC getNearNPC(Vector p){
		int min = 999999;
		NPC near = null;
		for(NPC n: list){
			double len = p.distanceNijo(n.p);
			if(len <= min){
				near = n;
			}
		}
		return near;
	}
	
	public void update(){
		
	}
	
	
	public void talk(MyByteBuffer bb){
		int len = list.size();
		bb.putInt(len);
		for(NPC n : list){
			n.talk(bb);
		}
		
	}
}
