package tonyu.android;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.view.MotionEvent;

/** ゲーム上の画面から端末画面に映像を映すクラス */
public class TonyuViewDisplay {
	/**
	 * screenはゲーム上の仮想的な画面
	 * displayは端末上の実際の画面を表すこととする
	 */
	public static final int DIRECT_DRAW  = 0;
	public static final int VIRTUAL_DRAW = 1;
	
	
    // ゲーム上の画面サイズ
    private int screenWidth  = 800;
    private int screenHeight = 480;
    
    // 端末の画面サイズ
    private int displayWidth  = 800;
    private int displayHeight = 480;
    private int resizeWidth, resizeHeight;
    private int marginWidth = 0, marginHeight = 0;
    private float widthR = 0, heightR = 0; // 画面拡大倍率
    private int resizeSW = 2;
    //private float displayR = 1.0f;
    
    // タップの情報
    private float padDisplayX = 0; // ディスプレイ解像度の絶対座標X
    private float padDisplayY = 0; // ディスプレイ解像度の絶対座標Y
    private float padX = 0;
    private float padY = 0;
    private int padPush = 0;
    private float padPushX = 0;
    private float padPushY = 0;
    
    // 画面関係
    private Rect displayRectSrc, displayRectDst;
    private Paint textPaint, virtualDisplayPaint;

    private Bitmap virtualBitmap;
    private Canvas virtualCanvas;
    //private BitmapDrawable bitmapDrawable;
    private int bgColor;
    private int marginColor;
    private int drawMode = VIRTUAL_DRAW;
    
    private Canvas displayCanvas; // 端末画面のキャンバスを受け取る
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // コンストラクタ
    TonyuViewDisplay(int width, int height, int bgColor) {
    	this.screenWidth  = width;
    	this.screenHeight = height;
		
    	// 仮想画面用のBitmapを生成
    	virtualBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);

        // 仮想画面用キャンバス
    	virtualCanvas = new Canvas(virtualBitmap);
    	virtualCanvas.drawColor(Color.BLACK);
		
    	// 描画設定
        virtualDisplayPaint = new Paint();
        virtualDisplayPaint.setColor(bgColor);
        this.bgColor = bgColor;
    	virtualDisplayPaint.setFilterBitmap(false);
    	marginColor = Color.BLACK;
        
    	// 描画設定
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 
    public boolean getDisplayAntiAlias() { return virtualDisplayPaint.isFilterBitmap(); }
    public void setDisplayAntiAlias(boolean a) { virtualDisplayPaint.setFilterBitmap(a); }
    
    public int getScreenWidth() { return this.screenWidth; }
    public int getScreenHeight() { return this.screenHeight; }
    public int getDisplayWidth() { return this.displayWidth; }
    public int getDisplayHeight() { return this.displayHeight; }
    public int getMarginWidth() { return this.marginWidth; }
    public int getMarginHeight() { return this.marginHeight; }
    public float getWidthR() { return this.widthR; }
    public float getHeightR() { return this.heightR; }
    public void resizeScreen(int width, int height, boolean padXYUpdate) { // ゲーム画面サイズ変更
    	if (this.screenWidth == width && this.screenHeight == height) return;
		if (width  < 2) width  = 2;
		if (height < 2) height = 2;
    	this.screenWidth  = width;
    	this.screenHeight = height;
    	setResize(padXYUpdate);
    }
    public void resizeDisplay(int width, int height) { // 端末画面サイズ変更
    	if (this.displayWidth == width && this.displayHeight == height) return;
    	this.displayWidth  = width;
    	this.displayHeight = height;
    	setResize(false);
    }
    public int getBGColor() { return bgColor; }                    // 背景色変更
    public int getMarginColor() { return marginColor; }            // 余白色変更
    public void setBGColor(int color) { bgColor = color; }         // 背景色変更
    public void setMarginColor(int color) { marginColor = color; } // 余白色変更
    
