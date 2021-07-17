package tonyu.kernel;

import android.view.KeyEvent;

public class TonyuKeyboardManager {
	private boolean backkeyExit = true;
	private boolean keyStateAry[];
	private int keyFrameAry[];
	private int keyCntAry[];
	private final int KEY_MAX = 256;
	private int frameCnt = 0;
	public TonyuKeyboardManager() {
		keyStateAry = new boolean[KEY_MAX];
		for (int i=0; i<KEY_MAX; i++) {
			keyStateAry[i] = false;
		}
		keyFrameAry = new int[KEY_MAX];
		for (int i=0; i<KEY_MAX; i++) {
			keyFrameAry[i] = -1;
		}
		keyCntAry = new int[KEY_MAX];
		for (int i=0; i<KEY_MAX; i++) {
			keyCntAry[i] = -1;
		}
	}
	
	public boolean dispatchKeyEvent(KeyEvent event) {
		
    	int action = event.getAction(); 
    	int code = event.getKeyCode();
        if      (action == KeyEvent.ACTION_DOWN) keyStateAry[code] = true;
        else if (action == KeyEvent.ACTION_UP)   keyStateAry[code] = false;
        
        switch (code) {
        case KeyEvent.KEYCODE_BACK: // BACKキー
        	if (backkeyExit) return false; else return true;
        default:
            return false;
        }
	}
	
	// Backキーでアプリが終了するのを有効／無効にする
	public void setBackKeyExit(boolean b) {
		backkeyExit = b;
	}
	
	public int getkey(int code) {
		if (code < 0 || KEY_MAX <= code) return -1;
		if (keyFrameAry[code] != TGL.frameCount) setFrameCnt(code); // フレームがわかっていたら更新
		return keyCntAry[code];
	}
	public void setFrameCnt(int code) {
		keyFrameAry[code] = TGL.frameCount;
		keyCntAry[code] = (keyStateAry[code] ? ++(keyCntAry[code]) : 0);
	}
}
