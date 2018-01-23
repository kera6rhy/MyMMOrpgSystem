package apptemplate;

import java.util.ArrayList;

public class Scene implements ProcessTemplate{
	ArrayList<Part> parts = new ArrayList<Part>();
	private SceneMgr sceneMgr;
	public Scene(SceneMgr sm){
		sceneMgr = sm;
	}
	
	public void changeScene(String str){
		sceneMgr.changeScene(str);
	}
	
	protected void addPart(Part p){
		parts.add(p);
	}
	
	final public void update(){
		for(Part p: parts){
			p.update();
		}
		update2();
	}
	
	final public void draw(){
		for(Part p: parts){
			p.draw();
		}
		draw2();
	}
	
	public void update2(){
		
		
	}
	
	public void draw2(){
		
	}
	
	public void debugDraw(){
		
	}
}
