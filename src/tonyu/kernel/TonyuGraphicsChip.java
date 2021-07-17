package tonyu.kernel;

import tonyu.android.GL11Until;

import android.graphics.Bitmap;
import android.graphics.Rect;

// 画像ファイルの１枚分のチップ
public class TonyuGraphicsChip {
	//int haveBitmap; // ビットマップは自分が持っているか？
	Bitmap bitmap = null;
	Rect rect;
	int width, height;
	float halfWidth, halfHeight;
	
	private boolean tex = false;
	int texNo;
	int texX;
	int texY;
	int texWidth;
	int texHeight;
	int texBitmapWidth;
	int texBitmapHeight;
	float texBitmapWidthR;
	float texBitmapHeightR;
	int[] texRect = new int[4];
	int[] texRectFlip = new int[4];
	
	//public TonyuGraphicsChip(Bitmap bitmap, int l, int t, int r, int b) {
	//	this.bitmap = bitmap;
	//	width  = r - l;
	//	height = b - t;
	//	halfWidth  = width  / 2;
	//	halfHeight = height / 2;
	//	rect = new Rect(l, t, r, b);
	//}

	public TonyuGraphicsChip(Bitmap bitmap) {
		setBitmap(bitmap);
	}
	
	public void setBitmap(Bitmap bitmap) {
		if (this.bitmap != null) {
			this.bitmap.recycle();
		}
		this.bitmap = bitmap;
		if (TGL.drawMode == TonyuBoot.DRAW_OPENGL11) {
			createTextureGL11();
		} else if (TGL.drawMode == TonyuBoot.DRAW_SURFACEVIEW) {
			width  = bitmap.getWidth();
			height = bitmap.getHeight();
		}
		halfWidth  = (float)width  / 2;
		halfHeight = (float)height / 2;
		rect = new Rect(0, 0, this.width, this.height);
	}
	
	public void createTextureGL11() {
		if (tex) {
			GL11Until.deleteTexture(TGL.gl11, texNo);
			tex = false;
		}
		Bitmap tmpBitmap = GL11Until.resizeBitmap(bitmap); // サイズがOpenGLで扱える場合、bitmapがそのまま返ってくるので注意
		texNo = GL11Until.createTexture(TGL.gl11, tmpBitmap);
		tex = true;
		texX      = 0;
		texY      = bitmap.getHeight();
		texWidth  = bitmap.getWidth();
	   	texHeight = -bitmap.getHeight();
	 	width     = bitmap.getWidth();
	 	height    = bitmap.getHeight();
	 	texBitmapWidth   = tmpBitmap.getWidth();
	 	texBitmapHeight  = tmpBitmap.getHeight();
	 	texBitmapWidthR  = (float)width  / texBitmapWidth;
	 	texBitmapHeightR = (float)height / texBitmapHeight;
   		texRect[0] = texX;
   		texRect[1] = texY;
   		texRect[2] = texWidth;
   		texRect[3] = texHeight;
   		texRectFlip[0] = texX-texWidth;
   		texRectFlip[1] = texY;
   		texRectFlip[2] = -texWidth;
   		texRectFlip[3] = texHeight;
		if (tmpBitmap != bitmap) tmpBitmap.recycle(); // 新たなbitmapが作られていたら削除
	}
	
	void release() {
		if (tex) {
			GL11Until.deleteTexture(TGL.gl11, texNo);
			tex = false;
		}
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		rect = null;
	}
	

	
	
	public Rect getRect() {
		return rect;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public float getHalfWidth() {
		return halfWidth;
	}
	public float getHalfHeight() {
		return halfHeight;
	}
	
}