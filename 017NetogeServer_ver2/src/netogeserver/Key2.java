package netogeserver;

import apptemplate.MyByteBuffer;
import apptemplate.Vector;

public class Key2 {
	//--------------------------------------------------------
	//package クラス フィールド
	//--------------------------------------------------------
	private int keyState[] = new int[256];//キーが押されてるかどうか
	private int keyCount[] = new int[256];//キーが押されている時間
    private int mouseState[] = new int[10];//マウスボタンが押されてるかどうか
    int mouseCount[] = new int[10];//マウスボタンが押されている時間
    int mouseMoveF=0;
    Vector mouse=new Vector();
    Vector mouseOld=new Vector();
    char typedChar;
    int justTyped;

	//--------------------------------------------------------
	//クラス メソッド
	//--------------------------------------------------------
    public void talk(MyByteBuffer bb){
    	for(int i=0; i<6;i++){
    		int id = bb.getInt();
    		int count = bb.getInt();
    		if( count != 0){
    			keyState[id] = 1;
    		}
    		else{
    			keyState[id] = 0;
    		}
    	}
    }
	public int getMouseX(){
		return (int) mouse.x;
	}
	public int getMouseY(){
		return (int) mouse.y;
	}
	public int getKeyCount(int id){
		return keyCount[id];
	}
	public int getMouseCount(){
		return mouseCount[0];
	}
	public boolean justTyped(){
		if(justTyped != 0){
			return true;
		}
		return false;
	}
	public char getJustChar(){
		return typedChar;
	}
    public void update(){
    	/*
    	if(justTyped == 1){
    		justTyped++;
    	}
    	else if(justTyped == 2){
    		justTyped =0;
    	}*/
    	if(mouse.x == mouseOld.x && mouse.y == mouseOld.y)
    	{
    		mouseMoveF = 0;
    	}
    	else
    	{
    		mouseMoveF = 1;
    	}

		mouseOld.x = mouse.x;
		mouseOld.y = mouse.y;

		for(int i=0;i<256;i++)
		{
		    if(keyState[i] == 1)
		    {
		    	keyCount[i]++;
		    }
		    else
		    {
		    	keyCount[i]=0;
		    }
		}
		for(int i=0;i<10;i++)
		{
		    if(mouseState[i] == 1)
		    {
		    	mouseCount[i]++;
		    }
		    else
		    {
		    	mouseCount[i]=0;
		    }
		}
    }
}
