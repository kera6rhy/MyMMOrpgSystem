package netogeserver;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import npc.NPC;
import weapon.Weapon;
import weapon.Weapon1;
import apptemplate.Vector;

public class ClientData {
	public static final int TIMEUP = 600;
	String pw;
	String id;
	boolean isLogin;
	int lastLoginCount;
	boolean inField;
	int chatCount;
	int loginLogId;
	PlayerData playerData[] = new PlayerData[3];
	int playerDataCount;
	int loginCharaId;
	
	
	Key2 key = new Key2();
	
	public void addPlayerData(String n, int c, int r){
		playerData[playerDataCount] = new PlayerData();;
		playerData[playerDataCount].name = n;
		playerData[playerDataCount].color = c;
		playerData[playerDataCount].r = r;
		playerData[playerDataCount].hp= 30;//ここで初期化しないと死亡時すぐ復活的な感じになるから

		inField = false;
		playerDataCount++;
	}
	
	public PlayerData getLoginPlayer(){
		return playerData[loginCharaId];
		
	}
	
	public boolean isContainChara(String name){
		for(PlayerData pd: playerData){
			if(pd.name.equals(name)){
				return true;
			}
		}
		return false;
	}
	public void deleteChara(int id){

		playerData[id].name = "";
		playerData[id].color = 0;
		playerData[id].r = 0;
		
		playerDataCount--;
	}
	public ClientData(String str1, String str2){
		id = str1;
		pw = str2;
		for(int i=0;i<3;i++){
			playerData[i] = new PlayerData();
		}
	}
	
	
	public void login(){

		playerData[loginCharaId].loadStageFlag = true;
		playerData[loginCharaId].nextP.copyFrom(playerData[loginCharaId].p);
		playerData[loginCharaId].sendExpFlag = true;
	}
	
	
	
	public void logout(){
		isLogin = false;
		inField = false;
	}
	public void update(){
		key.update();
		playerData[loginCharaId].update();

		chatCount++;
		lastLoginCount++;
		if(lastLoginCount >= TIMEUP){
			lastLoginCount = 0;
			logout();
			System.out.println("["+id+"]との接続がタイムアウトしました");
		}
	}
	
	public class PlayerData{
		static final int BULLET_NUM = 10;
		String name = "";
		int color;
		public int r;
		public Vector p = new Vector(768/2, 480/2);
		Vector v = new Vector();
		public Bullet bl[] = new Bullet[BULLET_NUM];
		public Vector nextP = new Vector();
		public String chatStr = "";
		public Weapon weapon = new Weapon1();
		public int stageId = 1;
		public int nextStageId = 1;
		double speed = 0.6;
		int attackCount = 0;
		public boolean loadStageFlag = false;
		int hp = 30;
		int maxHp = 30;
		int level = 1;
		int exp = 0;
		int nextExp = 5;
		public int money = 0;
		int STR = 3;
		int DEF = 0;
		boolean npcTalkFlag ;
		int abilityPoint = 0;
		int knockback = 5;
		NPC talkNPC;
		ArrayList<Item> itemList = new ArrayList<Item>();		
		
		public boolean damaged = false;
		private int damageCount = 0;

		boolean sendExpFlag = true;
		public PlayerData(){
			for(int i=0;i<bl.length;i++){
				bl[i] = new Bullet();
			}
		}
		
		public void update(){
			if(hp <= 0 && name.equals("") == false){
				for(Bullet b: bl){
					b.exist = 0;
				}
				name = "";
				deleteChara(loginCharaId);
				for(int i=loginCharaId; i<2;i++){
					playerData[i].swap(playerData[i+1]);
				}
				return ;
			}
			if(hp <= 0)return ;
			if(key.getKeyCount(KeyEvent.VK_UP) >= 1){
				v.y-=speed;
			}
			if(key.getKeyCount(KeyEvent.VK_DOWN) >= 1){
				v.y+=speed;
			}
			if(key.getKeyCount(KeyEvent.VK_LEFT) >= 1){
				v.x-=speed;
			}
			if(key.getKeyCount(KeyEvent.VK_RIGHT) >= 1){
				v.x+=speed;
			}
			
			if(key.getKeyCount(KeyEvent.VK_X) == 1){
				if(attackCount == 0){
					attackCount = 1;
					Bullet fire = null;
					for(Bullet b: bl){
						if(b.exist ==0){
							fire = b;
							break;
						}
					}
					if(fire != null){
						fire.fire(p, v, weapon);
					}
				}
			}
			

			p.addV(v);
			v.mulV(0.7);
			for(Bullet b:bl){
				b.update();
			}
			if(p.x <0)p.x =0;
			if(p.x > 768)p.x=768;
			if(p.y<0)p.y=0;
			if(p.y>480)p.y=480;
			
			
			if(attackCount >= 1){
				attackCount++;
				if(attackCount == weapon.fireInterval){
					attackCount = 0;
				}
			}
			if(chatCount >=300){
				chatCount = 0;
				chatStr = "";
			}
			
			if(damaged){
				damageCount++;
				if(damageCount > 60){
					damaged = false;
					damageCount = 0;
				}
			}
		}	
		
		public void abilityUp(int id){
			if(abilityPoint == 0)return;
			abilityPoint--;
			switch(id){
			case 0:
				maxHp += 5;
				break;
			case 1:
				STR += 1;
				break;
			case 2:
				DEF += 1;
				break;
			case 3:
				speed += 0.1;
				break;
			}
		}
		public void useItem(int id){
			for(Item i:itemList){
				if(i.id == id && i.num >0){
					i.use(this);
					i.num--;
				}
			}
		}
		public void attack(int damage, Vector ep){
			int tmp = damage-DEF;
			if(tmp < 1){
				tmp = 1;
			}
			hp-= tmp;
			damaged = true;
			Vector tv = new Vector();
			tv.copyFrom(p);;
			tv.subV(ep);
			tv.transEV();
			tv.mulV(damage*3);
			v.addV(tv);;
		}
		
		public void swap(PlayerData pd){
			String tmps = name;
			name = pd.name;
			pd.name = tmps;
			int tmp = color;
			color = pd.color;
			pd.color = tmp;
			tmp = r;
			r = pd.r;
			pd.r = tmp;
		}
		public void giveExp(int e){
			exp += e;
			sendExpFlag = true;
			if(exp >= nextExp){
				levelup();
			}	
		}
		
		public void levelup(){
			int tmp = exp - nextExp;
			exp = tmp;
			level ++;
			nextExp *= 1.5;
			abilityPoint += 2;
		}
		
		public void buy(int buyId, int val){
			money -= val;
			for(Item i: itemList){
				if(i.id == buyId){
					i.num++;
					return;
				}
			}
			itemList.add(new Item(buyId));
		}
	}
	
	public class Bullet{
		public Vector p = new Vector();
		Vector v = new Vector();
		public double r = 3;
		public int exist = 0;
		int time = 0;
		int deadTime = 0;

		public void fire(Vector ap, Vector av,Weapon w){
			p.x = ap.x;
			p.y = ap.y;
			v = Vector.getEV(av);
			v.mulV(w.speed+av.length());
			exist  = 1;
			time = 0;
			deadTime = (int)(w.dist / (w.speed)) + 1;
			r = w.r;
		}
		public void bulletHit(){
			exist = 0;
		}
		public void update(){
			if(exist==0)return;
			time++;
			p.addV(v);
			if(time == deadTime){
				exist = 0;
			}
		}
	}
}
