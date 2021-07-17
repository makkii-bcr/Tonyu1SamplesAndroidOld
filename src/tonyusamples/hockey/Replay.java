package tonyusamples.hockey;

import tonyu.kernel.TGL;
import tonyu.kernel.TonyuTextChar;
import android.graphics.Color;
import android.view.KeyEvent;

public class Replay extends TonyuTextChar {
	
	public Replay(float x, float y, String text, int col, float size) {
		super(x, y, text, col, size);
	}
	
	int cnt, padPushCnt;
	
	@Override
	public void onAppear() {
		setVisible(0);
		cnt = 0;
	}

	@Override
	public void loop() {
		cnt --;
		if (cnt == 0) {
			replay();
		}
		
		if (getVisible() == 1) {
			if (TGL.padPush == 1) {
				padPushCnt ++;
				if (padPushCnt == 1) checkMouseDown();
			} else {
				padPushCnt = 0;
			}
		}
		
		// ホームに戻る
		if (getkey(KeyEvent.KEYCODE_BACK) == 1) {
			TGL.projectManager.loadPage(tonyusamples.home.GLB.page_home);
		}
		
	}
	
	public void show() {
		setVisible(1);
		cnt = 600;
	}
	
	public void replay() {
		GLB.tokuten_1.setValue(GLB.pat_tokuten + 0);
		GLB.tokuten.setValue(GLB.pat_tokuten + 0);
		GLB.ball.myNotify();
		cnt = 0;
		setVisible(0);
	}
	
	public void checkMouseDown() {
		if (scopeWH(TGL.padX, TGL.padY, x, y, getWidth(), getHeight())) {
			replay();
		}
	}
	
}
