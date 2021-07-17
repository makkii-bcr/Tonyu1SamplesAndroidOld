package tonyusamples.ribbon;

import tonyu.kernel.TonyuSpriteChar;

public class TBomb extends TonyuSpriteChar {
	private int i = 3, j = 30;
	private float vx, vy;

	public TBomb(float x, float y, float vx, float vy) {
		super(x, y, 0, 0, 0);
		this.vx = vx;
		this.vy = vy;
		p = GLB.pat_star+2;
	}
	
	@Override
	public void loop() {
		if (j<=0) {
			p=p-1;
			vx=vx*0.9f;vy=vy*0.9f;
			i=i-1;
			if (i<=0) {
				p = -1;
				die();
				return;
			}
			j = 30;
		}
		if (j>0) {
			x=x+vx;
			y=y+vy;
			j=j-1;
		}
	}
}
	

