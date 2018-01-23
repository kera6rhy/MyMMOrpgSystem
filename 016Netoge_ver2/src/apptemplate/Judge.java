package apptemplate;


public class Judge{
	public static boolean inRect(int x, int y, int width, int height, int mx, int my){
		if(mx>=x && mx<x+width && my>=y && my<y+height){
			return true;
		}
		return false;
	}
	
	public static boolean circleInRect(Vector p, double r, double x, double y, double w, double h){
		if(p.x>=x+r && p.x<x+w-r && p.y>=y+r && p.y<y+h-r){
			return true;
		}
		return false;
		
	}
	public static boolean isRomaji(char ch){
		if(( ch >= '!' && ch <= '}') || (ch=='~' )){
			return true;
		}
		return false;
	}
	
	public static boolean circleInCircle(Vector p1, double r1, Vector p2, double r2){
		if(nijo(p1.x-p2.x) + nijo(p1.y-p2.y) <= nijo(r1+r2)){
			return true;
		
		}
		return false;
	}
	public static boolean inCircle(Vector p, Vector cp, double r){
		if(nijo(p.x-cp.x) + nijo(p.y-cp.y) <= r*r){
			return true;
		
		}
		return false;
	}
	public static double zchi(double a){
		return (a>0)?a:-a;
	}
	public static boolean lineInCircle(Vector p1, Vector p2, Vector c,double r){
		Vector va = new Vector();
		va.copyFrom(c);
		va.subV(p1);
		Vector vb = new Vector();
		vb.copyFrom(c);
		vb.subV(p2);
		Vector vs = new Vector();
		vs.copyFrom(p2);
		vs.subV(p1);
		double th1 = va.getAngle(vs);
		double th2 = vb.getAngle(vs);
		
		double d = zchi(Vector.cross(vs, va)) / zchi(vs.length());
		if(d<= r){
			if(Vector.dot(va,vs) * Vector.dot(vb, vs) <= 0){
				return true;
			}
			if( r>va.length() || r>vb.length()){
				return true;
			}
		}
		return false;
	}
	
	public static double nijo(double a){
		
		return a*a;
	}
}