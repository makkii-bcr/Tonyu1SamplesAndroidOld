package tonyusamples.ribbon;

import tonyu.kernel.*;
import android.graphics.Color;

public class Pend extends TonyuSpriteChar {
	// リボンのパーツ(節点)。このオブジェクトをたくさん並べてリボンを構成する
	//public float x,y;
	private float mu = 0.70f;
	private float ospd = 0.1f;
	private float spd = ospd;
	private float thr = 0;
	private float grav = 0.3f;
	private float cspd;
	private float acc;
	private float vx,vy;
	private float tx,ty;
	public Pend t;
	
	
	public Pend(float x, float y) {
		super(x, y);
	}

	@Override
	public void loop() {
		cspd = vx * vx + vy * vy;
		// tは次のパーツ。tがない部分はマウスによって位置を指定する。
		if (t != null) {
			tx = t.x; ty = t.y;
		} else {
			tx = TGL.padX;
			ty = TGL.padY;
		}
		// 次のパーツの位置まで移動しようとする
		vx += (tx - x) * spd;
		vy += (ty - y) * spd + grav;

		/*
		if (x>$ScreenWidth) {x=$ScreenWidth;vx=-abs(vx);}
		if (y>$ScreenHeight) {y=$ScreenHeight;vy=-abs(vy);}
		if (x<0) {x=0;vx=abs(vx);}
		if (y<0) {y=0;vy=abs(vy);}
		*/
		vx = vx * mu;
		vy = vy * mu;
		
		x += vx;
		y += vy;
		if(cspd < thr * thr) {
			spd = ospd;
			acc = rnd() * rnd() * 8;
			vx  = vx * acc;
			acc = rnd() * rnd() * 8;
			vy  = vy * acc;
			thr = 1.5f + rnd();
		}
		p = -1;
	}

	@Override
	public void draw(Object drawObj) {
		if (t != null) {
			tx = t.x; ty = t.y;
		}
		drawLine(x, y, tx, ty, color(trunc(cspd * 20), trunc(255 - cspd * 20), 255));
	}
	
	@Override
	public int color(int r, int g, int b) {
		int col, r2, g2, b2;
		col = (int)(r + g * 256 + b * 65536);
		r2 = col         % 256; if (r2 < 0) r2 = 0; if (r2 > 255) r2 = 255;
		g2 = col /   256 % 256; if (g2 < 0) g2 = 0; if (g2 > 255) g2 = 255;
		b2 = col / 65536 % 256; if (b2 < 0) b2 = 0; if (b2 > 255) b2 = 255;
		return (int)(Color.rgb(r2, g2, b2));
	}
	@Override
	public int color(float r, float g, float b) {
		int col, r2, g2, b2;
		col = (int)(r + g * 256 + b * 65536);
		r2 = col         % 256; if (r2 < 0) r2 = 0; if (r2 > 255) r2 = 255;
		g2 = col /   256 % 256; if (g2 < 0) g2 = 0; if (g2 > 255) g2 = 255;
		b2 = col / 65536 % 256; if (b2 < 0) b2 = 0; if (b2 > 255) b2 = 255;
		return (int)(Color.rgb(r2, g2, b2));
	}
}
