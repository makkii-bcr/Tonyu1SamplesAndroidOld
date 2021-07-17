package tonyu.android;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import tonyu.kernel.TonyuBoot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.opengl.GLES10;
import android.opengl.GLES11;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.SurfaceHolder;
// てすと０２
public class TonyuGLSurfaceViewTest02 extends GLSurfaceView implements Runnable, GLSurfaceView.Renderer, TonyuView {
	private SurfaceHolder holder;
	private Thread thread;
	public TonyuGLSurfaceViewTest02(Context context) {
		super(context);

		//setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		//setEGLConfigChooser(5, 6, 5, 0, 0, 1);
        setRenderer(this);
		
		setRenderMode(RENDERMODE_WHEN_DIRTY);
		//setRenderMode(RENDERMODE_CONTINUOUSLY);
		
	}

	@Override
	public void run() {
		int cnt = 0;
		
		while (thread != null) {
			//Log.v("run",""+cnt++);
			requestRender(); // ゲーム実行・描画
			sleep(16);
		}
	}
	
	
	private void sleep(long time) {
		try{
			Thread.sleep(time);
		}catch(Exception e){}
	}

	private int cnt = 0;
	private int fpsCnt = 0;
	private int fps = 0;
	private long startTime = 0;
	private float xx = 0;
	private int yySW = 0;

	private int displayWidth  = 800;
	private int displayHeight = 480;
	private int screenWidth  = 640;
	private int screenHeight = 480;
	
	@Override
	public void onDrawFrame(GL10 gl) {
        // 描画内容をクリアする
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

   	   	//gl.glEnable(GL10.GL_DEPTH_TEST);
    	//背景色を白色で塗りつぶし
   		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
   		//テクスチャ0番をアクティブにする
   		gl.glActiveTexture(GL10.GL_TEXTURE0);
   		for (int j=0; j<1; j++) {
   	   		for (int i=0; i<1; i++) {
		   		//テクスチャIDに対応するテクスチャをバインドする
		   		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureNo);
				//テクスチャの座標と幅と高さを指定
		   		int rect[] = { texX,  texY,  texWidth, texHeight};
		   		//テクスチャ画像のどの部分を使うかを指定
		   		((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);
		   		//描画
		   		((GL11Ext) gl).glDrawTexfOES( pos_x+xx+i*width, pos_y+j*height, pos_z+(float)Math.random(), width, height);
   	   		}
   		}
        int cw = 32, ch = 32;
        int xs = 20, ys = 20;
        xx += 8;
        if (xx + (cw+1)*xs >= displayWidth) xx = -1300*40;
        
		cnt ++;
		if (System.currentTimeMillis() - startTime >= 1000) {
			fps = fpsCnt;
	        Log.v("fps : ", Integer.toString(fpsCnt));
			fpsCnt = 0;
			startTime = System.currentTimeMillis();
		} else {
			fpsCnt ++;
		}
		
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		displayWidth  = width;
		displayHeight = height;
        // 描画範囲を指定する
		
        gl.glViewport(0, 0, displayWidth, displayHeight);
        gl.glMatrixMode(GL10.GL_PROJECTION);
	    gl.glLoadIdentity();
        GLU.gluOrtho2D(gl, 0, displayWidth, displayHeight, 0);
        //gl.glOrthof(0, displayWidth, displayHeight, 0, 100, -100);
		
        

	    // ビューポートの設定
	    //gl.glViewport(0, 0, width, height);
	    // プロジェクションモードに設定
	    //gl.glMatrixMode(GL10.GL_PROJECTION);
	    // スクリーン座標を初期化
	    //gl.glLoadIdentity();
	    // 2Dの投影
        //GLU.gluOrtho2D(gl, 0, screenWidth, screenHeight, 0);
	    //GLU.gluOrtho2D( gl, -1.0f, 1.0f, 1.0f, -1.0f );
	    // 頂点の配列の利用を有効にする
	    //gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	    // 色の配列の利用を有効にする
	    //gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
	    
	}

	private Bitmap bitmap;
	private Canvas canvas;
	private Paint paint;
	
	public int textureNo;
    //表示位置
	float  pos_x;
	float  pos_y;
	float  pos_z;
	//テクスチャ（画像）の位置とサイズ
	int    texX;
	int    texY;
	int    texWidth;
	int    texHeight;
	//配置する時の幅と高さ
	float  width;
	float  height;

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO 自動生成されたメソッド・スタブ

		
		//背景色をクリア
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		//ディザを無効化
		gl.glDisable(GL10.GL_DITHER);
		//深度テストを有効化
		//gl.glEnable(GL10.GL_DEPTH_TEST);
		//テクスチャ機能ON
		gl.glEnable(GL10.GL_TEXTURE_2D);
		//透明可能に
		gl.glEnable(GL10.GL_ALPHA_TEST);
		//ブレンド可能に
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		bitmap = Bitmap.createBitmap(64, 16, Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		canvas.drawColor(Color.BLACK);
		paint = new Paint();
		paint.setTextSize(10);
		paint.setColor(Color.GREEN);
		canvas.drawText("写ってるかー", 0, 10, paint);

		
		gl.glEnable(GL10.GL_ALPHA_TEST);
    	gl.glEnable(GL10.GL_BLEND);
    	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    	gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
    	//テクスチャIDを割り当てる
    	int[] textureID = new int[1];
    	gl.glGenTextures(1, textureID, 0);
    	textureNo = textureID[0];

    	//テクスチャIDのバインド
    	gl.glBindTexture(GL10.GL_TEXTURE_2D, textureNo);
     	//OpenGL ES用のメモリ領域に画像データを渡す。上でバインドされたテクスチャIDと結び付けられる。
    	GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
    	
    	//テクスチャ座標が1.0fを超えたときの、テクスチャを繰り返す設定
    	gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT );
    	gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT );
    	//	繰り返し方法を設定
		//gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE );
		//gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE );

    	//テクスチャを元のサイズから拡大、縮小して使用したときの色の使い方を設定
		//gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );	//<	重ければニアレストネイバーに変更
		//gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );	//<	重ければニアレストネイバーに変更
		gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST );	//<	軽い
		gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST );	//<	軽い

    	texX      = 0;
    	texY      = bitmap.getHeight();
    	texWidth  = bitmap.getWidth()*40*40;
       	texHeight = -bitmap.getHeight()*44;
     	pos_x     = 0;
     	pos_y     = 0;
     	pos_z     = 0;
     	width     = bitmap.getWidth()*40*40;
     	height    = bitmap.getHeight()*44;
     	
     	bitmap.recycle();
	}

	@Override
	public void onActivityPause() {
		// TODO 自動生成されたメソッド・スタブ
		thread = null;
	}

	@Override
	public void onActivityStop() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void onActivityResume() {
		// TODO 自動生成されたメソッド・スタブ

		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void onActivityDestroy(int exitSW) {
		// TODO 自動生成されたメソッド・スタブ
		thread = null;
	}

	@Override
	public int getActivityState() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public TonyuViewDisplay getScreen() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public TonyuBoot getTonyuBoot() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}


}
