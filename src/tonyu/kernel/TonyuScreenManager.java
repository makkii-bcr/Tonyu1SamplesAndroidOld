package tonyu.kernel;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class TonyuScreenManager {
	private int _visible = 1;
	private Paint paint, paint2, paint3;
	private Path path;
	//private int cnt = 0;
	//private int texNo;
	public TonyuScreenManager() {
		paint = new Paint();
        paint.setColor(Color.WHITE);
        
		paint2 = new Paint();
        paint2.setColor(Color.BLACK);
        
        path = new Path();
        
		paint3 = new Paint();
        paint3.setColor(Color.rgb(17, 255, 17));
	}
	
	public void moveCursor(float x, float y) {
		TGL.tonyuBoot.setPadXY(x, y);
	}

	// スクリーンサイズを変更する
	public void resizeScreen(int w, int h) {
		TGL.screenWidth  = w;
		TGL.screenHeight = h;
	}
	// スクリーンをズームする。タップ位置も
	public void zoomScreen(int w, int h) {
		TGL.screenWidth  = w;
		TGL.screenHeight = h;
		TGL.tonyuBoot.updatePadXY();
	}
	
	public void showCursor(int v) {
		_visible = v;
	}
	
	public void loadDraw(Object drawObj) {
		GL11 gl;
		Canvas canvas;

		if (TGL.drawMode == TonyuBoot.DRAW_OPENGL11) {
			gl = (GL11) drawObj;
			// ページロード中の緑の奴
			//GL11Until.drawTextureColor(gl, TGL.whiteTexNo, x, y, width, height, colR, colG, colB, colA);
		} else if (TGL.drawMode == TonyuBoot.DRAW_SURFACEVIEW) {
			canvas = (Canvas) drawObj;
			// ページロード中の緑の奴
			canvas.drawRect(TGL.screenWidth-24, TGL.screenHeight-16, TGL.screenWidth-8, TGL.screenHeight-8, paint3);
		}
	}
	
	public void draw(Object drawObj) {
		GL11 gl;
		Canvas canvas;
		
		if (TGL.drawMode == TonyuBoot.DRAW_OPENGL11) {
			gl = (GL11) drawObj;
			
			// 画面の余白に枠を表示 //
			int col = TGL.map.getMarginColor(); // 色取得

            gl.glEnable( GL10.GL_TEXTURE_2D );
			gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			gl.glEnable(GL11.GL_BLEND);
	   		gl.glActiveTexture(GL11.GL_TEXTURE0);
	   		gl.glBindTexture(GL11.GL_TEXTURE_2D, TGL.whiteTexNo);
	   		int rect[] = { 0,  1,  1, -1};
	   		((GL11) gl).glTexParameteriv(GL11.GL_TEXTURE_2D,GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);

			gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			gl.glEnable(GL11.GL_BLEND);
			gl.glColor4f(Color.red(col)/255f, Color.green(col)/255f, Color.blue(col)/255f, Color.alpha(col)/255f);
	   		
	   		//描画
			if (TGL.marginHalfWidth > 0) {
				((GL11Ext) gl).glDrawTexfOES(0, 0, 0, TGL.marginHalfWidth, TGL.displayHeight);
				((GL11Ext) gl).glDrawTexfOES(TGL.displayWidth - TGL.marginHalfWidth, 0, 0, TGL.marginHalfWidth, TGL.displayHeight);
			} else
			if (TGL.marginHalfHeight > 0) {
				((GL11Ext) gl).glDrawTexfOES(0, TGL.displayHeight - TGL.marginHalfHeight, 0, TGL.displayWidth, TGL.marginHalfHeight);
				((GL11Ext) gl).glDrawTexfOES(0, 0, 0, TGL.displayWidth, TGL.marginHalfHeight);
			}
			
			
		} else if (TGL.drawMode == TonyuBoot.DRAW_SURFACEVIEW) {
			canvas = (Canvas) drawObj;
			
			// WindowsXPまでの旧マウスポインタ
			if (1 <= _visible && _visible <= 2) {
				
				int mx = (int)TGL.padX;
				int my = (int)TGL.padY;
				
				path.reset();
		        path.moveTo(mx, my);
		        path.lineTo(mx+12, my+12);
		        path.lineTo(mx, my+12);
		        canvas.drawPath(path, paint);
				path.reset();
		        path.moveTo(mx, my+11);
		        path.lineTo(mx, my+17);
		        path.lineTo(mx+5, my+11);
		        canvas.drawPath(path, paint);
		        
		        canvas.drawLine(mx, my, mx+12, my+12, paint2);      // ＼
		        canvas.drawLine(mx, my, mx, my+16, paint2);         // |
		        canvas.drawLine(mx, my+17, mx+4, my+13, paint2);    // ／
		        canvas.drawLine(mx+7, my+11, mx+11, my+11, paint2); // ―
		        canvas.drawLine(mx+4, my+12, mx+8, my+20, paint2);  // \ 黒
		        canvas.drawLine(mx+5, my+12, mx+9, my+20, paint);   // \ 白
		        canvas.drawLine(mx+6, my+12, mx+10, my+20, paint);  // \ 白
		        canvas.drawLine(mx+7, my+12, mx+11, my+20, paint2); // \ 黒
		        canvas.drawLine(mx+8, my+20, mx+10, my+20, paint2); // ― 
			}
		}
	}
}
