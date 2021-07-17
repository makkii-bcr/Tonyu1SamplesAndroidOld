package tonyusamples.hockey2;

import android.view.KeyEvent;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSpriteChar;

public class Player extends TonyuSpriteChar {
	private float rs;
	private boolean touchSW = false;
	private boolean touchStart = true;
	public Player(float x, float y, int p) {
		super(x, y, p);
	}
	
	public void onAppear() {
		rs = 0.4f;
		setVisible(0);
		if (TGL.padPushCnt == 0) {
			touchSW = true;     // タッチ有効
			touchStart = false; // タッチしながらスタート
		}
	}
	
	public void loop() {
		if (touchStart && TGL.padPushCnt == 0) touchStart = false;
		if (!touchSW && !touchStart && TGL.padPushCnt >= 1) touchSW = true;
		if (touchSW) { // 画面に指を触れながらページ遷移したら、ラケットは動かさない
			GLB.racket.tx = (TGL.padX + TGL.viewX) * rs + GLB.racket.x * (1 - rs);
			GLB.racket.ty = (TGL.padY + TGL.viewY) * rs + GLB.racket.y * (1 - rs);
		}
		// ホームに戻る
		if (getkey(KeyEvent.KEYCODE_BACK) == 1) {
			TGL.projectManager.loadPage(tonyusamples.home.GLB.page_home);
		}
	}

}
