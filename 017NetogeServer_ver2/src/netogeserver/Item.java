package netogeserver;

import netogeserver.ClientData.PlayerData;

public class Item {
	int id;
	int num;
	public Item(int i){
		id = i;
		num = 1;
	}
	
	public void use(PlayerData pd){
		switch(id){
		case 0:
			pd.hp += 5;
			if(pd.hp > pd.maxHp){
				pd.hp = pd.maxHp;
			}
			break;
		}
	}
}
