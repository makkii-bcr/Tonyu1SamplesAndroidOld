package tonyu.android;

import tonyu.kernel.*;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.MotionEvent;
import android.graphics.Canvas;
import android.graphics.Color;

/** ゲーム実行、画面処理をまとめるクラス */
public class TonyuSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable, TonyuView {
	static public TonyuSurfaceView myObj; // 自分
	static private TonyuBoot sTonyuBoot;  // ゲームメインクラス
	private TonyuBoot mTonyuBoot;  // ゲームメインクラス(staticはきえるのでメンバ変数にも保存)
	static public TonyuSurfaceView create(Context context, TonyuPage startPage) {
		// ２回目にクリエイトされた場合、新しいViewを渡さないといけない
		TonyuSurfaceView newObj;

		Log.v("sTonyuBoot:", ""+sTonyuBoot);
		// ２回目の場合はtonyuBootの情報を書き換える
		if (sTonyuBoot == null || myObj == null) { // 初回
			newObj = new TonyuSurfaceView(context, startPage);
		} else { // ２回目以降
			//TonyuBoot tonyuBoot = myObj.getTonyuBoot();
			newObj = new TonyuSurfaceView(context, startPage, myObj.getTonyuBoot(), myObj.getScreen());
			sTonyuBoot.changeView(newObj, TonyuBoot.DRAW_SURFACEVIEW);
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
    
    // スレッド
    private Thread thread;
    
    // 仮想画面
    private TonyuViewDisplay display;
	private SurfaceHolder holder;
	private Canvas displayCanvas;
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// コンストラクタ (初回)
    private TonyuSurfaceView(Context context, TonyuPage startPage) {
        this(context, startPage, null, null);
    }
    // コンストラクタ(２回目以降)
    private TonyuSurfaceView(Context context, TonyuPage startPage, TonyuBoot tonyuBoot, TonyuViewDisplay display) {
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
        	mTonyuBoot = new TonyuBoot(this, startPage, TonyuBoot.DRAW_SURFACEVIEW);
        	mTonyuBoot.init(); // 初期化
        } else {
	        // ゲームメインオブジェクトを持ってくる
        	mTonyuBoot = tonyuBoot;
        }
        
        holder = getHolder();
        holder.addCallback(this); // callbackメソッド（下の３つ）を登録
        
    	Log.v("SurfaceView", "コンストラクタ2: "+tonyuBoot+" "+sTonyuBoot);
    }
    
    // ゲームメインオブジェクトを取得
    public TonyuBoot getTonyuBoot() {
    	return mTonyuBoot;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // コールバック
    //サーフェイス生成で実行される
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	Log.v("SurfaceView", "Created: "+holder);
    	// 別スレッド作成し実行させる
        if (thread == null) {
	    	thread = new Thread(this);
	    	thread.start();
        }
    }

    //サーフェイス変化で実行される
    @Override
    public void surfaceChanged(SurfaceHolder holder, int f, int w, int h) {
    	Log.v("SurfaceView", "Changed: "+holder);
    	// 画面サイズ変更
    	display.resizeDisplay(w, h);
    	mTonyuBoot.updateDisplay();
    }

    //サーフェイス破棄で実行される
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    	Log.v("SurfaceView", "Destroyed: "+holder);
    	thread = null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 実行
	@Override
    public void run() {
		
    	// 開始まで少し待機
		sleep(1000);
    	
    	int dwCnt = 0;
    	int procNum, drawCharge = 0;
    	float procNumF;
    	boolean doDraw;
    	boolean loadPageDraw = false;
    	
    	// フレームレート設定
    	setFrameRate(60, 5, 1);
    	
    	long startTime = System.currentTimeMillis();
    	long nowTime   = 0;

    	long runTime = System.currentTimeMillis();
    	float runTimeFloat = 0.0f;
    	
		threadLook(); // 他のスレッドが動かないようにロック
        // 実行ループ 
        while(thread != null) {
        	// フレーム速度計算
        	procNumF = (System.currentTimeMillis() - runTime) / frameTime;
    		procNum = (int) procNumF; // 必要な処理回数を計算
    		drawCharge +=  procNumF;
    		if (procNum > frameSkipNum) {
    			procNum = frameSkipNum; // スキップできるフレーム数を制限
    			runTime = System.currentTimeMillis() - (long)((frameSkipNum) * frameTime);
    			drawCharge = frameHalf;
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
	        	
	        	// 描画処理
	    		if (doDraw || loadPageDraw) {
	            	displayCanvas = holder.lockCanvas(); // 描画開始
	                if (displayCanvas != null) { // 端末画面のCanvas取得が成功
		            	display.setDisplayCanvas(displayCanvas);   // displayに端末画面のCanvasを渡しておく
	            		display.drawClear(); // 画面クリア
		            	mTonyuBoot.draw(display.getDrawCanvas(), doDraw);  // ゲーム描画処理を実行 (描画すべき描画キャンバスを渡す)
		            	if (loadPageDraw) mTonyuBoot.loadDraw(display.getDrawCanvas());  // ページロード描画処理を実行 (描画すべき描画キャンバスを渡す)
		            	
		    			onDraw();                                  // ゲーム画面を端末画面にコピー (描画モードによっては何もしないこともある)
		    			holder.unlockCanvasAndPost(displayCanvas); // 描画終了
		    			displayCanvas = null;
	                }
	    		}
        	}
    		/////////////////////////////////////////////////////////////////////////////
        	
        	// 処理回数計測
            runRpsCount += procNum;
        	
        	// 実行時間計算
            runTimeFloat += frameTime * procNum; // １フレームあたりの処理時間 × 処理回数
            runTime += (long)runTimeFloat;       // 処理したフレーム分、時間を加算
            runTimeFloat -= (int)runTimeFloat;   // 小数が切り捨てられないように工夫
    		/////////////////////////////////////////////////////////////////////////////
            
        	// FPS計算
        	nowTime = System.currentTimeMillis();
        	if (nowTime - startTime >= 1000) {
        		runFps  = runFpsCount;
        		runRps = runRpsCount;
        		runFpsCount = 0;
        		runRpsCount = 0;
        		startTime = nowTime;
        	}
    		/////////////////////////////////////////////////////////////////////////////
    		threadUnLook(); // ロック解除
        	// 処理が間に合っているときはスリープする（処理が軽いときは省電力化できるかも）
        	if (procNum <= 0) {
    			sleep(1);
        	}
    		threadLook(); // 他のスレッドが動かないようにロック
        }
		threadUnLook(); // ロック解除
    }
    // 描画処理
    protected void onDraw() {
    	//display.draw();
    	display.draw(runFps, runRps);
        runFpsCount ++;
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
	@Override
    public TonyuViewDisplay getScreen() {
    	return display;
    }
    
    // 状態
	@Override
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
		threadUnLook();
	}
	@Override
	public void onActivityPause() {
		threadLook();
		activityState = 0; // 実行可能フラグをOFF
		mTonyuBoot.onActivityPause();
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
	
}
