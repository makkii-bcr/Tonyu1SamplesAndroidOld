package tonyu.kernel;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

// ゲームのマップ
public class TonyuMap extends TonyuSprite {
	private Paint paint;
	private float sx = 0, sy = 0;
	private int zOrder = 200;
	private int width = 16, height = 12;   // マップの縦横のパターン数
	private int pWidth = 32, pHeight = 32; // １パターンの大きさ
	private int[] mapData;
	private int visible = 0;
	private float[] tmpAry;
	private int tmpArySize = 1024;
	
	public TonyuMap() {
		paint = new Paint();

		tmpAry = new float[tmpArySize];
		
	}
	
	public void init() {
		sx = 0;
		sy = 0;
		TGL.viewX = 0;
		TGL.viewY = 0;
	}
	
	/**
	 *  リソースファイルからマップデータを読み込む
	 * @param resId リソースID
	 * @return マップのチップ数(横方向のパターン数×縦方向のパターン数)<br>
	 * -1:ファイルが存在しない<br>
	 * -2:ファイルのデータ形式が不正<br>
	 * -3:ファイルのバージョンが違う<br>
	 */
	public int loadMapDataRes(int resId) {
		TonyuFileReader file = new TonyuFileReader();
		byte[] data = file.readBinaryRes(resId);
		if (data == null) return -1;
		if (data.length < 4) return -2;

		int mapDataVer  = data[0]*256 + data[1];
		int mapWHMode   = data[2];
		int mapChipMode = data[3];
		if (mapDataVer != 0x00000000) return -3; // ver0.0か？
		
		
		if (data.length < 16) return -2;
		int width   = data[4]*16777216 + data[5]*65536 + data[ 6]*256 + data[ 7];
		int height  = data[8]*16777216 + data[9]*65536 + data[10]*256 + data[11];
		int pWidth  = data[12]*256 + data[13];
		int pHeight = data[14]*256 + data[15];
		
		// 新しいマップを作る
		int[] mapData = new int[width * height];
		
		int p;
		int skip, po = 0, po2 = 0;
		if (mapWHMode == 0) { // 横モード
			if (mapChipMode == 0) { // 256モード
				for(int i=16; i+1<data.length; i+=2) {
					p = usByte(data[i]);
					skip = usByte(data[i+1]);
					for (int j=skip; j>=0; j--) {
						if (po >= mapData.length) return -2;
						mapData[po++] = p;
					}
				}
			} else if (mapChipMode == 1) { // 65536モード
				for(int i=16; i+2<data.length; i+=3) {
					p = usByte(data[i])*256 + usByte(data[i+1]);
					skip = usByte(data[i+2]);
					for (int j=skip; j>=0; j--) {
						if (po >= mapData.length) return -2;
						mapData[po++] = p;
					}
				}
			}
		} else if (mapWHMode == 1) { // 縦モード
			for(int i=16; i+1<data.length; i+=2) {
				if (mapChipMode == 0) { // 256モード
					p = usByte(data[i]);
					skip = usByte(data[i+1]);
					for (int j=skip; j>=0; j--) {
						if (po2+po*width >= mapData.length) return -2;
						po++;
						if (po >= width) {
							po=0;
							po2++;
						}
						mapData[po2+po*width] = p;
					}
				} else if (mapChipMode == 0) { // 65536モード
					p = usByte(data[i])*256 + usByte(data[i+1]);
					skip = usByte(data[i+2]);
					for (int j=skip; j>=0; j--) {
						if (po2+po*width >= mapData.length) return -2;
						po++;
						if (po >= width) {
							po=0;
							po2++;
						}
						mapData[po2+po*width] = p;
					}
				}
			}
		} else {
			return -2;
		}
		
		this.width   = width;
		this.height  = height;
		this.pWidth  = pWidth;
		this.pHeight = pHeight;
		this.mapData = mapData;
		
		Log.v("mapDataReadSize", ""+data.length);
		Log.v("mapDataSize", ""+mapData.length);
		return mapData.length;
	}

	/** 横方向のパターン数を取得 */
	public int getWidth() { return width; }
	/** 縦方向のパターン数を取得 */
	public int getHeight() { return height; }
	/** １パターンの幅を取得 */
	public int getPWidth() { return pWidth; }
	/** １パターンの高さを取得 */
	public int getPHeight() { return pHeight; }
	
	
	/** 背景色を取得 */
	public int getBGColor() { return TGL.tonyuBoot.getBGColor();}
	/** 画面比率によってできる余白の色を取得 */
	public int getMarginColor() { return TGL.tonyuBoot.getMarginColor(); }
	/** 背景色を変更 */
	public void setBGColor(int color) { TGL.tonyuBoot.setBGColor(color);}
	/** 画面比率によってできる余白の色を変更 */
	public void setMarginColor(int color) { TGL.tonyuBoot.setMarginColor(color); }
	/** マップのzOrderを設定 */
	public void setZOrder(int zOrder) { this.zOrder = zOrder; }
	/** マップの表示／非表示 */
	public void setVisible(int v) { visible = v; }

