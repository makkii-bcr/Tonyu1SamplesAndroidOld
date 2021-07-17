package tonyu.kernel;

import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

// 画像ファイル管理クラス
public class TonyuGraphicsFileManager {
	public static final int ONE_CHIP          = 0; // 一枚絵のチップ
	public static final int EQUAL_SIZE_CHIP   = 1; // 画像を等間隔のチップに分割
	public static final int VARIOUS_SIZE_CHIP = 2; // チップを自由に設定できる
	public static final int TONYU_CHIP        = 3; // Tonyu形式の画像を分析してチップを作り出す
	
	private String bitmapName;
	private Bitmap bitmap;
	private int graphicsNo; // 全画像の中で自分の画像ファイルの最初のチップは何番目かが格納される
	private ArrayList<TonyuGraphicsChip> chipAry = new ArrayList<TonyuGraphicsChip>();
	private int chipArySize;
	private int chipMode;
	private boolean addSW = true; // 追加できるかどうか(登録完了後・追加できないモードのときは追加できないようにする)
	private boolean oneBitmap = false;

	///////////////////////////////////////////////////////////////////////////////////
	// コンストラクタ

	// (チップモード, リソース番号)
	public TonyuGraphicsFileManager(int chipMode, int resId) {
		this.bitmap = BitmapFactory.decodeResource(TGL.res, resId);
		if (bitmap == null) return;
		this.chipMode = chipMode;
		chipModeInit(0, 0); // チップ初期化
	}
	// (チップモード, リソース番号, 幅, 高さ)
	public TonyuGraphicsFileManager(int chipMode, int resId, int chipWidth, int chipHeight) {
		this.bitmap = BitmapFactory.decodeResource(TGL.res, resId);
		if (bitmap == null) return;
		this.chipMode = chipMode;
		chipModeInit(chipWidth, chipHeight); // チップ初期化
	}

	// (チップモード, ビットマップ)
	public TonyuGraphicsFileManager(int chipMode, Bitmap bitmap) {
		if (bitmap == null) return;
		this.chipMode = chipMode;
		chipModeInit(0, 0); // チップ初期化
	}
	// (チップモード, ビットマップ, 幅, 高さ)
	public TonyuGraphicsFileManager(int chipMode, Bitmap bitmap, int chipWidth, int chipHeight) {
		if (bitmap == null) return;
		this.chipMode = chipMode;
		chipModeInit(chipWidth, chipHeight); // チップ初期化
	}
	
	// チップモードによって初期化を選ぶ
	private void chipModeInit(int chipWidth, int chipHeight) {
		switch (chipMode) {
			case ONE_CHIP          : setOneChip();                            break; // 一枚絵
			case EQUAL_SIZE_CHIP   : setEqualSizeChip(chipWidth, chipHeight); break; // 等間隔分割
			case VARIOUS_SIZE_CHIP :                                          break; // チップを自由に設定できる
			case TONYU_CHIP        : setTonyuChip();                          break; // Tonyu形式
			default : break;
		}
	}

	///////////////////////////////////////////////////////////////////////////////////
	// チップの初期化

	// 画像全体で１枚のチップ
	private void setOneChip() {
		chipAry.add(new TonyuGraphicsChip(bitmap));
		oneBitmap = true;
		addSW = false;
	}

	// 画像から同じ大きさのチップに区切る
	private void setEqualSizeChip(int chipWidth, int chipHeight) {
		int bmpWidth  = bitmap.getWidth();
		int bmpHeight = bitmap.getHeight();
		for (int y=chipHeight; y<bmpHeight; y+=chipHeight) {
			for (int x=chipWidth; x<bmpWidth; x+=chipWidth) {
				Bitmap chipBitmap = Bitmap.createBitmap(bitmap, x - chipWidth, y - chipHeight, x, y);
				chipAry.add(new TonyuGraphicsChip(chipBitmap));
				//chipAry.add(new TonyuGraphicsChip(bitmap, x - chipWidth, y - chipHeight, x, y));
			}
		}
		addSW = false;
	}
	
