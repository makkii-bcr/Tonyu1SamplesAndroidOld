package tonyusamples.hockey;

import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSpriteChar;

public class Racket extends TonyuSpriteChar {

	public Racket(float x, float y, int p) {
		super(x, y, p);
	}
	
	float px, py, d, spd, avx, avy;
	int sew;
	
	@Override
	public void onAppear() {
		px = x;
		py = y;
	}
	
	@Override
	public void loop() {
		sew -= 1;
		if (crashTo(GLB.ball)) {
			// ボールにぶつかったときの処理
			if (sew <= 0) { sew = 8; TGL.mplayer.playSE("shot"); }
			avx = (GLB.ball.x - x) / d;
			avy = (GLB.ball.y - y) / d;
			spd = 1;
			if (d < 32) {
				// ボールとの距離が32以下の場合、ラケットとボールがめりこんでいるのを修正する
				GLB.ball.x = x + avx * 32;
				GLB.ball.y = y + avy * 32;
				spd = dist(GLB.ball.vx, GLB.ball.vy) / 2;
			}
			// ボールに力を与える
			GLB.ball.vx += avx * spd + (x - px) * 0.1f;
			GLB.ball.vy += avy * spd + (y - py) * 0.1f;
		}
		px = x; // 現在のボールの位置を覚える
		py = y;
	}
	
	
	public boolean crashTo(Ball t) {
		d = dist(t.x - x, t.y - y) + 1;
		return (d < (dist(x - px, y - py) + dist(GLB.ball.vx, GLB.ball.vy)) / 2 + 32);
	}
	
}
