package tonyu.kernel;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

public class TonyuTextChar extends TonyuPlainChar {
	public String text;
	public int col;
	public float size;
	private Paint paint;
	private TonyuTextBitmap textBitmap;
	
	public TonyuTextChar(float x, float y) {
		this(x, y, "テキスト", Color.WHITE, 20, 0);
	}
	public TonyuTextChar(float x, float y, String text) {
		this(x, y, text, Color.WHITE, 20, 0);
	}
	public TonyuTextChar(float x, float y, String text, int col) {
		this(x, y, text, col, 20, 0);
	}
	public TonyuTextChar(float x, float y, String text, int col, float size) {
		this(x, y, text, col, size, 0);
	}
	public TonyuTextChar(float x, float y, String text, int col, float size, int zOrder) {
		super(x, y);
		this.paint = new Paint();
		this.text = text;
		this.col = col;
		this.size = size;
		this.zOrder = zOrder;
		this.textBitmap = new TonyuTextBitmap(text, size);
	}
	
	@Override
	public void draw(Object drawObj) {
		//drawText(x, y, text, col, size, zOrder);
		textBitmap.drawText(x, y, text, col, size, zOrder);
		super.draw(drawObj);
	}
	
	@Override
	protected void release() {
		super.release();
		textBitmap.delete();
	}
	
	@Override
	public float getWidth() {
		paint.setTextSize(size);
		return paint.measureText(text);
	}
	@Override
	public float getHeight() {
		paint.setTextSize(size);
		FontMetrics fm = paint.getFontMetrics();
		return fm.descent - fm.ascent;
	}
	
}
