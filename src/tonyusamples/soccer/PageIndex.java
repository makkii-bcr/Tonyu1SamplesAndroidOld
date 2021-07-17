package tonyusamples.soccer;

import jp.tonyumakkii.tonyusample.R;
import android.graphics.Color;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuPage;

public class PageIndex extends TonyuPage {

	@Override
	public void open(TonyuPage oldPage) {
		
		// 前のページが無しまたはこのページと同じ場合、画像・音の読み込みをしない
		if (oldPage == null || oldPage.getClass() != this.getClass()) {
			
			// 画像読み込み　//
			TGL.grManager.addBitmapFileTonyu("ball", R.drawable.ball);
			TGL.grManager.addBitmapFileTonyu("chars", R.drawable.chars);
			TGL.grManager.addBitmapFileTonyu("32", R.drawable.soccer32);
			
			// グローバル変数設定
			GLB.pat_chars = getPatNo("chars");
			GLB.pat_32    = getPatNo("32");
			
			// マップ読み込み　//
			TGL.map.loadMapDataRes(R.raw.soccerindex);
			
			// サウンド読み込み //
			TGL.mplayer.addResSE("beep", R.raw.beep);
			TGL.mplayer.addResSE("kick", R.raw.kick);
			sleep(500);
		}
		TGL.map.setVisible(1);
		TGL.screenWidth  = 560;
		TGL.screenHeight = 372;
		TGL.keyManager.setBackKeyExit(false); // Backキーでアプリ終了するのを無効

		GLB.pl1 = (TPlayer) appear(new TPlayer(207, 254, GLB.pat_chars + 7));
		GLB.pl1.dir = 1;
		GLB.ball = (TBall) appear(new TBall(272.159729003906f, 185.341705322266f, 24));
		GLB.pl1_1 = (TPlayer) appear(new TPlayer(219, 297, GLB.pat_chars + 13));
		GLB.pl1_1.dir = -1;
		GLB.pl1_2 = (TPlayer) appear(new TPlayer(434, 99, GLB.pat_chars + 15));
		GLB.pl1_2.dir = -1;
		GLB.pl1_3 = (TPlayer) appear(new TPlayer(82, 213, GLB.pat_chars + 7));
		GLB.pl1_3.dir = 1;
		GLB.pl1_4 = (TPlayer) appear(new TPlayer(463, 164, GLB.pat_chars + 7));
		GLB.pl1_4.dir = 1;
		GLB.pl1_5 = (TPlayer) appear(new TPlayer(98, 137, GLB.pat_chars + 11));
		GLB.pl1_5.dir = -1;
		GLB.pl1_6 = (TPlayer) appear(new TPlayer(494, 55, GLB.pat_chars + 16));
		GLB.pl1_6.dir = -1;
		GLB.pl1_6.sdlim = 100;
		GLB.pl1_7 = (TPlayer) appear(new TPlayer(38, 54, GLB.pat_chars + 7));
		GLB.pl1_7.dir = 1;
		GLB.pl1_8 = (TPlayer) appear(new TPlayer(376, 211, GLB.pat_chars + 9));
		GLB.pl1_8.dir = 1;
		GLB.pl1_9 = (TPlayer) appear(new TPlayer(376, 167, GLB.pat_chars + 12));
		GLB.pl1_9.dir = -1;
		GLB.timer = (Timer) appear(new Timer(263, 5, "0", Color.WHITE, 20));
		GLB.timer.t = 90;
		
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
