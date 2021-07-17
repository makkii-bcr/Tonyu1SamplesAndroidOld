package tonyusamples.hockey2;

import android.graphics.Color;
import android.util.Log;
import jp.tonyumakkii.tonyusample.R;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuGraphicsFileManager;
import tonyu.kernel.TonyuPage;

public class PageIndex extends TonyuPage {

	@Override
	public void open(TonyuPage oldPage) {
		
		// 前のページが無しまたはこのページと同じ場合、画像・音の読み込みをしない
		if (oldPage == null || oldPage.getClass() != this.getClass()) {
			// 画像読み込み //
			TGL.grManager.addBitmapFileTonyu("ball", R.drawable.ball);
			TGL.grManager.addBitmapFileTonyu("table", R.drawable.table);
			TGL.grManager.addBitmapFileTonyu("tokuten", R.drawable.tokuten);
			
			// グローバル変数設定
			GLB.pat_table   = getPatNo("table");
			GLB.pat_tokuten = getPatNo("tokuten");
			
			// マップ読み込み　//
			TGL.map.loadMapDataRes(R.raw.hockey2index);
			
			// サウンド読み込み //
			TGL.mplayer.addResSE("bound", R.raw.bound);
			TGL.mplayer.addResSE("shot", R.raw.shot);
			TGL.mplayer.addResSE("in", R.raw.in);
			TGL.mplayer.addResSE("jingle", R.raw.jingle);
			TGL.mplayer.addResBGM("bgm", R.raw.bgm);
			
		}
		TGL.screenManager.moveCursor(-9999, -9999);
		TGL.screenWidth  = 560;
		TGL.screenHeight = 384;
		TGL.map.setVisible(1);
		//TGL.screenWidth  = 1280;
		//TGL.screenHeight = 720;
		//TGL.screenWidth  = 1920;
		//TGL.screenHeight = 1080;
		//TGL.screenWidth  = 3840;
		//TGL.screenHeight = 2160;
		
		GLB.player    = (Player)  appear(new Player(79, 314, 5));
		GLB.ball      = (Ball)    appear(new Ball(112, 186, 20));
		GLB.racket    = (Racket)  appear(new Racket(113, 219, 17));
		GLB.tokuten   = (Tokuten) appear(new Tokuten(314, 38, GLB.pat_tokuten+0, 0, 0, 0, 255, 1));
		GLB.tokuten_1 = (Tokuten) appear(new Tokuten(278, 38, GLB.pat_tokuten+0, 0, 0, 0, 255, 1));
		GLB.tokuten_2 = (Tokuten) appear(new Tokuten(218, 38, GLB.pat_tokuten+0, 0, 0, 0, 255, 1));
		GLB.tokuten.nextInc = 10;
		GLB.tokuten.next = GLB.tokuten_1;
		GLB.tokuten_1.nextInc = 6;
		GLB.tokuten_1.next = GLB.tokuten_2;
		GLB.tokuten_2.nextInc = 10;
		GLB.pass = (Pass) appear(new Pass(-39,  55, GLB.pat_table+20));
		GLB.lap1 = (Lap)  appear(new Lap(219,  74, "1st:", Color.WHITE, 15));
		GLB.lap2 = (Lap)  appear(new Lap(215,  97, "2nd:", Color.WHITE, 15));
		GLB.lap3 = (Lap)  appear(new Lap(200, 119, "Final :", Color.WHITE, 15));
		
	}

	@Override
	public void close(TonyuPage newPage) {
		// 新しいページへ移行する時、画像・音のデータをクリアする
		if (newPage.getClass() != this.getClass()) {
			TGL.grManager.clearBitmap();
			TGL.mplayer.clearBGM();
			TGL.mplayer.clearSE();

			Log.v("loadRes", ""+ newPage.getClass() +" "+ this.getClass());
		}
	}

}
