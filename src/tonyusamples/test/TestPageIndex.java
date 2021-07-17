package tonyusamples.test;

import jp.tonyumakkii.tonyusample.R;
import android.util.Log;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuGraphicsFileManager;
import tonyu.kernel.TonyuPage;

public class TestPageIndex extends TonyuPage {

	@Override
	public void open(TonyuPage oldPage) {
		// 前のページが無しまたはこのページと同じ場合、画像・音の読み込みをしない
		if (oldPage == null || oldPage.getClass() != this.getClass()) {
			TGL.grManager.addBitmapFileTonyu("ball", R.drawable.ball);
		}
		TGL.screenWidth  = 600;
		TGL.screenHeight = 360;

		appear(new TestManager(0, 0));
		
		for (int i=0; i<500; i++){
			appear(new TestSpriteChar(30+i*34, 30+34*0, i));
			appear(new TestDxChar(30+i*34, 30+34*1, i));
		}
	}

	@Override
	public void close(TonyuPage newPage) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
