package tonyusamples.block;

import tonyu.kernel.TGL;
import tonyu.kernel.TonyuPage;
import tonyu.kernel.TonyuSecretChar;
import tonyu.kernel.TonyuTextBitmap;

public class Monitor extends TonyuSecretChar {
	int blc;
	private int xx;
	private int yy;
	private int pa;
	private int i;
	private int loop = 0, loop2 = 0;
	TonyuPage nextPage;
	private TonyuTextBitmap textBlockCnt, textGameOver, textClear;
	public Monitor(float x, float y) {
		super(x, y);
	}

	@Override
	public void onAppear() {
		// 面全体にブロックがいくつあるか数える。その数をblcに入れる
		blc=0;
		xx=0;
		while (xx<TGL.map.getWidth()) {
			yy=0;
			while (yy<TGL.map.getHeight()) {
				pa = TGL.map.get(xx,yy) ;
				if (pa>=GLB.pat_block+1 && pa<=GLB.pat_block+4) blc+=1; 
				yy+=1;   
			}   
			xx+=1; 
		}
		GLB.ballC = 1;
		textBlockCnt = new TonyuTextBitmap();
		textGameOver = new TonyuTextBitmap();
		textClear = new TonyuTextBitmap();
	}
	@Override
	public void loop() {

		
		if (loop2 == 0) {
			// ブロックの数が0になるか、ボールが0になるまで待機。
			if (blc > 0 && GLB.ballC > 0) {
				loop = 1;
			} else {
				loop = 0;
				loop2 = 1;
				i = 0;
			}
			
			if (loop == 1) {
				//drawText(0, 0, intToStr(blc), color(255,0,0), 20);
				textBlockCnt.drawText(0, 0, intToStr(blc), color(255,0,0), 20);
				GLB.ballC=0;
				return;
			}
		}
		if (loop2 == 1) {

			if (i < 60) {
				loop = 1;
			} else {
				// 次の面に移動する(gameoverならタイトルに)
				TGL.projectManager.loadPage(nextPage);
				loop = 0;
			}
			if (loop == 1) {
				// ブロックが0ならクリア
				if (blc<=0) {
					//drawText(x, y, "Clear!", color(255,255,255), 20);
					textClear.drawText(x, y, "Clear!", color(255,255,255), 20);
				}
				// ボール0ならゲームオーバー
				if (GLB.ballC<=0) {
					//drawText(x, y, "Game Over", color(255,255,0), 20);
					textGameOver.drawText(x, y, "Game Over", color(255,255,0), 20);
					nextPage = GLB.page_title;
				}
				i+=1;
			}
		}
	}
	
	@Override
	public void onRelease() {
		textBlockCnt.delete();
		textGameOver.delete();
		textClear.delete();
	}
	
}
