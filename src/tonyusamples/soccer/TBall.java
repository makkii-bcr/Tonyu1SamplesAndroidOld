package tonyusamples.soccer;

import android.graphics.Color;
import android.view.KeyEvent;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSpriteChar;
import tonyu.kernel.TonyuTextBitmap;

public class TBall extends TonyuSpriteChar {

	public TBall(float x, float y, int p) {
		super(x, y, p);
	}
	
	float vx, vy;
	int pena, lastdir;
	int lsc, rsc;
	int loopSW = 0, loopCnt = 0, cnt = 0;

	private TonyuTextBitmap textLsc, textRsc;
	
	@Override
	public void onAppear() {
		rsc = 0;
		lsc = 0;
		
		// プレイヤーがマウスで操作できる選手を$ctrlにセットする
		GLB.ctrl = GLB.pl1_3;
		GLB.ctrl.spd = 20;

		textLsc = new TonyuTextBitmap();
		textRsc = new TonyuTextBitmap();
		
		vx = 0f; vy = 0f;
		TGL.mplayer.playSE("beep");
	}
	
	@Override
	public void loop() {
		if (loopSW == 0) {
			if (cnt > 0) {
				if (GLB.kickc != 0) GLB.kickc += 1;
				if (GLB.kickc > 8) GLB.kickc = 0;
				// ボールの位置にあわせてスクロールする
				TGL.map.scrollTo(trunc((x - TGL.screenWidth / 2) / 4), 0);
			}
			cnt ++;
			
			// ボールの移動。vx,vyが速度
			vx = vx * 0.95f;
			vy = vy * 0.95f;
			x = x + vx; y = y + vy;
			if (x - TGL.viewX < 0 || x - TGL.viewX > TGL.screenWidth) {
				// 画面の左右端をはみだすとゴール
				if (x < 0) rsc = rsc + 1;
				else lsc = lsc + 1;
				x = TGL.screenWidth / 2;
				y = TGL.screenHeight / 2;
				vx = 0; vy = 0;
				TGL.mplayer.playSE("beep");
				// GLB.kickc = -120;
			}
			if (getkey(KeyEvent.KEYCODE_MENU) == 1) {
				if (GLB.ctrl != null) GLB.ctrl = null;
				else                  GLB.ctrl = GLB.pl1_3;
			}
			
			if (y < 0) {
				// 画面の上下端をはみだすとペナルティを与えられる
				y = 10;
				penalty(); return;
			}
			if (y > TGL.screenHeight) {
				// 画面の上下端をはみ出すとペナルティを与えられる
				y = TGL.screenHeight - 10;
				penalty(); return;
			}
			
			if (loopSW == 0) loopSW = 2;
			
		} else if (loopSW == 1) {
			if (loopCnt++ >= 2) {
				pena = 0;
				loopSW = 2;
				loopCnt = 0;
			}
		}
		
		if (loopSW == 2) {
			GLB.anc += 0.4f; if (GLB.anc >= 2) GLB.anc = 0;
			// update();
			loopSW = 0;
		}
		
	}
	
	@Override
	public void onDraw() {
		// 得点表示
		//setFont("ＭＳ 明朝");
		textRsc.drawText(312, 15, intToStr(rsc), color(0, 0, 0), 20, 50);
		textLsc.drawText(212, 15, intToStr(lsc), color(0, 0, 0), 20, 50);
		textRsc.drawText(310, 17, intToStr(rsc), color(255, 155, 155), 20);
		textLsc.drawText(210, 17, intToStr(lsc), color(155, 155, 255), 20);
	}
	
	public void penalty() {
		// 最後にボールをけったチーム(lastdir)にペナルティ。
		// penaにlastdirをセットするとそのチームは数秒間ボールをけられなくなる
		vx = 0; vy = 0;
		pena = lastdir;
		
		loopSW = 1;
		loopCnt = 0;
		// update();
		// update();
		
	}
	
	@Override
	public void onRelease() {
		textRsc.delete();
		textLsc.delete();
	}

}
