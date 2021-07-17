package tonyusamples.hockey2;

import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSpriteChar;

public class Pass extends TonyuSpriteChar {
	int enabled;
	private int laps;
	private int time;

	private final float ZOOM_MIN = 1.0f;
	private final float ZOOM_MAX = 1.5f;
	private final float ZOOM_UP   = 0.005f;
	private final float ZOOM_DOWN = 0.01f;
	private final int ZOOM_WAIT = 180;
	private int zoomSW = 1;
	private float zoomCnt = 1;
	private int zoomWaitTime = 0;
	private int dfScreenWidth, dfScreenHeight;
	private int loop = 0;
	
	
	// 1周したところに配置される
	public Pass(float x, float y, int p) {
		super(x, y, p);
	}
	
	@Override
	public void onAppear() {
		laps = 0;
		time = 0;
		dfScreenWidth  = TGL.screenWidth;
		dfScreenHeight = TGL.screenHeight;
	}
	
	private int cnt = 0;
	private int lapEndSW = 0;
	@Override
	public void loop() {

		if (lapEndSW == 0) {
			if (loop == 0) {
				if (cnt++ >= 60) {
					cnt = 0;
					loop = 1;
				}
			} else {
				if (laps>=3) { // 終了
					zoomSW = -1;
					setVisible(0);
					GLB.ball.die();
					TGL.mplayer.stopBGM();
					lapEndSW = 1;
				}
				if (laps<3) {
					GLB.tokuten.incValue();
					time+=1;
				}
				loop = 0;
			}
		}
		//zoom();
	}
	@Override
	public void onDead() {
		GLB.ball.die();
		TGL.mplayer.stopBGM();
	}
	
	

	private String toSec(int s) {
		if (s<10) return strcat("0", intToStr(s));
		return intToStr(s);
	}
	void lap() {
		Lap tl = null;
		if (enabled == 0) return;
		TGL.mplayer.playSE("jingle");
		enabled=0;
		laps+=1;
		if (laps==1) tl=GLB.lap1;
		if (laps==2) tl=GLB.lap2;
		if (laps==3) tl=GLB.lap3;
		if (tl != null) {
			tl.text = tl.text+trunc(time/60)+":"+toSec(time % 60);
			time=0;
		}
		zoomSW = 0;
	}
	
	private void zoom() {
		if (zoomSW == 1) {
			if (zoomCnt >= ZOOM_MAX) {
				zoomCnt = ZOOM_MAX;
			} else {
				zoomCnt += ZOOM_UP;
			}
		} else {
			if (zoomWaitTime == 0) {
				if (zoomCnt <= ZOOM_MIN) {
					zoomCnt = ZOOM_MIN;
					if (zoomSW == 0) zoomWaitTime = 1;
				} else {
					zoomCnt -= ZOOM_DOWN;
				}
			} else {
				if (zoomWaitTime++ >= ZOOM_WAIT) {
					zoomWaitTime = 0;
					zoomSW = 1;
				}
			}
		}
		int afterSW = (int)(dfScreenWidth  / zoomCnt);
		int afterSH = (int)(dfScreenHeight / zoomCnt);
		TGL.screenManager.zoomScreen(afterSW, afterSH);
	}
	
}
