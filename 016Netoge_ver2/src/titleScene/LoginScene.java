package titleScene;

import java.awt.Button;
import java.awt.Color;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import titleScene.Title.MyClient;
import apptemplate.AppTemplate;
import apptemplate.Draw;


public class LoginScene{
	boolean isFirstTurn = true;
	int x,y;
	TextField idTextField;
	TextField pwTextField;
	Button logginButton;
	Button registerButton;
	int successRegCount;
	int messageCount;
	int failedLoginCount;
	boolean isGetResponse;
	String message;
	Title title;
	int time = 0;

	public LoginScene(Title t){
		title = t;
		x=300;
		y=200;
		idTextField = (new TextField(200));
		idTextField.setLocation(x+0,y+0);
		idTextField.setSize(100,20);
		pwTextField = new TextField();
		pwTextField.setLocation(x+0,y+20);
		pwTextField.setSize(100,20);
		logginButton = new Button("ログイン");
		logginButton.setSize(80,40);
		logginButton.setLocation(x+100,y+0);
		logginButton.setBackground(Color.white);
		logginButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(idTextField.getText().equals("") || pwTextField.getText().equals("")){
					return;
				}
				//title.setTalkString("0 "+idTextField.getText() + " " + pwTextField.getText());
				title.bb.clear();
				title.bb.putInt(0).putString(idTextField.getText()).putString(pwTextField.getText());
				byte[] b = title.bb.getArray();
				title.mc.setSendByte(b);
				isGetResponse = false;
			}
		});
		registerButton = new Button("会員登録");
		registerButton.setSize(80,40);
		registerButton.setLocation(x+30, y+100);
		registerButton.setBackground(Color.white);
		registerButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				title.changeScene(Title.Scene.REGISTER);
			}
		});

		AppTemplate.addComponent(idTextField);
		AppTemplate.addComponent(logginButton);
		AppTemplate.addComponent(pwTextField);
		AppTemplate.addComponent(registerButton);
	}

	public void finalize(){
		AppTemplate.removeComponent(idTextField);
		AppTemplate.removeComponent(logginButton);
		AppTemplate.removeComponent(pwTextField);
		AppTemplate.removeComponent(registerButton);
	}

	public void draw(){

		if(isFirstTurn){//コンストラクタに書くとWindow生成した瞬間に先走って表示されるからここで追加
			isFirstTurn = false;
			idTextField.setVisible(true);
			logginButton.setVisible(true);
			pwTextField.setVisible(true);
			registerButton.setVisible(true);
		}
		Draw.string("ID", x-20,y+3,0x000000);
		Draw.string("PW", x-23, y+23, 0x000000);
		if(successRegCount > 0){
			successRegCount--;
			Draw.string("会員登録が完了しました",x-23, y+50,0x000000);
		}
		if(failedLoginCount > 0){
			failedLoginCount--;
			Draw.string("IDまたはPWが違います", x-23, y+43, 0x000000);
		}
		if(messageCount > 0){
			messageCount--;
			Draw.string(message, x-23, y+50, 0x000000);
		}
	}

	public void update(){
		time++;
		if(time % 300 == 0){
			System.out.println("time==300");
			title.bb.clear();
			title.bb.putInt(10000);
			byte[] b = title.bb.getArray();
			title.mc.setSendByte(b);
		}
	}
	public void show(){
		idTextField.setVisible(true);
		logginButton.setVisible(true);
		pwTextField.setVisible(true);
		registerButton.setVisible(true);
	}

	public void hide(){
		idTextField.setVisible(false);
		logginButton.setVisible(false);
		pwTextField.setVisible(false);
		registerButton.setVisible(false);
		successRegCount = 0;
		failedLoginCount = 0;
		idTextField.setText("");
		pwTextField.setText("");
	}

	public void talk(MyClient cl){
		String str;
		int num;

		System.out.println("a");
		cl.printByte(cl.sendByte);
		cl.flushByte();
		System.out.println("b");

		//cl.println(title.talkStr);
		//cl.flush();
		str  = cl.readLine();
		num = str.charAt(0) - '0';
		switch(num){
		case 0:
			failedLoginCount = 180;
			break;
		case 1:
			title.loginId = idTextField.getText();
			title.changeScene(Title.Scene.START);
			break;
		case 2:
			message = "このIDはすでにログインしています";
			messageCount = 180;
			break;
		}
		isGetResponse = true;

	}
}
