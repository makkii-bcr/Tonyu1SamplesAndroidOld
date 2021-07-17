package tonyusamples.ribbon;

import tonyu.kernel.TonyuSpriteChar;
import tonyu.kernel.TonyuTextBitmap;

abstract public class TRank0 extends TonyuSpriteChar {
	public int rank, gcon;
	private TonyuTextBitmap text;

	public TRank0(float x, float y, int p) {
		this(x, y, p, 0);
	}
	public TRank0(float x, float y, int p, int f) {
		super(x, y, p, f);

		text = new TonyuTextBitmap();
	}
	
	public void putRank() {
		String s;
		// ランクにより Bad,Good,Greatを表示する
		if (rank < 10) {
			//drawText(x, y, "Bad", color(0, 255, 0), 14);
			text.drawText(x, y, "Bad", color(0, 255, 0), 14);
		} else if (rank < 20) {
			//drawText(x, y, "Good", color(255, 255, 0), 14);
			text.drawText(x, y, "Good", color(255, 255, 0), 14);
		} else {
			s = "Great ";
			if (gcon > 1) s = s.concat(Integer.toString(gcon));
			//drawText(x, y, s, color(255, 155, 0), 14);
			text.drawText(x, y, s, color(255, 155, 0), 14);
		}
	}
	
	@Override
	public void onRelease() {
		text.delete();
	}
}
