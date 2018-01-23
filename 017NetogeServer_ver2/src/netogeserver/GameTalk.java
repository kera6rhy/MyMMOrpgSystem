package netogeserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import netogeserver.ClientData.Bullet;
import netogeserver.ClientData.PlayerData;
import apptemplate.MyByteBuffer;
import apptemplate.Server.SocketData;

public class GameTalk {
	private NetogeServer ns;
	private HashMap<String, ClientData> clientData;
	private ArrayList<LogData> log = new ArrayList<LogData>();
	private int logCount;
	
	
	public GameTalk(NetogeServer n, HashMap cd){
		ns = n;
		clientData = cd;
	}
	void talk(MyByteBuffer bb, SocketData socketData)throws Exception{

		String id = null;
		ClientData cd = null;
		if(socketData.readState == 0){
			socketData.switchNum2 = /*str.charAt(2) - '0'*/bb.getInt();
			id = /*str1[2]*/bb.getString();
	
			cd = clientData.get(id);
			
			
		}
		
		switch(socketData.switchNum2){
		case 0:
			if(socketData.readState == 0){
				//ここから入力系
				cd.key.talk(bb);
				int num12 = bb.getInt();
				String str3 = null;
				if(num12 == 1){
					str3 = bb.getString();
					//System.out.println(str3);
				}
				boolean f = false;
				
				boolean buyFlag = false;
				int buyId = -1;
				int buyShopId = -1;
				int useId = -1;
				boolean sendStatus = false;	
				boolean abilityUp = false;
				int abilityId = -1;
				LABEL1:{
					while(true){
						int t = bb.getInt();
						switch(t){
						case 0:
							
							break LABEL1;
						case 1://money送信依頼
							f = true;
							//faaa = bb.getInt();
							
							break;
						case 2://購入依頼
							buyFlag = true;
							buyId = bb.getInt();
							buyShopId = bb.getInt();
							break;
						case 3://アイテム使用依頼
							useId = bb.getInt();
							break;
						case 4://ステータス情報送信依頼
							sendStatus = true;
							break;
						case 5://アビリティアップ依頼
							abilityUp = true;
							abilityId = bb.getInt();
							break;
						}
					}
				}
				int lastLogId = /*Integer.parseInt(str1[14])*/bb.getInt();
				if(cd.inField == false){//初フィールド誕生時、初期ログカウントを記録
					cd.inField = true;
					cd.loginLogId = logCount;
					cd.login();
				}
				int loginLogId = cd.loginLogId;
				cd.lastLoginCount = 0;
	
				PlayerData clientPd = cd.getLoginPlayer();
				
				if(/*str1[12].length() ==1*/num12 == 0){
					//cd.chatStr = "";
				}
				else{
					//String str3 = str1[13].substring(1);
					ClientData c = clientData.get(id);
					PlayerData pd = c.getLoginPlayer();
					log.add(new LogData(str3, id, pd.name, logCount,pd.stageId));
					logCount++;
					if(log.size()>15){
						log.remove(0);
					}
					System.out.println("add log");
				}
	
				if(buyFlag == true){
					//clientPd.buy(buyId, buyShopId);
					ns.gp.sm.stageList[clientPd.stageId].nm.shoping(buyId, buyShopId, clientPd);
				}
				if(useId != -1){
					clientPd.useItem(useId);
				}
				if(abilityUp == true){
					clientPd.abilityUp(abilityId);
				}	
				
				//ここから出力系
				bb.clear();
	
				bb.putInt(1);
				int count = 0;
				
				for(ClientData c : clientData.values()){
					if(clientPd.stageId != c.getLoginPlayer().stageId)continue;
					if(c.inField == true){
						count++;
					}
				}
				bb.putInt(count);
				for(ClientData c : clientData.values()){
					if(clientPd.stageId != c.getLoginPlayer().stageId)continue;
					PlayerData pd = c.getLoginPlayer();
					if(c.inField == true){
						bb.putString(c.id).putDouble(pd.p.x).putDouble(pd.p.y).putDouble(pd.v.x).putDouble(pd.v.y);
	
						int bcount= 0;
						for(Bullet b: pd.bl){
							if(b.exist == 1){
								bcount ++ ;
							}
						}
						bb.putInt(bcount);
						for(Bullet b: pd.bl){
							if(b.exist == 1){
								//System.out.println(b.p.x+"  "  +b.v.x);
								bb.putDouble(b.p.x).putDouble(b.p.y).putDouble(b.v.x).putDouble(b.v.y);
							}
						}
						bb.putInt(pd.hp).putInt(pd.maxHp).putBoolean(pd.damaged).putInt(pd.level);
					}
				}
	
				if(clientPd.sendExpFlag == true){
					bb.putBoolean(true);
					bb.putInt(clientPd.exp).putInt(clientPd.nextExp).putInt(clientPd.level);
					clientPd.sendExpFlag = false;
				}
				else{
					bb.putBoolean(false);
				}
	
				int lastId = -1;
				count = 0;
				for(LogData lo: log){
					if( clientPd.stageId != lo.stageId){
						//continue;
					}
					else if( lo.logId <= lastLogId || lo.logId < loginLogId){
						//continue;
					}
					else{
						count++;
					}
					
				}
				bb.putInt(count); 
				
				int n = 0;
				for(LogData lo: log){
					if( clientPd.stageId != lo.stageId){
						//continue;
					}
					else if( lo.logId <= lastLogId || lo.logId < loginLogId){
						//continue;
					}
					else{
						bb.putString(lo.id).putString(lo.charaName).putString(lo.str);
						n++;
					}
					lastId = lo.logId;
				}
				//if(n!= 0)
					//System.out.println("n="+n);
				bb.putInt(lastId);
				//sendStr.append(lastId);
			
				int stageId = clientPd.nextStageId;
				ns.gp.sm.stageList[stageId].talk(bb, cd);
				
				if(f){
					bb.putInt(1);
					bb.putInt(clientPd.money);
					bb.putInt(clientPd.itemList.size());
					for(Item i: clientPd.itemList){
						bb.putInt(i.id).putInt(i.num);
					}
				}
				if(clientPd.npcTalkFlag){
	
					bb.putInt(2);
					clientPd.talkNPC.talk(bb,0);;
					clientPd.npcTalkFlag = false;
				}
				if(sendStatus == true){
					bb.putInt(3);
					bb.putInt(clientPd.STR).putInt(clientPd.DEF).putDouble(clientPd.speed).putInt(clientPd.abilityPoint);
				}
				
				bb.putInt(0);
				
				byte[]b = bb.getArray();
				socketData.printByte(b);
				socketData.flushByte();
				socketData.readState = 1;
			}
			
			//ここから再入力系ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー
			bb = socketData.readByte();
			if(bb == null){
				return ;
			}
			int num = /*Integer.parseInt(str1[0])*/bb.getInt();
			if(num == 1){
				int datasize = bb.getInt();
				ClientData c;
				for(int i=0;i</*str1.length*/datasize;i++){
					c = clientData.get(/*str1[i]*/bb.getString());
					int cid = c.loginCharaId;
					bb.putString(c.id).putString(c.playerData[cid].name).putInt(c.playerData[cid].color).putInt(c.playerData[cid].r);
				}
				byte[]b1 = bb.getArray();
				socketData.printByte(b1);;
				socketData.flushByte();
			}

			//System.out.println(str + "   lastId="+lastId );
			break;
		case 1:
			int lId = cd.loginCharaId;
			
			bb.clear();
			bb.putInt(0).putString(cd.playerData[lId].name).putInt(cd.playerData[lId].color).putInt(cd.playerData[lId].r);
			byte[]b2 = bb.getArray();
			socketData.printByte(b2);
			socketData.flushByte();
			//socketData.println("0 "+cd.playerData[lId].name+" "+cd.playerData[lId].color+" "+cd.playerData[lId].r);
			//socketData.flush();
			break;
		}
		socketData.readState = 0;
	}
}
