package titleScene;

import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import titleScene.CharacterSelect.Scene;
import apptemplate.AppTemplate;
import apptemplate.Draw;
import apptemplate.Judge;
import apptemplate.Key;

public class CharacterSelectScene {
	Button charaDeleteBt;
	Button charaMakeBt;
	CharacterSelect cs;
	CharaPanel charaPanels[] = new CharaPanel[cs.CHARA_PANEL_NUM];
	Button startBt;
	
	public CharacterSelectScene(CharacterSelect c){
		cs = c;
		for(int i=0;i<cs.CHARA_PANEL_NUM;i++){
			charaPanels[i] = new CharaPanel(40+i*200,30);
		}
		charaDeleteBt = new Button("キャラ削除");
		charaDeleteBt.setLocation(680, 400);
		charaDeleteBt.setSize(80,40);
		charaDeleteBt.setBackground(Color.white);
		charaDeleteBt.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e){
				int tmp = -1;
				for(int i=0;i<cs.CHARA_PANEL_NUM; i++){
					if(charaPanels[i].isActive == true){
						tmp = i;
						break;
					}
				}
				cs.btStr = "4 3 "+cs.netoge.loginId+" "+tmp;
				cs.bb.clear();
				cs.bb.putInt(4).putInt(3).putString(cs.netoge.loginId).putInt(tmp);
				byte[] b = cs.bb.getArray();
				cs.mc.setSendByte(b);
				while(cs.resBtStrFlag == false){
					try{
						Thread.sleep(16);
					}catch(Exception ex){}
				}
				cs.resBtStrFlag = false;
				cs.deleteCharacter(tmp);
			}
		});
		charaMakeBt = new Button("キャラ作成");
		charaMakeBt.setLocation(600,400);
		charaMakeBt.setSize(80,40);
		charaMakeBt.setBackground(Color.white);
		charaMakeBt.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e){
				cs.changeScene(Scene.MAKE);
			}
		});
		
		startBt = new Button("ゲームスタート");
		startBt.setLocation(30,400);
		startBt.setSize(120,40);
		startBt.setBackground(Color.white);
		startBt.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e){
				cs.btStr = "4 4 "+cs.netoge.loginId+" " +cs.lastActivePanelId;
				cs.bb.clear();
				cs.bb.putInt(4).putInt(4).putString(cs.netoge.loginId).putInt(cs.lastActivePanelId);
				byte[]b = cs.bb.getArray();
				cs.mc.setSendByte(b);
				while(cs.resBtStrFlag == false){
					try{
						Thread.sleep(16);
					}catch(Exception ex){}
				}
				cs.resBtStrFlag = false;
				cs.changeScene(Scene.START);
			}
		});

		AppTemplate.addComponent(charaDeleteBt);
		AppTemplate.addComponent(charaMakeBt);
		AppTemplate.addComponent(startBt);

		charaDeleteBt.setVisible(true);
		charaMakeBt.setVisible(true);
		startBt.setVisible(true);
	}
	
	public void finalize(){
		AppTemplate.removeComponent(charaDeleteBt);
		AppTemplate.removeComponent(charaMakeBt);
		AppTemplate.removeComponent(startBt);
	}
	public void update(){
		for(int i=0; i<cs.CHARA_PANEL_NUM; i++){
			charaPanels[i].update();
		}
		//isOneActive = false;
		int activePanelId = -1;
		for(int i=0; i<cs.CHARA_PANEL_NUM; i++){
			if(charaPanels[i].isActive){
				cs.lastActivePanelId = activePanelId = i;
			}
		}
		if(activePanelId != -1 && cs.data[activePanelId].exist == true){
			startBt.setEnabled(true);
			charaDeleteBt.setEnabled(true);
		}
		else{
			startBt.setEnabled(false);
			charaDeleteBt.setEnabled(false);
		}
		if(cs.charaCount < 3){
			charaMakeBt.setEnabled(true);
		}
		else{
			charaMakeBt.setEnabled(false);
		}
	}

	public void draw(){
		for(int i=0; i<cs.CHARA_PANEL_NUM;i++){
			charaPanels[i].draw();
			cs.data[i].draw(95+i*200,165);
		}
	}
	
	public void show(){
		charaDeleteBt.setVisible(true);
		charaMakeBt.setVisible(true);
		startBt.setVisible(true);
	}
	
	public void hide(){
		for(int i=0; i<cs.CHARA_PANEL_NUM;i++){
			charaPanels[i].isActive = false;
		}
		charaDeleteBt.setVisible(false);
		charaMakeBt.setVisible(false);
		startBt.setVisible(false);
	}
	
	static class CharaPanel{
		static int charaPanelCount = 0;
		static int activePanelId = -1;
		
		int drawX;
		int drawY;
		int height = 300;
		int width = 150;
		boolean isActive;
		boolean isContainMouse;
		int color;
		int charaPanelId;
		
		public CharaPanel(int x, int y){
			drawX = x;
			drawY = y;
		}
		public void update(){
			int mx = Key.getMouseX();
			int my = Key.getMouseY();
			if(Judge.inRect(drawX, drawY, width, height, mx, my)){
				isContainMouse = true;
				color = 0x5555cc;
			}
			else{
				isContainMouse = false;
				color = 0x333333;
			}
			if(Key.getMouseCount() == 1){
				if(isContainMouse == true){
					isActive = true;
				}
				else{
					isActive = false;
				}
			}
			if(isActive){
				color = 0x9999ff;
			}
		}
		
		public void draw(){
			Draw.rect(drawX, drawY, width, height, color);
		}
	}	
	
}
