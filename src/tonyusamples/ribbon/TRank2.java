package tonyusamples.ribbon;

public class TRank2 extends TRank0 {
	private int i;
	
	public TRank2(float x, float y, int r, int gc) {
		super(x, y, -1, 0);
		rank = r;
		gcon = gc;
	}

	@Override
	public void onAppear() {
		p = -1;
		i = 60;
	}

	@Override
	public void loop() {
		// ボール破壊時に出現する"Bad" "Good" "Great"の表示
		if (i <= 0) { die(); return; }
		i = i - 1;
		if (i % 4 < 2) {
			// putRankはTRank0で定義されている
			putRank();
		}
	}
}