	// 画像の範囲を指定してチップを作り出す(左上X, 左上Y, 右下X, 右下Y)
	public boolean addChip(int l, int t, int r, int b) {
		if (!addSW) return false;
		if (bitmap == null) return false;
		Bitmap chipBitmap = Bitmap.createBitmap(bitmap, l, t, r - l, b - t);
		chipAry.add(new TonyuGraphicsChip(chipBitmap));
		return true;
	}
	// 画像の範囲を指定してチップを作り出す(左上X, 左上Y, 画像の幅,　画像の高さ)
	public boolean addChipWH(int x, int y, int width, int height) {
		if (!addSW) return false;
		if (bitmap == null) return false;
		int bW = bitmap.getWidth();
		int bH = bitmap.getHeight();
		int w = x + width  - 1;
		int h = y + height - 1;
		if (x < 0 || y < 0 || w >= bW || h >= bH || w < x || h < y) return false;
		//Log.v("bmpSize", ""+bitmap.getWidth()+" "+bitmap.getHeight()+" "+x+" "+y+" "+w+" "+h);
		Bitmap chipBitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
		chipAry.add(new TonyuGraphicsChip(chipBitmap));
		//chipAry.add(new TonyuGraphicsChip(bitmap, x, y, x + width, y + height));
		return true;
	}
	
	// Tonyu形式の画像を分析してチップを作り出す
	private void setTonyuChip() {
		TonyuConvTonyuGraphicsChip conv = new TonyuConvTonyuGraphicsChip();
		Bitmap[] bitmapAry = conv.conv(bitmap);
		
		if (bitmapAry == null) { // 分析に失敗したら１つの画像とする
			chipAry.add(new TonyuGraphicsChip(bitmap));
			oneBitmap = true;
		} else { // 分析に成功したら配列からBitmapを取り出し追加
			for (Bitmap b : bitmapAry) {
				chipAry.add(new TonyuGraphicsChip(b));
			}
		}
		addSW = false;
	}
	

	///////////////////////////////////////////////////////////////////////////////////
	// 呼ばれるメソッド
	
	// 画像を渡す
	public Bitmap getBitmap(int chipNo) {
		int rNo = chipNo - graphicsNo;
		if (0 <= rNo && rNo < chipAry.size()) {
			return chipAry.get(rNo).bitmap;
		} else {
			return null;
		}
	}
	
	// グラフィックスチップを渡す
	public TonyuGraphicsChip getGraphicsChip(int id) {
		int chipId = id - graphicsNo;
		if (0 <= chipId && chipId < chipAry.size()) {
			return chipAry.get(chipId);
		} else {
			return null;
		}
	}
	
	// TonyuGraphicsManagerに登録されたとき実行される
	void onBindGraphicsManager(String bitmapName, int graphicsNo) {
		addSW = false; // これ以上追加できないようにする
		this.bitmapName = bitmapName; // 画像ファイルの名前を登録
		this.graphicsNo = graphicsNo; // 全体チップの中で自分の画像ファイルの最初のチップが何番目かを格納
		chipArySize = chipAry.size(); // 画像のサイズを求める
		if (oneBitmap) { // ビットマップが１枚しかないとき以外は、最初の画像はいらないので放棄(最初の画像はチップごとに新たに画像を作ったため)
			bitmap.recycle();
		}
		bitmap = null;
	}
	
	// チップ数を渡す
	public int getChipArySize() {
		return chipArySize;
	}
	
	// 画像全体からの画像番号を渡す
	public int getGraphicsNo() {
		return graphicsNo;
	}
	
	// 画像名を渡す
	public String getBitmapName() {
		return bitmapName;
	}
	
	// メモリ解放
	void release() {
		for (TonyuGraphicsChip gChip : chipAry) {
			gChip.release();
		}
		chipAry.clear();
	}
	
}