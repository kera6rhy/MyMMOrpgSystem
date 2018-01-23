package gameScene;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import apptemplate.AppTemplate;
import apptemplate.Draw;
import apptemplate.Key;

public class Chat {
	TextField tx;
	boolean isShowText;
	boolean isEnterType;
	String talkStr = "";
	Game game;
	
	public Chat(Game g){
		game = g;
		tx = new TextField();
		tx.setLocation(0,460);
		tx.setSize(500, 20);
		tx.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				tx.setFocusable(false);
				isShowText = false;
				tx.setVisible(false);
				tx.setEnabled(false);
				talkStr = tx.getText();
				if(talkStr.length()!=0){
					//System.out.println(talkStr+"  len="+talkStr.length());
					game.setChatStr(talkStr);
				}
				tx.setText("");
				
			}
		});
		AppTemplate.addComponent(tx);
	}
	
	public void update(){
		if(Key.getMouseCount() == 1){
			int mx = Key.getMouseX();
			int my = Key.getMouseY();
			if(tx.contains(mx, my) == false){	
				tx.setFocusable(false);
				isShowText = false;
				//tx.setVisible(false);
				tx.setEnabled(false);
			}
		}
		if(Key.getKeyCount(KeyEvent.VK_ENTER) >= 1){
			isEnterType = true;
		}
		if(Key.getKeyCount(KeyEvent.VK_ENTER) == 0 && isEnterType == true){
			isEnterType = false;
			if(isShowText == true){
				isShowText = false;
				tx.setVisible(false);
				tx.setEnabled(false);
			}
			else{
				isShowText = true;
				tx.setVisible(true);
				tx.setEnabled(true);
				tx.setFocusable(true);
				tx.requestFocus();
			}
			
		}
		if(isShowText == false){
			return;
		}
	
	}
	
	public void draw(){
		if(isShowText == false){
			return;
		}
	}
}
