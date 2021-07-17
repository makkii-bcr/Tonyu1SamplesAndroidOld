package tonyusamples.ribbon;

import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSpriteChar;

public class Flash extends TonyuSpriteChar {
	private int v;
	
	public Flash(float x, float y, int p) {
		super(x, y, p);
	}
	
	@Override
	public void onAppear() {
		p = -1;
		v = 0;
	}

	@Override
	public void loop() {
		TGL.map.setBGColor(color(v, v, 100 + trunc(v / 2)));
		v = v - 20;
		if (v < 10) v = 0;
	}
	
	public void doFlash() {
		v = 255;
	}
}
