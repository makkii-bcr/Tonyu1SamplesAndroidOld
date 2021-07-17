package tonyusamples.hockey;

import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSpriteChar;

public class Enemy extends TonyuSpriteChar {

	public Enemy(float x, float y, int p) {
		super(x, y, p);
	}
	
	int dir = 0, t, loopSW = 0, loopSW2 = 0, i;
	float ty, a;
	Racket tracket;
	@Override
	public void onAppear() {
		p = -1;
		if (dir == 0f) dir = -1;
		GLB.us = 16;
		GLB.ds = TGL.screenHeight - 16;
		GLB.cx = TGL.screenWidth / 2;
		
	}
	
	@Override
	public void loop() {
		if (loopSW == 0) {
			if (ty > GLB.us && ty < GLB.ds) y = ty * 0.05f + y * 0.95f;
			if (x < 0) x = 0;
			if (x > TGL.screenWidth) x = TGL.screenWidth;
			if (dist(GLB.ball.vx, GLB.ball.vy * 10) < 50f) {
				if (GLB.ball.x - dir * 16 > x && (GLB.ball.x - GLB.cx) * dir <= 0) x += 2;
				if (GLB.ball.x - dir * 16 < x && (GLB.ball.x - GLB.cx) * dir <= 0) x -= 2;
				if (GLB.ball.y > y) ty += 2;
				if (GLB.ball.y < y) ty -= 2;
			}
			if (GLB.ball.vx * dir < -0.001f) {
				ty = GLB.ball.y + GLB.ball.vy * abs(x - GLB.ball.x) / abs(GLB.ball.vx);
				loopSW = 1;
			} else {
				loopSW = 2;
			}
		}

		if (loopSW == 1) {
			if ((ty < GLB.us || ty > GLB.ds) && abs(GLB.ball.vx) > 1) {
				if (ty < GLB.us) ty = -(ty - GLB.us) + GLB.us;
				if (ty > GLB.ds) ty = -(ty - GLB.ds) + GLB.ds;
			} else {
				loopSW = 2;
			}
		}

		if (loopSW == 2) {
			if (loopSW2 == 0) {
				if ((x - GLB.ball.x) * dir < 0.01f) {
					if (dist(GLB.ball.x - x, GLB.ball.y - y) < 80f) attack();
				}
				if (loopSW2 == 0) loopSW = 0;
			}

			if (loopSW2 == 1) {
				if (i < 10) {
					if (rnd(2) == 0) a = angle(GLB.ball.x - x, GLB.ball.y - y);
					x += cos(a) * 16; y += sin(a) * 16;
					i += 1;
				} else {
					t = TGL.screenWidth / 2 - dir * (rnd(100) + 150);
					loopSW2 = 2;
				}
			}

			if (loopSW2 == 2) {
				if ((x - t) * dir > 0) {
					x -= 16 * dir;
				} else {
					loopSW2 = 0;
					loopSW = 0;
				}
			}
			
		}
		
		myUpdate();
	}
	
	public void attack() {
		i = 0;
		a = angle(GLB.ball.x - x, GLB.ball.y - y);
		loopSW2 = 1;
	}
	
	public void myUpdate() {
		if (dir > 0 && GLB.player.manPlay == 1) return;
		tracket.y = y;
		if ((x - GLB.cx) * dir <= 0) tracket.x = x;
	}
}
