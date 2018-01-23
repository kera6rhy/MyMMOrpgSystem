package titleScene;

import java.util.ArrayList;

import titleScene.CharacterSelectScene.CharaPanel;
import main.ClientAdapter;
import main.Netoge;
import apptemplate.AppTemplate;
import apptemplate.Draw;
import apptemplate.MyByteBuffer;
import apptemplate.ProcessTemplate;

public class CharacterSelect implements ProcessTemplate {
	static final int CHARA_PANEL_NUM = 3;
	MyClient mc;
	Netoge netoge;
	String btStr = "";
	String resBtStr= "";
	boolean resBtStrFlag;
	ArrayList<String> chatStrStack = new ArrayList<String>();
	int startCount;
	int charaCount;
	int lastActivePanelId=-1;

	PlayerData data[] = new PlayerData[3];
	CharacterSelectScene css;
	CharacterMakeScene cms;
	int timeoutIntervalVal = 300;
	int timeoutIntervalCount = 0;
	MyByteBuffer bb =  new MyByteBuffer();
	
	Scene scene = Scene.SELECT;
	public CharacterSelect(Netoge n){
		netoge = n;

		for(int i=0;i<CHARA_PANEL_NUM;i++){
			data[i] = new PlayerData();
		}
		mc = new MyClient(netoge.HOST);

		//mc.setSendStr("4 2 "+netoge.loginId);
		bb.clear();
		bb.putInt(4).putInt(2).putString(netoge.loginId);
		byte[] b = bb.getArray();
		mc.setSendByte(b);
		
		
		while(resBtStrFlag == false){
			try{
				Thread.sleep(16);
			}catch(Exception ex){}
		}
		resBtStrFlag = false;
		css = new CharacterSelectScene(this);
		cms = new CharacterMakeScene(this);
		
		
	}
	
	
	void deleteCharacter(int charId){
		data[charId].charaDelete();
		for(int i=charId;i<CHARA_PANEL_NUM-1;i++){
			data[i].swap(data[i+1]);
		}
		charaCount--;
	}
	
	
	public void changeScene(Scene s){
		switch(scene){
		case SELECT:
			css.hide();
			break;
		case MAKE:
			cms.hide();
			break;
		}
		
		scene = s;
		
		switch(scene){
		case SELECT:
			css.show();
			break;
		case MAKE:
			cms.show();
			break;
		case START:
			startCount = 180;
			break;
		}
	}
	public void update(){
		switch(scene){
		case SELECT:
			css.update();
			break;
		case MAKE:
			cms.update();
			break;
		case START:
			startCount--;
			if(startCount <= 0){
				finalize();
				netoge.chengeProcess("Game");
			}
			break;
		}
		if(btStr.equals("") == false){
			//mc.setSendStr(btStr);
			btStr = "";
		}
		else{
			if(timeoutIntervalCount % timeoutIntervalVal == 1){
				//mc.setSendStr("4 0 "+netoge.loginId);
				bb.clear();
				bb.putInt(4).putInt(0).putString(netoge.loginId);
				byte[] b = bb.getArray();
				mc.setSendByte(b);
			}

			timeoutIntervalCount++;
		}
	}
	
	public void finalize(){
		mc.roopEnd();
		css.finalize();
		cms.finalize();

	}
	public void draw(){
		switch(scene){
		case SELECT:
			css.draw();
			break;
		case MAKE:
			cms.draw();
			break;
		case START:
			Draw.string(data[lastActivePanelId].name+"でゲームを開始します", 100, 100, 0x000000);
			break;
		}
	}
	
	public void debugDraw(){
		
	}
	
	class MyClient extends ClientAdapter{
		String sendStr;
		byte[] sendByte;
		
		/*
		public void setSendStr(String s){
			chatStrStack.add(s);
		}*/
		MyClient(String HOST){
			super(HOST);
		}
		public void setSendByte(byte[] b){
			sendByte = b;
			talkFlag = true;
		}
		
		@Override
		public void update(){
			if( chatStrStack.size() > 0 ){
				talkFlag = true;
				sendStr = chatStrStack.get(0);
				chatStrStack.remove(0);

			}
		}
		@Override
		public void talk(){
			printByte(sendByte);
			flushByte();
			//println(sendStr);
			//flush();
			String str = readLine();
			String str1[] = str.split(" ");
			int num = Integer.parseInt(str1[0]);
			switch(num){
			case 0:
				
				break;
			case 1:
				int tmp = Integer.parseInt(str1[1]);
				if( tmp == 0){
					resBtStr = "failed";
					resBtStrFlag = true;
				}
				else if(tmp==1){
					resBtStr = "success";
					resBtStrFlag = true;
				}
				break;
			case 2:
				int dataNum = str1.length-1;
				for(int i=0; i<dataNum/3;i++){
					data[i].name = str1[i*3+1];
					data[i].color = Integer.parseInt(str1[i*3+2]);
					data[i].r = Integer.parseInt(str1[i*3+3]);
					data[i].exist = true;
					charaCount++;
				}
				resBtStrFlag = true;
				break;
			case 3:
				resBtStrFlag = true;
				break;
			case 4:
				resBtStrFlag = true;
				break;
			}
			
			
		}
	}
	class PlayerData{
		int r;
		int color;
		String name;
		boolean exist;
		
		public void swap(PlayerData pd){
			int tmp;
			tmp = pd.r;
			pd.r = r;
			r = tmp;
			tmp = pd.color;
			pd.color = color;
			color = tmp;
			String tmps = pd.name;
			pd.name = name;
			name = tmps;
			boolean tmpb = pd.exist;
			pd.exist = exist;
			exist = tmpb;
		}
		public void charaDelete(){
			r = 0;
			color = 0x000000;
			name = "";
			exist = false;
		}
		public void draw(int x, int y){
			if(exist == false){
				return;
			}
			Draw.string(name, x, y-30, 0x000000);
			Draw.fillOval(x, y, r,color);
		}	
	}
	enum Scene{
		SELECT, MAKE,START
	}
}
