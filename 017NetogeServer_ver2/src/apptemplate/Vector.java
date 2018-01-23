
package apptemplate;

import java.util.Random;

public class Vector {
	public double x,y;
	public static final double PI = 3.1415926535897932384;

	public Vector(double a, double b)
	{
		x = a;
		y = b;
	}
	public Vector()
	{

	}
	//--------------------------------------------------------
	//クラス メソッド
	//--------------------------------------------------------
	public static Vector getSubV(Vector v1, Vector v2)
	{
		Vector v = new Vector();
		v.x = v1.x - v2.x;
		v.y = v1.y - v2.y;
		return v;
    }

    public static Vector getEV(Vector v)
    {
		double tmp = v.length();
		Vector tmpv = new Vector();
		tmpv.x = v.x/tmp;
		tmpv.y = v.y/tmp;
		return tmpv;

    }
    public static Vector mulV(Vector v, double a)
    {
		Vector v1 = new Vector();
		v1.x = v.x*a;
		v1.y = v.y*a;
		return v1;
    }
    public static Vector addV(Vector v1, Vector v2)
    {
		Vector v = new Vector();
		v.x = v1.x + v2.x;
		v.y = v1.y + v2.y;
		return v;
    }
    
    public static double dot(Vector v1, Vector v2){
    	return v1.x*v2.x + v1.y*v2.y;
    	
    }
    public static double cross(Vector v1, Vector v2)
    {
    	return v1.x * v2.y - v2.x * v1.y;
    }
    public static Vector routV(Vector v, double a)
    {
    	Vector tmp = new Vector();
    	tmp.x = (double)(v.x*Math.cos(a)-v.y*Math.sin(a));
    	tmp.y = (double)(v.x*Math.sin(a)+v.y*Math.cos(a));
    	return tmp;
    }
    
	//--------------------------------------------------------
	//インスタンス メソッド
	//--------------------------------------------------------
	public void addV(Vector v){
		this.x += v.x;
		this.y += v.y;
	}
	public void subV(Vector v){
		this.x -= v.x;
		this.y -= v.y;
	
	}
    public double length()
    {
		return (double)Math.sqrt(x*x + y*y);
    }
    public double distanceNijo(Vector v){
    	return nijo(v.x-x)+nijo(v.y-y);
    	
    }
    public double distance(Vector v){
    	return Math.sqrt(nijo(v.x-x)+nijo(v.y-y));
    	
    }
	public double getAngle(Vector v){
		double bunbo = v.length() * this.length();
		return this.dot(v) / bunbo;
	}
	public double dot(Vector v)
    {
    	return this.x *  v.x  + this.y * v.y;
    }
	public Vector getCopy(){
		return new Vector(this.x, this.y);
	}
	public void translate(double x, double y){
		this.x += x;
		this.y += y;
	}

	public void mulV(double a){
		this.x *= a;
		this.y *= a;
	}
	public double nijo(double a){
		return a*a;
	}
	public void copyFrom(Vector v){
		this.x = v.x;
		this.y = v.y; 
	}
	public void setSubV(Vector v1, Vector v2){
		this.x = v1.x - v2.x;
		this.y = v1.y - v2.y;
	}
	public void transEV(){
		double len = this.length();
		this.x /= len;
		this.y /= len;
	}
	public void set(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public void transRandEV(){
		Random rand = Global.rand;
		double a,b,x,y;
		do{
			a = rand.nextDouble();
			b = rand.nextDouble();
			x = 2*a-1;
			y = b;
		}while(x*x+y*y > 1);
		double si = 2*x*y/(x*x+y*y);
		double co = (x*x-y*y) / (x*x+y*y);
		this.x = co;
		this.y = si;
	}
}
