package titleScene;

import java.util.ArrayList;

import main.ClientAdapter;
import main.Netoge;
import apptemplate.MyByteBuffer;
import apptemplate.ProcessTemplate;

public class Title implements ProcessTemplate{
	
	String talkStr;
	private ArrayList<String>talkStrings= new ArrayList<String>();
	MyClient mc;
	Scene nowScene = Scene.LOGIN;
	public String loginId;
	Netoge netoge;
	int chengeSceneCount;
	
	LoginScene ls;
	RegisterScene rs;

	MyByteBuffer bb =  new MyByteBuffer();
	
	public Title(Netoge n){
		netoge = n;
		ls = new LoginScene(this);
		rs = new RegisterScene(this);
		mc = new MyClient(netoge.HOST);
	}

	public void finalize(){
		ls.finalize();
		rs.finalize();
	}
	
	void changeScene(Scene s){
		switch(nowScene){
		case LOGIN:
			ls.hide();
			break;
		case REGISTER:
			rs.hide();
			break;
		case START:
			
			break;
		}
		
		nowScene = s;
		switch(nowScene){
		case LOGIN:
			ls.show();
			break;
		case REGISTER:
			rs.show();
			break;
		case START:
			chengeSceneCount = 180;
			break;
		}
	}
	
	public void update(){
		switch(nowScene){
		case LOGIN:
			ls.update();
			break;
		case REGISTER:
			rs.update();
			break;
		case START:
			chengeSceneCount--;
			mc.roopEnd();
			finalize();
			netoge.chengeProcess("CharacterSelect");
			if(chengeSceneCount <= 0){
				
			}
			break;
		}
	}
	
	public void draw(){
		switch(nowScene){
		case LOGIN:
			ls.draw();
			break;
		case REGISTER:
			rs.draw();
			break;
		case START:
			//Draw.string(loginId+"でゲームを開始します", x, y, 0x000000);
			break;
		}
	}

	
	public void debugDraw(){
		
	}	
	/*
	public void setTalkString(String str){
		mc.setTalkString(str);
	}*/
	class MyClient extends ClientAdapter{
		byte[] sendByte;
		/*public void setTalkString( String str){
			talkStr = str;
			talkFlag = true;
		}*/
		MyClient(String HOST){
			super(HOST);
		}
		public void setSendByte(byte[] b){
			sendByte = b;
			talkFlag = true;
		}
		@Override
		public void talk(){
			System.out.println("send");
			switch(nowScene){
			case LOGIN:
				ls.talk(this);
				break;
			case REGISTER:
				rs.talk(this);
				break;
			}
		}
	}
	enum Scene{
		LOGIN,REGISTER,START
	}
}
