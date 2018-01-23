package netogeserver;

import java.util.HashMap;

import apptemplate.FrameRate;
import apptemplate.MyByteBuffer;
import apptemplate.Server;


public class NetogeServer extends Server{
	public static final int PORT = 50020;
	public HashMap<String, ClientData>clientData = new HashMap<String, ClientData>();
	private GameTalk gt;
	MyByteBuffer bb = new MyByteBuffer();
	GameProcess gp = new GameProcess(this);
	
	public NetogeServer(){
		super(PORT);
		gt = new GameTalk(this, clientData);
		mainLoop();
	}
	
	@Override
	public void talk(SocketData socketData)throws Exception{
		
		//String str = socketData.readLine();
		
		MyByteBuffer bb = null;
		if(socketData.readState == 0){
			bb= socketData.readByte();
			if(bb == null){
				return;
			}
			
			socketData.switchNum1 = bb.getInt();
		}
		String str1;
		String str2;
		int num = 0;
		
		switch(socketData.switchNum1){
		case 0:
			//str1 = str.split(" ", 3);
			str1 = bb.getString();
			str2 = bb.getString();
			if(clientData.get(/*str1[1]*/str1) == null){
				socketData.println("0");
				socketData.flush();
			}
			else{
				if(/*str1[2]*/str2.equals(clientData.get(/*str1[1]*/str1).pw)== true){
					if(clientData.get(/*str1[1]*/str1).isLogin == true){
						socketData.println("2");
						socketData.flush();
					}
					else{
						socketData.println("1");
						socketData.flush();
						clientData.get(/*str1[1]*/str1).isLogin = true;
						System.out.println("ID["+/*str1[1]*/str1+"]がログインしました");
					}
				}
				else{
					socketData.println("0");
					socketData.flush();
				}
			}
			break;
		case 1:
			//str1 = str.split(" ", 3);
			str1 = bb.getString();
			str2 = bb.getString();
			if(clientData.get(/*str1[1]*/str1) == null){
				clientData.put(/*str1[1]*/str1, new ClientData(/*str1[1]*/str1, /*str1[2]*/str2));
				socketData.println("0");
				socketData.flush();
				System.out.println("ID["+/*str1[1]*/str1+"]が登録されました");
			}
			else{
				socketData.println("1");
				socketData.flush();
			}
			break;
		case 3:
			gt.talk(/*str*/bb,socketData);
			break;
		case 4:
			//str1 = str.split(" ");
			//num = Integer.parseInt(str1[1]);
			num = bb.getInt();
			String id = /*str1[2]*/ bb.getString();
			ClientData cd = clientData.get(id);
			String str3;
			switch(num){
			case 0:
				cd.lastLoginCount = 0;
				socketData.println("0");
				socketData.flush();
				break;
			case 1:
				str3 = bb.getString();
				if(cd.isContainChara(/*str1[3]*/str3)){
					socketData.println("1 0");
					socketData.flush();
				}
				else{
					socketData.println("1 1");
					socketData.flush();
					cd.addPlayerData(/*str1[3]*/str3,/*Integer.parseInt(str1[4])*/bb.getInt(), /*Integer.parseInt(str1[5])*/bb.getInt());
				}
				break;
			case 2:
				StringBuilder sb = new StringBuilder("2 ");
				for(int i=0;i<cd.playerDataCount; i++){
					sb.append(cd.playerData[i].name+" "+cd.playerData[i].color+" "+cd.playerData[i].r+" ");
				}
				socketData.println(sb.toString());
				socketData.flush();
				break;
			case 3:
				int num3 = bb.getInt();
				int deleteNum = /*Integer.parseInt(str1[3])*/num3;
				cd.deleteChara(deleteNum);
				for(int i=deleteNum; i<2;i++){
					cd.playerData[i].swap(cd.playerData[i+1]);
				}
				socketData.println("3");
				socketData.flush();
				break;
			case 4:
				int num33 = bb.getInt();
				int charaId = /*Integer.parseInt(str1[3])*/num33;
				cd.loginCharaId = charaId;
				socketData.println("4");
				socketData.flush();
				break;
			}
			break;
		case 10000:
			socketData.println("9");
			socketData.flush();
			break;
		}
		if(socketData.switchNum1 != 3){
			socketData.readState = 0;
		}
	}
	
	public void update(){
		for(ClientData cd: clientData.values()){
			if(cd.isLogin == true){
				cd.update();
			}
		}
		gp.update();
	}
	
	public void mainLoop(){
		FrameRate fr = new FrameRate();
		while(true){
			fr.update();
			update();
		}	
	}
	public static void main(String args[]){
		System.out.println("起動");
		new NetogeServer();
		
	}
}
 
