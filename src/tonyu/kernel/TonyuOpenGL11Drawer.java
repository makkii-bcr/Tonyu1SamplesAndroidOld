package tonyu.kernel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.FontMetrics;
import android.util.Log;
import tonyu.android.GL11Until;

public class TonyuOpenGL11Drawer implements TonyuDrawer {
	// nativeライブラリ呼び出し
	//static {
    //    System.loadLibrary("TonyuSampleAndroid");
    //}
	//public native void allDraw_NDK(Object drawObj, boolean updateFrame);
	
	private ArrayList<ArrayList<DrawPack>> drawObjBuf01;
	private ArrayList<ArrayList<DrawPack>> drawObjBuf02;
	private ArrayList<ArrayList<DrawPack>> drawObjBuf;
	private int drawObjBufSW;
	
	private Paint textPaint;
	private Canvas canvas;
	
	public TonyuOpenGL11Drawer() {
		drawObjBuf01 = new ArrayList<ArrayList<DrawPack>>();
		drawObjBuf02 = new ArrayList<ArrayList<DrawPack>>();
		drawObjBuf = drawObjBuf01;
		drawObjBufSW = 0;
		
		textPaint = new Paint();
		textPaint.setAntiAlias(true);
		
		canvas = new Canvas();
	}
	
	public static void init() {
		Bitmap tmpBitmap = Bitmap.createBitmap(2, 2, Config.ARGB_8888);
		Canvas canvas2 = new Canvas(tmpBitmap);
		canvas2.drawColor(Color.WHITE);
		
		Log.v("gl",""+TGL.gl11);
		TGL.whiteTexNo = GL11Until.createTexture(TGL.gl11, tmpBitmap);
		tmpBitmap.recycle();
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
		int color;
		int f;
		TonyuGraphicsChip gChip;
	}
	class DPDxSprite extends DrawPack {
		float x, y;
		int p;
		int color;
		int f;
		float angle, alpha, scaleX, scaleY;
		TonyuGraphicsChip gChip;
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
		dp.color = Color.WHITE;
		dp.zOrder = zOrder;
		dp.gChip = null;
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
		dp.color = Color.WHITE;
		dp.zOrder = zOrder;
		dp.gChip = null;
		drawObjAdd(dp, zOrder);
	}

	@Override
	public void drawSpriteLT(float x, float y, int p, int color, int f, int zOrder) {
		if (!TGL._doDraw) return;
		DPSprite dp = new DPSprite();
		dp.kindNo = DPK_DRAWSPRITE_LT;
		dp.x = x;
		dp.y = y;
		dp.p = p;
		dp.color = color;
		dp.f = f;
		dp.zOrder = zOrder;
		dp.gChip = null;
		drawObjAdd(dp, zOrder);
	}

	@Override
	public void drawSpriteLT(float x, float y, TonyuGraphicsChip gChip, int color, int f, int zOrder) {
		if (!TGL._doDraw) return;
		DPSprite dp = new DPSprite();
		dp.kindNo = DPK_DRAWSPRITE_LT;
		dp.x = x;
		dp.y = y;
		dp.p = -1;
		dp.color = color;
		dp.f = f;
		dp.zOrder = zOrder;
		dp.gChip = gChip;
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
		dp.color = Color.WHITE;
		dp.f = f;
		dp.zOrder = zOrder;
		dp.angle = angle;
		dp.alpha = alpha;
		dp.scaleX = scaleX;
		dp.scaleY = scaleY;
		dp.gChip = null;
		drawObjAdd(dp, zOrder);
	}

