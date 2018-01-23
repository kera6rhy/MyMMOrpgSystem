package titleScene;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Scrollbar;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import apptemplate.AppTemplate;
import apptemplate.Draw;
import titleScene.CharacterSelect.Scene;

public class CharacterMakeScene {
	Button retSelectBt;
	TextField nameTF;
	Scrollbar redSB;
	Scrollbar blueSB;
	Scrollbar greenSB;
	Scrollbar radSB;
	String messageStr;
	Button makeBt;
	int messageStrCount;
	CharacterSelect cs;
	public CharacterMakeScene(CharacterSelect c){
		cs = c;
		retSelectBt = new Button("戻る");
		retSelectBt.setLocation(680,400);
		retSelectBt.setSize(80,40);
		retSelectBt.setBackground(Color.white);
		retSelectBt.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e){
				cs.changeScene(Scene.SELECT);
			}
		});
		
		nameTF = new TextField();
		initComponent(nameTF,100,150,100,20);
		
		redSB = new Scrollbar(Scrollbar.HORIZONTAL , 128 , 0 , 0 , 255);
		initComponent(redSB,100,200,200,10);
		blueSB = new Scrollbar(Scrollbar.HORIZONTAL, 128, 0, 0, 255);
		initComponent(blueSB,100,280,200,10);
		greenSB = new Scrollbar(Scrollbar.HORIZONTAL, 128, 0, 0, 255);
		initComponent(greenSB, 100, 240, 200, 10);
		radSB = new Scrollbar(Scrollbar.HORIZONTAL, 16, 0, 2, 30);
		initComponent(radSB, 100, 320, 200, 10);
		makeBt = new Button("作成");
		initComponent(makeBt, 20, 400, 80, 40);
		makeBt.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e){
				int color = redSB.getValue()*0x10000 + greenSB.getValue()*0x100 + blueSB.getValue();
				//mc.sendStr2 = color + " "+nameTF.getText() +" "+ radSB.getValue();
				cs.btStr = "4 1 "+cs.netoge.loginId+" " + nameTF.getText() + " "+color+" "+ radSB.getValue();
				cs.bb.clear();
				cs.bb.putInt(4).putInt(1).putString(cs.netoge.loginId).putString(nameTF.getText()).putInt(color).putInt(radSB.getValue());
				byte[] b = cs.bb.getArray();
				cs.mc.setSendByte(b);
				while(cs.resBtStrFlag == false){
					try{
						Thread.sleep(16);
					}catch(Exception ex){}
				}
				cs.resBtStrFlag = false;
				if(cs.resBtStr.equals("failed")){
					messageStr = "この名前はすでに登録されています";
					messageStrCount = 300;
					return;
				}
				cs.data[cs.charaCount].color = redSB.getValue()*0x10000 + greenSB.getValue()*0x100 + blueSB.getValue();
				cs.data[cs.charaCount].name = nameTF.getText();
				cs.data[cs.charaCount].r = radSB.getValue();
				cs.data[cs.charaCount].exist = true;
				cs.charaCount++;
				cs.changeScene(Scene.SELECT);
			}
		});

		AppTemplate.addComponent(retSelectBt);
	}
	
	public void finalize(){

		AppTemplate.removeComponent(retSelectBt);
		AppTemplate.removeComponent(makeBt);
		AppTemplate.removeComponent(nameTF);
		AppTemplate.removeComponent(redSB);
		AppTemplate.removeComponent(blueSB);
		AppTemplate.removeComponent(greenSB);
		AppTemplate.removeComponent(radSB);
	}
	
	public void update(){
		if(nameTF.getText().equals("")){
			makeBt.setEnabled(false);
		}
		else{
			makeBt.setEnabled(true);
		}
		if(messageStrCount >0){
			messageStrCount--;
		}
	}
	public void draw(){
		int color = redSB.getValue()*0x10000 + greenSB.getValue()*0x100 + blueSB.getValue(); 
		Draw.rect(500, 50, 150, 300, 0x000000);
		Draw.fillOval(575,200, radSB.getValue(), color);
		Draw.string("赤", 80, 200, 0xff0000);
		Draw.string("緑", 80, 240, 0x00ff00);
		Draw.string("青", 80, 280, 0x0000ff);
		Draw.string("大きさ", 50, 320, 0x000000);
		Draw.string("名前", 60, 150, 0x000000);
		if(messageStrCount > 0){
			Draw.string(messageStr, 100, 130,0x000000);
		}
	}
	public void show(){
		retSelectBt.setVisible(true);
		redSB.setVisible(true);
		blueSB.setVisible(true);
		greenSB.setVisible(true);
		radSB.setVisible(true);
		nameTF.setVisible(true);
		makeBt.setVisible(true);
		
	}
	
	
	public void hide(){
		retSelectBt.setVisible(false);
		redSB.setVisible(false);
		blueSB.setVisible(false);
		greenSB.setVisible(false);
		radSB.setVisible(false);
		nameTF.setVisible(false);
		makeBt.setVisible(false);
		nameTF.setText("");
		redSB.setValue(128);
		greenSB.setValue(128);
		blueSB.setValue(128);
		radSB.setValue(16);
	}

	public void initComponent(Component c,int x, int y, int w, int h){
		c.setLocation(x,y);
		c.setSize(w,h);
		c.setBackground(Color.white);
		AppTemplate.addComponent(c);
	}
}
