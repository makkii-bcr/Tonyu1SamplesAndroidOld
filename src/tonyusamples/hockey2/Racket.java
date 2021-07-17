package tonyusamples.hockey2;

import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSpriteChar;

public class Racket extends TonyuSpriteChar {
	private float px, py, avx, avy, d, sew, spd, scx, scy;
	float tx, ty;
	private int loop = 0;

	public Racket(float x, float y, int p) {
		super(x, y, p);
	}
	
	@Override
	public void onAppear() {
		px = x;
		py = y;
	}
	
	@Override
	public void loop() {
		if (loop == 0) {
			px = x; py = y;
			loop = 1;
		} else {
			sew -= 1;
			if (crashTo(GLB.ball)) {
				// ボールにぶつかったときの処理
				if (sew<=0) { sew=8; TGL.mplayer.playSE("shot");}
				avx=(GLB.ball.x-x)/d;
				avy=(GLB.ball.y-y)/d;
				spd = 1;
				if (d<32) {
					// ボールとの距離が32以下の場合、ラケットとボールがめりこんでいるのを修正する
					GLB.ball.x=x+avx*32;
					GLB.ball.y=y+avy*32;
					spd=dist(GLB.ball.vx,GLB.ball.vy)/2;
				}
				// ボールに力を与える
				GLB.ball.vx += avx*spd+(x-px)*0.1f;
				GLB.ball.vy += avy*spd+(y-py)*0.1f;
			}
			// tx,tyはマウスのx,y座標になっている (tx,tyは$Playerによって更新されている)
			if (dist(tx-x,ty-y)<40) {
				// tx,tyが壁でなかったらその場所へ移動
				if (!kabe(x,ty)) {
					y=ty;
				}
				if (!kabe(tx,y)) {
					x=tx;
				}
			}
			// 画面スクロール。
			scx = scx * 0.95f + x * 0.05f;
			scy = scy * 0.95f + y * 0.05f;
			TGL.map.scrollTo(scx - TGL.screenWidth / 2, scy - TGL.screenHeight / 2);

			px = x; py = y;
		}
	}
	
	@Override
	public void onDead() {
		
	}
	
	public boolean crashTo(Ball t) {
		if (t.isDead()) return false;
		d = dist(t.x-x, t.y-y)+1;
		return (d<(dist(x-px,y-py)+dist(GLB.ball.vx,GLB.ball.vy))/2+32);
	}
	
	public boolean kabe(float xx, float yy) {
		int pa;
		pa = TGL.map.getAt(xx,yy);
		return (pa < GLB.pat_table+9 || pa > GLB.pat_table+14);
	}
	
}
