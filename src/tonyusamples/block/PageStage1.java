package tonyusamples.block;

import jp.tonyumakkii.tonyusample.R;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuPage;
import android.util.Log;

public class PageStage1 extends TonyuPage {

	@Override
	public void open(TonyuPage oldPage) {
		TGL.map.loadMapDataRes(R.raw.blockstage1);
		
		//TGL.screenWidth  = 560;
		//TGL.screenHeight = 384;
		TGL.screenWidth  = 542;
		TGL.screenHeight = 375;
		TGL.map.setVisible(1);
		//TGL.screenWidth  = 1280;
		//TGL.screenHeight = 720;
		//TGL.screenWidth  = 1920;
		//TGL.screenHeight = 1080;
		GLB.racket = (Racket) appear(new Racket(218, 362, GLB.pat_racket+0));
		GLB.tama  = (Ball) appear(new Ball(129, 274, 2, 2));
		GLB.tama2 = (Ball) appear(new Ball(68, 247, 2, 2));
		GLB.monitor = (Monitor) appear(new Monitor(194, 40));
		GLB.monitor.nextPage = GLB.page_stage2;
	}

	@Override
	public void close(TonyuPage newPage) {
	}

}