	@Override
	public void drawDxSprite(float x, float y, int p, int color, int f, int zOrder, float angle, float alpha, float scaleX, float scaleY) {
		if (!TGL._doDraw) return;
		DPDxSprite dp = new DPDxSprite();
		dp.kindNo = DPK_DRAWDXSPRITE;
		dp.x = x;
		dp.y = y;
		dp.p = p;
		dp.color = color;
		dp.f = f;
		dp.zOrder = zOrder;
		dp.angle = angle;
		dp.alpha = alpha;
		dp.scaleX = scaleX;
		dp.scaleY = scaleY;
		dp.gChip = null;
		drawObjAdd(dp, zOrder);
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
		
		//allDraw_NDK(drawObj, updateFrame);
		
		GL11 gl = (GL11)drawObj;
		TonyuGraphicsChip gChip;
		float scrollX = TGL.viewX;
		float scrollY = TGL.viewY;
		int whiteTexNo = TGL.whiteTexNo;
		int col;
		for (ArrayList<DrawPack> array : drawObjBuf ) {
			for (DrawPack dp : array ) {
				
				switch (dp.kindNo) {
				case DPK_DRAWMETHOD: { // 描画メソッド
					DPMethod dpEx = ((DPMethod)dp);
					dpEx.sprite.directDrawGL11(gl);
					break;
				}
				case DPK_DRAWSPRITE: { // drawSprite
					DPSprite dpEx = ((DPSprite)dp);
					gChip = (dpEx.gChip == null ? TGL.grManager.getGraphicsChip(dpEx.p) : dpEx.gChip);
					if (gChip == null) continue; // nullなら下は実行できない

	                gl.glEnable( GL10.GL_TEXTURE_2D );
			   		gl.glBindTexture(GL11.GL_TEXTURE_2D, gChip.texNo);
			   		((GL11) gl).glTexParameteriv(GL11.GL_TEXTURE_2D,GL11Ext.GL_TEXTURE_CROP_RECT_OES, dpEx.f == 0 ? gChip.texRect : gChip.texRectFlip, 0);
	
					gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
					gl.glEnable(GL11.GL_BLEND);
					col = dpEx.color;
					gl.glColor4f(((col >> 16) & 0xFF) / 255f, ((col >> 8) & 0xFF) / 255f, (col & 0xFF) / 255f, ((col >> 24) & 0xFF) / 255f);
			   		
			   		//描画
			   		((GL11Ext) gl).glDrawTexfOES( TGL.marginHalfWidth + (dpEx.x - gChip.halfWidth - scrollX) * TGL.widthR,
			                                      TGL.marginHalfHeight + TGL.displayHeight - (dpEx.y + gChip.halfHeight - scrollY) * TGL.heightR,
			   				                      0,
			   				                      gChip.width  * TGL.widthR,
			   				                      gChip.height * TGL.heightR
			   				                      );
	                gl.glBindTexture( GL10.GL_TEXTURE_2D, 0 );
	
					break;
				}
				case DPK_DRAWSPRITE_LT: { // drawSpriteLT
					DPSprite dpEx = ((DPSprite)dp);
					gChip = (dpEx.gChip == null ? TGL.grManager.getGraphicsChip(dpEx.p) : dpEx.gChip);
					if (gChip == null) continue; // nullなら下は実行できない

	                gl.glEnable( GL10.GL_TEXTURE_2D );
			   		gl.glBindTexture(GL11.GL_TEXTURE_2D, gChip.texNo);
			   		((GL11) gl).glTexParameteriv(GL11.GL_TEXTURE_2D,GL11Ext.GL_TEXTURE_CROP_RECT_OES, dpEx.f == 0 ? gChip.texRect : gChip.texRectFlip, 0);
	
					gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
					gl.glEnable(GL11.GL_BLEND);
					col = dpEx.color;
					gl.glColor4f(((col >> 16) & 0xFF) / 255f, ((col >> 8) & 0xFF) / 255f, (col & 0xFF) / 255f, ((col >> 24) & 0xFF) / 255f);
			   		
			   		//描画
			   		((GL11Ext) gl).glDrawTexfOES( TGL.marginHalfWidth + (dpEx.x - scrollX) * TGL.widthR,
			   		                              TGL.marginHalfHeight + TGL.displayHeight - (dpEx.y + gChip.height - scrollY) * TGL.heightR,
			   		                              0,
			   		                              gChip.width  * TGL.widthR,
			   		                              gChip.height * TGL.heightR
			   		                              );
	                gl.glBindTexture( GL10.GL_TEXTURE_2D, 0 );
			   		
					break;
				}
				case DPK_DRAWDXSPRITE: { // drawDxSprite
					
					DPDxSprite dpEx = ((DPDxSprite)dp);
					gChip = (dpEx.gChip == null ? TGL.grManager.getGraphicsChip(dpEx.p) : dpEx.gChip);
					if (gChip == null) continue; // nullなら下は実行できない

	                gl.glEnable( GL10.GL_TEXTURE_2D );
					if (dpEx.alpha == 0.0f) {
				   		gl.glBindTexture(GL11.GL_TEXTURE_2D, gChip.texNo);
				   		((GL11) gl).glTexParameteriv(GL11.GL_TEXTURE_2D,GL11Ext.GL_TEXTURE_CROP_RECT_OES, dpEx.f == 0 ? gChip.texRect : gChip.texRectFlip, 0);
				   		
						// 半透明の設定
						gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
						gl.glEnable(GL11.GL_BLEND);
						col = dpEx.color;
						gl.glColor4f(((col >> 16) & 0xFF) / 255f, ((col >> 8) & 0xFF) / 255f, (col & 0xFF) / 255f, dpEx.alpha/255f);
				   		
				   		//描画
				   		((GL11Ext) gl).glDrawTexfOES( TGL.marginHalfWidth + (dpEx.x - gChip.halfWidth - scrollX) * TGL.widthR,
				   		                              TGL.marginHalfHeight + TGL.displayHeight - (dpEx.y + gChip.halfHeight - scrollY) * TGL.heightR,
				   		                              0,
				   		                              gChip.width * dpEx.scaleX * TGL.widthR,
				   		                              gChip.height * (dpEx.scaleY == 0f ? dpEx.scaleX : dpEx.scaleY) * TGL.heightR
				   		                              );
					} else {
				        
				   		gl.glBindTexture(GL11.GL_TEXTURE_2D, gChip.texNo);

						// 半透明の設定
						gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
						gl.glEnable(GL11.GL_BLEND);
						col = dpEx.color;
						gl.glColor4f(((col >> 16) & 0xFF) / 255f, ((col >> 8) & 0xFF) / 255f, (col & 0xFF) / 255f, dpEx.alpha/255f);

			            // テクスチャの描画設定=>次の描画オブジェクトに適用される
			            {
			                float left   = 0;
			                float top    = 0;
			                float right  = gChip.texBitmapWidthR;
			                float bottom = gChip.texBitmapHeightR;
			                float[] uv = {
			                		left, bottom,
			                		left, top,
			                        right, bottom,
			                        right, top,
			                };

			                // Java => OpenGL にあたっての変換
			                ByteBuffer bb = ByteBuffer.allocateDirect(uv.length * 4);
			                bb.order(ByteOrder.nativeOrder());
			                FloatBuffer fb = bb.asFloatBuffer();
			                fb.put(uv);
			                fb.position(0);
			                
			                gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			                gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, fb);// UV配列をOpen GLに紐付け
			            }
			            
			            // ポリゴンの描画
			            {
			            	/*
			                float left   = TGL.marginHalfWidth + (dpEx.x - gChip.halfWidth - scrollX) * TGL.widthR;
			                float top    = TGL.marginHalfHeight + (dpEx.y + gChip.halfHeight - scrollY) * TGL.heightR;
			                float right  = left + gChip.width * dpEx.scaleX * TGL.widthR;
			                float bottom = top - gChip.height * (dpEx.scaleY == 0f ? dpEx.scaleX : dpEx.scaleY) * TGL.heightR;

			                float[] vertexes = {
			                        left,  top,   
			                        left,  bottom,
			                        right, top,   
			                        right, bottom,
			                };
			                */
			                
			                float hw = gChip.halfWidth * dpEx.scaleX * TGL.widthR;
			                float hh = gChip.halfHeight * (dpEx.scaleY == 0f ? dpEx.scaleX : dpEx.scaleY) * TGL.heightR;
			                if (dpEx.f != 0) { // 左右反転
			                	hh = -hh;
			                }
			                float x = TGL.marginHalfWidth  + (dpEx.x - scrollX) * TGL.widthR;
			                float y = TGL.marginHalfHeight + (dpEx.y - scrollY) * TGL.heightR;

			                float x1 = -hw, y1 =  hh;
			                float x2 = -hw, y2 = -hh;
			                float x3 =  hw, y3 =  hh;
			                float x4 =  hw, y4 = -hh;
			                
			                float angle = dpEx.angle;
			                float sin = (float)Math.sin(Math.toRadians(angle));
			                float cos = (float)Math.cos(Math.toRadians(angle));
			                
			                float tx;
			                tx = x1 * cos - y1 * sin + x;
			                y1 = y1 * cos + x1 * sin + y;
			                x1 = tx;
			                tx = x2 * cos - y2 * sin + x;
			                y2 = y2 * cos + x2 * sin + y;
			                x2 = tx;
			                tx = x3 * cos - y3 * sin + x;
			                y3 = y3 * cos + x3 * sin + y;
			                x3 = tx;
			                tx = x4 * cos - y4 * sin + x;
			                y4 = y4 * cos + x4 * sin + y;
			                x4 = tx;
			                
			                float[] vertexes = {
			                        x1, y1,   
			                        x2, y2,
			                        x3, y3,   
			                        x4, y4,
			                };
			                
			                
			                // Java => OpenGL にあたっての変換
			                ByteBuffer bb = ByteBuffer.allocateDirect(vertexes.length * 4);
			                bb.order(ByteOrder.nativeOrder());
			                FloatBuffer fb = bb.asFloatBuffer();
			                fb.put(vertexes);
			                fb.position(0);
			                
			                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);// 頂点バッファの有効化
			                gl.glVertexPointer(2, GL10.GL_FLOAT, 0, fb);// 頂点バッファをOpen GLに紐付け
			                gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);// 描画する
			            }
						
					}
	                gl.glBindTexture( GL10.GL_TEXTURE_2D, 0 );
					break;
				}
				case DPK_DRAWTEXT: { // drawText

					DPText dpEx = ((DPText)dp);
					col = dpEx.color;
					textPaint.setColor(Color.WHITE);
					textPaint.setTextSize(dpEx.size);
					FontMetrics fm = textPaint.getFontMetrics();
					int width  = (int) textPaint.measureText(dpEx.text);
					int height = (int) (fm.descent - fm.ascent);
					
					Bitmap bitmap = GL11Until.createBitmap(width, height);
					canvas.setBitmap(bitmap);
					canvas.drawARGB(0, 255, 255, 255);
					canvas.drawText(dpEx.text, 0, - fm.ascent, textPaint);
					
					int texNo = GL11Until.createTexture(gl, bitmap);
					bitmap.recycle();

	                gl.glEnable( GL10.GL_TEXTURE_2D );
			   		gl.glBindTexture(GL11.GL_TEXTURE_2D, texNo);
			   		int rect[] = { 0,  height,  width, -height};
			   		((GL11) gl).glTexParameteriv(GL11.GL_TEXTURE_2D,GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);
			   		
					gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
					gl.glEnable(GL11.GL_BLEND);
					gl.glColor4f(((col >> 16) & 0xFF) / 256f, ((col >> 8) & 0xFF) / 256f, (col & 0xFF) / 256f, ((col >> 24) & 0xFF) / 256f);
			   		
			   		//描画
			   		((GL11Ext) gl).glDrawTexfOES( TGL.marginHalfWidth + (dpEx.x - scrollX) * TGL.widthR,
			   		                              TGL.marginHalfHeight + TGL.displayHeight - (dpEx.y + height - scrollY) * TGL.heightR,
			   		                              0,
			   		                              width  * TGL.widthR,
			   		                              height * TGL.heightR
			   		                              );
			   		// テクスチャ削除
			   		GL11Until.deleteTexture(gl, texNo);
			   		
					break;
				}
				case DPK_DRAWTEXTCC: { // drawTextCC

					DPText dpEx = ((DPText)dp);
					col = dpEx.color;
					textPaint.setColor(Color.WHITE);
					textPaint.setTextSize(dpEx.size);
					FontMetrics fm = textPaint.getFontMetrics();
					int width  = (int) textPaint.measureText(dpEx.text);
					int height = (int) (fm.descent - fm.ascent);
					
					Bitmap bitmap = GL11Until.createBitmap(width, height);
					canvas.setBitmap(bitmap);
					canvas.drawText(dpEx.text, 0, - fm.ascent, textPaint);

	                gl.glEnable( GL10.GL_TEXTURE_2D );
					int texNo = GL11Until.createTexture(gl, bitmap);
					bitmap.recycle();
					
			   		gl.glBindTexture(GL11.GL_TEXTURE_2D, texNo);
			   		int rect[] = { 0,  height,  width, -height};
			   		((GL11) gl).glTexParameteriv(GL11.GL_TEXTURE_2D,GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);
	
					gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
					gl.glEnable(GL11.GL_BLEND);
					gl.glColor4f(((col >> 16) & 0xFF) / 256f, ((col >> 8) & 0xFF) / 256f, (col & 0xFF) / 256f, ((col >> 24) & 0xFF) / 256f);
			   		
			   		//描画
			   		((GL11Ext) gl).glDrawTexfOES( TGL.marginHalfWidth + (dpEx.x - width/2 - scrollX) * TGL.widthR,
			   		                              TGL.marginHalfHeight + TGL.displayHeight - (dpEx.y + height/2 - scrollY) * TGL.heightR,
			   		                              0,
			   		                              width  * TGL.widthR,
			   		                              height * TGL.heightR
			   		                              );
			   		// テクスチャ削除
			   		GL11Until.deleteTexture(gl, texNo);
			   		                                                                                
					break;
				}
				case DPK_DRAWLINE: { // drawLine

					DP2PC dpEx = ((DP2PC)dp);
					
					col = dpEx.color;
					
	                // Java => OpenGL にあたっての変換
					// 頂点座標

	                float sx = TGL.marginHalfWidth  + (dpEx.sx - scrollX) * TGL.widthR;
	                float sy = TGL.marginHalfHeight + (dpEx.sy - scrollY) * TGL.heightR;
	                float dx = TGL.marginHalfWidth  + (dpEx.dx - scrollX) * TGL.widthR;
	                float dy = TGL.marginHalfHeight + (dpEx.dy - scrollY) * TGL.heightR;
					float[] vertexes = {
	                        sx, sy,
							dx, dy,
	                };
					
	                ByteBuffer bb = ByteBuffer.allocateDirect(vertexes.length * 4);
	                bb.order(ByteOrder.nativeOrder());
	                FloatBuffer fb = bb.asFloatBuffer();
	                fb.put(vertexes);
	                fb.position(0);

	                gl.glDisable( GL10.GL_TEXTURE_2D );
					gl.glDisable(GL11.GL_BLEND);
	        		
	                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);// 頂点バッファの有効化
	                gl.glVertexPointer(2, GL10.GL_FLOAT, 0, bb);// 頂点バッファをOpen GLに紐付け

					gl.glColor4f(Color.red(col)/255f, Color.green(col)/255f, Color.blue(col)/255f, Color.alpha(col)/255f);
					gl.glLineWidth( TGL.widthR );
					gl.glDrawArrays( GL10.GL_LINES, 0, 2 );
	                gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);// 頂点バッファの無効化
					
					
	                
					
	                
