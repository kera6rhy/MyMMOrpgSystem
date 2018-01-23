package gameScene;

import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import main.ClientAdapter;
import main.Netoge;
import apptemplate.AppTemplate;
import apptemplate.Draw;
import apptemplate.MyByteBuffer;
import apptemplate.ProcessTemplate;

public class Game implements ProcessTemplate {
	Player pl;
	Player oriPl;
	HashMap<String, Player> players = new HashMap<String, Player>();
	GameClient gClient;
	Chat chat;
	Netoge netoge;
	ArrayList<String> log = new ArrayList<String>();
	ArrayList<String> chatStrStack = new ArrayList<String>();
	//MyByteBuffer bb =  new MyByteBuffer();
	EnemyMgr em = new EnemyMgr();
	Stage stage = new Stage();
	int lastLogId;
	//String btStr = "";
	String resBtStr;
	boolean resBtStrFlag;
	int sendInterval = 1;
	int sendIntervalCount = 0;
	Button retCharaBt;
	Menu menu = new Menu(this);
	
	public Game(Netoge n){
		pl = new Player(n.loginId);
		netoge = n;
		gClient = new GameClient(netoge.HOST);
		chat = new Chat(this);
		//gClient.setTalkString("3 1 "+netoge.loginId);
		MyByteBuffer bb =  new MyByteBuffer();
		bb.clear();
		bb.putInt(3).putInt(1).putString(netoge.loginId);
		gClient.setTalkBuf(bb);
		while(resBtStrFlag == false){
			try{
				Thread.sleep(16);
			}catch(Exception ex){}
		}
		resBtStrFlag = false;
		//String str[] = resBtStr.split(" ");
		//pl.name = str[1];
		//pl.color = Integer.parseInt(str[2]);
		//pl.r = Integer.parseInt(str[3]);
		retCharaBt = new Button("キャラ選択画面に戻る");
		retCharaBt.setLocation(680, 400);
		retCharaBt.setSize(80,40);
		retCharaBt.setBackground(Color.white);
		retCharaBt.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e){
				myFinalize();
			}
		});

		AppTemplate.addComponent(retCharaBt);
	}
	

	public void myFinalize(){
		gClient.roopEnd();
		AppTemplate.removeComponent(retCharaBt);
		netoge.chengeProcess("CharacterSelect");
	}
	
	public void addChatLog(String str){
		log.add(str);	
		if(log.size() >= 15){
			log.remove(0);
		}
	}
	                 
	
	public void setChatStr(String str){
		pl.chatStr = str;
		pl.talkStr = pl.chatStr;
		pl.chatStrCount = 0;							
	}
	
	public void setChatStr(String id, String str){
		Player p = players.get(id);
		p.chatStr = str;
		p.chatStrCount = 0; 
	}
	
	public void update(){
		//pl.chatStr = chat.talkStr;
		pl.update();
		chat.update();
		em.update();
		for(Player p : players.values()){
			if(p.id.equals(netoge.loginId)){
				pl.hp = p.hp;
				oriPl = p;
			}
			p.otherPlayerUpdate();
		}
		if(pl.hp <= 0){
			retCharaBt.setVisible(true);
		}
		/*
		if(btStr.equals("") == false){
			gClient.setTalkString(btStr);
			btStr = "";
		}
		else{*/
		menu.update();

		MyByteBuffer bb =  new MyByteBuffer();
		pl.talk(this,bb);
		menu.talk(bb);
		bb.putInt(0);
		gClient.setTalkBuf(bb);
		//}
	}
	
	public void draw(){

		stage.draw();
		em.draw();
		///pl.draw();
		for(Player p: players.values()){
			//if(p.id.equals(netoge.loginId) == false){
				p.draw();
			//}
				
		}
		chat.draw();
		int logNum = log.size();
		for(int i=0; i<log.size();i++){
			Draw.string(log.get(i), 0, 400-20*(logNum-i), 0x000000);
		}
		if(pl.hp <= 0){
			Draw.string("あなたは死にました", 200, 100,0 );
		}
		menu.draw();
	}
	
	public void debugDraw(){
		
	}
	class GameClient extends ClientAdapter{
		private String talkStr;
		private byte[] talkByte;
		ArrayList<String> newId = new ArrayList<String>();
		private MyByteBuffer buf;// =  new MyByteBuffer();
		private boolean bufLock = false;
		//private MyByteBuffer copyBuf = new MyByteBuffer();
		/*
		public void setTalkString(String s){
			chatStrStack.add(s);
		}*/
		GameClient(String HOST){
			super(HOST);
		}
		public void setTalkBuf(MyByteBuffer b){
			while(bufLock){
				try{
					Thread.sleep(1);
				}catch(Exception r){}
			}
			buf = b;
			talkFlag = true;
		}
		
		@Override
		public void update(){
			if(chatStrStack.size() >0 ){
				talkFlag = true;
				talkStr = chatStrStack.get(0);
				chatStrStack.remove(0);
			}
			newId.clear();
		}
		
		@Override
		public void talk()throws Exception{
			//buf.copyFrom(copyBuf);
			bufLock = true;
			//System.out.print(buf.getDataSize()+" "+ buf.getDataCount()+" "+buf.getListSize() +" ");
			synchronized(buf){
				buf.putInt(lastLogId);
				//System.out.print(buf.getDataSize()+" "+buf.getDataCount()+" "+buf.getListSize() +" ");
				//System.out.println("id="+lastLogId);
				byte[] array = buf.getArray();
				bufLock = false;
				//System.out.print(array.length+"byte  ");
				printByte(array);
				flushByte();
				/*
				for(int i=0;i<array.length;i++){
					System.out.print(array[i]);
				}
				System.out.println("");
				*/
			}
			buf = null;
		//	System.out.println("");
			//println(talkStr+" "+lastLogId);//lastLogIdは、このスレッドで追加しないと同期されないから仕方なく
			//flush();
			
			//String tmp[] = talkStr.split(" ");
			
			//System.out.println("send:"+talkStr);
			
			//String str = readLine();
			//String str1[] = str.split(" ");
			MyByteBuffer bb = readByte();
			
			int num = /*Integer.parseInt(str1[0])*/ bb.getInt();
			switch(num){
			case 0:
			//	resBtStr = str;

				pl.name = /*str[1]*/bb.getString();;
				pl.color = /*Integer.parseInt(str[2])*/bb.getInt();
				pl.r = /*Integer.parseInt(str[3])*/bb.getInt();
				resBtStrFlag = true;
				break;
			case 1:
				//String str2 = readLine();

				//System.out.println(str);
		
				int len = /*str1.length*/bb.getInt();
				for(Player p: players.values()){
					p.isJustExist = false;
				}
				
				for(int ii=0; ii<len;ii++){
					//int i=ii-1;
					//System.out.print("1");
					//if(i%10 == 0){
						String id = bb.getString();
						Player p = players.get(/*str1[ii]*/id);
						if(p == null){
							p = new Player(/*str1[ii]*/id);
							p.point.x = /*Integer.parseInt(str1[ii+1])*/bb.getDouble();
							p.point.y = /*Integer.parseInt(str1[ii+2])*/bb.getDouble();
							p.a.x = /*Integer.parseInt(str1[ii+3])/100.0*/bb.getDouble();//少数をはしょるるために１００かけて送ったから、
							p.a.y = /*Integer.parseInt(str1[ii+4])/100.0*/bb.getDouble();
							int blSize = bb.getInt();
							for(int i=0;i<blSize;i++){	
								p.bl[i].p.x = /*Integer.parseInt(str1[ii+5])*/bb.getDouble();
								p.bl[i].p.y = /*Integer.parseInt(str1[ii+6])*/bb.getDouble();
								p.bl[i].v.x = /*Integer.parseInt(str1[ii+7])/100.0*/bb.getDouble();
								p.bl[i].v.y = /*Integer.parseInt(str1[ii+8])/100.0*/bb.getDouble();
								p.bl[i].exist = true;
							}
							for(int i=blSize; i<10;i++){
								p.bl[i].exist = false;
							}
							//int tt = /*Integer.parseInt(str1[ii+9])*/bb.getInt();
							
							p.hp = bb.getInt();
							p.maxHp = bb.getInt();
							p.damaged = bb.getBoolean();
							p.level = bb.getInt();
							//p.bl.exist = ((tt==0)?false:true);
							newId.add(/*str1[ii]*/id);
							
							players.put(/*str1[ii]*/id, p);
							if(id.equals(netoge.loginId)){
								oriPl = p;
							}
						}
						else{
							p.point.x = /*Integer.parseInt(str1[ii+1])*/bb.getDouble();
							p.point.y = /*Integer.parseInt(str1[ii+2])*/bb.getDouble();
							p.a.x = /*Integer.parseInt(str1[ii+3])/100.0*/bb.getDouble();
							p.a.y = /*Integer.parseInt(str1[ii+4])/100.0*/bb.getDouble();
							int blSize = bb.getInt();
							for(int i=0;i<blSize;i++){	
								p.bl[i].p.x = /*Integer.parseInt(str1[ii+5])*/bb.getDouble();
								p.bl[i].p.y = /*Integer.parseInt(str1[ii+6])*/bb.getDouble();
								p.bl[i].v.x = /*Integer.parseInt(str1[ii+7])/100.0*/bb.getDouble();
								p.bl[i].v.y = /*Integer.parseInt(str1[ii+8])/100.0*/bb.getDouble();
								p.bl[i].exist = true;
							}
							for(int i=blSize; i<10;i++){
								p.bl[i].exist = false;
							}
							p.hp = bb.getInt();
							p.maxHp = bb.getInt();
							p.damaged = bb.getBoolean();
							p.level = bb.getInt();
							//p.bl.exist = ((tt==0)?false:true);
						}
						p.isJustExist = true;
					//}
				}

				boolean isGetExp = bb.getBoolean();
				if(isGetExp){
					oriPl.exp = bb.getInt();
					oriPl.nextExp = bb.getInt();
					oriPl.level = bb.getInt();
					
				}

				//oriPl.money = bb.getInt();
				
				//log.clear();
				//str1 = str2.split(" ");
				int size = bb.getInt();
				//System.out.println("size="+size);;
				for(int i=0;i</*str1.length-1*/size;i++){
					String s0 = bb.getString();
					String s1 = bb.getString();
					String s2 = bb.getString();
					//System.out.println(s0+" "+s1+" "+s2);
					//System.out.println("res2:"+str2+" i="+i+" str1.length="+str1.length);
					addChatLog("["+/*str1[i+1]*/s1 +"]"+/*str1[i+2]*/s2);
					setChatStr(/*str1[i]*/s0, /*str1[i+2]*/s2);
				}
				//System.out.println("end");
				
				lastLogId = /*Integer.parseInt(str1[str1.length-1])*/bb.getInt();
				
				int enemyLen = bb.getInt();
				em.waitLock();
				em.newEnemyLock = true;
				em.newEnemys.clear();
				for(int i=0; i<enemyLen;i++){
					Enemy e = new Enemy();
					e.p.x = bb.getDouble();
					e.p.y = bb.getDouble();
					e.color = bb.getInt();
					e.r = bb.getDouble();
					em.newEnemys.add(e);
				}
				em.newEnemyLock = false;
				
				stage.talk(bb,pl);
				LABEL1:{
					while(true){
						int tmp = bb.getInt();
						switch(tmp){
						case 0:
							break LABEL1;
						case 1:
							oriPl.money = bb.getInt();
							menu.talkRes(bb);
							break;
						case 2:
							menu.shopTalkRes(bb);
							break;
						case 3:
							oriPl.STR = bb.getInt();
							oriPl.DEF = bb.getInt();
							oriPl.speed = bb.getDouble();
							oriPl.abilityPoint = bb.getInt();
							break;
						}
					}
				}
				
				if(newId.size() > 0){
					//StringBuilder sb = new StringBuilder("1 ");
					bb.clear();
					bb.putInt(1);
					bb.putInt(newId.size());
					for(String s: newId){
						//sb.append(s);
					//	sb.append(" ");
						bb.putString(s);
					}
					byte[]b = bb.getArray();
					printByte(b);
					flushByte();
					//println(sb.toString());
					//flush();
					//str = readLine();
					//str1 = str.split(" ");

					bb = readByte();
					Player p;
					for(int i=0;i</*str1.length*/newId.size();i++){
						//String tmp = bb.getString();
					//	System.out.println(tmp);
						p = players.get(/*str1[i]*/bb.getString());
						p.name = /*str1[i+1]*/bb.getString();
						p.color = /*Integer.parseInt(str1[i+2])*/bb.getInt();;
						p.r = /*Integer.parseInt(str1[i+3])*/bb.getInt();;
					}
				}
				else{
					bb.clear();
					bb.putInt(0);
					byte[]b = bb.getArray();
					printByte(b);
					flushByte();
					//println("0");
					//flush();
				}
				
				ArrayList<Player> removeList = new ArrayList<Player>();
				for(Player p: players.values()){
					if(p.isJustExist == false){
						removeList.add(p);
					}
				}
				for(Player p: removeList){
					players.remove(p.id);
				}
				
				
				//System.out.println("llId="+lastLogId+"  str1[]="+str1[str1.length-1]+" str1.length-1="+(str1.length-1));
				//lastLogId = Integer.parseInt(str1[str1.length - 2]);
				break;
			}
			
		}
	}
	
	
}