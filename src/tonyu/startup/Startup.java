package tonyu.startup;

import android.graphics.Color;
import android.view.KeyEvent;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSecretChar;
import tonyu.kernel.TonyuTextBitmap;

public class Startup extends TonyuSecretChar {
	private int cnt = 0, barCnt = 0, barWaitTime = 0;
	private float barR = 0.0f;
	private final int BLACK_BACK = 5;
	private final int TEXT_APPEAR_CNT = 10;
	private final int BLACK_BACK_CNT = 30;
	private final int BAR_CNT = 120;
	private final int BAR_WAIT_CNT = 30;
	private final int LOAD_CNT = 90;
	private TonyuTextBitmap text;
	
	public Startup(float x, float y) {
		super(x, y);
	}
	
	@Override
	public void onAppear() {
		TGL.map.setBGColor(Color.WHITE);
		text = new TonyuTextBitmap();
	}
	@Override
	public void loop() {
		if (getkey(KeyEvent.KEYCODE_BACK) == 1) TGL.system.exit();
		
		if (cnt == BLACK_BACK) TGL.map.setBGColor(Color.BLACK);
		if (cnt <= BLACK_BACK_CNT) fillRect(183, 121, 183+193, 121+114, Color.WHITE);
		if (TEXT_APPEAR_CNT <= cnt && cnt <= BLACK_BACK_CNT + BAR_CNT + BAR_WAIT_CNT) text.drawText(183+10, 121+114-61, "Now Loading.....", Color.BLACK, 22);
		if (BLACK_BACK_CNT <= cnt && cnt < BLACK_BACK_CNT + BAR_CNT) {
			if (cnt == BLACK_BACK_CNT) TGL.map.setBGColor(Color.WHITE);
			if (barWaitTime <= 0) {
				barR = (float)barCnt / BAR_CNT;
				if ((int)(Math.random() * 30) == 0) barWaitTime = (int)(Math.random() * 30);
			} else {
				barWaitTime --;
			}
			fillRect(183, 121+114-4, (int)(183+193*barR), 121+114, color(163, 244, 208));
			barCnt ++;
		} else if (BLACK_BACK_CNT <= cnt && cnt < BLACK_BACK_CNT + BAR_CNT + BAR_WAIT_CNT) {
			fillRect(183, 121+114-4, (int)(183+193), 121+114, color(163, 244, 208));
			barCnt ++;
		} else if (BLACK_BACK_CNT + BAR_CNT <= cnt && cnt < BLACK_BACK_CNT + BAR_CNT + BAR_WAIT_CNT + LOAD_CNT) {
			int loadLED;
			if (rnd(2) == 0) loadLED = color(17, 255, 17);
			else             loadLED = color(0, 17, 0);
			fillRect(TGL.screenWidth-24, TGL.screenHeight-16, TGL.screenWidth-8, TGL.screenHeight-8, loadLED);
		} else if (BLACK_BACK_CNT + BAR_CNT + LOAD_CNT <= cnt) {
			TGL.projectManager.loadPage(tonyusamples.home.GLB.page_home);
		}

		cnt ++;
	}
	
	@Override
	public void onRelease() {
		text.delete();
	}
}
