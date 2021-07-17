package tonyu.kernel;

public class TonyuSpriteChar extends TonyuPlainChar {
	public int p, f;
	
	public TonyuSpriteChar(float x, float y) {
		this(x, y, 0, 0, 0);
	}
	public TonyuSpriteChar(float x, float y, int p) {
		this(x, y, p, 0, 0);
	}
	public TonyuSpriteChar(float x, float y, int p, int f) {
		this(x, y, p, f, 0);
	}
	public TonyuSpriteChar(float x, float y, int p, int f, int zOrder) {
		super(x, y);
		this.p = p;
		this.f = f;
		this.zOrder = zOrder;
	}
	
	@Override
	public void draw(Object drawObj) {
		drawSprite(x, y, p, f, zOrder);
		super.draw(drawObj);
	}

	
	@Override
	public float getWidth() {
		return getPatWidth(p);
	}
	@Override
	public float getHeight() {
		return getPatHeight(p);
	}

	@Override
	public float getWidthOld() {
		int r = getPatWidth(p);
		if (r > 24) return (int)(r * 0.8f);
		return (int)(r*0.66f);
	}
	@Override
	public float getHeightOld() {
		int r = getPatHeight(p);
		if (r > 24) return r * 0.8f;
		return r * 0.66f;
	}
}
