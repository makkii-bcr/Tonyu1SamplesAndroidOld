package tonyu.kernel;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class TonyuMediaPlayer {
	private final int SOUNDPOOL_MAX_STREAM = 64;
	private Context context;
	private HashMap<String, MediaPlayer> bgmMap;
	private HashMap<String, Integer> seMap;
	private SoundPool soundPool;
	private BgmPlayer[] bgmPlayerAry = new BgmPlayer[4]; // 同時に4つのBGMを鳴らせるらしいのでその時のための配列
	private Thread thread;
	private LoopCheckClass loopCheckClass;

	private float bgmVolL = 1.0f, bgmVolR = 1.0f; 
	private float seVolL  = 1.0f, seVolR  = 1.0f; 
	
	public TonyuMediaPlayer() {
		context = TGL.context;
		bgmMap = new HashMap<String, MediaPlayer>();
		seMap = new HashMap<String, Integer>();

		for (int i=0; i<bgmPlayerAry.length; i++) {
			bgmPlayerAry[i] = new BgmPlayer();
		}
		
		soundPool = new SoundPool(SOUNDPOOL_MAX_STREAM, AudioManager.STREAM_MUSIC, 0);
		//sp.stop(stream_id);
		//sp.release();
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////
	// BGM関係

	///////////////////////////////////////////////////////////////////////////////////
	// BGM追加/削除メソッド
	
	// BGMをres/rawから追加
	public boolean addResBGM(String bgmName, int id) {
		MediaPlayer mp;
		try {
			mp = MediaPlayer.create(context, id);
			
			if (!bgmMap.containsKey(bgmName)) { // 同じ名前のデータが無かったら追加できる
				bgmMap.put(bgmName, mp);
				return true;
			} else { // 同じ名前のデータがすでにあったら追加はできない
				return false;
			}
		} catch (Exception e) { // 指定したファイルが存在しない場合など
			e.printStackTrace();
			return false;
		}
	}
	// BGMを削除
	public boolean deleteBGM(String bgmName) {
		MediaPlayer mp;
		if (bgmMap.containsKey(bgmName)) { // 同じ名前のデータが合ったら削除できる
			mp = bgmMap.get(bgmName);
			for (BgmPlayer bgmPlayer : bgmPlayerAry) {
				if (bgmPlayer.equalsMediaPlayer(mp)) {
					bgmPlayer.release(); // 削除するMediaPlayerが使われていたら、使用解除する
				}
			}
			mp.release(); // MediaPlayer自体をメモリ解放
			bgmMap.remove(bgmName);
			return true;
		} else {
			return false;
		}
	}
	// BGMを全て削除
	public void clearBGM() {
		// BGM一応ストップ
		for (BgmPlayer bgmPlayer : bgmPlayerAry) {
			bgmPlayer.release();
		}
		// BGMメモリ解放
		for (HashMap.Entry<String, MediaPlayer> mp : bgmMap.entrySet()) {
			mp.getValue().release();
		}
		bgmMap.clear();
	}

	///////////////////////////////////////////////////////////////////////////////////
	// BGM演奏関係
	
	static public final int BGM_PLAYER1 = 0;
	static public final int BGM_PLAYER2 = 1;
	static public final int BGM_PLAYER3 = 2;
	static public final int BGM_PLAYER4 = 3;
	// BGMを再生
	public boolean playBGM(String bgmName) {
		return playBGMEx(0, bgmName, true, 0, 0);
	}
	// BGMを再生
	public boolean playBGM(String bgmName, boolean loop) {
		return playBGMEx(0, bgmName, loop, 0, 0);
	}
	// BGMを再生
	public boolean playBGM(String bgmName, boolean loop, int seekTime) {
		return playBGMEx(0, bgmName, loop, seekTime, 0);
	}
	// BGMを再生
	public boolean playBGM(String bgmName, boolean loop, int seekTime, int loopStartSeekTime) {
		return playBGMEx(0, bgmName, loop, seekTime, loopStartSeekTime);
	}
	// BGMを再生(Player指定)
	public boolean playBGMEx(int playerNo, String bgmName) {
		return playBGMEx(playerNo, bgmName, true, 0, 0);
	}
	// BGMを再生(Player指定)
	public boolean playBGMEx(int playerNo, String bgmName, boolean loop) {
		return playBGMEx(playerNo, bgmName, loop, 0, 0);
	}
	// BGMを再生(Player指定)
	public boolean playBGMEx(int playerNo, String bgmName, boolean loop, int seekTime) {
		return playBGMEx(playerNo, bgmName, loop, seekTime, 0);
	}
	// BGMを再生(Player指定)
	public boolean playBGMEx(int playerNo, String bgmName, boolean loop, int seekTime, int loopStartSeekTime) {
		if ((bgmMap.containsKey(bgmName)) && (0 <= playerNo && playerNo < bgmPlayerAry.length)) {
			return bgmPlayerAry[playerNo].play(bgmMap.get(bgmName), loop, seekTime, loopStartSeekTime);
		}
		return false;
	}
	
	// BGMを停止
	public void stopBGM() {
		bgmPlayerAry[0].stop();
	}
	// BGMを停止(Player指定)
	public void stopBGMEx(int playerNo) {
		if (0 <= playerNo && playerNo < bgmPlayerAry.length) {
			bgmPlayerAry[playerNo].stop();
		}
	}

	// BGMを一時停止
	public void pauseBGM() {
		bgmPlayerAry[0].pause();
	}
	// BGMを一時停止(Player指定)
	public void pauseBGMEx(int playerNo) {
		if (0 <= playerNo && playerNo < bgmPlayerAry.length) {
			bgmPlayerAry[playerNo].pause();
		}
	}

	// BGMを再開
	public void restartBGM() {
		bgmPlayerAry[0].restart();
	}
	// BGMを再開(Player指定)
	public void restartBGMEx(int playerNo) {
		if (0 <= playerNo && playerNo < bgmPlayerAry.length) {
			bgmPlayerAry[playerNo].restart();
		}
	}
	
	// BGMの音量を調整
	public void setBGMVolume(float vol) {
		setBGMVolume(vol, vol);
	}
	public void setBGMVolume(float volL, float volR) {
		bgmVolL = volL;
		bgmVolR = volR;
		bgmPlayerAry[0].setVolume(volL, volR);
	}

	// BGMの再生位置を変更
	public void seekBGM(int seek) {
		bgmPlayerAry[0].seekBGM(seek);
	}
	// BGMの再生位置を変更(Player指定)
	public void seekBGMEx(int playerNo, int seek) {
		if (0 <= playerNo && playerNo < bgmPlayerAry.length) {
			bgmPlayerAry[playerNo].seekBGM(seek);
		}
	}
	
	// BGMの現在再生位置を取得(再生位置というよりもバッファ書き込み位置なので、実際の再生位置よりも早い時間が返ってくる)
	public int getTimeBGM() {
		return bgmPlayerAry[0].getPlayingTime();
	}
	// BGMの現在再生位置を取得(Player指定)
	public int getTimeBGMEx(int playerNo) {
		if (0 <= playerNo && playerNo < bgmPlayerAry.length) {
			return bgmPlayerAry[playerNo].getPlayingTime();
		}
		return -1; // エラー
	}

	// BGMの長さ
	public int getLengthBGM() {
		return bgmPlayerAry[0].getLength();
	}
	// BGMの長さ(Player指定)
	public int getLengthBGMEx(int playerNo) {
		if (0 <= playerNo && playerNo < bgmPlayerAry.length) {
			return bgmPlayerAry[playerNo].getLength();
		}
		return -1; // エラー
	}

	///////////////////////////////////////////////////////////////////////////////////
	// BGMプレイヤーのクラス
	// プレイヤー
	class BgmPlayer {
		static final int BGM_EMPTY = -1;
		static final int BGM_STOP  = 0;
		static final int BGM_PLAY  = 1;
		static final int BGM_PAUSE = 2;
		private int playState = BGM_EMPTY;
		//private boolean loop = false;
		private MediaPlayer playMP;
		private int loopStartSeekTime = 0;
		private int nowPlayTime = 0;
		private int oldPlayTime = 0;
		// 再生
		boolean play(MediaPlayer mp, boolean loop, int seekTime, int loopStartSeekTime) {
			if (playState == BGM_PLAY) {
				stop();
			}
			this.playMP = mp; // MediaPlayerにBGMが入っているためそのMediaPlayerを指定
			//this.loop = loop; // ループ有無
			
			// 特別なループ関係 //
			oldPlayTime = 0; 
			if (loop) this.loopStartSeekTime = loopStartSeekTime;
			else      this.loopStartSeekTime = 0; // ループしないなら特別なループは無し
			if (this.loopStartSeekTime < 0) this.loopStartSeekTime = 0; // マイナスは指定させないぞ
			
			playMP.setLooping(loop); // ループ設定
			playMP.setVolume(bgmVolL, bgmVolR); // 音量設定
			playMP.seekTo(seekTime); // 再生位置設定
			playMP.start();          // BGM鳴らす
			Log.v("bgm play", ""+playMP.isPlaying());
			if (playMP.isPlaying()) { // 無事に再生された場合
				playState = BGM_PLAY;
				if (this.loopStartSeekTime > 0) { // 特別なループが指定されていたら
					createBGMThread(); // ループ監視スレッドを起動する
				}
			} else { // 再生に失敗
				this.loopStartSeekTime = 0;
				return false;
			}
			return true;
		}
		// 一時停止
		void pause() {
			if (playState == BGM_PLAY) {
				playMP.pause();
				if (!playMP.isPlaying()) playState = BGM_PAUSE;
			}
		}
		// 再開
		void restart() {
			if (playState == BGM_PAUSE) {
				playMP.start();
				if (playMP.isPlaying()) playState = BGM_PLAY;
			}
		}
		// 停止
		void stop() {
			if (playState == BGM_PLAY || playState == BGM_PAUSE) {
				playMP.pause();
				playMP.seekTo(0);
				if (!playMP.isPlaying()) playState = BGM_STOP;
			}
		}
		// 音量
		void setVolume(float volL, float volR) {
			playMP.setVolume(volL, volR);
		}
		// 再生位置を変更
		void seekBGM(int seek) {
			if (playState == BGM_EMPTY) return;
			playMP.seekTo(seek);
		}
		// 現在の再生位置(というよりバッファ書き込み位置)を取得
		int getPlayingTime() {
			if (playState == BGM_EMPTY) return -1;
			return playMP.getCurrentPosition();
		}
		// 曲の長さ
		int getLength() {
			if (playState == BGM_EMPTY) return -1;
			return playMP.getDuration();
		}
		// Activity一時停止
		synchronized void onActivityPause() {
			if (playState == BGM_PLAY) {
				Log.v("bgm pause1", ""+playMP.isPlaying());
				playMP.pause();
				Log.v("bgm pause2", ""+playMP.isPlaying());
			}
		}
		// Activity再開
		synchronized void onActivityResume() {
			if (playState == BGM_PLAY) {
				playMP.start();
			}
		}
		// RPGツクールループ？
		// loopStartSeekTimeが0以外の時、ループしたか監視しなければならない。このメソッドは別スレッドが呼び出す。
		// 現在の再生位置を監視し、ループして再生位置が戻った時、指定位置に再生位置を変更する
		// 戻り値: true == 監視が必要, false == 監視しなくてもよい
		synchronized boolean onloopCheck() {
			if (playState == BGM_EMPTY) return false;
			if (loopStartSeekTime == 0) return false;
			nowPlayTime = playMP.getCurrentPosition();
			//Log.v("onloopCheck",""+nowPlayTime+"/"+getLength());
			if (nowPlayTime < oldPlayTime) { // 再生位置が戻ったら指定位置に移動
				playMP.seekTo(loopStartSeekTime);
			}
			oldPlayTime = nowPlayTime;
			return true;
		}
		boolean equalsMediaPlayer(MediaPlayer mp) {
			return mp == playMP;
		}
		// 使用中のMediaPlayerをメモリ解放するため、使用をやめさせる
		synchronized void release() {
			stop();
			playMP = null;
			playState = BGM_EMPTY;
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	// SE関係

	///////////////////////////////////////////////////////////////////////////////////
	// SE追加メソッド
	
	// SEをres/rawから追加
	public boolean addResSE(String seName, int id) {
		int seId;
		try {
			seId = soundPool.load(context, id, 1);
			if (!seMap.containsKey(seName)) { // 同じ名前のデータが無かったら追加できる
				seMap.put(seName, seId);
				return true;
			} else { // 同じ名前のデータがすでにあったら追加はできない
				return false;
			}
		} catch (Exception e) { // 指定したファイルが存在しない場合など
			e.printStackTrace();
			return false;
		}
	}
	// SEを削除
	public boolean deleteSE(String seName) {
		if (seMap.containsKey(seName)) { // 同じ名前のデータが合ったら削除できる
			boolean rt = soundPool.unload(seMap.get(seName)); // SEデータのメモリ解放
			seMap.remove(seName);
			if (seMap.isEmpty()) { // 全部削除ならまたSoundPoolを作り直す(soundPool.unload()しても再利用できないため)
				soundPool.release();
				soundPool = null;
				soundPool = new SoundPool(SOUNDPOOL_MAX_STREAM, AudioManager.STREAM_MUSIC, 0);
			}
			return rt;
		} else {
			return false;
		}
	}
	// SEを全て削除
	public void clearSE() {
		for (HashMap.Entry<String, Integer> mp : seMap.entrySet()) {
			soundPool.unload(mp.getValue());
		}
		seMap.clear();
		soundPool.release();
		soundPool = null;
		soundPool = new SoundPool(SOUNDPOOL_MAX_STREAM, AudioManager.STREAM_MUSIC, 0);
	}
	
	// SEを再生(id)
	public int playSE(String seName) {
		return playSE(seName, 1.0f, 1.0f, 1.0f, 0);
	}
	// SEを再生(id,音量(左右))
	public int playSE(String seName, float vol) {
		return playSE(seName, vol, vol, 1.0f, 0);
	}
	// SEを再生(id,左音量,右音量)
	public int playSE(String seName, float volL, float volR) {
		return playSE(seName, volL, volR, 1.0f, 0);
	}
	// SEを再生(id,左音量,右音量,再生スピード)
	public int playSE(String seName, float volL, float volR, float speed) {
		return playSE(seName, volL, volR, speed, 0);
	}
	// SEを再生
	public int playSE(String seName, float volL, float volR, float speed, int loop) {
		if (seMap.containsKey(seName)) {
			
			int streamId = soundPool.play(seMap.get(seName), volL * seVolL, volR * seVolR, 0, loop, speed);
			Log.v("playSE", "id : "+streamId);
			return streamId;
		} else {
			return -1;
		}
	}
	

	// BGMの音量を調整
	public void setSEVolume(float vol) {
		seVolL = vol;
		seVolR = vol;
	}
	public void setSEVolume(float volL, float volR) {
		seVolL = volL;
		seVolR = volR;
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	// Activityの状態変化によるコールバック
	
	// 画面消灯・画面切り替え時に音楽を一時停止
	public void onActivityPause() {
		waitBGMThread(); // スレッドを停止
		for (BgmPlayer bgmPlayer : bgmPlayerAry) {
			bgmPlayer.onActivityPause();
		}
	}
	// 画面復帰時に音楽を再生
	public void onActivityResume() {
		notifyBGMThread(); // スレッドを開始
		for (BgmPlayer bgmPlayer : bgmPlayerAry) {
			bgmPlayer.onActivityResume();
		}
	}
	// Backキーでアプリ終了時、メモリを解放
	public void onActivityDestroy() {
		// ループ監視スレッドがいたら消す
		if (loopCheckClass != null) {
			loopCheckClass.myEnd();
			while (loopCheckClass.active == true) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) { e.printStackTrace(); }
			}
			thread = null;
			loopCheckClass = null;
		}
		// メモリ解放
		clearBGM();
		clearSE();
		
		soundPool.release(); // SEメモリ解放
	}

	///////////////////////////////////////////////////////////////////////////////////
	// BGMのループを制御するためのスレッド
	
	private void createBGMThread() {
		if (loopCheckClass == null) {
			loopCheckClass = new LoopCheckClass();
			thread = new Thread(loopCheckClass);
			thread.start();
		}
	}
	public void waitBGMThread() {
		if (loopCheckClass != null) {
			loopCheckClass.myWait();
		}
	}
	public void notifyBGMThread() {
		if (loopCheckClass != null) {
			Log.v("notifyBGMThread", "notify:start");
			loopCheckClass.myNotify();
			Log.v("notifyBGMThread", "notify:end");
		}
	}

	// ループ監視クラス (同じクラス(TonyuMediaPlayer)にrun()を定義すると、notifyBGMThread()を呼び出すときにデッドロックが起きてしまうので別クラスで定義)
	class LoopCheckClass implements Runnable {
		private boolean active = false;
		private boolean wait   = false;
		private boolean end    = false;
		// ループ監視スレッド

		private synchronized void myNotify() {
			wait = false;
			notifyAll();
		}
		private void myWait() {
			wait = true;
		}
		private synchronized void _wait() {
			try {
				wait(); // 止める
			} catch (InterruptedException e) { e.printStackTrace();	}
		}
		private void myEnd() {
			myNotify();
			end = true;
		}
		@Override
		public void run() {
			active = true;
			boolean checkBool = false;
			boolean checkSW = false;
			int freeCnt = 0;
			int freeCntAdd = 1;
			// BGMをループする時、始端から
			while (!(thread == null || end == true)) {
				
				// ループしてないかチェック
				checkSW = false;
				for (BgmPlayer bgmPlayer : bgmPlayerAry) {
					checkBool = bgmPlayer.onloopCheck();
					if (checkBool) checkSW = true;
				}
				
				// 監視が必要ないならスリープ時間を増やす(負担軽減のつもり)
				if (checkSW) freeCnt = 0; // チェックが必要
				else freeCnt += freeCntAdd; // チェックがいらない : スリープ時間を増やしていく
				
				//Log.v("loopCheckThread", ""+freeCnt);

				// スリープ
				try {
					if (wait == false) { // 実行状態
						if      (freeCnt < 60 *  1) { Thread.sleep( 16); freeCntAdd =  16/16; } // 60/sec
						else if (freeCnt < 60 *  5) { Thread.sleep( 64); freeCntAdd =  64/16; } // 15/sec
						else if (freeCnt < 60 * 10) { Thread.sleep(512); freeCntAdd = 512/16; } //  2/sec
					} else { // 停止状態
						while (wait) {
							_wait(); // 止める
						}
					}
				} catch (InterruptedException e) { e.printStackTrace();	}
			}
			active = false;
		}
	}
	
}
