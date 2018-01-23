package npc;

import java.util.ArrayList;

import apptemplate.MyByteBuffer;

public class ShopNPC extends NPC{
	int shopId = 0;
	ArrayList<Goods> goodsList = new ArrayList<Goods>();
	
	public ShopNPC(int x, int y,int i){
		super(x,y,NPCType.SHOP);
		goodsList.add(new Goods(0,5));
		shopId = i;
	}

	@Override public void talk(MyByteBuffer bb){
		bb.putInt(0);//NPC type ID
		bb.putDouble(p.x).putDouble(p.y).putDouble(10).putInt(color);
	}
	@Override public void talk(MyByteBuffer bb, int i){
		switch(i){
		case 0:
			bb.putInt(shopId);
			bb.putInt(goodsList.size());
			for(Goods g: goodsList){
				bb.putInt(g.id).putInt(g.val);
			}
			break;
		}
	}
	
	public int getValOf(int id){
		for(Goods g: goodsList){
			if(g.id == id){
				return g.val;
			}
		}	
		return 999999999;
	}
	
	class Goods{
		int id;
		int val;
		public Goods(int i, int v){
			id = i;
			val = v;
		}
	}
}
