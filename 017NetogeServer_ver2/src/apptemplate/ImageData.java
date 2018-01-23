package apptemplate;

import java.awt.Image;
import java.awt.Toolkit;

public class ImageData {
	//--------------------------------------------------------
	//private クラス フィールド
	//--------------------------------------------------------
	private static Toolkit s_Toolkit;

	//--------------------------------------------------------h
	//クラス メソッド
	//--------------------------------------------------------
	public static void setToolkit(Toolkit t){
		s_Toolkit = t;
	}

	//--------------------------------------------------------
	//package インスタンス フィールド
	//--------------------------------------------------------
	Image image;
	int xSize;
	int ySize;
	int xNum;
	int yNum;
	
	//--------------------------------------------------------
	//コンストラクタ
	//--------------------------------------------------------
	public ImageData(int xSize, int ySize, int xNum, int yNum){
		this.xSize = xSize;
		this.ySize = ySize;
		this.xNum = xNum;
		this.yNum = yNum;
	}


	//--------------------------------------------------------
	//インスタンス メソッド
	//--------------------------------------------------------
	public void load(String str){
		image = s_Toolkit.getImage(str);
	}
}
