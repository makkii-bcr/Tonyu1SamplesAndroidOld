package tonyusamples.ribbon;

import tonyu.kernel.TonyuSpriteChar;
import tonyu.kernel.TonyuTextBitmap;

public class TimeIncr extends TonyuSpriteChar {
	public int i;
	public int sc;
	private TonyuTextBitmap textBonus;
	
	public TimeIncr(int x, float f, int s) {
		super(x, f, -1);
		sc = s;
		
		textBonus = new TonyuTextBitmap(strcat("Bonus:", intToStr(sc)), 14);
	}

	@Override
	public void onAppear() {
		i = 120;
	}

	@Override
	public void loop() {
		if (i > 0) {
			i -= 1;
		} else if (sc > 0) {
			GLB.score += 10;
			sc -= 10;
		} else {
			die();
		}
	}

	@Override
	public void draw(Object drawObj) {
		drawText(x, y, strcat("Bonus:", intToStr(sc)), color(200, 240, 100), 14);
		textBonus.drawText(x, y, strcat("Bonus:", intToStr(sc)), color(200, 240, 100), 14);
	}
	
	@Override
	public void onRelease() {
		textBonus.delete();
	}
}
