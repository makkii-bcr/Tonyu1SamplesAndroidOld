package tonyu.kernel;

import java.util.ArrayList;

public class TonyuProcessGroup {

	private ArrayList<TonyuPlainChar> chars;
	
	public TonyuProcessGroup() {
		chars = new ArrayList<TonyuPlainChar>();
		TGL.chars = chars;
	}
	
	
	// 実行
	public void exec() {
		TonyuPlainChar obj;
		for (int i=0; i<chars.size(); i++) {
			obj = chars.get(i);
			if (obj != null) {
				if (obj.isDead()) { // オブジェクトが死んだら配列から消す (残酷だ…)
					chars.remove(obj);
					i--;
				} else { // 実行
					obj.exec();
				}
			}
		}
	}
	
	//　描画
	public void draw(Object drawObj) {
		// オブジェクトを表示する (SpriteChar, DxChar, TextChar, PanelCharなどを表示)(SecretCharは表示されない)
		for (TonyuPlainChar obj : chars) {
			if (obj != null) {
				if (obj._visible != 0) obj.draw(drawObj);
			}
		}
	}
	
	// オブジェクト全削除
	public void clear() {
		for (TonyuPlainChar obj : chars) {
			if (obj != null) obj.release();
		}
		chars.clear();
	}
	
	// オブジェクト生成
	public TonyuPlainChar appear(TonyuPlainChar obj) {
		chars.add(obj);
		return obj;
	}
}
