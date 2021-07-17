package tonyu.android;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import tonyu.kernel.TGL;
import tonyu.kernel.TonyuBoot;
import tonyu.kernel.TonyuPage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;


/** ゲーム実行、画面処理をまとめるクラス */
public class TonyuGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer, TonyuView {
	static public TonyuGLSurfaceView myObj; // 自分
	static private TonyuBoot sTonyuBoot;  // ゲームメインクラス
	private TonyuBoot mTonyuBoot;  // ゲームメインクラス(staticはきえるのでメンバ変数にも保存)
	static public TonyuGLSurfaceView create(Context context, TonyuPage startPage) {
		// ２回目にクリエイトされた場合、新しいViewを渡さないといけない
		TonyuGLSurfaceView newObj;

		Log.v("sTonyuBoot:", ""+sTonyuBoot);
		// ２回目の場合はtonyuBootの情報を書き換える
		if (sTonyuBoot == null || myObj == null) { // 初回
			newObj = new TonyuGLSurfaceView(context, startPage);
		} else { // ２回目以降
			//TonyuBoot tonyuBoot = myObj.getTonyuBoot();
			newObj = new TonyuGLSurfaceView(context, startPage, myObj.getTonyuBoot(), myObj.getScreen());
			sTonyuBoot.changeView(newObj, TonyuBoot.DRAW_OPENGL11);
		}
		myObj = newObj;
		sTonyuBoot = newObj.getTonyuBoot();
		
		return myObj;
	}
	
	
	
	public Context context;
	private int activityState = 0;
	
	// フレームレート関係 //
	// setFrameRate()で設定できる。
	private float fps = 60;               // １秒間のフレーム処理数
	private float frameTime = 1000 / fps; // １フレームあたりの時間（自動計算）
	private int   frameSkipNum = 5;       // 処理落ちしたときにいくつまでコマ落ちさせるか
	private int   frameHalf    = 1;       // 実行速度はそのままにして、描画回数を半減させる（1/3, 1/4, ... も可能）
    private int runFps = 0, runRps = 0;
    private int runFpsCount = 0, runRpsCount = 0;
    
    private Paint paint;
    
    // スレッド
    private Thread thread;
    
    // 仮想画面
    private TonyuViewDisplay display;
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// コンストラクタ (初回)
    private TonyuGLSurfaceView(Context context, TonyuPage startPage) {
        this(context, startPage, null, null);
    }
    // コンストラクタ(２回目以降)
    private TonyuGLSurfaceView(Context context, TonyuPage startPage, TonyuBoot tonyuBoot, TonyuViewDisplay display) {
        super(context);
        
        this.context = context;

        if (display == null) {
            // 仮想画面を生成
            //display = new TonyuSurfaceViewDisplay(560, 381, Color.argb(255, 20, 80, 180)); // Tonyu初期サイズ
            //display = new TonyuSurfaceViewDisplay(563, 386, Color.argb(255, 0, 0, 100)); // Ribonサイズ
            this.display = new TonyuViewDisplay(800, 480, Color.argb(255, 20, 80, 180)); // androidゲーム推奨サイズ
        } else {
            // Viewが置き換わっても、ゲームの処理は続行
            this.display = display;
        }
        
        if (tonyuBoot == null) {
        	// ゲームメインオブジェクト作成
        	mTonyuBoot = new TonyuBoot(this, startPage, TonyuBoot.DRAW_OPENGL11);
        	//mTonyuBoot.init(); // 初期化
        } else {
	        // ゲームメインオブジェクトを持ってくる
        	mTonyuBoot = tonyuBoot;
        }
        
        if (paint == null) paint = new Paint();
        
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		//setEGLConfigChooser(5, 6, 5, 0, 0, 1);
        setRenderer(this);
		//setRenderMode(RENDERMODE_WHEN_DIRTY);
		//setRenderMode(RENDERMODE_CONTINUOUSLY);

		/*
		sleep(1000);
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
		*/
        
        
        // マルチスレッドを使う場合
        // メインスレッドで、OpenGLの命令を使いたい場合queueEvent()を使うといいらしい
        /*
        @Override
        public void onLongPress(MotionEvent arg0) {
            queueEvent(new Runnable(){
                public void run(){
                    glRememberField.glEnable(GL10.GL_LIGHT);
                }
            });
        }
        */
        
        
		
    	Log.v("SurfaceView", "コンストラクタ2: "+tonyuBoot+" "+sTonyuBoot);
    }
    
