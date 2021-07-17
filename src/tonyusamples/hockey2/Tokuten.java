package tonyusamples.hockey2;

import tonyu.kernel.TonyuDxChar;

public class Tokuten extends TonyuDxChar {
	private int value;
	int nextInc;
	Tokuten next;

	public Tokuten(float x, float y, int p, int f, int zOrder, float angle,
			float alpha, float scaleX) {
		super(x, y, p, f, zOrder, angle, alpha, scaleX);
	}
	
	//0-9までのカウンタ

	//カウンタの値を設定する
	private void setValue(int v) {
		value = v;
		p = GLB.pat_tokuten + v;
	}

	//カウンタの値を１ふやす。値がnextInc以上になったら０にして、
	//nextの値を1増やす。
	void incValue() {
		value += 1;
		if (value>=nextInc) { 
			if (next != null) next.incValue(); 
			value=0;
		}
		setValue(value);
	}
	
	protected void onAppear() {
		value=0;
	}
}