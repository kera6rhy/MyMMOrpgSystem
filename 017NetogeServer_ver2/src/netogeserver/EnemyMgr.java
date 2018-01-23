package netogeserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import apptemplate.Global;
import apptemplate.MyByteBuffer;
import enemy.Enemy1;
import enemy.Enemy2;
import enemy.Enemy3;
import enemy.Enemy4;

public class EnemyMgr {
	public static final int MAX_ENEMY_NUM = 10;
	public List<Enemy> enemys = Collections.synchronizedList(new ArrayList<Enemy>());
	public int aliveEnemyNum = 0;
	int time =0;
	public static final int ENEMY_TYPE_NUM = 5;
	
	int enemyRatio[] = new int[ENEMY_TYPE_NUM];
	

	public EnemyMgr(){
		enemyRatio[0] = 1;
	}
	public void setEnemyNum(int id, int val){
		enemyRatio[id] = val;
	}
	
	public void update(){
		if(time %600== 0){
			int bornCount = 0;
			while(aliveEnemyNum < MAX_ENEMY_NUM){
				
				Enemy e = null;
				Random rand = Global.rand;
				int sum = 0;
				for(int i=0;i<enemyRatio.length;i++)sum += enemyRatio[i];
				int num = rand.nextInt(sum);
				for(int i=0; i<enemyRatio.length;i++){
					if(num <enemyRatio[i]){
						switch(i){
						case 0:
							e = new Enemy();
							break;
						case 1:
							e = new Enemy1();
							break;
						case 2:
							e = new Enemy2();
							break;
						case 3:
							e = new Enemy3();
							break;
						case 4:
							e = new Enemy4();
							break;
						}
						break;
					}
					num -= enemyRatio[i];
				}

				aliveEnemyNum++;
				enemys.add(e);
				bornCount++;
			}
			if(bornCount >= 1){
				//System.out.println(bornCount+"enemys has born");
				
			}
		}
		for(Enemy e: enemys){
			e.update();
		}
		time++;
	}
	
	public void talk(MyByteBuffer bb){

		int enemyLen = enemys.size();
		bb.putInt(enemyLen);
		synchronized(enemys){
			for(Enemy e: enemys){
				bb.putDouble(e.p.x).putDouble(e.p.y).putInt(e.color).putDouble(e.r);
			}
		}
	}
	public void bornEnemy(){
		
		
	}
}

