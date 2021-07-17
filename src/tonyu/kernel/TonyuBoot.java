package tonyu.kernel;

import javax.microedition.khronos.opengles.GL11;

import tonyu.android.TonyuActivity;
import tonyu.android.TonyuViewDisplay;
import tonyu.android.TonyuView;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

/** ゲームのメイン処理をするクラス */
public class TonyuBoot {
	public static final int DRAW_SURFACEVIEW = 0x01;
	public static final int DRAW_OPENGL11 = 0x11;
	public static final int DRAW_OPENGL20 = 0x20;
	
	private int drawMode;
	
	private TonyuView tsv;
	private TonyuViewDisplay tsvDisplay;
	private Context context;

	private int frameCount;
	private int screenWidth;
	private int screenHeight;
	private boolean setPadXYUpdate = false;
	
	private TonyuDrawer drawer;                   // 描画オブジェクト
	private TonyuGraphicsManager graphicsManager; // 画像管理オブジェクト
	private TonyuMediaPlayer mplayer;             // サウンド管理オブジェクト
	private TonyuMap map;                         // マップ管理オブジェクト
	private TonyuProjectManager projectManager;   // ページ管理オブジェクト
	private TonyuScreenManager screenManager;     // スクリーン管理オブジェクト
	private TonyuKeyboardManager keyManager;      // キーボード管理オブジェクト
	private TonyuOutput output;                   // 出力オブジェクト
	private TonyuSelectBox selectBox;             // アラートボックスオブジェクト
	private TonyuSystem system;                   // システムオブジェクト
	
	// 実行中のページ
	private TonyuPage currentPage;
	private TonyuPage loadPage;
    
	// Tonyuプロセス
	private TonyuProcessGroup curProcGroup = new TonyuProcessGroup();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// コンストラクタ
	public TonyuBoot(TonyuView tsv, TonyuPage startPage, int drawMode) {
		this.tsv = tsv;
		this.loadPage = startPage;
		
		this.tsvDisplay = tsv.getScreen();
		this.context = tsv.getContext();
		TGL.context = context;
		TGL.res = context.getResources();
		
		setDrawer(drawMode);
		
		TGL.activityHandler = ((TonyuActivity)context).mHandler;
		
		this.frameCount = 0;
		this.screenWidth  = tsvDisplay.getScreenWidth();
		this.screenHeight = tsvDisplay.getScreenHeight();
		TGL.screenWidth  = screenWidth;
		TGL.screenHeight = screenHeight;
		TGL.viewX = 0;
		TGL.viewY = 0;
		
		this.graphicsManager = new TonyuGraphicsManager();
		this.mplayer = new TonyuMediaPlayer();
		this.map = new TonyuMap();
		this.projectManager = new TonyuProjectManager();
		this.screenManager = new TonyuScreenManager();
		this.keyManager = new TonyuKeyboardManager();
		this.output = new TonyuOutput();
		this.selectBox = new TonyuSelectBox();
		this.system = new TonyuSystem();
		TGL.tonyuBoot = this;
		TGL.tonyuDrawer = drawer;
		TGL.grManager = graphicsManager;
		TGL.mplayer = mplayer;
		TGL.map = map;
		TGL.projectManager = projectManager;
		TGL.screenManager = screenManager;
		TGL.keyManager = keyManager;
		TGL.output = output;
		TGL.selectBox = selectBox;
		TGL.system = system;
		
		
	}

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 主なメソッド
	// 初期化
	public void init() {
		if (drawMode == TonyuBoot.DRAW_OPENGL11) {
			TonyuOpenGL11Drawer.init();
		}
	}
	
	// Viewが置き換わった
	public void changeView(TonyuView tsv, int drawMode) {
		this.tsv = tsv;
		this.context = tsv.getContext();
		TGL.context = context;
		TGL.res = context.getResources();
		
		if (this.drawMode != drawMode) {
			this.drawMode = drawMode;
			TGL.drawMode = drawMode;
			setDrawer(drawMode);
		}
	}
	
	// 描画モードによって設定するdrawerを決める
	public void setDrawer(int drawMode) {
		TonyuDrawer drawer;
		switch (drawMode) {
			case DRAW_SURFACEVIEW: drawer = new TonyuSurfaceViewDrawer(); break;
			case DRAW_OPENGL11:    drawer = new TonyuOpenGL11Drawer();    break;
			case DRAW_OPENGL20:    drawer = new TonyuSurfaceViewDrawer(); break;
			default:               drawer = new TonyuSurfaceViewDrawer(); break;
		}
		this.drawer = drawer;
		this.drawMode = drawMode;
		TGL.drawMode = drawMode;
	}
	