	/** スクロール */
	public void scrollTo(float sx, float sy) {
		this.sx = sx;
		this.sy = sy;
		TGL.viewX = sx;
		TGL.viewY = sy;
	}
	
	/**
	 * マップパターン上の座標からマップパターンを取得
	 * @param x マップパターン上のX座標
	 * @param y マップパターン上のY座標
	 * @return キャラクタパターン番号
	 **/
	public int get(int x, int y) {
		if (mapData == null) return -1;
		int xx = amod(x, width);
		int yy = amod(y, height);
		return mapData[xx + yy * width];
	}
	
	/**
	 * 画面上の座標からマップパターンを取得
	 * @param x 画面の座標上のX座標
	 * @param y 画面の座標上のY座標
	 * @return キャラクタパターン番号
	 **/
	public int getAt(int x, int y) {
		if (mapData == null) return -1;
		int xx = amod((int)Math.floor(x/pWidth) , width );
		int yy = amod((int)Math.floor(y/pHeight), height);
		return mapData[xx + yy * width];
	}
	/**
	 * 画面上の座標からマップパターンを取得
	 * @param x 画面の座標上のX座標
	 * @param y 画面の座標上のY座標
	 * @return キャラクタパターン番号
	 **/
	public int getAt(float x, float y) {
		if (mapData == null) return -1;
		int xx = amod((int)Math.floor(x/pWidth) , width );
		int yy = amod((int)Math.floor(y/pHeight), height);
		return mapData[xx + yy * width];
	}
	
	/**
	 * マップパターン上の座標からマップパターンを設定
	 * @param x マップパターン上のX座標
	 * @param y マップパターン上のY座標
	 **/
	public void set(int x, int y, int p) {
		if (mapData == null) return;
		int xx = amod(x, width);
		int yy = amod(y, height);
		mapData[xx + yy * width] = p;
	}
	
	/**
	 * 画面上の座標からマップパターンを設定
	 * @param x 画面の座標上のX座標
	 * @param y 画面の座標上のY座標
	 **/
	public void setAt(int x, int y, int p) {
		if (mapData == null) return;
		int xx = amod((int)Math.floor(x/pWidth) , width );
		int yy = amod((int)Math.floor(y/pHeight), height);
		mapData[xx + yy * width] = p;
	}

	
	
	
	
	public void draw() {
		if (visible != 0) drawMethod(this, zOrder); // 描画 (TonyuBootから直接呼び出される)
	}
	
