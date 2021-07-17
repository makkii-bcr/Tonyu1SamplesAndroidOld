package tonyu.kernel;

abstract public class TonyuPage extends TonyuObject {

	abstract public void open(TonyuPage oldPage);
	abstract public void close(TonyuPage newPage);

	// 実行できるオブジェクト生成
	public TonyuPlainChar appear(TonyuPlainChar obj) {
		return TGL.tonyuBoot.getCurProcGroup().appear(obj);
	}
	// 画像の開始番号を取得
	public int getPatNo(String bitmapName) {
		return TGL.grManager.getPatNo(bitmapName);
	}
	// 現在のページと同じか比べる
	public boolean equalsePage(TonyuPage cmpPage) {
		return cmpPage == null || cmpPage.getClass() != this.getClass();
	}
}