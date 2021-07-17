package tonyusamples.block;

import jp.tonyumakkii.tonyusample.R;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuPage;

public class PageTest extends TonyuPage {

	@Override
	public void open(TonyuPage oldPage) {
		TGL.map.loadMapDataRes(R.raw.blocktest);
		
		//TGL.screenWidth  = 560;
		//TGL.screenHeight = 384;
		TGL.screenWidth  = 560;
		TGL.screenHeight = 384;
		TGL.map.setVisible(1);
		GLB.racket = (Racket) appear(new Racket(218, 362, GLB.pat_racket+0));
		GLB.tama  = (Ball) appear(new Ball(25, 75, 2, 2));
		GLB.monitor = (Monitor) appear(new Monitor(194, 40));
		GLB.monitor.nextPage = GLB.page_test;
	}

	@Override
	public void close(TonyuPage newPage) {
	}

}