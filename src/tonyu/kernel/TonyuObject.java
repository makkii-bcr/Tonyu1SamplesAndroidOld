package tonyu.kernel;

import android.graphics.Color;
import android.util.Log;

public class TonyuObject {

	public float dist(float dx, float dy) {
		return (float)Math.sqrt(dx * dx + dy * dy);
	}
	public double dist(double dx, double dy) {
		return Math.sqrt(dx * dx + dy * dy);
	}
	/**
	 * 線分(1,0)-(0,0)と線分(0,0)-(x,y)のなす角を求めます
	 * @param x 線分(0,0)-(x,y)のx
	 * @param y 線分(0,0)-(x,y)のy
	 * @return 線分(1,0)-(0,0)と(0,0)-(x,y)のなす角(単位:度)<br>範囲 : -180　～ 180
	 */
	public float angle(float x, float y) {
		double angle;
		angle = Math.acos(x / Math.sqrt(x * x + y * y));
		angle = (angle / Math.PI) * 180.0f;
		if (y<0) angle = -angle;
		return (float)angle;
	}
	/**
	 * 線分(1,0)-(0,0)と線分(0,0)-(x,y)のなす角を求めます
	 * @param x 線分(0,0)-(x,y)のx
	 * @param y 線分(0,0)-(x,y)のy
	 * @return 線分(1,0)-(0,0)と(0,0)-(x,y)のなす角(単位:度)<br>範囲 : -180　～ 180
	 */
	public double angle(double x, double y) {
		double angle;
		angle = Math.acos(x / Math.sqrt(x * x + y * y));
		angle = (angle / Math.PI) * 180.0;
		if (y<0) angle = -angle;
		return angle;
	}

	public float sin(float a) {
		if (a % 180f == 0f) return 0f;
		return (float)Math.sin(Math.toRadians(a));
	}
	public double sin(double a) {
		if (a % 180.0 == 0.0) return 0.0;
		return Math.sin(Math.toRadians(a));
	}

	public float cos(float a) {
		if (Math.abs(a % 180f) == 90f) return 0f;
		return (float)Math.cos(Math.toRadians(a));
	}
	public double cos(double a) {
		if (Math.abs(a % 180.0) == 90.0) return 0.0;
		return Math.cos(Math.toRadians(a));
	}

	public int trunc(float f) {
		return (int)f;
	}
	public int trunc(double d) {
		return (int)d;
	}
	
	public int floor(float f) {
		return (int)Math.floor(f);
	}
	public int floor(double d) {
		return (int)Math.floor(d);
	}
	

	public int amod(int v, int m) {
		if (v < 0) return (v+((1-v/m)*m))%m;
		else       return v%m;
	}
	public float amod(float v, float m) {
		if (v < 0) return (v+((1-(int)(v/m))*m))%m;
		else       return v%m;
	}

	public int color(int r, int g, int b) {
		return Color.rgb(r, g, b);
	}
	public int color(float r, float g, float b) {
		return Color.rgb((int)r, (int)g, (int)b);
	}
	
	public int colorHSL(int h, int s, int l) { 
		return Color.HSVToColor(new float[] {h*360/240, (float)s/240, (float)l/240});
	}
	public int colorHSL(float h, float s, float l) {
		return Color.HSVToColor(new float[] {h*360/240, s/240, l/240});
	}
	
	public int colorHSLEx(int h, float s, float l) {
		return Color.HSVToColor(new float[] {h, s, l});
	}

	public float rnd() {
		return (float)Math.random();
	}
	public int rnd(int n) {
		return (int)(Math.random() * n);
	}

	public int    abs(int    i) { return Math.abs(i); }
	public long   abs(long   l) { return Math.abs(l); } 
	public float  abs(float  f) { return Math.abs(f); }
	public double abs(double d) { return Math.abs(d); }
	