	// 実行
	public boolean execute(boolean doDraw) {
		
		// ページ移動が発生
		if (loadPage != null) {
			closePage(loadPage);
			frameCount = 0;
			openPage(loadPage);
			map.init();
		}
		
		// スクリーンサイズが変更されたら設定
		if (screenWidth != TGL.screenWidth || screenHeight != TGL.screenHeight) {
			screenWidth  = TGL.screenWidth;
			screenHeight = TGL.screenHeight;
			tsvDisplay.resizeScreen(screenWidth, screenHeight, setPadXYUpdate);
			updateDisplay();
			setPadXYUpdate = false;
		}
		
		// フレーム数を更新(Tonyuは1から始まるが、こちらは0から始まる)
		TGL.frameCount = frameCount;
		
		// このコマは描画できるか
		TGL._doDraw = doDraw;
		
		// タップ情報を更新
		TGL.padX = tsvDisplay.getPadX();
		TGL.padY = tsvDisplay.getPadY();
		TGL.padPush = tsvDisplay.getPadPush();
		if (TGL.padPush >= 1) {
			TGL.padPushCnt ++;
		} else {
			TGL.padPushCnt = 0;
		}
		
		// オブジェクトを実行
		curProcGroup.exec();
		
		frameCount ++;
		
		return loadPage != null;
	}

	// 描画
	public void draw(Object drawObj, boolean updateFrame) {
		if (updateFrame) {
			curProcGroup.draw(drawObj); // オブジェクトを描画
			map.draw();
		}
		drawer.allDraw(drawObj, updateFrame);
		screenManager.draw(drawObj);
	}
	
	// ロード時に描画
	public void loadDraw(Object drawObj) {
		screenManager.loadDraw(drawObj);
	}
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 仲介メソッド
	
	// ページ切替(消す)
	public void movePage(TonyuPage loadPage) {
		if (loadPage != null) {
			this.loadPage = loadPage;
		}
	}
	
	// ページ切替(消す)
	private void closePage(TonyuPage loadPage) {
		if (loadPage != null) {
			if (currentPage != null) {
				curProcGroup.clear();
				currentPage.close(loadPage);
				graphicsManager.clearTempGraphicsChip();
			}
		}
	}
	
	// ページ切替
	private void openPage(TonyuPage loadPage) {
		if (loadPage != null) {
			System.gc(); // GC
			loadPage.open(currentPage);
			currentPage = loadPage;
			this.loadPage = null;
		}
	}
	

	public int getBGColor() { return tsvDisplay.getBGColor(); }                 // 背景色取得
	public int getMarginColor() { return tsvDisplay.getMarginColor(); }         // 余白色取得
	public void setBGColor(int color) { tsvDisplay.setBGColor(color); }         // 背景色変更
	public void setMarginColor(int color) { tsvDisplay.setMarginColor(color); } // 余白色変更
	public void updatePadXY() { setPadXYUpdate = true; }                        // タップ位置更新
	public void setPadXY(float x, float y) { // タップ位置更新
		tsvDisplay.setPadXY(x, y);
	}

    public boolean dispatchKeyEvent(KeyEvent event) {
    	if (keyManager != null && keyManager.dispatchKeyEvent(event)) {
    		return true;
    	} else {
    		return false;
    	}
    }

	public void setGL(GL11 gl) {
		TGL.gl11 = gl;
	}
	
	
	// ディスプレイの更新があった場合値を更新
	public void updateDisplay() {
		// 端末の画面サイズ更新
		TGL.displayWidth  = tsvDisplay.getDisplayWidth();
		TGL.displayHeight = tsvDisplay.getDisplayHeight();

		// 画面の余白サイズ更新
		TGL.marginWidth  = tsvDisplay.getMarginWidth();
		TGL.marginHeight = tsvDisplay.getMarginHeight();
		TGL.marginHalfWidth  = tsvDisplay.getMarginWidth()  / 2;
		TGL.marginHalfHeight = tsvDisplay.getMarginHeight() / 2;

		// 画面の拡大率更新
		TGL.widthR  = tsvDisplay.getWidthR();
		TGL.heightR = tsvDisplay.getHeightR();

		Log.v("size1", ""+TGL.displayWidth+" "+TGL.displayHeight);
		Log.v("size2", ""+TGL.marginHalfWidth+" "+TGL.marginHalfHeight);
		Log.v("size3", ""+TGL.widthR+" "+TGL.heightR);
	}
	
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ゲッター
	Context getContext() { return context; } // コンテキストを返す
	TonyuView getTonyuSurfaceView() { return tsv; } // サーフェイスビューを返す
	TonyuProcessGroup getCurProcGroup() { return curProcGroup; } // ProcessGroupを返す
	TonyuDrawer getTonyuDrawer() { return drawer; } // 描画オブジェクトを返す
	TonyuPage getCurrentPage() { return currentPage; } // 現在のページオブジェクトを返す

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Activityの状態変化時のコールバック
	
	// スタート・再開
	public void onActivityResume() {
		TGL.activityState = tsv.getActivityState();
		mplayer.onActivityResume();
	}
	
	// 画面消えた・画面切り替え
	public void onActivityPause() {
		TGL.activityState = tsv.getActivityState();
		mplayer.onActivityPause();
	}
	
	// 画面が隠れた
	public void onActivityStop() {
		TGL.activityState = tsv.getActivityState();
		//activityState = ACTIVITY_STOP;
		//mplayer.onActivityPause();
	}
	
	// Backキーによりアプリが終了したとき
	public void onActivityDestroy() {
		TGL.activityState = tsv.getActivityState();
		graphicsManager.onActivityDestroy();
		mplayer.onActivityDestroy();
	}

}
