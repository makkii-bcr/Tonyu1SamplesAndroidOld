package tonyusamples.home;

import android.graphics.Color;
import android.view.KeyEvent;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSecretChar;
import tonyu.kernel.TonyuSelectBox.SelectMsg;
import tonyu.kernel.TonyuTextChar;
import tonyusamples.block.GLB;

public class Main extends TonyuSecretChar {
	private int tx=60, ty=40, ty2=35;
	private int selectGame = -1;
	private int selectBoxSW = 0;
	private SelectMsg msg;
	private int alphaSW = 0, alpha = 0;
	public Main(float x, float y) {
		super(x, y);
	}
	
	@Override
	public void onAppear() {
		TGL.map.setBGColor(Color.WHITE);
		appear(new TonyuTextChar(tx, ty+ty2*0, "Ribbon", Color.BLACK, 24));
		appear(new TonyuTextChar(tx, ty+ty2*1, "block", Color.BLACK, 24));
		appear(new TonyuTextChar(tx, ty+ty2*2, "Hockey", Color.BLACK, 24));
		appear(new TonyuTextChar(tx, ty+ty2*3, "Hockey2", Color.BLACK, 24));
		appear(new TonyuTextChar(tx, ty+ty2*4, "Meteo", Color.BLACK, 24));
		appear(new TonyuTextChar(tx, ty+ty2*5, "Garbage", Color.BLACK, 24));
		appear(new TonyuTextChar(tx, ty+ty2*6, "NumTest", Color.BLACK, 24));
		appear(new TonyuTextChar(tx, ty+ty2*7, "Soccer", Color.BLACK, 24));
		appear(new TonyuTextChar(tx, ty+ty2*8, "UFO", Color.BLACK, 24));

		appear(new TonyuTextChar(300, 320, "このゲームで遊ぶ", Color.BLACK, 26));
		
		
		for(int i=0; i<10; i++) appear(new MyChar1(rnd(TGL.screenWidth), rnd(TGL.screenHeight-40)+20));
		//printT("Tonyu System サンプル集へようこそ！！");
		
		TGL.mplayer.playBGM("bosmidi", true);
	}
	
	@Override
	public void loop() {
		if (TGL.padPush == 1) {
			int mx = (int)TGL.padX;
			int my = (int)TGL.padY;
			for (int i=0; i<9; i++) {
				if (scopeXY(mx, my, tx, ty+ty2*i, tx+100, ty+ty2*(i+1))) {
					selectGame = i;
				}
			}

			if (scopeXY(mx, my, 300, 320-30, 300+220, 320+30+30)) {
				fillRect(300, 320-30, 300+220, 320+30+30, Color.argb(192, 17, 255, 17), 10);
				loadPage(selectGame);
			}
		}
		
		if (selectGame >= 0) {
			fillRect(tx, ty+ty2*selectGame, tx+100, ty+ty2*(selectGame+1), Color.argb(alpha, 17, 255, 17), 10);
		}
		
		if (alphaSW == 0) {
			alpha += 4;
			if (alpha >= 255) {
				alpha = 255;
				alphaSW = 1;
			}
		} else {
			alpha -= 4;
			if (alpha <= 16) {
				alpha = 16;
				alphaSW = 0;
			}
		}
		
		if (getkey(KeyEvent.KEYCODE_BACK) == 1) {
			//TGL.selectBox.open("アプリを終了しますか？", "アプリ終了", "終了します", "終了しません");
			msg = TGL.selectBox.open("アプリを終了しますか？", "アプリ終了");
			selectBoxSW = 1;
		}
		if (msg != null) {
			if (msg.getStatus() != -1) {
				if (msg.getStatus() == 1) TGL.system.exit(); // アプリ終了
				msg = null;
			}
		}
		
		// テストスページ
		if (getkey(KeyEvent.KEYCODE_MENU) == 60) {
			TGL.projectManager.loadPage(new tonyusamples.test.TestPageIndex());
		}
	}
	
	private void loadPage(int i) {
		switch (i) {
		case 0: TGL.projectManager.loadPage(tonyusamples.ribbon.GLB.page_index); break;
		case 1: TGL.projectManager.loadPage(tonyusamples.block.GLB.page_title); break;
		case 2: TGL.projectManager.loadPage(tonyusamples.hockey.GLB.page_index); break;
		case 3: TGL.projectManager.loadPage(tonyusamples.hockey2.GLB.page_index); break;
		case 4:  break;
		case 5:  break;
		case 6:  break;
		case 7: TGL.projectManager.loadPage(tonyusamples.soccer.GLB.page_index); break;
		case 8:  break;
		default: break;
		}
	}
}
