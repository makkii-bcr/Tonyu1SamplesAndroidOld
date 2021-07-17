package tonyusamples.soccer;

import android.graphics.Color;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSpriteChar;

public class TPlayer extends TonyuSpriteChar {

	public TPlayer(float x, float y, int p) {
		super(x, y, p);
	}
	
	int state, pena, sdlim, rwidth, svp, dir;
	float range, decsp, spd, tx, ty, vx, vy, prx, pry;
	int loopSW = 0, nextState;

	float ox, oy;
	float halfy, i;
	int st1, hy;
	
	@Override
	public void onAppear() {
		GLB.kickc = 0;
		// range = rnd(TGL.screenWidth * 0.6f) + TGL.screenWidth * 0.2;
		range = x;
		decsp = 0.001f + rnd() * 0.002f;
		pena = 0;
		if (spd == 0f) spd = 3 + rnd(5);
		if (this == GLB.ctrl) spd = 20;
		if (sdlim == 0) sdlim = rnd(30);
		if (rwidth == 0) rwidth = 150 + rnd(100);
		svp = p;
		state = 2;
		nextState = 2;
	}
	
	@Override
	public void loop() {
		if (state == 0) {
			if (state == 0) mawarikomi();
			if (state == 1) setdir();
			if (state == 2) kick();
			if (state == 3) waitFor();
		} else if (state == 1) {
			if (state == 1) setdir();
			if (state == 2) kick();
			if (state == 3) waitFor();
			if (state == 0) mawarikomi();
		} else if (state == 2) {
			if (state == 2) kick();
			if (state == 3) waitFor();
			if (state == 0) mawarikomi();
			if (state == 1) setdir();
		} else if (state == 3) {
			if (state == 3) waitFor();
			if (state == 0) mawarikomi();
			if (state == 1) setdir();
			if (state == 2) kick();
		}
		
		myUpdate();
	}
	
	public void chkrange() {
		if (abs(x - range) > rwidth) nextState = 3;
	}
	
	public void mawarikomi() {
		//float ox, oy;
		//int i;

		if (loopSW == 0) {
			// ボールを蹴る準備。ボールをゴール方面に蹴れる位置まで移動
			i = 50;
			ox = -60 * dir;
			oy = rnd(20) + 20;
			
			nextState = 0;
			loopSW = 1;
		}

		//while (state == 0) {
		if (loopSW == 1) {
			if (nextState == 0) {
				if ((GLB.ball.y - y) * oy > 0) oy = -oy;
				tx = GLB.ball.x + ox;
				ty = GLB.ball.y + oy;
				chkrange();
				if (dist(tx - x, ty - y) < 5 || i < 0) nextState = 1;
				if ((GLB.ball.x - x) * dir > 0) i = i - 1;
			} else {
				state = nextState;
				loopSW = 0;
			}
		}
			// update();
		//}
	}
	
	public void setdir() {
		//float ox, oy, i, halfy;
		if (loopSW == 0) {
			// ボールを蹴る準備2　。蹴る方向を決める
			halfy = TGL.screenHeight / 2;
			i = 5 + rnd() * 10;
			ox = -30 * dir;
			oy = rnd(10) + 10;
			
			nextState = 1;
			loopSW = 1;
		}
		//while (state == 1) {
		if (loopSW == 1) {
			if (nextState == 1) {
				if ((GLB.ball.y - halfy) * oy < 0) oy = -oy;
				i = i - 1;
				tx = GLB.ball.x + GLB.ball.vx * 10 + ox;
				ty = GLB.ball.y + GLB.ball.vy * 10 + oy;
				if (dist(tx - x, ty - y) < 10 || i < 0) nextState = 2;
				chkrange();
			} else {
				state = nextState;
				loopSW = 0;
			}
		}
			//update();
		//}
	}
	
	public void kick() {
		//int halfy, st1;
		if (loopSW == 0) {
			// ボールを蹴る
			st1 = 0;
			halfy = TGL.screenHeight / 2;
			
			nextState = 2;
			loopSW = 1;
		}
		//while (state == 2) {
		if (loopSW == 1) {
			if (nextState == 2) {
				tx = GLB.ball.x + GLB.ball.vx * 10;
				ty = GLB.ball.y + GLB.ball.vy * 10;
				if ((y - ty) * (ty - halfy)/* * oy*/ < 0) {
					st1 += 1; if (st1 > sdlim) nextState = 1;
				} else st1 = 0;
				if ((tx - x) * dir < 0) nextState = 0;
				chkrange();
			} else {
				state = nextState;
				loopSW = 0;
			}
		}
			//update();
		//}
	}
	
	public void waitFor() {
		//int hy;
		if (loopSW == 0) {
			// ボールが自分の守備範囲外の場合、待機する
			hy = TGL.screenHeight / 2;
			ty = rnd(hy * 2);
			
			nextState = 3;
			loopSW = 1;
		}
		
		//while (state == 3) {
		if (loopSW == 1) {
			if (nextState == 3) {
				tx = range;
				if (abs(GLB.ball.x - range) < rwidth) nextState = 2;
			} else {
				state = nextState;
				loopSW = 0;
			}
		}
			//update();
		//}
	}
	
	public void myUpdate() {
		float d, a;
		if (GLB.ctrl == this && GLB.timer.t > 0) {
			// マウスで操作できるプレイヤーの動き
			x = TGL.padX + TGL.viewX;
			y = TGL.padY;
		}
		d = dist(tx - x, ty - y);
		if (d > 0.01f) {
			vx = vx + (tx - x) / d * spd / 10;
			vy = vy + (ty - y) / d * spd / 10;
		}
		vx = vx * 0.88f;
		vy = vy * 0.88f;
		if (rnd(40) < 1 && this != GLB.ctrl) spd = 3 + rnd() * 5;
		x = x + vx; y = y + vy;
		if (crashToOld(GLB.ball) && pena == 0) {
			// ボールに触れていて、ペナルティを与えられていないならばボールを蹴る
			GLB.ball.vx = GLB.ball.vx + (GLB.ball.x - x) * 0.1f;
			GLB.ball.vy = GLB.ball.vy + (GLB.ball.y - y) * 0.1f;
			GLB.ball.lastdir = dir; // 最後にボールを蹴ったチームを自分のチーム(dir)にする
			if (GLB.kickc == 0) {
				TGL.mplayer.playSE("kick");
				GLB.kickc = 1;
			}
		}
		if (GLB.ball.pena == dir) { pena = 40; }
		// pena>0ならばペナルティ中。
		if (pena > 0) { pena = pena - 1; }
		// ペナルティ中は点滅する
		setVisible(1 - (pena % 2));
		if (GLB.timer.t <= 0) { nextState = -1; tx = TGL.screenWidth * (0.5f + dir); }
		
		// 移動方向(a)によりキャラクタパターンをセットする
		a = angle(x - prx, y - pry) + 180; prx = x; pry = y;
		f = 0;
		if (a < 45 || a > 270 + 45) { p = GLB.pat_chars + 7; f = 1; }
		if (a > 45 && a < 90 + 45) { p = GLB.pat_chars + 9; }
		if (a > 90 + 45 && a < 180 + 45) { p = GLB.pat_chars + 7; }
		if (a > 180 + 45 && a < 270 + 45) p = GLB.pat_chars + 5;
		p += trunc(GLB.anc); if (dir < 0) p += 6;
	}
	
}
