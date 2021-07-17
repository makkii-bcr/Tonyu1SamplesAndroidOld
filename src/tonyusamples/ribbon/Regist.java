package tonyusamples.ribbon;

import tonyu.kernel.TonyuTextChar;

public class Regist extends TonyuTextChar {

	public Regist(float x, float y, String text, int col, int size) {
		super(x, y, text, col, size);
	}
	
	@Override
	public void onAppear() {
		setVisible(0);
	}
}