	                /*
					
					
			   		gl.glBindTexture(GL11.GL_TEXTURE_2D, whiteTexNo);
			   		int rect[] = { 0,  0,  1, 1};
			   		((GL11) gl).glTexParameteriv(GL11.GL_TEXTURE_2D,GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);
	
					gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					gl.glEnable(GL11.GL_BLEND);
					gl.glColor4f(Color.red(col)/255f, Color.green(col)/255f, Color.blue(col)/255f, Color.alpha(col)/255f);
			   		
					
			   		//描画
					float _sx, _sy, _dx, _dy;
					float sx, sy, dx, dy;
					float w, h;
					float x1, y1, x2, y2;
					float vx, vy;
					
					_sx = dpEx.sx;
					_sy = dpEx.sy;
					_dx = dpEx.dx;
					_dy = dpEx.dy;
					w = Math.abs(_dx - _sx);
					h = Math.abs(_dy - _sy);
					
					if (w>=h) {

					    if (_sx <= _dx) {
					    	sx = _sx;
					    	sy = _sy;
					    	dx = _dx;
					    	dy = _dy;
					    } else {
					    	sx = _dx;
					    	sy = _dy;
					    	dx = _sx;
					    	dy = _sy;
					    }
					    if (h == 0) {
					   		((GL11Ext) gl).glDrawTexfOES( TGL.marginHalfWidth + (sx - scrollX) * TGL.widthR,
		                            TGL.marginHalfHeight + TGL.displayHeight - (sy - scrollY) * TGL.heightR,
		                            0,
		                            w * TGL.widthR,
		                            TGL.heightR
		                            );
					    } else {
					    	if (sy < dy) {
					    		vx = w/(h+1);
					    		x1 = sx; x2 = sx;
					    		for (y1=sy; y1<=dy+1; y1++) {
					    			x2 += vx;
					    			if (x2 >= dx) x2 = dx;
							   		((GL11Ext) gl).glDrawTexfOES( TGL.marginHalfWidth + (x1 - scrollX) * TGL.widthR,
		   		                            TGL.marginHalfHeight + TGL.displayHeight - (y1 - scrollY) * TGL.heightR,
		   		                            0,
							   				(x2 - x1) * TGL.heightR,
		   		                            TGL.heightR
		   		                            );
					    			x1 = x2;
					    		}
					        } else {
					        	vx = w/(h+1);
					        	x1 = sx; x2 = sx;
					        	for (y1=sy; y1>=dy-1; y1--) {
					        		x2 += vx;
					        		if (x2 >= dx) x2 = dx;
							   		((GL11Ext) gl).glDrawTexfOES( TGL.marginHalfWidth + (x1 - scrollX) * TGL.widthR,
				                            TGL.marginHalfHeight + TGL.displayHeight - (y1 - scrollY) * TGL.heightR,
				                            0,
							   				(x2 - x1) * TGL.heightR,
				                            TGL.heightR
				                            );
					        		x1 = x2;
					        	}
					        }
					    }
					} else {

					    if (_sy <= _dy) {
					    	sx = _sx;
					    	sy = _sy;
					    	dx = _dx;
					    	dy = _dy;
					    } else {
					    	sx = _dx;
					    	sy = _dy;
					    	dx = _sx;
					    	dy = _sy;
					    }
					    if (w == 0) {
					   		((GL11Ext) gl).glDrawTexfOES( TGL.marginHalfWidth + (dx - scrollX) * TGL.widthR,
					   				TGL.marginHalfHeight + TGL.displayHeight - (dy - scrollY) * TGL.heightR,
				   					0,
				   					TGL.widthR,
				   					h * TGL.heightR
					   				);
					    } else {
					    	if (sx < dx) {
					    		vy = h/(w+1);
					    		y1 = sy; y2 = sy;
					    		for (x1=sx; x1<=dx+1; x1++) {
					    			y2 += vy;
					    			if (y2 >= dy) y2 = dy;
							   		((GL11Ext) gl).glDrawTexfOES( TGL.marginHalfWidth + (x1 - scrollX) * TGL.widthR,
							   				TGL.marginHalfHeight + TGL.displayHeight - (y2 - 1 - scrollY) * TGL.heightR,
							   				0,
							   				TGL.widthR,
							   				(y2 - y1) * TGL.heightR
							   				);
					    			y1 = y2;
					    		}
					    	} else {
					    		vy = h/(w+1);
					    		y1 = sy; y2 = sy;
					    		for (x1=sx; x1>=dx-1; x1--) {
					    			y2 += vy;
					    			if (y2 >= dy) y2 = dy;
							   		((GL11Ext) gl).glDrawTexfOES( TGL.marginHalfWidth + (x1 - scrollX) * TGL.widthR,
							   				TGL.marginHalfHeight + TGL.displayHeight - (y2 - 1 - scrollY) * TGL.heightR,
							   				0,
							   				TGL.widthR,
							   				(y2 - y1) * TGL.heightR
							   				);
					    			y1 = y2;
					    		}
					    	}
					    }
					}
					*/
					break;
				}
				case DPK_FILLRECT: { // fillRect
					/*
					DP2PC dpEx = ((DP2PC)dp);
					float width  = dpEx.dx - dpEx.sx;
					float height = dpEx.dy - dpEx.sy;

					col = dpEx.color;
					
			   		gl.glBindTexture(GL11.GL_TEXTURE_2D, whiteTexNo);
			   		int rect[] = { 0,  1,  1, -1};
			   		((GL11) gl).glTexParameteriv(GL11.GL_TEXTURE_2D,GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);
	
					gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					gl.glEnable(GL11.GL_BLEND);
					gl.glColor4f(Color.red(col)/255f, Color.green(col)/255f, Color.blue(col)/255f, Color.alpha(col)/255f);
			   		
			   		//描画
			   		((GL11Ext) gl).glDrawTexfOES( TGL.marginHalfWidth + (dpEx.sx - scrollX) * TGL.widthR,
			   		                              TGL.marginHalfHeight + TGL.displayHeight - (dpEx.sy + height - scrollY) * TGL.heightR,
			   		                              0,
			   		                              width  * TGL.widthR,
			   		                              height * TGL.heightR
			   		                              );
					*/
					
					DP2PC dpEx = ((DP2PC)dp);
					
					col = dpEx.color;
					
	                // Java => OpenGL にあたっての変換
					// 頂点座標
	                float sx = TGL.marginHalfWidth  + (dpEx.sx - scrollX) * TGL.widthR;
	                float sy = TGL.marginHalfHeight + (dpEx.sy - scrollY) * TGL.heightR;
	                float dx = TGL.marginHalfWidth  + (dpEx.dx - scrollX) * TGL.widthR;
	                float dy = TGL.marginHalfHeight + (dpEx.dy - scrollY) * TGL.heightR;
					float[] vertexes = {
	                        sx, sy,
							dx, sy,
							sx, dy,
							dx, dy,
	                };
					
	                ByteBuffer bb = ByteBuffer.allocateDirect(vertexes.length * 4);
	                bb.order(ByteOrder.nativeOrder());
	                FloatBuffer fb = bb.asFloatBuffer();
	                fb.put(vertexes);
	                fb.position(0);

	                gl.glDisable( GL10.GL_TEXTURE_2D );
					gl.glDisable(GL11.GL_BLEND);
	        		
	                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);// 頂点バッファの有効化
	                gl.glVertexPointer(2, GL10.GL_FLOAT, 0, bb);// 頂点バッファをOpen GLに紐付け

					gl.glColor4f(Color.red(col)/255f, Color.green(col)/255f, Color.blue(col)/255f, Color.alpha(col)/255f);
					gl.glDrawArrays( GL10.GL_TRIANGLE_STRIP, 0, 4 );
	                gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);// 頂点バッファの無効化
					
	                
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
