package tonyusamples.block;

import android.view.KeyEvent;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSecretChar;
import tonyu.kernel.TonyuTextBitmap;

public class Press extends TonyuSecretChar {
	private int i;
	private int loop = 0, loop2 = 0;
	private int tapTime = 9999;
	private int pushTime = 9999;
	private int tapCnt = 0;
	private TonyuTextBitmap text;
	
	public Press(float x, float y) {
		super(x, y);
	}
	
	@Override
	public void onAppear() {
		text = new TonyuTextBitmap();
	}
	
	@Override
	public void loop() {
		loop0 : for(int a=0; a<1; a++) {
			if (loop2 == 0) {
				if (loop == 0) {
					i = 20;
				}
				if (i > 0) loop = 1; else { loop = 0; loop2 = 1;}
				if (loop == 1) {
					//drawText(x,y,"Double Tap to Start",color(255,255,255),26,-10);
					text.drawText(x,y,"Double Tap to Start",color(255,255,255),26,-10);
					i -= 1;
				}
			}
			if (loop2 == 1) {
				if (loop == 0) {
					i = 20;
				}
				if (i > 0) loop = 1; else { loop = 0; loop2 = 0; i--; continue loop0;}
				if (loop == 1) {
					//drawText(x,y,"Click Mouse to Start",color(255,255,255),20,-10);
					i -= 1;
				}
			}
		}
		
		// ダブルタップを監視する
		if (TGL.padPush == 1) {
			//if (scopeXY(TGL.padX, TGL.padY, x, y, x+250, TGL.screenHeight)) {
				if (pushTime == 0) { tapCnt++; tapTime=0; }
				if (tapCnt >= 2) TGL.projectManager.loadPage(GLB.page_stage1);
				pushTime ++;
			//}
		} else {
			pushTime=0;
		}
		tapTime++;
		if (tapTime >= 20) tapCnt=0;
		//print(TGL.padX+" "+TGL.padY+" "+x+" "+y+" "+tapCnt+" "+tapTime);
		
		
		// テストステージ
		//print(getkey(KeyEvent.KEYCODE_MENU));
		if (getkey(KeyEvent.KEYCODE_MENU) == 60) {
			TGL.projectManager.loadPage(GLB.page_test);
		}
	}
	
	@Override
	public void onRelease() {
		text.delete();
	}
}
