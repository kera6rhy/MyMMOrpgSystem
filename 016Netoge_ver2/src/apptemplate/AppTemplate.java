package apptemplate;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;

import javax.swing.JFrame;


public class AppTemplate extends JFrame{
	//--------------------------------------------------------
	//public final クラス フィールド
	//--------------------------------------------------------
	public final int WIDTH ;
	public final int HEIGHT;
	private static AppTemplate at;

	//--------------------------------------------------------
	//private インスタンス フィールド
	//--------------------------------------------------------
	private Graphics dbg;//BufferStrategyに必要な独自のGraphics
	private MainProcess mp;
	private int frameLeftSize_;
	private int frameTopSize_;
	private RenderThread renderThread;
	
	public static void addComponent(Component c){
		c.setVisible(false);//追加しただけで表示されるのがうざいから
		at.add(c);
	}
	public static void removeComponent(Component c){
		at.remove(c);
	}
	//--------------------------------------------------------
	//コンストラクタ
	//--------------------------------------------------------
	public AppTemplate(ProcessTemplate p, int x,int y, String title){
		super(title);
		at = this;
		WIDTH = x;
		HEIGHT = y;
		
		
		addMouseMotionListener(new MyListener());
		addMouseListener(new MyMouseListener());
		addKeyListener(new MyKeyListener());
		setFocusable(true);	
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setIgnoreRepaint(true); //paint()では描画されないようにする
        setVisible(true);
        frameLeftSize_ = getInsets().left;
        frameTopSize_ = getInsets().top;
		setSize(WIDTH+getInsets().left*2, HEIGHT+getInsets().top + getInsets().bottom);//Windows,Mac両方で問題ない描画になった
		
		createBufferStrategy(2);//スクリーンバッファは２つあれば十分
		Draw.g = getBufferStrategy().getDrawGraphics();//フォント設定するために、事前にGraphicsを取得する必要があるから仕方なく
        
		Draw.setImageObserver(this);//DrawImageする時ImageObserverが必要だから。仕方なく
		ImageData.setToolkit(getToolkit());//イメージのロードに使うから仕方なく
		setLayout(null);//ボタンとか自由配置にするため
		
		mp = new MainProcess(p);
		renderThread = new RenderThread(this);
	}

	
	//--------------------------------------------------------
	//インスタンス メソッド
	//--------------------------------------------------------
	public void startProcess(){
		mp.start();
		renderThread.start();
	}
	
	public void gameRender(){// BufferStrategyを用いての高速描画
		Graphics dbg = getBufferStrategy().getDrawGraphics();// BufferStrategy版のGraphicsの取得
		if (!getBufferStrategy().contentsLost()) {//これを満たせば描画することができる仕様になってる
			AffineTransform af = new AffineTransform();
			af.setToTranslation(frameLeftSize_, frameTopSize_);
			Draw.init(af, dbg);
			Draw.fillRect(0,0,WIDTH,HEIGHT,0xffffff);//背景色で全画面クリア
	        mp.draw();//メイン描画処理
			mp.debugDraw();
			getBufferStrategy().show();//BufferStrategyの描画最終処理
			dbg.dispose();//Graphicsの最終処理
        } else {
            System.out.println("Contents Lost");
        }
        Toolkit.getDefaultToolkit().sync();//BufferStrategyの後片付け
	}
	

	//--------------------------------------------------------
	//内部クラス
	//--------------------------------------------------------
    public class MyKeyListener extends KeyAdapter{
		@Override 
		public void keyPressed(KeyEvent e){
			Key.keyState[e.getKeyCode()] = 1;
		}
		@Override
		public void keyReleased(KeyEvent e){
			Key.keyState[e.getKeyCode()] = 0;
		}
		@Override
		public void keyTyped(KeyEvent e){
			Key.typedChar = e.getKeyChar();
			Key.justTyped = 1;
		}
	}

    public class MyListener extends MouseMotionAdapter{
		@Override
		public void mouseMoved(MouseEvent e){
			Key.mouse.x = (double)e.getX()-frameLeftSize_;
		    Key.mouse.y = (double)e.getY()-frameTopSize_;
		}
		@Override
		public void mouseDragged(MouseEvent e){
		    Key.mouse.x = (double)e.getX()-frameLeftSize_;
		    Key.mouse.y = (double)e.getY()-frameTopSize_;
		}
    }
    public class MyMouseListener extends MouseAdapter{
    	@Override
		public void mousePressed(MouseEvent e){
		    Key.mouseState[0] =1;
		}
    	@Override
		public void mouseReleased(MouseEvent e){
		    Key.mouseState[0] =0;
		}
    }
}
