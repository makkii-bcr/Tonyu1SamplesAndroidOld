package tonyu.kernel;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.util.Log;

public class TonyuGraphicsManager {
	private int chipSize = 0;
	private HashMap<String, TonyuGraphicsFileManager> gfManagerMap;
	private ArrayList<TonyuGraphicsFileManager> gfManagerAry; // インデックス番号に対応するグラフィックスファイルマネージャを格納する
	private ArrayList<TonyuGraphicsChip> tempGChipManagerAry; // 一時的に使用される画像ファイル
	// 例
	// 0 ~ 4 までが味方キャラ画像("myChar") => インデックス0 ~ 4 が全て"myChar"の画像管理オブジェクトの参照を指している
	// 5 ~ 20 までが敵キャラ画像("enemyChar") => インデックス5 ~ 20 が全て"enemyChar"の画像管理オブジェクトの参照を指している

	///////////////////////////////////////////////////////////////////////////////////
	// コンストラクタ
	public TonyuGraphicsManager() {
		gfManagerMap = new HashMap<String, TonyuGraphicsFileManager>();
		gfManagerAry = new ArrayList<TonyuGraphicsFileManager>();
		tempGChipManagerAry = new ArrayList<TonyuGraphicsChip>();
	}

	///////////////////////////////////////////////////////////////////////////////////
	// 画像追加メソッド
	
	// リソースから追加 //
	// 画像ファイルを追加(一枚絵)
	public void addBitmapFile(String bitmapName, int id) {
		TonyuGraphicsFileManager gfManager = new TonyuGraphicsFileManager(TonyuGraphicsFileManager.ONE_CHIP, id);
		addBitmapSet(bitmapName, gfManager);
	}
	// 画像ファイルを追加(チップ分割(等間隔))
	public void addBitmapFile(String bitmapName, int id, int chipWidth, int chipHeight) {
		TonyuGraphicsFileManager gfManager = new TonyuGraphicsFileManager(TonyuGraphicsFileManager.EQUAL_SIZE_CHIP, id, chipWidth, chipHeight);
		addBitmapSet(bitmapName, gfManager);
	}
	// 画像ファイルを追加(Tonyu形式)
	public void addBitmapFileTonyu(String bitmapName, int id) {
		TonyuGraphicsFileManager gfManager = new TonyuGraphicsFileManager(TonyuGraphicsFileManager.TONYU_CHIP, id);
		addBitmapSet(bitmapName, gfManager);
	}

	// Bitmapを直接追加 //
	// 画像ファイルを追加 (一枚絵)
	public void addBitmap(String bitmapName, Bitmap bitmap) {
		TonyuGraphicsFileManager gfManager = new TonyuGraphicsFileManager(TonyuGraphicsFileManager.ONE_CHIP, bitmap);
		addBitmapSet(bitmapName, gfManager);
	}
	// 画像ファイルを追加 (チップ分割(等間隔))
	public void addBitmap(String bitmapName, Bitmap bitmap, int chipWidth, int chipHeight) {
		TonyuGraphicsFileManager gfManager = new TonyuGraphicsFileManager(TonyuGraphicsFileManager.EQUAL_SIZE_CHIP, bitmap, chipWidth, chipHeight);
		addBitmapSet(bitmapName, gfManager);
	}
	
	// 画像ファイル管理クラスで追加 //
	public void addBitmapFileManager(String bitmapName, TonyuGraphicsFileManager gfManager) {
		addBitmapSet(bitmapName, gfManager);
	}
	
	// 画像ファイル追加の処理をまとめたメソッド(主に登録系)
	private void addBitmapSet(String bitmapName, TonyuGraphicsFileManager gfManager) {
		gfManager.onBindGraphicsManager(bitmapName, chipSize); // 登録させる

		gfManagerMap.put(bitmapName, gfManager);
		int size = gfManager.getChipArySize();
		for (int i=0; i<size; i++) { // 画像番号と一致させるため、チップの個数分参照をいれて追加する
			gfManagerAry.add(gfManager);
		}
		chipSize += size;
		Log.v("addBitmap", ""+chipSize);
	}
	///////////////////////////////////////////////////////////////////////////////////
	// 画像削除メソッド
	
	// 画像ファイルを削除
	public boolean deleteBitmap(String bitmapName) {
		TonyuGraphicsFileManager gfm;
		if (gfManagerMap.containsKey(bitmapName)) {
			gfm = gfManagerMap.get(bitmapName);
			for (int i = gfManagerAry.size() - 1; i>=0; i--) { // 先に配列の参照から消す
				if (gfm == gfManagerAry.get(i)) {
					gfManagerAry.remove(i); // 配列から削除
				}
			}
			
			chipSize = gfManagerAry.size();
			gfManagerMap.remove(bitmapName); // ハッシュから参照を消す
			gfm.release(); // メモリ解放
			return false;
		} else {
			return false;
		}
	}
	// 全ての画像ファイルを削除
	public void clearBitmap() {
		for (HashMap.Entry<String, TonyuGraphicsFileManager> entry : gfManagerMap.entrySet()) {
			entry.getValue().release();
		}
		gfManagerMap.clear();
		gfManagerAry.clear();
		
		chipSize = 0;
	}


	///////////////////////////////////////////////////////////////////////////////////
	// 一時使用画像のメソッド
	// 画像追加
	public TonyuGraphicsChip addTempGraphicsChip(TonyuGraphicsChip gChip) {
		tempGChipManagerAry.add(gChip); // 配列に追加
		return gChip;
	}
	// 画像削除
	public void deleteTempGraphicsChip(TonyuGraphicsChip gChip) {
		tempGChipManagerAry.remove(gChip); // 配列から削除
		gChip.release(); // メモリ解放
	}
	// 画像削除
	public void clearTempGraphicsChip() {
		for (TonyuGraphicsChip gChip : tempGChipManagerAry) {
			gChip.release(); // メモリ解放
		}
		tempGChipManagerAry.clear();
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	// 呼ばれるメソッド
	
	// Bitmapを取得(番号)
	public Bitmap getBitmap(int id) {
		if (0 <= id && id < chipSize) {
			return gfManagerAry.get(id).getBitmap(id);
		} else {
			return null;
		}
	}
	
	// グラフィックスチップを取得(番号)
	public TonyuGraphicsChip getGraphicsChip(int id) {
		if (0 <= id && id < chipSize) {
			return gfManagerAry.get(id).getGraphicsChip(id);
		} else {
			return null;
		}
	}
	
	// 画像の開始idを求める
	public int getPatNo(String bitmapName) {
		if (gfManagerMap.containsKey(bitmapName)) {
			return gfManagerMap.get(bitmapName).getGraphicsNo();
		} else {
			return -1;
		}
	}
	
	// 画像のチップ数を求める
	public int getPatChipSize(String bitmapName) {
		if (gfManagerMap.containsKey(bitmapName)) {
			return gfManagerMap.get(bitmapName).getChipArySize();
		} else {
			return -1;
		}
	}
	
	// 画像チップの数を取得
	public int getChipSize() {
		return chipSize;
	}
	
	
	// Activityが終了したら
	public void onActivityDestroy() {
		clearBitmap();
	}
	
}
