package tonyusamples.hockey;

import jp.tonyumakkii.tonyusample.R;
import android.graphics.Color;
import android.util.Log;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuGraphicsFileManager;
import tonyu.kernel.TonyuPage;
import tonyusamples.hockey.GLB;
import tonyusamples.home.Main;

public class PageIndex extends TonyuPage {

	@Override
	public void open(TonyuPage oldPage) {
		
		// 前のページが無しまたはこのページと同じ場合、画像・音の読み込みをしない
		if (oldPage == null || oldPage.getClass() != this.getClass()) {
			
			// 画像読み込み　//
			TGL.grManager.addBitmapFileTonyu("ball", R.drawable.ball);
			TGL.grManager.addBitmapFileTonyu("table", R.drawable.table);
			TGL.grManager.addBitmapFileTonyu("tokuten", R.drawable.tokuten);
			
			// グローバル変数設定
			GLB.pat_table   = getPatNo("table");
			GLB.pat_tokuten = getPatNo("tokuten");
			
			// マップ読み込み　//
			TGL.map.loadMapDataRes(R.raw.hockeyindex);
			
			// サウンド読み込み //
			TGL.mplayer.addResSE("bound", R.raw.bound);
			TGL.mplayer.addResSE("shot", R.raw.shot);
			TGL.mplayer.addResSE("in", R.raw.in);
			TGL.mplayer.addResSE("jingle", R.raw.jingle);
			
		}
		TGL.map.setVisible(1);
		TGL.screenWidth  = 560;
		TGL.screenHeight = 384;
		TGL.keyManager.setBackKeyExit(false); // Backキーでアプリ終了するのを無効
		
		GLB.player    = (Player)  appear(new Player(77, 316, 5));
		GLB.player.manPlay = 1;
		GLB.ball      = (Ball)    appear(new Ball(222, 200, 20));
		GLB.racket    = (Racket)  appear(new Racket(64, 86, 17));
		GLB.racket_1  = (Racket)  appear(new Racket(128.575592041016f, 58.8583068847656f, 21));
		GLB.enemy     = (Enemy)   appear(new Enemy(477, 279, 6));
		GLB.enemy.tracket = GLB.racket_1;
		GLB.tokuten   = (Tokuten) appear(new Tokuten(236, 65, GLB.pat_tokuten + 0));
		GLB.tokuten_1 = (Tokuten) appear(new Tokuten(306, 64, GLB.pat_tokuten + 0));
		GLB.replay_1  = (Replay)  appear(new Replay(138+30, 101, "Tap Here to Replay", Color.WHITE, 25));
		GLB.enemy_2   = (Enemy)   appear(new Enemy(192, 293, 6));
		GLB.enemy_2.dir = 1;
		GLB.enemy_2.tracket = GLB.racket;
		
		TGL.screenManager.moveCursor(GLB.racket.x, GLB.racket.y);
	}

	@Override
	public void close(TonyuPage newPage) {
		// 新しいページへ移行する時、画像・音のデータをクリアする
		if (newPage.getClass() != this.getClass()) {
			TGL.grManager.clearBitmap();
			TGL.mplayer.clearBGM();
			TGL.mplayer.clearSE();
		}
	}

}