    // サイズ変更時の設定
    public void setResize(boolean padXYUpdate) {
    	
	    // 元画像から切り出す位置を指定
        displayRectSrc = new Rect(0, 0, screenWidth, screenHeight);

		// 実際の解像度とゲーム解像度のギャップを拡大縮小で合わせる（アスペクト比）
		int temp = screenHeight * displayWidth / screenWidth;
		if (temp <= displayHeight) { // 上下に余白ができる
			resizeWidth = displayWidth;
			resizeHeight = temp;
			//displayR = (float) displayWidth / virtualDisplayWidth;
		} else { // 左右に余白ができる
			resizeWidth = screenWidth * displayHeight / screenHeight;
			resizeHeight = displayHeight;
			//displayR = (float) displayHeight / virtualDisplayHeight;
		}
		marginWidth = displayWidth - resizeWidth; // 上下の余白の幅
		marginHeight = displayHeight - resizeHeight; // 左右の余白の幅
		
		widthR  = (float)resizeWidth  / screenWidth;
		heightR = (float)resizeHeight / screenHeight;
        displayRectDst = new Rect(marginWidth / 2, marginHeight / 2, resizeWidth + marginWidth / 2, resizeHeight + marginHeight / 2);

        textPaint.setTextSize(24 * displayWidth / 1280);
        
        if (padXYUpdate) updatePadXY();
        
        if (virtualBitmap != null) {
        	virtualBitmap.recycle();
        	virtualBitmap = null;
        }
        // 仮想画面を作り直す //
        if (drawMode == VIRTUAL_DRAW) {
	    	virtualBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Config.ARGB_8888);
	    	virtualCanvas = new Canvas(virtualBitmap);
        }
    	resizeSW = 4; // リサイズしたことを知らせる(ダブルバッファなので２回以上クリアしなければならない)
    }
    
    
    // 描画モードを変える
    public void setDrawMode(int drawMode) {
    	this.drawMode = drawMode;
    }
    
    
    // TonyuBootに描画すべき描画キャンバスを渡す
    public Canvas getDrawCanvas() {
    	switch (drawMode) {
    	case 0: // 端末画面
    		return displayCanvas;
    	case 1: // 仮想画面
    		return virtualCanvas;
		default:
    		return virtualCanvas;
    	}
    }
    
    // 端末画面のキャンバスを受け取る
    public void setDisplayCanvas(Canvas displayCanvas) {
    	this.displayCanvas = displayCanvas;
    }
    
    // 画面クリア
    public void drawClear() {
    	switch (drawMode) {
    	case 0: // 端末画面クリア
    		displayCanvas.drawColor(bgColor);
    		break;
    	case 1: // 仮想画面クリア
    		if (resizeSW > 0) {
        		displayCanvas.drawColor(marginColor);
        		resizeSW --;
    		}
    		virtualCanvas.drawColor(bgColor);
    		break;
		default:
			break;
    	}
    }
    
    // 端末画面に描画(FPS非表示)
    public void draw() {
    	draw(-1, -1);
    }
    // 端末画面に描画
    public void draw(int fps, int rps) {
    	
        //端末画面に指定された方式で描画
    	switch (drawMode) {
    	case 0: // 端末画面描画
        	//　FPS表示
        	if (fps >= 0) {
                textPaint.setColor(Color.rgb(255-Color.red(bgColor), 255-Color.green(bgColor), 255-Color.blue(bgColor)));
                displayCanvas.drawText("fps = " + fps + " rps = " + rps, 0, textPaint.getTextSize(), textPaint);
        	}
        	break;
    	case 1: // 仮想画面描画
        	displayCanvas.drawBitmap(virtualBitmap, displayRectSrc, displayRectDst, virtualDisplayPaint);
        	//　FPS表示
        	if (fps >= 0) {
                textPaint.setColor(Color.argb(192, 255-Color.red(bgColor), 255-Color.green(bgColor), 255-Color.blue(bgColor)));
                displayCanvas.drawText("fps = " + fps + " rps = " + rps, marginWidth / 2, marginHeight / 2 + textPaint.getTextSize(), textPaint);
        	}
        	break;
		default:
			break;
        }
    	
    }
    
    // タップ情報
    public boolean onTouchEvent(MotionEvent me) {
    	padDisplayX = me.getX();
    	padDisplayY = me.getY();
    	//if (   x < yohakuWidth /2 || resizeWidth  + yohakuWidth /2 < x
    	//	|| y < yohakuHeight/2 || resizeHeight + yohakuHeight/2 < y) {
    	//	return true;
    	//}
    	
    	updatePadXY();
    	if (me.getAction() == MotionEvent.ACTION_DOWN) {
    		padPush = 1;
        	padPushX = padX;
        	padPushY = padY;
    	} else if (me.getAction() == MotionEvent.ACTION_UP) {
    		padPush = 0;
    	}
        return true;
    }
    private void updatePadXY() {
    	padX = (int) ((padDisplayX - marginWidth  / 2) * ((float) screenWidth  / resizeWidth ));
    	padY = (int) ((padDisplayY - marginHeight / 2) * ((float) screenHeight / resizeHeight));
    }
    public void setPadXY(float x, float y) {
    	padX = x;
    	padY = y;
    }
    public float getPadDisplayX() { return padDisplayX; }
    public float getPadDisplayY() { return padDisplayY; }
    public float getPadX() { return padX; }
    public float getPadY() { return padY; }
    public int getPadPush() { return padPush; }
    public float getPadPushX() { return padPushX; }
    public float getPadPushY() { return padPushY; }
    
}