	public String strcat(String str1, String str2) {
		StringBuilder buf = new StringBuilder();
		buf.append(str1);
		buf.append(str2);
		return buf.toString();
	}
	

	
	// 型変換メソッド（文字⇒数値） ///////////////////////////////////////////////////////////
	public int strToInt(String s) {
		int i;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			i = -1;
		}
		return i;
	}
	public long strToLong(String s) {
		long l;
		try {
			l = Long.parseLong(s);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			l = 0xffffffffffffffffL;
		}
		return l;
	}
	public float strToFloat(String s) {
		float f;
		try {
			f = Float.parseFloat(s);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			f = -1f;
		}
		return f;
	}
	public double strToDouble(String s) {
		double d;
		try {
			d = Double.parseDouble(s);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			d = -1.0;
		}
		return d;
	}
	
	// 型変換メソッド（数値⇒文字） ///////////////////////////////////////////////////////////////////////
	public String intToStr(int i) { return Integer.toString(i); }
	public String longToStr(long l) { return Long.toString(l); }
	public String floatToStr(float f) { return Float.toString(f); }
	public String doubleToStr(double d) { return Double.toString(d); }
	
	// 型変換メソッド（様々なもの⇒文字） ///////////////////////////////////////////////////////////////////////
	public String toStr(Object obj) { return obj.toString(); }
	
	// 符号なしとしてIntに変換する ///////////////////////////////////////////////////////////////////////
	public int usByte(byte b) { return b & 0xFF; }
	public int usShort(short s) { return s & 0xFFFF; }
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	// キーボード判定
	public int getkey(int code) {
		return TGL.keyManager.getkey(code);
	}

	/** tx, ty が(r,t)-(l,b)の範囲内か？ */
	public boolean scopeXY(float tx, float ty, float r, float t, float l, float b) {
		return r <= tx && tx < l && t <= ty && ty < b;
	}
	/** tx, ty が(x,y)-(x+w,x+h)の範囲内か？ */
	public boolean scopeWH(float tx, float ty, float x, float y, float w, float h) {
		return x <= tx && tx < x+w && y <= ty && ty < y+h;
	}
	
	// print群 //////////////////////////////////////////////////////////////////////////////////////////////////
	public void printTS(String s) {
		TGL.output.printTS(s);
	}
	public void printT(String s) {
		TGL.output.printT(s);
	}
	
	public void print(byte b) {
		Log.v("TonyuALib print", Byte.toString(b));
	}
	public void print(char c) {
		Log.v("TonyuALib print", Character.toString(c));
	}
	public void print(short s) {
		Log.v("TonyuALib print", Short.toString(s));
	}
	public void print(int i) {
		Log.v("TonyuALib print", Integer.toString(i));
	}
	public void print(long l) {
		Log.v("TonyuALib print", Long.toString(l));
	}
	public void print(float f) {
		Log.v("TonyuALib print", Float.toString(f));
	}
	public void print(double d) {
		Log.v("TonyuALib print", Double.toString(d));
	}
	public void print(boolean b) {
		Log.v("TonyuALib print", Boolean.toString(b));
	}
	public void print(String s) {
		Log.v("TonyuALib print", s);
	}

	public void print(String tag, byte b) {
		Log.v(tag, Byte.toString(b));
	}
	public void print(String tag, char c) {
		Log.v(tag, Character.toString(c));
	}
	public void print(String tag, short s) {
		Log.v(tag, Short.toString(s));
	}
	public void print(String tag, int i) {
		Log.v(tag, Integer.toString(i));
	}
	public void print(String tag, long l) {
		Log.v(tag, Long.toString(l));
	}
	public void print(String tag, float f) {
		Log.v(tag, Float.toString(f));
	}
	public void print(String tag, double d) {
		Log.v(tag, Double.toString(d));
	}
	public void print(String tag, boolean b) {
		Log.v(tag, Boolean.toString(b));
	}
	public void print(String tag, String s) {
		Log.v(tag, s);
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 指定ミリ秒スリープ(スレッドの処理が止まるので注意) */
	public void sleep(long l) {
		try {
			Thread.sleep(l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
