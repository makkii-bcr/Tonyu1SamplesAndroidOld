package tonyusamples.block;

import jp.tonyumakkii.tonyusample.R;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuPage;
import android.util.Log;

public class PageStage2 extends TonyuPage {

	@Override
	public void open(TonyuPage oldPage) {
		TGL.map.loadMapDataRes(R.raw.blockstage2);
		//TGL.screenWidth  = 560;
		//TGL.screenHeight = 384;
		TGL.screenWidth  = 542;
		TGL.screenHeight = 375;
		TGL.map.setVisible(1);
		//TGL.screenWidth  = 1280;
		//TGL.screenHeight = 720;
		//TGL.screenWidth  = 1920;
		//TGL.screenHeight = 1080;
		GLB.racket = (Racket) appear(new Racket(218, 363, GLB.pat_racket+0));
		GLB.tama  = (Ball) appear(new Ball(66, 194, 2, 2));
		GLB.monitor = (Monitor) appear(new Monitor(50, 50));
		GLB.monitor.nextPage = GLB.page_title;
		
	}

	@Override
	public void close(TonyuPage newPage) {
	}

}
