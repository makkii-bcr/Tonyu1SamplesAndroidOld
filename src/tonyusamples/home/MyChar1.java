package tonyusamples.home;

import tonyu.kernel.TGL;
import tonyu.kernel.TonyuDxChar;

public class MyChar1 extends TonyuDxChar {
	private float vx, vAngle, vAlpha, vScaleX, vScaleY;
	private int alphaSW = 0;
	public MyChar1(float x, float y) {
		super(x, y);
	}

	@Override
	public void onAppear() {
		vx = rnd()*5+1;
		vAngle = rnd()*5;
		vAlpha = rnd()*5;
		vScaleX = rnd()/20;
		vScaleY = rnd()/20;
		zOrder = 20;
		if (rnd(2) == 0) p = rnd(12)+3;
		else             p = rnd(15)+17;
	}
	@Override
	public void loop() {
		angle += vAngle;
		if (alphaSW == 0) alpha += vAlpha; else alpha -= vAlpha;
		if (alpha > 255) { alphaSW = 1; alpha = 255;}
		if (alpha <   0) { alphaSW = 0; alpha =   0;}
		scaleX += vScaleX;
		if (scaleX > 5) scaleX = 0;
		scaleY += vScaleY;
		if (scaleY > 5) scaleY = 0;
		x += vx;
		if (x >= TGL.screenWidth) x = 0;
	}
}
