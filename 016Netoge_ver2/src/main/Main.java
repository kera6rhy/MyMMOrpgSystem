package main;

import apptemplate.AppTemplate;
import apptemplate.Draw;
import apptemplate.ProcessTemplate;

public class Main implements ProcessTemplate{
	private ProcessTemplate pt;

	public static void main(String args[]){
		Main main = new Main();
		AppTemplate at = new AppTemplate(main,768,480,"Template");//これ１行でウィンドウ生成してくれる
		main.init();//AppTemplateが生成し終わってからじゃないとloadとかができないからここで初期化
		at.startProcess();
	}

	public void init(){
		pt = new Netoge();
	}
	
	public void update(){
		pt.update();
	}

	public void draw(){
		pt.draw();
	}

}