	// 描画処理
	@Override
	public void directDrawCanvas(Canvas canvas) {
		if (mapData == null) return;
		TonyuGraphicsChip gChip;
		int x, y, x2, y2, w, h, px, py;
		x = (int) Math.floor(this.sx);
		y = (int) Math.floor(this.sy);
		x2 = (int) Math.floor(this.sx/pWidth);
		y2 = (int) Math.floor(this.sy/pHeight);
		w = TGL.screenWidth  / pWidth +1;
		h = TGL.screenHeight / pHeight+1;
		for (int i=0; i<=h; i++) {
			py = amod((i + y2) , height) * width;
			for (int j=0; j<=w; j++) {
				px = amod((j + x2) , width);
				gChip = TGL.grManager.getGraphicsChip(mapData[px+py]);
				if (gChip == null) continue;
				canvas.drawBitmap(gChip.bitmap, j*pWidth-amod(x, pWidth), i*pHeight-amod(y, pHeight), paint);
			}
		}
		//canvas.drawBitmap(bitmap, x, y, paint);
	}
/*
	// 描画処理(Opengl 軽量化なし)
	@Override
	public void directDrawGL11(GL10 gl) {
		if (mapData == null) return;
		TonyuGraphicsChip gChip;
		int x, y, x2, y2, w, h, px, py;
		x = (int) Math.floor(this.sx);
		y = (int) Math.floor(this.sy);
		x2 = (int) Math.floor(this.sx/pWidth);
		y2 = (int) Math.floor(this.sy/pHeight);
		w = TGL.screenWidth  / pWidth +1;
		h = TGL.screenHeight / pHeight+1;
		for (int i=0; i<=h; i++) {
			py = amod((i + y2) , height) * width;
			for (int j=0; j<=w; j++) {
				px = amod((j + x2) , width);
				gChip = TGL.grManager.getGraphicsChip(mapData[px+py]);
				if (gChip == null) continue;
				//canvas.drawBitmap(gChip.bitmap, j*pWidth-amod(x, pWidth), i*pHeight-amod(y, pHeight), paint);

				//テクスチャIDに対応するテクスチャをバインドする
		   		gl.glBindTexture(GL11.GL_TEXTURE_2D, gChip.texNo);
				//テクスチャの座標と幅と高さを指定
		   		int rect[] = { gChip.texX,  gChip.texY, gChip.texWidth, gChip.texHeight};
		   		//テクスチャ画像のどの部分を使うかを指定
		   		((GL11) gl).glTexParameteriv(GL11.GL_TEXTURE_2D,GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);

				gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
				gl.glEnable(GL10.GL_BLEND);
				gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		   		
		   		//描画
		   		((GL11Ext) gl).glDrawTexfOES( TGL.marginHalfWidth + (j*pWidth-amod(x, pWidth)) * TGL.widthR,
		                                      TGL.marginHalfHeight + TGL.displayHeight - (i*pHeight-amod(y, pHeight)+pHeight) * TGL.heightR,
		   				                      0,
		   				                      gChip.width  * TGL.widthR,
		   				                      gChip.height * TGL.heightR
		   				                      );
				
			}
		}
		//canvas.drawBitmap(bitmap, x, y, paint);
	}
*/
	// 描画処理(Opengl 軽量化あり)
	@Override
	public void directDrawGL11(GL11 gl) {
		if (mapData == null) return;
		TonyuGraphicsChip gChip, gChip2 = null;
		int tmpAryIdx = 0, tmpInt, tmpInt2 = -99999999, tmpCnt = 0;
		int sum = 0, sum2 = 0;
		float x, y, x2, y2;
		int w, h, px, py;
		x = (float) this.sx;
		y = (float) this.sy;
		x2 = (float) this.sx/pWidth;
		y2 = (float) this.sy/pHeight;
		w = TGL.screenWidth  / pWidth +1;
		h = TGL.screenHeight / pHeight+1;
		
		tmpAryIdx = 0;
		for (int i=0; i<=h; i++) {
			int j;
			py = amod((int)(i + Math.floor(y2)) , height) * width;
			gChip2 = null;
			for (j=0; j<=w; j++) {
				px = amod((int)(j + Math.floor(x2)) , width);
				tmpInt = mapData[px+py];
				//sum++;
				if (tmpInt != tmpInt2) {
					if (tmpInt2 != -99999999) {
						if (tmpAryIdx+4 > tmpArySize) resizeTmpAry(); // 配列サイズが足りなかったら増量
						tmpAry[tmpAryIdx++] = tmpInt2;
						tmpAry[tmpAryIdx++] = tmpCnt;
						tmpAry[tmpAryIdx++] = (j-tmpCnt)*pWidth-amod(x, pWidth);
						tmpAry[tmpAryIdx++] = i*pHeight-amod(y, pHeight)+pHeight;
					}
					tmpInt2 = tmpInt;
					tmpCnt = 0;
				}
				tmpCnt ++;
			}
			if (tmpAryIdx+4 > tmpArySize) resizeTmpAry(); // 配列サイズが足りなかったら増量
			tmpAry[tmpAryIdx++] = tmpInt2;
			tmpAry[tmpAryIdx++] = tmpCnt;
			tmpAry[tmpAryIdx++] = (j-tmpCnt)*pWidth-amod(x, pWidth);
			tmpAry[tmpAryIdx++] = i*pHeight-amod(y, pHeight)+pHeight;
			tmpInt2 = -99999999;
			tmpCnt  = 0;
		}
		for (int i=0; i+3<=tmpAryIdx; i+=4) {
			//sum2++;
			gChip = TGL.grManager.getGraphicsChip((int)tmpAry[i]);
			if (gChip == null) continue;
			//canvas.drawBitmap(gChip.bitmap, j*pWidth-amod(x, pWidth), i*pHeight-amod(y, pHeight), paint);
			
			//テクスチャIDに対応するテクスチャをバインドする
	   		gl.glBindTexture(GL11.GL_TEXTURE_2D, gChip.texNo);
			//テクスチャの座標と幅と高さを指定
	   		int rect[] = { gChip.texX,  gChip.texY, gChip.texWidth*(int)tmpAry[i+1], gChip.texHeight};
	   		//テクスチャ画像のどの部分を使うかを指定
	   		((GL11) gl).glTexParameteriv(GL11.GL_TEXTURE_2D,GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);

			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			gl.glEnable(GL10.GL_BLEND);
			gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	   		
	   		//描画
	   		((GL11Ext) gl).glDrawTexfOES( TGL.marginHalfWidth + (tmpAry[i+2]) * TGL.widthR,
	                                      TGL.marginHalfHeight + TGL.displayHeight - (tmpAry[i+3]) * TGL.heightR,
	   				                      0,
	   				                      gChip.width  * tmpAry[i+1] * TGL.widthR,
	   				                      gChip.height * TGL.heightR + 0.5f
	   				                      );
			
		}
		//Log.v("mapDraw",""+sum+" "+sum2);
		tmpAryIdx = 0;
	}
	
	private void resizeTmpAry() {
		tmpArySize += 1024;
		float[] newAry = new float[tmpArySize];
		System.arraycopy(tmpAry, 0, newAry, 0, tmpAry.length);
		tmpAry = newAry;
		Log.v("Map resizeTmpAry", ""+tmpArySize);
	}
	
}
