package tonyu.kernel;


/**
 * １枚のチップ画像を表示するクラス。<br>
 * TonyuSpriteCharの機能に加え<b>回転・半透明・拡大縮小</b>ができる。<br>
 * TonyuのDxSpriteに相当。<br>
 */
public class TonyuDxChar extends TonyuPlainChar {
	public int p, f;
	public float angle, alpha, scaleX, scaleY;
	
	public TonyuDxChar(float x, float y) {
		this(x, y, 0, 0, 0, 0, 255, 1, 0);
	}
	public TonyuDxChar(float x, float y, int p) {
		this(x, y, p, 0, 0, 0, 255, 1, 0);
	}
	public TonyuDxChar(float x, float y, int p, int f) {
		this(x, y, p, f, 0, 0, 255, 1, 0);
	}
	public TonyuDxChar(float x, float y, int p, int f, int zOrder) {
		this(x, y, p, f, zOrder, 0, 255, 1, 0);
	}
	public TonyuDxChar(float x, float y, int p, int f, int zOrder, float angle) {
		this(x, y, p, f, zOrder, angle, 255, 1, 0);
	}
	public TonyuDxChar(float x, float y, int p, int f, int zOrder, float angle, float alpha) {
		this(x, y, p, f, zOrder, angle, alpha, 1, 0);
	}
	public TonyuDxChar(float x, float y, int p, int f, int zOrder, float angle, float alpha, float scaleX) {
		this(x, y, p, f, zOrder, angle, alpha, scaleX, 0);
	}
	public TonyuDxChar(float x, float y, int p, int f, int zOrder, float angle, float alpha, float scaleX, float scaleY) {
		super(x, y);
		this.p = p;
		this.f = f;
		this.zOrder = zOrder;
		this.angle  = angle;
		this.alpha  = alpha;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	
	@Override
	public void draw(Object drawObj) {
		drawDxSprite(x, y, p, f, zOrder, angle, alpha, scaleX, scaleY);
		super.draw(drawObj);
	}
	

	@Override
	public float getWidth() {
		return getPatWidth(p) * scaleX;
	}
	@Override
	public float getHeight() {
		float sc = (scaleY == 0 ? scaleX : scaleY);
		return getPatHeight(p) * sc;
	}

	@Override
	public float getWidthOld() {
		float r = getPatWidth(p) * scaleX;
		if (r > 24) return r - 8;
		return r*0.66f;
	}
	@Override
	public float getHeightOld() {
		float sc = (scaleY == 0 ? scaleX : scaleY);
		float r = getPatHeight(p) * sc;
		if (r > 24) return r - 8;
		return r * 0.66f;
	}

}