    // ゲームメインオブジェクトを取得
    public TonyuBoot getTonyuBoot() {
    	return mTonyuBoot;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // コールバック
    
	private int cnt = 0;
	private long startTime = 0;

	private int displayWidth  = 800;
	private int displayHeight = 480;
	
	private int fpsTexNo = -1;
	private int fpsTexWidth, fpsTexHeight, rpsTexWidth, timeTexWidth;

	int dwCnt = 0;
	int procNum, drawCharge = 0;
	float procNumF;
	boolean doDraw;
	boolean loadPageDraw = false;
	
	
	//long startTime = System.currentTimeMillis();
	long nowTime   = 0;

	long runTime = System.currentTimeMillis();
	float runTimeFloat = 0.0f;

	int testTexNo;
	float testL;
	float testR;
	float testT;
	float testB;
	
	float xx;
	
	@Override
	public void onDrawFrame(GL10 gl10) {
		GL11 gl = (GL11)gl10;

		// 実行関係 ///////////////////////////////////////////////////////////////////////////////////////////
		// 初期化 /////////////////////////////////////////////
		if (cnt == 0) {
			// 開始まで少し待機
			sleep(1000);
			
			dwCnt = 0;
			drawCharge = 0;
			
			loadPageDraw = false;
			
			startTime = System.currentTimeMillis();
			nowTime   = 0;

			runTime = System.currentTimeMillis();
			runTimeFloat = 0.0f;
			
			// フレームレート設定
			setFrameRate(60, 5, 1);
			
		}
		threadLook(); // 他のスレッドが動かないようにロック
		
		// フレーム速度計算
    	procNumF = (System.currentTimeMillis() - runTime) / frameTime;
		procNum = (int) procNumF; // 必要な処理回数を計算
		drawCharge +=  procNumF;
		if (procNum > frameSkipNum) {
			procNum = frameSkipNum; // スキップできるフレーム数を制限
			runTime = System.currentTimeMillis() - (long)((frameSkipNum) * frameTime);
			drawCharge = frameHalf;
		}
		
		// 始めのフレームは何が何でも実行する
		if (cnt == 0) {
			procNum = 1;
		}
		
        if (activityState == 1) { // Activityが実行状態
    		/////////////////////////////////////////////////////////////////////////////
    		
    		// 描画するか？
    		doDraw = false;
        	if (procNum > 0) { // 実行される場合は描画
    			//Log.v("draw", ""+drawCharge+" "+procNum);
        		if (frameHalf == 1) {
        			doDraw = true;
        		} else { // 描画半減: 2,3,4...フレームに1度だけ描画
        			if (drawCharge >= frameHalf) {
	        			doDraw = true;
        				drawCharge -= frameHalf;
        			}
        		}
        		dwCnt ++;
        	}
        	//
        	
    		// 実行処理
    		boolean doDraw2 = false;
    		for(int i=procNum; i>=1; i--) {
    			if (i == 1) doDraw2 = doDraw; // ２回以上実行する時、描画を最後のフレーム１回に抑える
    			loadPageDraw = mTonyuBoot.execute(doDraw2); // ゲームのメイン処理
        		if (loadPageDraw) { doDraw = doDraw2; break;} // 描画OFFの時に描画するとロード時に背景色が表示されてしまう
        	}//loadPageDraw=false;
        	
			/////////////////////////////////////////////////////////////////////////////
	    	
	    	// 処理回数計測
	        runRpsCount += procNum;
	    	
	    	// 実行時間計算
	        runTimeFloat += frameTime * procNum; // １フレームあたりの処理時間 × 処理回数
	        runTime += (long)runTimeFloat;       // 処理したフレーム分、時間を加算
	        runTimeFloat -= (int)runTimeFloat;   // 小数が切り捨てられないように工夫
			/////////////////////////////////////////////////////////////////////////////
	        
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    	
	    	// 描画関係 ///////////////////////////////////////////////////////////////////////////////////////////////////////
	        
	    	//背景色を白色で塗りつぶし
	    	mTonyuBoot.setGL(gl);
			int bgCol = display.getBGColor();
			float bgColR = (float)Color.red(bgCol)   / 255;
			float bgColG = (float)Color.green(bgCol) / 255;
			float bgColB = (float)Color.blue(bgCol)  / 255;
			float bgColA = (float)Color.alpha(bgCol) / 255;
			gl.glClearColor(bgColR, bgColG, bgColB, bgColA);
			// 描画内容をクリアする
	        //gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	        gl.glClear(GL11.GL_COLOR_BUFFER_BIT);
	
	   		//テクスチャ0番をアクティブにする
	   		gl.glActiveTexture(GL11.GL_TEXTURE0);
	   		
	        
   			//display.drawClear();    // 画面クリア
   			mTonyuBoot.draw(gl, doDraw);  // ゲーム描画処理を実行 (描画すべき描画キャンバスを渡す)
   			
	    	if (loadPageDraw) mTonyuBoot.loadDraw(display.getDrawCanvas());  // ページロード描画処理を実行 (描画すべき描画キャンバスを渡す)
	    	if (doDraw) runFpsCount ++;
	    	
	    	
   			
	    	// FPS計算
	    	nowTime = System.currentTimeMillis();
	    	if (nowTime - startTime >= 1000) {
	    		
	    		runFps = runFpsCount;
	    		runRps = runRpsCount;
	    		runFpsCount = 0;
	    		runRpsCount = 0;
	    		startTime = nowTime;
	    		
	    		if (fpsTexNo != -1) GL11Until.deleteTexture(gl, fpsTexNo);
	    		String fpsStr = "fps = "+ runFps;
	    		String rpsStr = "rps = "+ runRps;
	    		
	    		Calendar cal = Calendar.getInstance();
	    		int timeHour   = cal.get(Calendar.HOUR_OF_DAY);
	    		int timeMinute = cal.get(Calendar.MINUTE);
	    		
	    		String timeStr = "";
	    		if (timeHour < 10) timeStr += " ";
	    		timeStr += timeHour + ":" + (timeMinute / 10 % 6) + (timeMinute % 10);
	    		
	    		float size = 24 * displayWidth / 1280;
	    		
	    		paint.setAntiAlias(true);
	    		paint.setTextSize(size);
	    		paint.setColor(Color.WHITE);
	    		FontMetrics fm = paint.getFontMetrics();
	    		timeTexWidth = (int) paint.measureText(timeStr);
	    		rpsTexWidth  = (int) paint.measureText(rpsStr);
	    		fpsTexWidth  = (int) paint.measureText(fpsStr);
	    		fpsTexHeight = (int) (fm.descent - fm.ascent) * 3;
	    		if (fpsTexWidth < rpsTexWidth ) fpsTexWidth = rpsTexWidth;
	    		if (fpsTexWidth < timeTexWidth) fpsTexWidth = timeTexWidth;
	    		
	    		//Bitmap fpsStrBitmap = GL11Until.createTextBitmap(fpsStr, Color.WHITE, size * 2);
	    		Bitmap fpsStrBitmap = GL11Until.createBitmap(fpsTexWidth, fpsTexHeight);
	    		Canvas canvas = new Canvas(fpsStrBitmap);
	    		canvas.drawText(timeStr, 0, + fpsTexHeight / 3 * 1, paint); // fpsを描画
	    		canvas.drawText(fpsStr , 0, + fpsTexHeight / 3 * 2, paint); // rpsを描画
	    		canvas.drawText(rpsStr , 0, + fpsTexHeight / 3 * 3, paint); // rpsを描画
	    		fpsTexNo = GL11Until.createTexture(gl, fpsStrBitmap);
	    		
	    		fpsStrBitmap.recycle();
	    		
	    		//Log.v("fps : ", ""+runFps+" "+runRps);
	    	}
	    	
	    	if (fpsTexNo != -1) {
	    		GL11Until.drawTextureColor(gl, TGL.whiteTexNo, 0, displayHeight - fpsTexHeight, fpsTexWidth, fpsTexHeight, 0f, 0f, 0f, 0.5f);
	    		GL11Until.drawTexture(gl, fpsTexNo, 0, displayHeight - fpsTexHeight, fpsTexWidth, fpsTexHeight);
	    	}
	        
			cnt ++;

    	}
		threadUnLook(); // ロック解除
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	}
	@Override
	public void onSurfaceChanged(GL10 gl10, int w, int h) {
		GL11 gl = (GL11)gl10;
    	Log.v("GL Changed", "Changed: "+gl);

    	mTonyuBoot.setGL(gl);
    	
		displayWidth  = w;
		displayHeight = h;
		
    	// 画面サイズ変更
    	display.resizeDisplay(w, h);
    	mTonyuBoot.updateDisplay();

        // 描画範囲を指定する
        gl.glViewport(0, 0, displayWidth, displayHeight);
        gl.glMatrixMode(GL11.GL_PROJECTION);
	    gl.glLoadIdentity();
        GLU.gluOrtho2D(gl, 0, displayWidth, displayHeight, 0);
        //gl.glOrthof(0, displayWidth, displayHeight, 0, 100, -100);
        
	    // 頂点の配列の利用を有効にする
	    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	    // 色の配列の利用を有効にする
	    //gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
	    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    
	}
	
	
	@Override
	public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
		GL11 gl = (GL11)gl10;
    	Log.v("GL Created", "Created: "+gl);
    	
		//背景色をクリア
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		//ディザを無効化
		gl.glDisable(GL11.GL_DITHER);
		//深度テストを有効化
		//gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL11.GL_DEPTH_TEST);
		//テクスチャ機能ON
		gl.glEnable(GL11.GL_TEXTURE_2D);
		//透明可能に
		gl.glEnable(GL11.GL_ALPHA_TEST);
		//ブレンド可能に
		gl.glEnable(GL11.GL_BLEND);
		gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
        gl.glClear(GL11.GL_COLOR_BUFFER_BIT);

    	display.setDrawMode(TonyuViewDisplay.DIRECT_DRAW);
    	mTonyuBoot.setGL(gl);
    	mTonyuBoot.init();
		
	}
    
