package tonyusamples.block;

import android.util.Log;
import jp.tonyumakkii.tonyusample.R;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuGraphicsFileManager;
import tonyu.kernel.TonyuPage;
import tonyusamples.block.GLB;

public class PageTitle extends TonyuPage {

	@Override
	public void open(TonyuPage oldPage) {
		
		// 前のページが無しまたはこのページと同じ場合、画像・音の読み込みをしない
		if (oldPage == null || oldPage.getClass() != this.getClass()) {
			Class<? extends TonyuPage> c = oldPage.getClass(); // 前のページオブジェクトのクラス名を取得
			// 前のページはblock内のページか？
			// そうでなければ、画像・音の読み込みをする
			if (c != PageTitle.class && c != PageStage1.class && c != PageStage2.class && c != PageTest.class) {
				
				// 画像読み込み //
				TGL.grManager.addBitmapFileTonyu("block", R.drawable.block);
				TGL.grManager.addBitmapFileTonyu("racket", R.drawable.racket);
				
				// グローバル変数設定 //
				GLB.pat_block  = getPatNo("block");
				GLB.pat_racket = getPatNo("racket");
				
				// サウンド読み込み //
				TGL.mplayer.addResSE("bpon", R.raw.bpon);
				TGL.mplayer.addResSE("katai", R.raw.katai);
				TGL.mplayer.addResSE("multi", R.raw.multi);
				TGL.mplayer.addResSE("racket", R.raw.racket);
				TGL.mplayer.addResSE("speed", R.raw.speed);
				
				sleep(500);
			}
		}
		TGL.map.loadMapDataRes(R.raw.blocktitle);
		TGL.screenWidth  = 560;
		TGL.screenHeight = 384;
		//TGL.screenWidth  = 542;
		//TGL.screenHeight = 375;
		TGL.map.setVisible(1);
		//TGL.screenWidth  = 1280;
		//TGL.screenHeight = 720;
		//TGL.screenWidth  = 1920;
		//TGL.screenHeight = 1080;
		GLB.racket = (Racket) appear(new Racket(219, 361, GLB.pat_racket+0));
		GLB.tama = (Ball) appear(new Ball(250, 48, 2, 2));
		GLB.press = (Press) appear(new Press(163, 324));
	}

	@Override
	public void close(TonyuPage newPage) {
		// 新しいページへ移行する時、画像・音のデータをクリアする
		if (newPage.getClass() != this.getClass()) {
			if (newPage.getClass() == tonyusamples.home.PageIndex.class) {
				TGL.grManager.clearBitmap();
				TGL.mplayer.clearSE();
				Log.v("loadRes", ""+ newPage.getClass() +" "+ this.getClass());
			}
		}
	}

}
