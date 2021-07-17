package tonyusamples.ribbon;

import tonyu.kernel.TonyuSpriteChar;
import tonyu.kernel.TonyuTextBitmap;

public class PTS extends TonyuSpriteChar {
	private String score;
	private int i;
	private int c;
	private TonyuTextBitmap textScore;
	
	public PTS(float x, float y, int sc, int dp) {
		super(x, y, -1);
		score = strcat(intToStr(sc), "pts");
		if (dp > 1) score = strcat(score, strcat("x", intToStr(dp)));
		
		textScore = new TonyuTextBitmap(score, 14);
	}

	@Override
	public void onAppear() {
		i = 60;
	}

	@Override
	public void loop() {
		if (i <= 0) { die(); return; }
		i = i - 1;
		c = 255;
		if (i < 12) c = i * 20;
		// if (i % 4 < 2) {
		//drawText(x, y, score, color(c, c, c), 14);
		textScore.drawText(x, y, score, color(c, c, c), 14);
		// }
	}

	@Override
	public void onRelease() {
		textScore.delete();
	}

}
