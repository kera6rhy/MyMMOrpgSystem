
package apptemplate;

public class Key {
	//--------------------------------------------------------
	//package クラス フィールド
	//--------------------------------------------------------
	static int keyState[] = new int[256];//キーが押されてるかどうか
    static int keyCount[] = new int[256];//キーが押されている時間
    static int mouseState[] = new int[10];//マウスボタンが押されてるかどうか
    static int mouseCount[] = new int[10];//マウスボタンが押されている時間
    static int mouseMoveF=0;
    static Vector mouse=new Vector();
    static Vector mouseOld=new Vector();
    static char typedChar;
    static int justTyped;

	//--------------------------------------------------------
	//クラス メソッド
	//--------------------------------------------------------

	public static int getMouseX(){
		return (int) mouse.x;
	}
	public static int getMouseY(){
		return (int) mouse.y;
	}
	public static int getKeyCount(int id){
		return keyCount[id];
	}
	public static int getMouseCount(){
		return mouseCount[0];
	}
	public static boolean justTyped(){
		if(justTyped != 0){
			return true;
		}
		return false;
	}
	public static char getJustChar(){
		return typedChar;
	}
    public static void update(){
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

		Key.mouseOld.x = Key.mouse.x;
		Key.mouseOld.y = Key.mouse.y;

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
