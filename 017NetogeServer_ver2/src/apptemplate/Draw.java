
package apptemplate;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

import javax.swing.JFrame;

public class Draw {
	//--------------------------------------------------------
	//private クラス フィールド
	//--------------------------------------------------------
	private static ImageObserver imageObserver;
	public static Graphics g;//Font生成させるためpackageアクセス
	private static AffineTransform at;
	static FontMetrics fontMetrics;

	private static Font font10 = new Font("YuGothic", Font.PLAIN, 14);
	private static Font font20 = new Font("YuGothic", Font.PLAIN, 20);
	private static Font font30 = new Font("YuGothic", Font.PLAIN, 30);

	//--------------------------------------------------------
	//クラス メソッド
	//--------------------------------------------------------
	public static void setImageObserver(JFrame f){
		imageObserver = f;
	}
	
	public static void translate(int x, int y){
		g.translate(x,y);
	}
	public static void init(AffineTransform a, Graphics gr){
		at = a;
		g = gr;
		g.setFont(font10);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setTransform(at);
		fontMetrics = g.getFontMetrics();
	}

	public static void image(Graphics g, Image image, int x, int y){
		g.drawImage(image,0,100,imageObserver);
	}
	public static void divImage(Graphics g, Image image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2){
		g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, imageObserver);
	}
	public static void setFontSize(int size){
		switch(size){
			case 10:
				g.setFont(font10);

				break;
			case 20:
				g.setFont(font20);

				break;
			case 30:
				g.setFont(font30);
				break;
		}
	}
	public static void string(String str, int x, int y, int c, int size){
		g.setColor(new Color(c));
		int realSize=10;
		switch(size){
			case 10:
				g.setFont(font10);
				realSize = 14;
				break;
			case 20:
				g.setFont(font20);
				realSize = 20;
				break;
			case 30:
				g.setFont(font30);
				realSize = 30;
				break;
		}
		g.drawString(str, x, y+realSize);
	}


	public static void string(String str, int x, int y, int c){
		string(str,x,y,c,10);
	}
	public static void string(String str, double x, double y, int c){
		string(str, (int)x, (int)y, c);
	}
	public static void image(ImageData img, int x, int y, int id){
		g.drawImage(
				img.image,
				x,
				y,
				x+img.xSize,
				y+img.ySize,
				(id%img.xNum)*img.xSize,
				(id/img.yNum)*img.ySize,
				(id%img.xNum)*img.xSize + img.xSize,
				(id/img.yNum)*img.ySize + img.ySize,
				imageObserver
		);
	}
	public static void scaleImage(ImageData img, int x, int y, int id, double ex, boolean transFlag){
		int a=0,b=0;
		if( transFlag == true){
			a = (int)(img.xSize*ex);
		} else {
			b = (int)(img.xSize*ex);
		}
		g.drawImage(
				img.image,
				x+a,
				y,
				x+b,
				y+(int)(img.ySize*ex),
				(id%img.xNum)*img.xSize,
				(id/img.xNum)*img.ySize,
				(id%img.xNum)*img.xSize + img.xSize,
				(id/img.xNum)*img.ySize + img.ySize,
				imageObserver
		);
	}
	public static void affineImage(ImageData img, int x, int y, int id, double ex, double rad, int rx, int ry, boolean transFlag){
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform af = new AffineTransform();
		double atX = at.getTranslateX();
		double atY = at.getTranslateY();
		af.translate(atX, atY);

		af.rotate(rad * Math.PI/180,x+rx,y+ry);
		g2.setTransform(af);
		int a=0,b=0;
		if( transFlag == true){
			a = (int)(img.xSize*ex);
		} else {
			b = (int)(img.xSize*ex);
		}
		g.drawImage(
				img.image,
				x+a,
				y,
				x+b,
				y+(int)(img.ySize*ex),
				(id%img.xNum)*img.xSize,
				(id/img.xNum)*img.ySize,
				(id%img.xNum)*img.xSize + img.xSize,
				(id/img.xNum)*img.ySize + img.ySize,
				imageObserver
		);
		g2.setTransform(at);

	}
	public static void fillOval(int x, int y, int r){
		g.setColor(Color.red);
		g.fillOval(x-r, y-r, r*2, r*2);
	}
	public static void fillOval(double x, double y, double r, int c){
		g.setColor(new Color(c));
		g.fillOval((int)(x-r), (int)(y-r), (int)(r*2), (int)(r*2));
	}

	public static void fillCircle(Vector p, double r, int c){
		g.setColor(new Color(c));
		g.fillOval((int)(p.x-r), (int)(p.y-r), (int)(r*2), (int)(r*2));
		
	}
	public static void fillCircle(Vector p, double r, int c, boolean alpha){
		g.setColor(new Color(c,alpha));
		g.fillOval((int)(p.x-r), (int)(p.y-r), (int)(r*2), (int)(r*2));
		
	}
	public static void circle(Vector p, double r, int c){
		g.setColor(new Color(c));
		g.drawOval((int)(p.x-r), (int)(p.y-r), (int)(r*2), (int)(r*2));
		
	}
	public static void oval(int x, int y, int r, int c){
		g.setColor(new Color(c));
		g.drawOval(x-r/2, y-r/2, r*2/2, r*2/2);
	}
	public static void oval(double x, double y, double r, int c){
		oval((int)x, (int)y, (int)r, c);
	}
	public static void fillRect(int x, int y, int dx, int dy, int c){
		g.setColor(new Color(c));
		g.fillRect(x, y, dx, dy);
	}
	public static void fillRect(double x, double y, double dx, double dy, int c){
		fillRect((int)x, (int)y, (int)dx, (int)dy, c);
	}
	public static void rect(int x, int y, int dx, int dy, int c){
		g.setColor(new Color(c));
		g.drawRect(x,y,dx,dy);
	}
	public static void rect(double x, double y, double dx, double dy, int c){
		rect((int)x, (int)y, (int)dx, (int)dy, c);
	}
	public static void fillTryangle(int x1, int y1, int x2, int y2, int x3, int y3){
		g.setColor(Color.red);
		int[] xPoints = {x1, x2, x3};
		int[] yPoints = {y1, y2, y3};
		g.fillPolygon(xPoints, yPoints, 3);
	}

	public static void drawTryangle(int x1, int y1, int x2, int y2, int x3, int y3){
		g.setColor(Color.red);
		int[] xPoints = {x1, x2, x3};
		int[] yPoints = {y1, y2, y3};
		g.drawPolygon(xPoints, yPoints, 3);
	}

	public static void fillHishigata(int x1, int y1, int dx, int dy, int c){
		g.setColor(new Color(c));
		int x2 = x1 + dx;
		int y2 = y1 + dy;
		int[] xPoints = {x1, (x1+x2)/2, (x1+x2)/2};
		int[] yPoints = {(y1+y2)/2, y1, y2};
		g.fillPolygon(xPoints, yPoints, 3);

		int[] xPoints2 = {x2, (x1+x2)/2, (x1+x2)/2};
		int[] yPoints2 = {(y1+y2)/2, y1, y2};
		g.fillPolygon(xPoints2, yPoints2, 3);

	}

	public static void drawHishigata(int x1, int y1, int dx, int dy, int c){
		g.setColor(new Color(c));
		int x2 = x1 + dx;
		int y2 = y1 + dy;
		line(x1,(y1+y2)/2, (x1+x2)/2, y1,c);
		line(x1,(y1+y2)/2, (x1+x2)/2, y2,c);
		line(x2,(y1+y2)/2, (x1+x2)/2, y1,c);
		line(x2,(y1+y2)/2, (x1+x2)/2, y2,c);

	}

	public static void fillHishigata(double x1, double y1, double dx, double dy, int c){
		fillHishigata((int)x1, (int)y1, (int)dx, (int)dy, c);
	}
	public static void drawHishigata(double x1, double y1, double dx, double dy, int c){
		drawHishigata((int)x1, (int)y1, (int)dx, (int)dy, c);
	}
	
	public static void line(int x1, int y1, int x2, int y2, int c){
		line((double)x1,(double)y1,(double)x2,(double)y2,c);
	}
	public static void line(double a1, double a2, double b1, double b2, int c){
		g.setColor(new Color(c));
		g.drawLine((int)a1, (int)a2, (int)b1, (int)b2);
	}
}
