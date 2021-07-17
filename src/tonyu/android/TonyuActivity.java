package tonyu.android;

import tonyu.android.TonyuSurfaceView;
import tonyu.android.TonyuView;
import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.content.pm.ActivityInfo;

// アンドロイドのメインプログラム
public class TonyuActivity extends Activity {
	protected TonyuView tonyuView;
    public Handler mHandler;
	public int exitSW = 0;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("LifeCycle", "onCreate: "+this);
        
        // 画面を縦か横かで固定する
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 横画面
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 縦画面
        
        // フルスクリーン
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // タスクバー非表示
        requestWindowFeature(Window.FEATURE_NO_TITLE); // タイトルバー非表示
        
        // メディアの音量を常に調節できる
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        // ハンドラ
        mHandler = new Handler();

        
        // SurfaceViewをセットする
        //tonyuView = TonyuSurfaceView.create(this, new tonyusamples.hockey2.PageIndex());
        //tonyuView = TonyuSurfaceView.create(this, new tonyusamples.ribbon.PageIndex());
        //tonyuView = TonyuSurfaceView.create(this, new tonyusamples.test.TestPageIndex());
        //tonyuView = TonyuSurfaceView.create(this, new tonyu.startup.PageStart());
        //tonyuView = TonyuSurfaceView.create(this, jp.tonyu.muhongokushi.gamelogic.GLB.page_title);
        
        // GLSurfaceViewをセットする
        //tonyuView = new TonyuGLSurfaceViewTest02(this);
        tonyuView = TonyuGLSurfaceView.create(this, new tonyu.startup.PageStart());
        //tonyuView = TonyuGLSurfaceView.create(this, new tonyusamples.home.PageIndex());

        setContentView((View)tonyuView);
        //setContentView(new TonyuGLSurfaceViewTest01(this));
    }
    
    @Override
    protected void onStart(){
        super.onStart();
        Log.v("LifeCycle", "onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.v("LifeCycle", "onResume");
        // スタート・画面復帰
        if (tonyuView != null) tonyuView.onActivityResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.v("LifeCycle", "onPause");
        // 画面消灯・画面が切り替わるとき
        if (tonyuView != null) tonyuView.onActivityPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.v("LifeCycle", "onStop");
        // 画面が隠れた
        if (tonyuView != null) tonyuView.onActivityStop();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.v("LifeCycle", "onRestart");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.v("LifeCycle", "onDestroy");
        // Backキーによりアプリが終了する時
        if (tonyuView != null) tonyuView.onActivityDestroy(exitSW);
    }

    // キーイベント処理
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
    	if (tonyuView != null && tonyuView.dispatchKeyEvent(event)) {
    		return true;
    	} else {
    		return super.dispatchKeyEvent(event);
    	}
    }
    
}