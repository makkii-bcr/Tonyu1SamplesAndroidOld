package tonyu.kernel;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

public class TonyuSurfaceViewDrawer implements TonyuDrawer {
	private ArrayList<ArrayList<DrawPack>> drawObjBuf;
	private ArrayList<ArrayList<DrawPack>> drawObjBuf01;
	private ArrayList<ArrayList<DrawPack>> drawObjBuf02;
	private int drawObjBufSW;
	private Paint bitmapPaint, bitmapDxPaint;
	private Paint textPaint, textCCPaint;
	private Paint linePaint, fillRectPaint;
	private Matrix bitmapMatrix;
	public TonyuSurfaceViewDrawer() {
		drawObjBuf01 = new ArrayList<ArrayList<DrawPack>>();
		drawObjBuf02 = new ArrayList<ArrayList<DrawPack>>();
		drawObjBuf = drawObjBuf01;
		drawObjBufSW = 0;
		
		bitmapPaint = new Paint();
		bitmapDxPaint = new Paint();
		//bitmapPaint.setAntiAlias(true);
		//bitmapPaint.setFilterBitmap(true);
		bitmapMatrix = new Matrix();
		
		textPaint = new Paint();
		textPaint.setAntiAlias(true);
		
		textCCPaint = new Paint();
		textCCPaint.setAntiAlias(true);
		textCCPaint.setTextAlign(Paint.Align.CENTER);
		
		linePaint = new Paint();
		//linePaint.setAntiAlias(true);
		
		fillRectPaint = new Paint();
	}

	public static final int DPK_DRAWMETHOD    = 0x00;
	public static final int DPK_DRAWSPRITE    = 0x10;
	public static final int DPK_DRAWSPRITE_LT = 0x11;
	public static final int DPK_DRAWDXSPRITE  = 0x20;
	public static final int DPK_DRAWTEXT      = 0x30;
	public static final int DPK_DRAWTEXTCC    = 0x31;
	public static final int DPK_DRAWLINE      = 0x40;
	public static final int DPK_FILLRECT      = 0x50;
	

	//////////////////////////////////////////////////////////////////////////////////////////
	// 描画情報をクラスとして一時的に保存 //
	class DrawPack {
		int kindNo;
		int zOrder;
	}
	class DPMethod extends DrawPack {
		TonyuSprite sprite;
	}
	class DPSprite extends DrawPack {
		float x, y;
		int p;
		int f;
	}
	class DPDxSprite extends DrawPack {
		float x, y;
		int p;
		int f;
		float angle, alpha, scaleX, scaleY;
	}
	class DPText extends DrawPack {
		float x, y;
		String text;
		int color;
		float size;
	}
	class DP2PC extends DrawPack {
		float sx, sy, dx, dy;
		int color;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
	
	
	@Override
	public void drawMethod(TonyuSprite sprite, int zOrder) {
		if (!TGL._doDraw) return;
		DPMethod dp = new DPMethod();
		dp.kindNo = DPK_DRAWMETHOD;
		dp.sprite = sprite;
		dp.zOrder = zOrder;
		drawObjAdd(dp, zOrder);
	}
	@Override
	public void drawSprite(float x, float y, int p, int f, int zOrder) {
		if (!TGL._doDraw) return;
		DPSprite dp = new DPSprite();
		dp.kindNo = DPK_DRAWSPRITE;
		dp.x = x;
		dp.y = y;
		dp.p = p;
		dp.f = f;
		dp.zOrder = zOrder;
		drawObjAdd(dp, zOrder);
	}
	@Override
	public void drawSpriteLT(float x, float y, int p, int f, int zOrder) {
		if (!TGL._doDraw) return;
		DPSprite dp = new DPSprite();
		dp.kindNo = DPK_DRAWSPRITE_LT;
		dp.x = x;
		dp.y = y;
		dp.p = p;
		dp.f = f;
		dp.zOrder = zOrder;
		drawObjAdd(dp, zOrder);
	}
	
	@Override
	public void drawSpriteLT(float x, float y, int p, int color, int f, int zOrder) {
		drawSpriteLT(x, y, p, f, zOrder);
	}
	
