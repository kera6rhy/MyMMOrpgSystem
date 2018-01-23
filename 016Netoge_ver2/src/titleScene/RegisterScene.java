package titleScene;

import java.awt.Button;
import java.awt.Color;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import titleScene.Title.MyClient;
import apptemplate.AppTemplate;
import apptemplate.Draw;

public class RegisterScene {
	int x,y;
	TextField regIdTxtF;
	TextField regPwTxtF;
	Button regApplyButton;
	Button regCancelButton;
	boolean successRegFlag;
	boolean failedRegFlag;
	int failedRegCount;
	int successRegCount;
	boolean regEndFlag;
	Title title;
	int time = 0;
	public RegisterScene(Title t){
		title = t;
		x=300;
		y=200;
		regIdTxtF = new TextField();
		regIdTxtF.setLocation(x+0, y+0);
		regIdTxtF.setSize(100,20);
		regPwTxtF = new TextField();
		regPwTxtF.setLocation(x+0,y+20);
		regPwTxtF.setSize(100,20);
		regApplyButton = new Button("登録");
		regApplyButton.setSize(80,40);
		regApplyButton.setLocation(x-30,y+100);
		regApplyButton.setBackground(Color.white);
		regApplyButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//System.out.println("asdf");
				if(regIdTxtF.getText().equals("") || regPwTxtF.getText().equals("")){
					return;
				}
				//System.out.println("asdf");
				//title.setTalkString( "1 "+   regIdTxtF.getText() + " "    + regPwTxtF.getText());

				System.out.println("1");
				title.bb.clear();
				title.bb.putInt(1).putString(regIdTxtF.getText()).putString(regPwTxtF.getText());
				byte[] b = title.bb.getArray();
				System.out.println("2");
				title.mc.setSendByte(b);
				while(regEndFlag == false){
					try{
						Thread.sleep(16);
					}catch(Exception ex){

					}

				}
				System.out.println("3");
				regEndFlag = false;
				if(successRegFlag == true){
					successRegFlag = false;
					successRegCount = 180;
					title.changeScene(Title.Scene.LOGIN);
				}
				if(failedRegFlag == true){
					failedRegCount = 180;
					failedRegFlag = false;
				}
			}
		});
		regCancelButton = new Button("キャンセル");
		regCancelButton.setSize(80,40);
		regCancelButton.setLocation(x+70,y+100);
		regCancelButton.setBackground(Color.white);
		regCancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String str = regIdTxtF.getText();//なぜかTextFieldの値を読み込まないと、次の値がセットされないから仕方なく
				str = regPwTxtF.getText();
				title.changeScene(Title.Scene.LOGIN);
			}
		});

		AppTemplate.addComponent(regIdTxtF);
		AppTemplate.addComponent(regPwTxtF);
		AppTemplate.addComponent(regApplyButton);
		AppTemplate.addComponent(regCancelButton);

	}

	public void finalize(){
		AppTemplate.removeComponent(regIdTxtF);
		AppTemplate.removeComponent(regPwTxtF);
		AppTemplate.removeComponent(regApplyButton);
		AppTemplate.removeComponent(regCancelButton);
	}
	public void update(){
		time++;
		if(time % 300 == 0){
			title.bb.clear();
			title.bb.putInt(10000);
			byte[] b = title.bb.getArray();
			title.mc.setSendByte(b);
		}
	}
	public void draw(){
		Draw.string("ID", x-20,y+3,0x000000);
		Draw.string("PW", x-23, y+23, 0x000000);
		if(failedRegCount > 0){
			failedRegCount--;
			Draw.string("そのIDはすでに使用されています",x-23, y+50,0x000000);
		}

	}

	public void show(){
		regIdTxtF.setVisible(true);
		regPwTxtF.setVisible(true);
		regApplyButton.setVisible(true);
		regCancelButton.setVisible(true);
	}

	public void hide(){

		regIdTxtF.setVisible(false);
		regPwTxtF.setVisible(false);
		regApplyButton.setVisible(false);
		regCancelButton.setVisible(false);
		failedRegCount = 0;
		regIdTxtF.setText("");
		regPwTxtF.setText("");
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
		str = cl.readLine();
		num = str.charAt(0) - '0';
		if(num == 0){
			successRegFlag = true;
		}
		else if(num == 9){
			return;
		}
		else{
			failedRegFlag = true;
		}
		regEndFlag = true;
	}

}
