package tonyusamples.home;

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
			TGL.grManager.addBitmapFileTonyu("ball", R.drawable.ball);
			//TGL.mplayer.addResBGM("bosmidi", R.raw.bosmidi);
		}
		TGL.map.setVisible(0);
		TGL.screenWidth  = 560;
		TGL.screenHeight = 381;
		TGL.keyManager.setBackKeyExit(false); // Backキーでアプリを無効
		
		appear(new Main(0, 0));
		
		
	}

	@Override
	public void close(TonyuPage newPage) {
		// 新しいページへ移行する時、画像・音のデータをクリアする
		if (newPage.getClass() != this.getClass()) {
			TGL.grManager.clearBitmap();
			TGL.mplayer.clearBGM();
			Log.v("loadRes", ""+ newPage.getClass() +" "+ this.getClass());
		}
	}

}