	@Override // 未使用
	public void drawSpriteLT(float x, float y, TonyuGraphicsChip gChip, int color, int f, int zOrder) {
		if (!TGL._doDraw) return;
		DPSprite dp = new DPSprite();
		dp.kindNo = DPK_DRAWSPRITE_LT;
		dp.x = x;
		dp.y = y;
		dp.p = 0;
		dp.f = f;
		//dp.color = color;
		dp.zOrder = zOrder;
		//dp.gChip = gChip;
		drawObjAdd(dp, zOrder);
	}

	@Override
	public void drawDxSprite(float x, float y, int p, int f, int zOrder, float angle, float alpha, float scaleX, float scaleY) {
		if (!TGL._doDraw) return;
		DPDxSprite dp = new DPDxSprite();
		dp.kindNo = DPK_DRAWDXSPRITE;
		dp.x = x;
		dp.y = y;
		dp.p = p;
		dp.f = f;
		dp.zOrder = zOrder;
		dp.angle = angle;
		dp.alpha = alpha;
		dp.scaleX = scaleX;
		dp.scaleY = scaleY;
		drawObjAdd(dp, zOrder);
	}
	@Override
	public void drawDxSprite(float x, float y, int p, int color, int f, int zOrder, float angle, float alpha, float scaleX, float scaleY) {
		drawDxSprite(x, y, p, f, zOrder, angle, alpha, scaleX, scaleY);
	}

	@Override
	public void drawText(float x, float y, String text, int color, float size, int zOrder) {
		if (!TGL._doDraw) return;
		DPText dp = new DPText();
		dp.kindNo = DPK_DRAWTEXT;
		dp.x = x;
		dp.y = y;
		if (text != null) dp.text = text; else dp.text = "null";
		dp.color = color;
		dp.size = size;
		dp.zOrder = zOrder;
		drawObjAdd(dp, zOrder);
	}

	@Override
	public void drawTextCC(float x, float y, String text, int color, float size, int zOrder) {
		if (!TGL._doDraw) return;
		DPText dp = new DPText();
		dp.kindNo = DPK_DRAWTEXTCC;
		dp.x = x;
		dp.y = y;
		if (text != null) dp.text = text; else dp.text = "null";
		dp.color = color;
		dp.size = size;
		dp.zOrder = zOrder;
		drawObjAdd(dp, zOrder);
	}

	@Override
	public void drawLine(float sx, float sy, float dx, float dy, int color, int zOrder) {
		if (!TGL._doDraw) return;
		DP2PC dp = new DP2PC();
		dp.kindNo = DPK_DRAWLINE;
		dp.sx = sx;
		dp.sy = sy;
		dp.dx = dx;
		dp.dy = dy;
		dp.color = color;
		dp.zOrder = zOrder;
		drawObjAdd(dp, zOrder);
	}

