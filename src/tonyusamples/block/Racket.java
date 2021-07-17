package tonyusamples.block;

import android.graphics.Color;
import android.view.KeyEvent;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSpriteChar;

public class Racket extends TonyuSpriteChar {

	public Racket(float x, float y, int p) {
		super(x, y, p);
	}
	@Override
	public void onAppear() {
		TGL.map.setBGColor(Color.BLACK);
	}
	@Override
	public void loop() {
		x = TGL.padX;
		
		// ホームに戻る
		if (getkey(KeyEvent.KEYCODE_BACK) == 1) {
			// タイトル画面の場合ホームへ戻る
			if (TGL.projectManager.getCurrentPageName().getClass() == tonyusamples.block.PageTitle.class) {
				TGL.projectManager.loadPage(tonyusamples.home.GLB.page_home);
			} else { // タイトル画面以外はタイトル画面に戻る
				TGL.projectManager.loadPage(GLB.page_title);
			}
		}
	}
}
