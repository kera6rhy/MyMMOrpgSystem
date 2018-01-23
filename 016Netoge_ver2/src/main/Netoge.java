package main;

import gameScene.Game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import titleScene.CharacterSelect;
import titleScene.Title;
import apptemplate.ProcessTemplate;

public class Netoge implements ProcessTemplate {
	private ProcessTemplate nowProcess;
	public String loginId = null;
	public String HOST="133.80.208.126";// = "133.80.88.41";133.80.209.252

	public Netoge(){

		File file = new File("Address.txt");
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			HOST = br.readLine();
		}
		catch(Exception e){
			System.out.println("Address.txtファイルが見つかりません");
			System.exit(0);
		}
		System.out.println("host:"+HOST);

		nowProcess = new Title(this);
	}

	public void chengeProcess(String s){
		if(s.equals("Game")){
			//loginId = ((Title)nowProcess).loginId;
			nowProcess = new Game(this);
		}
		if(s.equals("CharacterSelect")){
			if(loginId == null){
				loginId = ((Title)nowProcess).loginId;
			}
			nowProcess = new CharacterSelect(this);
		}
	}

	public void update(){
		nowProcess.update();
	}

	public void draw(){
		nowProcess.draw();
	}

}
