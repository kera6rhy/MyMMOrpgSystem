package apptemplate;

import java.awt.Color;
import java.awt.Graphics;
//import main.*;



public class MainProcess extends Thread{
	//--------------------------------------------------------
	//private インスタンス フィールド
	//--------------------------------------------------------
	private FrameRate fr;
	private ProcessTemplate mg;



	//--------------------------------------------------------
	//コンストラクタ
	//--------------------------------------------------------
	public MainProcess(ProcessTemplate p){
		mg = p;
		fr = new FrameRate();
	}

	//--------------------------------------------------------
	//インスタンス メソッド
	//--------------------------------------------------------
	@Override
	public void run(){
		while (true) {
			Key.update();//キーボード、マウス情報の更新
			update();//メインゲームの更新
			fr.update();//フレームレートの管理、調整
		}
	}
	public void update(){
		mg.update();
	}
	public void draw(){
		mg.draw();
	}
	public void debugDraw(){
		//Draw.string("fps="+(int)fr.getFrameRate(), 0, 0, 0x000000);
		//Draw.string("RenderFps="+(int)RenderThread.fr.getFrameRate(), 100,0,0x000000);
		//mg.debugDraw();
	}
}
