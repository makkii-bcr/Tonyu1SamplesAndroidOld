package tonyusamples.ribbon;

import android.graphics.Color;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSpriteChar;

public class Star extends TonyuSpriteChar {


	public Star(float x, float y, int p, int f) {
		super(x, y, p, f);
	}
	public Star(float x, float y, int p, int f, int zOrder) {
		super(x, y, p, f, zOrder);
	}
	

	private float cy;
	private int bp;
	private int an;
	private float maxs;
	private float maxlife;
	private float life;
	private float vx, vy;
	
	@Override
	public void onAppear() {
		cy = GLB.level;
		bp = p;
		an = 0;
		GLB.mu = 0.99f;
		maxs = 6;
		maxlife = 100;
		life = maxlife * 0.5f;
		vx = 0; vy = -1;
		y = -150;
	}
	
	@Override
	public void loop() {
		//while ($time>0.8) {
		if (GLB.time <= 0.8f) { 
			GLB.rank.rank = 0;
			crash();
			die();
			return;
		}
		
		// アニメーション
		an = an + 1;
		if (an > 2) an = 0;
		p = bp + an;
		// 画面端にぶつかったら跳ね返る
		if (x < 0) vx = abs(vx);
		if (x > TGL.screenWidth) vx = -abs(vx);
		if (y > TGL.screenHeight) vy = -abs(vy);
		if (y < -110) vy = 1;
		// 空気抵抗を受ける。$muは抵抗係数
		x += vx; vx = vx * GLB.mu;
		y += vy; vy = vy * GLB.mu;
		// 当たり判定
		ath();
		// 落下
		vy = vy + 0.03f;
		if (vy >  maxs) vy =  maxs;
		if (vy < -maxs) vy = -maxs;
		if (vx >  maxs) vx =  maxs;
		if (vx < -maxs) vx = -maxs;
		// ライフが少しずつ回復する
		if (life < maxlife) life += 0.5f;
		// ライフゲージ表示
		drawLine(0, TGL.screenHeight - 10 - cy, life, TGL.screenHeight - 10 - cy, color(0, 255 - cy * 20, 0));
		
		if (life < 0) {
			// ライフが0になったら破壊される。画面上方に移動
			crash();
			life = maxlife;
			appear(new PTS(x, y + 30, GLB.rank.rank*4, GLB.gcont));
			y = -30; vy = 0;
			GLB.score = GLB.score + 50 + GLB.rank.rank * 4 * GLB.gcont;
			GLB.time = GLB.time + GLB.rank.rank * 0.05f * GLB.gcont;
		}
		
		//if (y > TGL.screenHeight -100) y = TGL.screenHeight -100; // チートコード
		if (y>TGL.screenHeight) {
			// 下に落ちたらゲームオーバー
			y=-30;vy=0;
			GLB.time = GLB.time*0;//$time-1-$score/1500;
			TGL.mplayer.stopBGM();
		} 
		
		//update();
		//}
	}
	
	@Override
	public void onDead() {
		
	}
	
	public void crash() {
		int i = 0;
		// ボールを破壊する
		i = 0;
		GLB.exp += 1; // 経験値 +1
		// ランク（マウスの静止時間）によりbad,bood,greatを判断する
		if (GLB.rank.rank < 10) {
			// Bad
			TGL.mplayer.playSE("bad");
			GLB.preg = 0;
			GLB.gcont = 1;
			// 破片を6個出現させる
			while (i < 360) {
				appear(new TBomb(x, y, cos(i) * 5, sin(i) * 5));
				i = i + 60;
			}
		} else if (GLB.rank.rank < 20) {
			// Good
			TGL.mplayer.playSE("good");
			GLB.preg = 0;
			GLB.gcont = 1;
			// 破片を12個出現させる
			while (i < 360) {
				appear(new TBomb(x, y, cos(i) * 10, sin(i) * 10));
				i = i + 30;
			}
		} else {
			// Great。  画面がフラッシュする
			GLB.Flsh.doFlash();
			TGL.mplayer.playSE("great");
			// GLB.pregが1なら前回もGreatなので、Greatコンボ数(GLB.gcont)を増やす
			if (GLB.preg != 0) GLB.gcont = GLB.gcont + 1; else GLB.gcont = 1;
			GLB.preg = 1;
			// 破片を18個出現させる
			while (i < 360) {
				appear(new TBomb(x, y, cos(i) * 5, sin(i) * 5));
				i = i + 60;
			}
			i = 15;
			while (i < 360) {
				appear(new TBomb(x, y, cos(i) * 10, sin(i) * 10));
				i = i + 30;
			}
		}
		// Bad Good Great(+コンボ数) を表示する
		appear(new TRank2(x, y, GLB.rank.rank, GLB.gcont));	
	}
	
	public boolean crashTo2(Pend t) {
		return (dist(t.x - x, t.y - y) < 35);
	}
	
	public void ath() {
		int i;
		float dl = 0, dm;
		Pend t = null, pt = null;
		
		// リボンとボールのあたり判定
		i = GLB.ths.size() - 1;
		while (i >= 0) {
			// GLB.ths(リボンのパーツを格納した配列)の各要素をtに代入しながら繰り返す
			t = GLB.ths.get(i);
			if (crashTo2(t)) {
				// tとぶつかっていたら、力を受ける
				vx = vx + (x - t.x) * 0.4f;
				vy = vy + (y - t.y) * 0.4f;
				dl = 1;
				if (pt != null) {
					dm = dist(pt.x - t.x, pt.y - y) * 0.8f + 20;
					// Log.v("Star.dm",""+dm);
					if (dm > dl) dl = dm;
				}
			}
			pt = t;
			i = i - 1;
		}
		if (dl != 0) {
			// ぶつかっていたら、lifeを減らす
			 if (GLB.kasc == 0) TGL.mplayer.playSE("kasuri"); else TGL.mplayer.playSE("kasuri2");
			GLB.kasc = 1 - GLB.kasc;
			appear(new TBomb(x, y, -vx, -vy));
			life = life - dl * 0.15f;
			GLB.score = GLB.score + 1;
		}
	}

	@Override
	public int color(int r, int g, int b) {
		int col, r2, g2, b2;
		col = (int)(r + g * 256 + b * 65536);
		r2 = col         % 256; if (r2 < 0) r2 = 0; if (r2 > 255) r2 = 255;
		g2 = col /   256 % 256; if (g2 < 0) g2 = 0; if (g2 > 255) g2 = 255;
		b2 = col / 65536 % 256; if (b2 < 0) b2 = 0; if (b2 > 255) b2 = 255;
		if (col >= 0) return Color.rgb(r2, g2, b2);
		else          return Color.rgb(212, 208, 200);
	}
	@Override
	public int color(float r, float g, float b) {
		int col, r2, g2, b2;
		col = (int)(r + g * 256 + b * 65536);
		r2 = col         % 256; if (r2 < 0) r2 = 0; if (r2 > 255) r2 = 255;
		g2 = col /   256 % 256; if (g2 < 0) g2 = 0; if (g2 > 255) g2 = 255;
		b2 = col / 65536 % 256; if (b2 < 0) b2 = 0; if (b2 > 255) b2 = 255;
		if (col >= 0) return Color.rgb(r2, g2, b2);
		else          return Color.rgb(212, 208, 200);
	}
}