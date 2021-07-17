package tonyu.startup;

import tonyu.kernel.TGL;
import tonyu.kernel.TonyuPage;

public class PageStart extends TonyuPage {

	@Override
	public void open(TonyuPage oldPage) {
		
		TGL.screenManager.moveCursor(-9999, -9999);
		TGL.screenWidth  = 560;
		TGL.screenHeight = 381;
		//TGL.screenWidth  = 1280;
		//TGL.screenHeight = 720;
		//TGL.screenWidth  = 1920;
		//TGL.screenHeight = 1080;
		
		appear(new Startup(0, 0));
		
	}

	@Override
	public void close(TonyuPage newPage) {
	}

}
