package gameScene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnemyMgr {
	ArrayList<Enemy> enemys = new ArrayList<Enemy>();
	List<Enemy> newEnemys = Collections.synchronizedList(new ArrayList<Enemy>());
	boolean newEnemyLock = false;
	
	
	public void update(){
		
	}
	
	public void waitLock(){
		while(newEnemyLock){
			try{
				Thread.sleep(1);
			}catch(Exception r){
				
			}
		}
	}
	public void draw(){
		waitLock();
		newEnemyLock = true;
		enemys.clear();
		synchronized(newEnemys){
			for(Enemy e:newEnemys){
				enemys.add(e);
			}
		}
		newEnemyLock = false;
		for(Enemy e: enemys){
			e.draw();
		}
	}
}
