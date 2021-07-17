package tonyusamples.test;

import tonyu.kernel.TonyuDxChar;

public class TestDxChar extends TonyuDxChar {

	public TestDxChar(float x, float y, int p, int f, int zOrder, float angle,
			float alpha, float scaleX, float scaleY) {
		super(x, y, p, f, zOrder, angle, alpha, scaleX, scaleY);
	}
	public TestDxChar(float x, float y, int p) {
		super(x, y, p);
	}
	public TestDxChar(float x, float y, int p, int f, int zOrder) {
		super(x, y, p, f, zOrder);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
/*
	@Override
	public void loop() {
		angle += 1;
		alpha -= 1;
		if(alpha < 0) alpha = 255;
		scaleX -= 0.005f;
	}
	*/
}
