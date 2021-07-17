package tonyusamples.ribbon;


import jp.tonyumakkii.tonyusample.R;
import android.graphics.Color;
import android.util.Log;

import tonyu.kernel.TGL;
import tonyu.kernel.TonyuPage;
import tonyusamples.ribbon.GLB;


public class PageIndex extends TonyuPage {
	@Override
	public void open(TonyuPage oldPage) {
		
		// 前のページが無しまたはこのページと同じ場合、画像・音の読み込みをしない
		if (oldPage == null || oldPage.getClass() != this.getClass()) {
			
			// 画像読み込み //
			TGL.grManager.addBitmapFileTonyu("ball", R.drawable.ball);
			TGL.grManager.addBitmapFileTonyu("star", R.drawable.star);
			
			// グローバル変数
			GLB.pat_star = getPatNo("star");
			
			// サウンド読み込み //
			TGL.mplayer.addResBGM("main", R.raw.main);
			TGL.mplayer.addResSE("bad"    , R.raw.bad);
			TGL.mplayer.addResSE("good"   , R.raw.good);
			TGL.mplayer.addResSE("great"  , R.raw.great);
			TGL.mplayer.addResSE("kasuri" , R.raw.kasuri);
			TGL.mplayer.addResSE("kasuri2", R.raw.kasuri2);
		}
		
		
		
		// 画面サイズを設定 //
		TGL.screenWidth  = 563;
		TGL.screenHeight = 386;
		TGL.map.setVisible(0);
		
		// オブジェクトを準備 //
		appear(new Pend(120, 101));   // $pend_22
		appear(new ABoot(32, 32, 3)); // $boot_1
		appear(new Pend(303, 226));   // $pend_23
		appear(new Pend(173, 256));   // $pend_24
		appear(new Pend(244,  87));   // $pend_25
		appear(new Pend(106, 183));   // $pend_26
		appear(new Pend(341, 159));   // $pend_27
		appear(new Pend(191, 219));   // $pend_28
		appear(new Pend(309, 114));   // $pend_29
		appear(new Pend(366, 205));   // $pend_30
		appear(new Pend(172, 137));   // $pend_31
		appear(new Pend(284, 186));   // $pend_32
		appear(new Pend(262, 267));   // $pend_33
		appear(new Star(179, 7, GLB.pat_star + 3, 0, 0));                   // $star
		GLB.rank = (TRank0) appear(new TRank1(456, 341, GLB.pat_star + 3)); // $rank
		GLB.Flsh = (Flash) appear(new Flash(60, 67, 3));                        // $Flsh
		GLB.regist = (Regist) appear(new Regist(331, 63, "Register HiScore", Color.WHITE, 18)); // $regist
		appear(new Pend(222, 147));   // $pend_34
		
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