	@Override
	public void fillRect(float sx, float sy, float dx, float dy, int color, int zOrder) {
		if (!TGL._doDraw) return;
		DP2PC dp = new DP2PC();
		dp.kindNo = DPK_FILLRECT;
		dp.sx = sx;
		dp.sy = sy;
		dp.dx = dx;
		dp.dy = dy;
		dp.color = color;
		dp.zOrder = zOrder;
		drawObjAdd(dp, zOrder);
	}

	
	// バッファにためられた描画情報をすべて表示
	@Override
	public void allDraw(Object drawObj, boolean updateFrame) {
		
		if (!updateFrame) { // バッファ更新なし
			changeDrawObjBuf(-1); // バッファを前回のものに戻す
		}
		
		Canvas canvas = (Canvas)drawObj;
		TonyuGraphicsChip gChip;
		int scrollX = (int)Math.floor(TGL.viewX);
		int scrollY = (int)Math.floor(TGL.viewY);
		for (ArrayList<DrawPack> array : drawObjBuf ) {
			for (DrawPack dp : array ) {
				switch (dp.kindNo) {
				case DPK_DRAWMETHOD: { // 描画メソッド
					DPMethod dpEx = ((DPMethod)dp);
					dpEx.sprite.directDrawCanvas(canvas);
					break;
				}
				case DPK_DRAWSPRITE: { // drawSprite
					DPSprite dpEx = ((DPSprite)dp);
					gChip = TGL.grManager.getGraphicsChip(dpEx.p);
					if (gChip == null) continue; // nullなら下は実行できない
					canvas.drawBitmap(gChip.bitmap, dpEx.x - gChip.halfWidth - scrollX, dpEx.y - gChip.halfHeight - scrollY, bitmapPaint);
					break;
				}
				case DPK_DRAWSPRITE_LT: { // drawSpriteLT
					DPSprite dpEx = ((DPSprite)dp);
					gChip = TGL.grManager.getGraphicsChip(dpEx.p);
					if (gChip == null) continue; // nullなら下は実行できない
					canvas.drawBitmap(gChip.bitmap, dpEx.x - scrollX, dpEx.y - scrollY, bitmapPaint);
					break;
				}
				case DPK_DRAWDXSPRITE: { // drawDxSprite
					DPDxSprite dpEx = ((DPDxSprite)dp);
					gChip = TGL.grManager.getGraphicsChip(dpEx.p);
					if (gChip == null) continue; // nullなら下は実行できない
					bitmapDxPaint.setAlpha((int)dpEx.alpha);
					bitmapMatrix.setScale(dpEx.scaleX, dpEx.scaleY == 0f ? dpEx.scaleX : dpEx.scaleY, gChip.halfWidth, gChip.halfHeight);
					bitmapMatrix.postRotate(dpEx.angle, gChip.halfWidth, gChip.halfHeight);
					bitmapMatrix.postTranslate(dpEx.x - gChip.halfWidth - scrollX, dpEx.y - gChip.halfHeight - scrollY);
					canvas.drawBitmap(gChip.bitmap, bitmapMatrix, bitmapDxPaint);
					break;
				}
				case DPK_DRAWTEXT: { // drawText
					DPText dpEx = ((DPText)dp);
					textPaint.setColor(dpEx.color);
					textPaint.setTextSize(dpEx.size);
					FontMetrics fm = textPaint.getFontMetrics();
					canvas.drawText(dpEx.text, dpEx.x - scrollX, dpEx.y - fm.ascent - scrollY, textPaint);
					break;
				}
				case DPK_DRAWTEXTCC: { // drawTextCC
					DPText dpEx = ((DPText)dp);
					textCCPaint.setColor(dpEx.color);
					textCCPaint.setTextSize(dpEx.size);
					FontMetrics fm = textPaint.getFontMetrics();
					canvas.drawText(dpEx.text, dpEx.x - scrollX, dpEx.y - fm.ascent/2f - scrollY, textCCPaint);
					break;
				}
				case DPK_DRAWLINE: { // drawLine
					DP2PC dpEx = ((DP2PC)dp);
					linePaint.setColor(dpEx.color);
					canvas.drawLine(dpEx.sx - scrollX, dpEx.sy - scrollY, dpEx.dx - scrollX, dpEx.dy - scrollY, linePaint);
					break;
				}
				case DPK_FILLRECT: { // fillRect
					DP2PC dpEx = ((DP2PC)dp);
					fillRectPaint.setColor(dpEx.color);
					canvas.drawRect(dpEx.sx - scrollX, dpEx.sy - scrollY, dpEx.dx - scrollX, dpEx.dy - scrollY, fillRectPaint);
					break;
				}
				default:
					break;
				}
			}
		}
		
		// バッファを切り替える
		changeDrawObjBuf(1);
		
		// バッファの描画情報をすべてクリア
		for (ArrayList<DrawPack> array : drawObjBuf ) {
			array.clear();
		}
		drawObjBuf.clear();
	}
	
	private void changeDrawObjBuf(int vv) {
		drawObjBufSW += vv;
		switch (drawObjBufSW % 2) {
			case 0: drawObjBuf = drawObjBuf01; break;
			case 1: drawObjBuf = drawObjBuf02; break;
			default: break;
		}
	}
	
	
	// 描画オブジェクト追加
	private void drawObjAdd(DrawPack dp, int zOrder) {
		ArrayList<DrawPack> buf;
		int tempZ;
		int size = drawObjBuf.size();
		for (int i=0; i<size; i++) {
			tempZ = drawObjBuf.get(i).get(0).zOrder;
			if (zOrder == tempZ) {
				drawObjBuf.get(i).add(dp);
				return;
			}
			if (zOrder > tempZ) {
				buf = new ArrayList<DrawPack>();
				buf.add(dp);
				drawObjBuf.add(i, buf);
				return;
			}
			//drawObjBuf.add(obj);
		}
		buf = new ArrayList<DrawPack>();
		buf.add(dp);
		drawObjBuf.add(buf);
	}
	
	
}
