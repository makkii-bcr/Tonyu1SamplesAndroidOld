package tonyusamples.soccer;

import android.view.KeyEvent;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuTextChar;

public class Timer extends TonyuTextChar {

	public Timer(float x, float y, String text, int col, float size) {
		super(x, y, text, col, size);
	}
	
	int t, loopSW = 0, waitCnt = 0;
	
	@Override
	public void onAppear() {
		t = 90;
	}
	
	@Override
	public void loop() {
		if (loopSW == 0) {
			if (t <= 0) { setVisible(0); loopSW = 2; }
			text = intToStr(t);
			t -= 1;
			loopSW = 1;
		} else if (loopSW == 1) {
			if (++waitCnt > 60) {
				waitCnt = 0;
				loopSW = 0;
			}
		}
		
		// ホームに戻る
		if (getkey(KeyEvent.KEYCODE_BACK) == 1) {
			TGL.projectManager.loadPage(tonyusamples.home.GLB.page_home);
		}
	}

}
