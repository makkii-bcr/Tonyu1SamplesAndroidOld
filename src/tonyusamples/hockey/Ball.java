package tonyusamples.hockey;

import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSpriteChar;

public class Ball extends TonyuSpriteChar {

	public Ball(float x, float y, int p) {
		super(x, y, p);
	}
	
	float vx, vy, spd;
	int loopSW = 0, i, s, waitCnt;
	
	@Override
	public void onAppear() {
		vx = 1f;
		vy = 1f;
		GLB.cy = TGL.screenHeight / 2;
		i = 0;
	}
	
	@Override
	public void loop() {
		if (loopSW == 0) {
			for (int i=0; i<1; i++) {
				x += vx;
				y += vy;
				if (x < 32) {
					// 左端に来たとき、得点してなければ跳ね返る
					if (!tokuten(1)) {
						TGL.mplayer.playSE("bound");
						vx = abs(vx);
						x = 32;
					} else break;
				}
				if (x > TGL.screenWidth - 32) {
				    // 右端に来たとき、得点してなければ跳ね返る
					if (!tokuten(-1)) {
						TGL.mplayer.playSE("bound");
						vx = -abs(vx);
						x = TGL.screenWidth - 32;
					} else break;
				}
				// 上下の跳ね返り
				if (y < 32) {
					TGL.mplayer.playSE("bound");
					vy = abs(vy);
					y = 32;
				}
				if (y > TGL.screenHeight - 32) {
					TGL.mplayer.playSE("bound");
					vy = -abs(vy);
					y = TGL.screenHeight - 32;
				}
				// 摩擦による減速
				spd = dist(vx, vy);
				vx = vx * 0.99f;
				vy = vy * 0.99f;
				// 速度30以上にならないようにする
				if (spd > 30) {
					vx = vx * 30 / spd;
					vy = vy * 30 / spd;
				}
			}
		}
		
		if (loopSW == 1) {
			
			if (i >= 1) vy = vy / 2;
			if (i < 10) {
				i += 1;
				x += vx; y += vy;
			} else {
				TGL.mplayer.playSE("jingle");
				// どちらに入ったかにより、得点をふやす
				if (s < 0) { GLB.tokuten.setValue(GLB.tokuten.value + 1); }
				else { GLB.tokuten_1.setValue(GLB.tokuten_1.value + 1); }
				x -= s * 150;
				loopSW = 2;
			}
		}
		
		if (loopSW == 2) {
			if (++waitCnt > 60) {
				waitCnt = 0;
				loopSW = 3;
			}
		}
		
		if (loopSW == 3) {
			if (GLB.tokuten_1.value >= GLB.pat_tokuten+7 ||
				GLB.tokuten.value >= GLB.pat_tokuten+7) {
				GLB.replay_1.show();
				loopSW = 4;
			} else {
				loopSW = 5;
			}
		}
		
		if (loopSW == 5) {
			x = GLB.cx - s * 200;
			vx = 0; vy = 0; y = rnd(200) - 100 + GLB.cy;
			loopSW = 0;
		}
	}
	
	
	public boolean tokuten(int _s) {
		// 左右のあいているところにはいれば得点とみなし、1を返す。それ以外は0をかえす
		// s: 入った場所 s=1 左端   s=-1 右端
		if (abs(y - TGL.screenHeight / 2) < TGL.screenHeight / 5) {
			TGL.mplayer.playSE("in");
			i = 0;
			s = _s;
			loopSW = 1;
			return true;
		}
		return false;
	}
	
	
	public void myNotify() {
		if (loopSW == 4) loopSW = 5;
	}
	
}
