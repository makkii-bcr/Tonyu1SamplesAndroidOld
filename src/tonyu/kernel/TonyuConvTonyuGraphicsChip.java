package tonyu.kernel;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.Config;

/**
 * Tonyu用に作られた画像ファイルを分析するクラス。
 * チップ単位に分割してアプリで使えるようにする
 */
public class TonyuConvTonyuGraphicsChip {
	public Bitmap[] conv(Bitmap bitmap) {
		int bW, bH, bgColor, col, trColor, error = 0;
		bW = bitmap.getWidth();
		bH = bitmap.getHeight();
		if (bW < 5 || bH < 4) return null; // pngのサイズが小さいなら終了

		// 取り出したチップの座標とサイズを記録する(１つの要素にはArrayList<Integer>で{x,y,w,h}の情報が格納される)
		ArrayList<ArrayList<Integer>> chipXYWHAry = new ArrayList<ArrayList<Integer>>();
		
		int pixels[] = new int[bW * bH];               // int配列で画像を分析
		bitmap.getPixels(pixels, 0, bW, 0, 0, bW, bH); // int配列に画像データを格納
		
		int cx, cy, cw, ch, chipF = 0;
		bgColor = bitmap.getPixel(0, 0); // 背景色を取得
		errorLabel :
		for (int y=0; y<bH; y++) {
			for (int x=0; x<bW; x++) {
				// チップの始まりをチェック //
				col = pixels[x + y * bW];
				if (col != bgColor) { // チップの始まりがあったら
					chipF = 1; // チップ分析中
					trColor = col; // 透過色を記憶
					// チップの幅をチェック //
					for (cx=x+1;cx<bW;cx++) {
						col = pixels[cx + y * bW];
						if (col != trColor) { chipF = 2; break; }
					}
					cw = cx - x;
					if (cw < 3 || chipF != 2) { error = 1; break errorLabel; } // ３未満は画像ができない/画像が右端を過ぎた⇒エラー

					// チップの高さをチェック //
					cx--;
					for (cy=y+1;cy<bH;cy++) {
						col = pixels[cx + cy * bW];
						if (col != trColor) { chipF = 3; break; }
					}
					ch = cy - y;
					if (ch < 3 || chipF != 3) { error = 1; break errorLabel; } // ３未満は画像ができない⇒エラー
					
					//Log.v("TonyuChip", ""+(x+1)+" "+(y+1)+" "+(cw-2)+" "+(ch-2));
					
					// チップの座標とサイズを記録する
					ArrayList<Integer> chipXYWH = new ArrayList<Integer>();
					chipXYWH.add(trColor);
					chipXYWH.add(x + 1);
					chipXYWH.add(y + 1);
					chipXYWH.add(cw - 2);
					chipXYWH.add(ch - 2);
					chipXYWHAry.add(chipXYWH);
					
					// 読み取った画像は背景色で塗りつぶしておく
					for (int yy=y; yy<y+ch; yy++) {
						for (int xx=x; xx<x+cw; xx++) {
							pixels[xx + yy * bW] = bgColor;
						}
					}

					chipF = 0; // チップ分析終了
					x += cw;
				}
			}
		}
		
		// 分析に失敗
		if (error == 1 || chipF != 0 || chipXYWHAry.size() <= 0) {
			return null;
		} else { // 分析成功
			// 画像ファイルをチップ単位に分割していく //
			bitmap.getPixels(pixels, 0, bW, 0, 0, bW, bH); // 画像データを再取得(分析途中でint配列の色を変えてしまうため)
			
			Bitmap[] bitmapAry = new Bitmap[chipXYWHAry.size()];
			Bitmap chipBitmap;
			int i = 0;
			for (ArrayList<Integer> cp : chipXYWHAry) {
				// 画像を新しく作る(元画像がアルファに対応していないと透過処理ができないため)
				chipBitmap = Bitmap.createBitmap(cp.get(3), cp.get(4), Config.ARGB_8888);
				
				// 変数使い回し
				cx = cp.get(1);
				cy = cp.get(2);
				cw = cp.get(3);
				ch = cp.get(4);
				int[] cPixels = new int[cw * ch]; // 新たな配列(チップの画像データのint配列)
				
				// 元データからチップデータにピクセルを転送 //
				trColor = cp.get(0); // 透過色を取得
				for (int y=0; y<ch; y++) {
					for (int x=0; x<cw; x++) {
						col = pixels[(x + cx) + (y + cy) * bW];
						cPixels[x + y * cw] = col == trColor ? Color.TRANSPARENT : col; // 透過色は透過
					}
				}
				chipBitmap.setPixels(cPixels, 0, cw, 0, 0, cw, ch); // int配列をBitmapに適応させる
				bitmapAry[i++] = chipBitmap; // Bitmap配列にBitmap１つを登録
			}
			return bitmapAry;
		}
		
	}
}
