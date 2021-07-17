package tonyusamples.ribbon;

import tonyu.kernel.TGL;

public class TRank1 extends TRank0 {
	private float px, py;
	private int loop = 0;
	
	public TRank1(float x, float y, int p) {
		super(x, y, p);
	}

	@Override
	public void onAppear() {
		GLB.gcont = 0;
		p = -1;
		rank = 0;
		px = TGL.padX;
		py = TGL.padY;
	}
	
	@Override
	public void loop() {
		// ランクを計算する
		if (loop == 0) {
			rank = 0;
			px = TGL.padX;
			py = TGL.padY;
			if (dist(TGL.padX - px, TGL.padY - py) < 10) {
				px = TGL.padX;
				py = TGL.padY;
				rank = rank + 1;
				loop = 1;
			} else {
				GLB.gcont = 0;
			}
		} else {
			if (dist(TGL.padX - px, TGL.padY - py) < 10) {
				px = TGL.padX;
				py = TGL.padY;
				rank = rank + 1;
			} else {
				GLB.gcont = 0;
				loop = 0;
			}
		}
		putRank();
	}
}
