package tonyu.android;

import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import tonyu.kernel.TGL;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.FontMetrics;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLUtils;
import android.util.Log;

public class GL11Until {
	/**
	 * 画像の幅・高さが2の累乗になるように変換
	 * @param bitmap
	 * @return 
	 * 画像の大きさを変える必要がなければ、引数のbitmapを返す<br>
	 * 画像の大きさを変える必要があれば、新たなbitmapを返す
	 */
	public static Bitmap resizeBitmap(Bitmap bitmap) {
		int width  = bitmap.getWidth();
		int height = bitmap.getHeight();
		int newWidth, newHeight;
		for (newWidth = 2; newWidth <= 4096; newWidth *= 2) {
			if (newWidth >= width) break;
		}
		for (newHeight = 2; newHeight <= 4096; newHeight *= 2) {
			if (newHeight >= height) break;
		}
		//Log.v("squareBitmapSize", ""+ newWidth +" "+newHeight);
		if (width == newWidth && height == newHeight) { // そのままテクスチャが使える
			return bitmap;
		} else {
			Bitmap newBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(bitmap, 0, 0, new Paint());
			return newBitmap;
		}
	}
	
	/**
	 * 幅・高さが2の累乗の空画像を生成
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap createBitmap(int width, int height) {
		int newWidth, newHeight;
		for (newWidth = 2; newWidth <= 4096; newWidth *= 2) {
			if (newWidth >= width) break;
		}
		for (newHeight = 2; newHeight <= 4096; newHeight *= 2) {
			if (newHeight >= height) break;
		}
		Bitmap newBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);
		return newBitmap;
	}
	
	/**
	 * 幅・高さが2の累乗のテキストビットマップを生成
	 * @param text
	 * @param color
	 * @param size
	 * @return
	 */
	public static Bitmap createTextBitmap(String text, int color, float size, boolean aa) {
		int newWidth, newHeight;
		Paint paint = new Paint();
		paint.setAntiAlias(aa);
		paint.setColor(color);
		paint.setTextSize(size);
		FontMetrics fm = paint.getFontMetrics();
		int width  = (int) paint.measureText(text);
		int height = (int) (fm.descent - fm.ascent);
		
		for (newWidth = 2; newWidth <= 4096; newWidth *= 2) {
			if (newWidth >= width) break;
		}
		for (newHeight = 2; newHeight <= 4096; newHeight *= 2) {
			if (newHeight >= height) break;
		}
		Bitmap newBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);
		
		Canvas canvas = new Canvas(newBitmap);
		canvas.drawText(text, 0, - fm.ascent, paint);
		
		return newBitmap;
	}
	public static Bitmap createTextBitmap(String text, int color, float size) {
		return createTextBitmap(text, color, size, true);
	}
	
	/**
	 * ビットマップからテクスチャを作成
	 * @param gl GLインタフェース
	 * @param bitmap コピー元ビットマップ
	 * @return
	 */
	public static int createTexture(GL11 gl, Bitmap bitmap) {
		//Log.v("createTexture", ""+gl+" "+bitmap);
		gl.glEnable(GL11.GL_ALPHA_TEST);
		gl.glEnable(GL11.GL_BLEND);
		gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		gl.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
		//テクスチャIDを割り当てる
		int[] textureID = new int[1];
		gl.glGenTextures(1, textureID, 0);
		int texNo = textureID[0];
		
		//テクスチャIDのバインド
		gl.glBindTexture(GL11.GL_TEXTURE_2D, texNo);

	 	//OpenGL ES用のメモリ領域に画像データを渡す。上でバインドされたテクスチャIDと結び付けられる。
		//GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, bitmap, 0);
		//Log.v("createTexture",""+texNo+" "+gl);
		int bmpWidth  = bitmap.getWidth();
		int bmpHeight = bitmap.getHeight();
		int[] pixels = new int[bmpWidth * bmpHeight];
		bitmap.getPixels(pixels, 0, bmpWidth, 0, 0, bmpWidth, bmpHeight);
		for (int i=0;i<pixels.length;i+=1) {
		    int argb = pixels[i];
		    pixels[i] = argb&0xff00ff00 | ((argb&0xff)<<16) | ((argb>>16)&0xff);
		}
		IntBuffer ib = IntBuffer.wrap(pixels);
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bmpWidth, bmpHeight, 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);
		ib.clear();
		

    	//テクスチャを元のサイズから拡大、縮小して使用したときの色の使い方を設定
		//gl.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR );	//<	重ければニアレストネイバーに変更
		//gl.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR );	//<	重ければニアレストネイバーに変更
		gl.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST );	//<	軽い
		gl.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST );	//<	軽い
		
    	//テクスチャ座標が1.0fを超えたときの、テクスチャを繰り返す設定
    	gl.glTexParameterf(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT );
    	gl.glTexParameterf(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT );
    	//	繰り返し方法を設定
		//gl.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP_TO_EDGE );
		//gl.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP_TO_EDGE );
		
		
		return texNo;
	}
	
	/**
	 * テクスチャを削除
	 * @param gl GLインタフェース
	 * @param texNo テクスチャオブジェクト
	 */
	public static void deleteTexture(GL11 gl, int texNo) {
		int[] textures = new int[1];
		textures[0] = texNo;
		gl.glDeleteTextures(1, textures, 0);
		//Log.v("deleteTexture",""+texNo+" "+gl);
	}

	
	public static void drawTexture(GL11 gl, int texNo, float x, float y, float width, float height) {
		GL11Until.drawTexture(gl, texNo, x, y, width, height, 0, (int)height, (int)width, (int)-height, 1f, 1f, 1f, 1f);
	}
	public static void drawTextureColor(GL11 gl, int texNo, float x, float y, float width, float height, float colR, float colG, float colB, float colA) {
		GL11Until.drawTexture(gl, texNo, x, y, width, height, 0, (int)height, (int)width, (int)-height, colR, colG, colB, colA);
	}
	public static void drawTexture(GL11 gl, int texNo, float x, float y, float width, float height, int texX, int texY, int texWidht, int texHeight) {
		GL11Until.drawTexture(gl, texNo, x, y, width, height, texX, texY, texWidht, texHeight, 1f, 1f, 1f, 1f);
	}
	public static void drawTexture(GL11 gl, int texNo, float x, float y, float width, float height, int texX, int texY, int texWidht, int texHeight, float colR, float colG, float colB, float colA) {
		gl.glBindTexture(GL11.GL_TEXTURE_2D, texNo);
   		int rect[] = { texX,  texY,  texWidht, texHeight};
   		((GL11) gl).glTexParameteriv(GL11.GL_TEXTURE_2D,GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);

        gl.glEnable( GL10.GL_TEXTURE_2D );
		gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL11.GL_BLEND);
		gl.glColor4f(colR, colG, colB, colA);
   		
   		//描画
   		((GL11Ext) gl).glDrawTexfOES(x, y, 0, width, height);
	}
	
}
