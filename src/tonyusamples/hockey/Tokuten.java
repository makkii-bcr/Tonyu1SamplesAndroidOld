package tonyusamples.hockey;

import tonyu.kernel.TonyuDxChar;

public class Tokuten extends TonyuDxChar {

	public Tokuten(float x, float y, int p) {
		super(x, y, p);
	}
	
	int value, loopSW = 0;
	
	@Override
	public void onAppear() {
		value = p;
		scaleY = 1;
	}
	
	@Override
	public void loop() {
		//wait(); // ここで待機 (*a)
		if (loopSW == 1) {
			// 点が変化するアニメーション
			// 小さくなる
			if (scaleY > 0.1f) {
				scaleY = scaleY * 0.8f;
			} else {
				p = value;
				loopSW = 2;
			}
		}
		if (loopSW == 2) {
			// 値を変え、大きくなる
			if (scaleY < 1f) {
				scaleY = scaleY * 1.5f;
			} else {
				scaleY = 1f;
				loopSW = 0;
			}
		}
	}
	
	public void setValue(int v) {
		// 値を設定する。(*a)での待機時間状態を解除し、アニメーションを開始する
		value = v;
		myNotify();
	}
	
	public void myNotify() {
		if (loopSW == 0) loopSW = 1;
	}
}