    // FPS取得
    public int getRunFps() { return runFps; }
    public int getRunRps() { return runRps; }
    
    // フレーム設定
    public void setFrameRate(float fps, int frameSkip, int frameHalf) {
    	this.fps = fps;                 // １秒間のフレーム処理数
    	this.frameTime = 1000 / fps;    // １フレームあたりの時間（自動計算）
    	this.frameSkipNum = frameSkip;  // 処理落ちしたときにいくつまでコマ落ちさせるか
    	this.frameHalf = frameHalf;     // 実行速度はそのままにして、描画回数を半減させる（1/3, 1/4, ... も可能）
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    // タッチ入力処理
    @Override
    public boolean onTouchEvent(MotionEvent me) {
    	return display.onTouchEvent(me);
    }
    // キーイベント処理
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
    	if (mTonyuBoot != null && mTonyuBoot.dispatchKeyEvent(event)) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    // ディスプレイオブジェクト取得
    public TonyuViewDisplay getScreen() {
    	return display;
    }
    
    // 状態
    public int getActivityState() {
    	return activityState;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Activityの状態変化時のコールバック

	@Override
	public void onActivityResume() {
		threadLook();
		activityState = 1; // 実行可能フラグをON
		mTonyuBoot.onActivityResume();
/*
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
		*/
		threadUnLook();
	}
	@Override
	public void onActivityPause() {
		threadLook();
		activityState = 0; // 実行可能フラグをOFF
		mTonyuBoot.onActivityPause();
		thread = null;
		threadUnLook();
	}
	@Override
	public void onActivityStop() {
		threadLook();
		activityState = 0; // 実行可能フラグをOFF
		mTonyuBoot.onActivityStop();
		threadUnLook();
	}
	@Override
	public void onActivityDestroy(int exitSW) {
		threadLook();
		activityState = 0; // 実行可能フラグをOFF
		if (exitSW == 1) {
			mTonyuBoot.onActivityDestroy();
			mTonyuBoot = null;
			myObj = null;
		}
		thread = null;
		threadUnLook();
	}
	
	
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // スレッドロック機構
    static private volatile int threadLookSW = 0;
    private final static Object lock = new Object();
	private void threadLook() {
		synchronized (lock) {
			while (threadLookSW == 1) {
				sleep(1);
			}
			threadLookSW = 1;
		}
	}
	private void threadUnLook() {
		threadLookSW = 0;
	}


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 便利メソッド
	
	private void sleep(long time) {
        try {
			Thread.sleep(time);
        } catch (InterruptedException e) {}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
}

