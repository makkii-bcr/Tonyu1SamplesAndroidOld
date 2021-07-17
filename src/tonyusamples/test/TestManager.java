package tonyusamples.test;
import android.graphics.Color;
import android.view.KeyEvent;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSecretChar;

public class TestManager extends TonyuSecretChar {

	public TestManager(float x, float y) {
		super(x, y);
	}
	
	@Override
	public void onAppear() {
		TGL.map.setBGColor(color(0,0,0));
		TGL.screenWidth = 160;
		TGL.screenHeight = 90;
	}
	@Override
	public void loop() {
		if (getkey(KeyEvent.KEYCODE_BACK) == 1) {
			TGL.projectManager.loadPage(tonyusamples.home.GLB.page_home);
		}
		drawLine(TGL.screenWidth/2, TGL.screenHeight/2, TGL.padX, TGL.padY-20, Color.WHITE, -99);
	}
}
