package tonyusamples.ribbon;

import android.graphics.Color;
import android.view.KeyEvent;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuArray;
import tonyu.kernel.TonyuSpriteChar;
import tonyu.kernel.TonyuTextBitmap;

public class ABoot extends TonyuSpriteChar {
	private Pend pt;
	private int i;
	private int padPushCnt = 0;

	private TonyuTextBitmap textScore, textLevel, textTime, textReplay;

	public ABoot(float x, float y, int p) {
		super(x, y, p);
	}

	@Override
	public void onAppear() {
		p = -1;
		GLB.mu = 0.95f;
		GLB.ths = new TonyuArray<Pend>();
		for (Object t : TGL.chars) {
			if (t.getClass() == Pend.class) {
				((Pend)t).t = null;
				GLB.ths.add((Pend) t);
				if (pt != null) pt.t = (Pend)t;
				pt = (Pend)t;
			}
		}
		GLB.score = 0;
		TGL.mplayer.playBGM("main", true, 0, 2000);
		GLB.srank = 2000;
		GLB.time = 60;
		GLB.level = 1;
		GLB.exp = 0;
		
		textScore  = new TonyuTextBitmap(strcat("Score: ", intToStr(GLB.score)), 14);
		textLevel  = new TonyuTextBitmap(strcat("level:", intToStr(GLB.level)), 14);
		textTime   = new TonyuTextBitmap(strcat("Time :", intToStr(trunc(GLB.time))), 14);
		textReplay = new TonyuTextBitmap("Replay Tap", 14);
	}

	@Override
	public void loop() {
		// スコア表示
		//drawText(x, y, "Score: " + GLB.score, color(255, 255, 255), 14);
		textScore.drawText(x, y, strcat("Score: ", intToStr(GLB.score)), Color.WHITE, 14);
		if (GLB.tincr != null) i = GLB.tincr.i; else i = 120;
		// レベル表示
		if (i < 60 || (i % 4) < 2) {
			//drawText(400, y, strcat("level:", intToStr(trunc(GLB.level))), color(155, 255, 255), 14);
			textLevel.drawText(400, y, strcat("level:", intToStr(GLB.level)), color(155, 255, 255), 14);
		}
		// 残り時間表示
		if (GLB.time > 0) {
			//drawText(300, y, "Time :" + trunc(GLB.time), color(255, 255, 255), 14);
			textTime.drawText(300, y, strcat("Time :", intToStr(trunc(GLB.time))), Color.WHITE, 14);
			GLB.time = GLB.time - 0.017f;
		} else {
			// ゲームオーバーならばReplay表示
			//drawText(300, y, "Replay Tap", color(255, 150, 155), 14);
			textReplay.drawText(300, y, "Replay Tap", color(255, 150, 155), 14);
			// ハイスコア登録用オブジェクトを画面に表示
			GLB.regist.setVisible(1);
		}
		// 経験値がレベル*5に達するとレベルアップ
		if (GLB.exp > GLB.level * 5 && GLB.time > 0) {
			GLB.tincr = (TimeIncr)appear(new TimeIncr(400, y+20, trunc(GLB.time) * 10 * GLB.level));
			GLB.level += 1;
			GLB.exp = 0;
			// 新しいボールが出現する
			appear(new Star(100, -100, getPatNo("star") + 3, 0));
		}
		// オブジェクト数表示
		//drawText(150, 0, intToStr(TGL.chars.size()), color(0,255,0), 12, -1);

		//drawLine(TGL.screenWidth/2, TGL.screenHeight/2, TGL.padX, TGL.padY, Color.WHITE, -999);
		
		if (GLB.time <= 0) {
			if (TGL.padPush == 1) {
				if (padPushCnt == 1) {
					if (0 <= TGL.padX && TGL.padX < TGL.screenWidth && 0 <= TGL.padY && TGL.padY <= 80) {
						TGL.projectManager.loadPage(new PageIndex());
					} else {
						padPushCnt --;
					}
				}
			} else {
				if (padPushCnt == 0) padPushCnt ++;
			}
		}
		

		// ホームに戻る
		if (getkey(KeyEvent.KEYCODE_BACK) == 1) {
			TGL.projectManager.loadPage(tonyusamples.home.GLB.page_home);
		}
	}
	
	@Override
	public void onRelease() {
		textScore.delete();
		textLevel.delete();
		textTime.delete();
		textReplay.delete();
	}
}
