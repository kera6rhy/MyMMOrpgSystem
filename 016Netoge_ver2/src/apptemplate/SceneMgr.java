package apptemplate;

abstract public class SceneMgr implements ProcessTemplate{
	protected Scene nowScene;
	
	abstract public void changeScene(String str);
	

	final public void update(){
		nowScene.update();
	}
	
	
	final public void draw(){
		nowScene.draw();
	}
	
	
	public void debugDraw(){
		
	}
}
