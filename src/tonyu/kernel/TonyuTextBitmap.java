package tonyu.kernel;

import tonyu.android.GL11Until;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;


/**
 *  文字列を効率よく表示するためのクラス<br>
 *  <br>
 *  drawText()でも文字列を表示できるが、<br>
 *  表示毎に文字列をビットマップに描画し、テクスチャに変換してから表示する<br>
 *  <br>
 *  このクラスでは、文字列の内容が変更されたときのみ、テクスチャを切り替える<br>
 *  文字列の内容の変更が少ない場合には、drawText()よりも軽量化が期待できる<br>
 */
public class TonyuTextBitmap extends TonyuObject {
	private String text, pastText = "";
	private float size, pastSize = -1;
	private boolean antiAlias, pastAntiAlias = true;
	
	private Bitmap bitmap = null;
	private Paint paint;
	private Canvas canvas;
	private TonyuGraphicsChip gChip = null;

	public TonyuTextBitmap() {
		this("", 12, true);
	}
	public TonyuTextBitmap(String text) {
		this(text, 12, true);
	}
	public TonyuTextBitmap(String text, float size) {
		this(text, size, true);
	}
	public TonyuTextBitmap(String text, float size, boolean antiAlias) {
		canvas = new Canvas();
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(antiAlias);
		this.text = text;
		this.size = size;
		this.antiAlias = antiAlias;
		setTexture();
	}

	public TonyuTextBitmap setAntiAlias(boolean antiAlias) {
		return setText(text, size, antiAlias);
	}

	public TonyuTextBitmap setText(String text) {
		return setText(text, size, antiAlias);
	}

	public TonyuTextBitmap setText(String text, float size) {
		return setText(text, size, antiAlias);
	}
	
	public TonyuTextBitmap setText(String text, float size, boolean antiAlias) {
		if (text != pastText || size != pastSize || antiAlias != pastAntiAlias) {
			this.text = text;
			this.size = size;
			this.antiAlias = antiAlias;
			setTexture();
		}
		pastText = text;
		pastSize = size;
		pastAntiAlias = antiAlias;
		return this;
	}
	
	private void setTexture() {
		delete();
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		
		if (text.equals("")) return;
		
		paint.setTextSize(size);
		FontMetrics fm = paint.getFontMetrics();
		int width  = (int) paint.measureText(text);
		int height = (int) (fm.descent - fm.ascent);
		
		bitmap = GL11Until.createBitmap(width, height);
		canvas.setBitmap(bitmap);
		canvas.drawARGB(0, 255, 255, 255);
		canvas.drawText(text, 0, - fm.ascent, paint);
		
		gChip = new TonyuGraphicsChip(bitmap);
		TGL.grManager.addTempGraphicsChip(gChip);
	}
	
	public void delete() {
		if (gChip != null) {
			TGL.grManager.deleteTempGraphicsChip(gChip);
			gChip = null;
		}
	}
	
	public void draw(float x, float y, int color, int f, int zOrder) {
		TGL.tonyuDrawer.drawSpriteLT(x, y, gChip, color, f, zOrder);
	}

	public void drawText(float x, float y, String text) {
		setText(text, size);
		TGL.tonyuDrawer.drawSpriteLT(x, y, gChip, Color.WHITE, 0, 0);
	}
	public void drawText(float x, float y, String text, int color) {
		setText(text, size);
		TGL.tonyuDrawer.drawSpriteLT(x, y, gChip, color, 0, 0);
	}
	public void drawText(float x, float y, String text, int color, float size) {
		setText(text, size);
		TGL.tonyuDrawer.drawSpriteLT(x, y, gChip, color, 0, 0);
	}
	public void drawText(float x, float y, String text, int color, float size, int zOrder) {
		setText(text, size);
		TGL.tonyuDrawer.drawSpriteLT(x, y, gChip, color, 0, zOrder);
	}
	
	public void drawText(float x, float y, String text, int color, float size, int f, int zOrder) {
		setText(text, size);
		TGL.tonyuDrawer.drawSpriteLT(x, y, gChip, color, f, zOrder);
	}
	
	
}
