package tonyu.kernel;


abstract public class TonyuPlainChar extends TonyuSprite {
	public float x, y;
	public int zOrder;
	int _visible = 1;
	
	public TonyuPlainChar(float x, float y) {
		this.x = x;
		this.y = y;
		zOrder = 0;
	}
	
	/**
	 * appear()によりオブジェクトが出現したときに、<br>特別な処理をする場合にユーザが定義するメソッドです
	 */
	protected void onAppear(){}
	/**
	 * 毎フレーム呼び出されるメソッドです<br>このメソッドを定義して処理を記述するとオブジェクトが動作します
	 */
	protected void loop(){} // 毎回の処理
	/**
	 * die()によりオブジェクトが消滅するときに、<br>特別な処理をする場合にユーザが定義するメソッドです
	 */
	protected void onDead(){} // 終了時の処理
	protected void onRelease(){} // 終了時の処理
	protected void draw(Object drawObj){  // 描画処理
		onDraw();
	}
	/**
	 * オブジェクトに描画をするときに、<br>特別な処理をする場合にユーザが定義するメソッドです。
	 */
	protected void onDraw(){}

	private int loopSW = 0; // ループ１回目か？
	private int dead   = 0; // オブジェクトは死んだか？
	void exec() {
		if (loopSW != 0) { // ２回目の実行
			loop();
		} else { // 初回の実行
			onAppear();
			loopSW = 1; 
			loop();
		}
	}
	protected void release() {
		onRelease();
	}
	public void die() { // オブジェクトを殺す命令 (怖い！)
		dead = 1;
		onDead();
		release();
	}
	public boolean isDead() { // オブジェクトは死んだか？
		return dead != 0;
	}
	
	// 実行できるオブジェクト生成
	public TonyuPlainChar appear(TonyuPlainChar obj) {
		return TGL.tonyuBoot.getCurProcGroup().appear(obj);
	}

	public void setVisible(int v) {
		_visible = v;
	}
	public int getVisible() {
		return _visible;
	}
	

	/** 現在のオブジェクトの幅 */
	public float getWidth() {
		return 16;
	}
	/** 現在のオブジェクトの高さ */
	public float getHeight() {
		return 16;
	}
	
	/** TonyuのgetWidth() (画像の幅より小さい) */
	public float getWidthOld() {
		return 16;
	}
	/** TonyuのgetHeight() (画像の高さより小さい) */
	public float getHeightOld() {
		return 16;
	}

	/**
	 * あたり判定
	 * @param pp 相手のオブジェクト
	 * @param xx 
	 * @param yy 
	 * @return 当たっているか　当たっていないか
	 */
	public boolean crashTo(TonyuPlainChar pp){
		// 他のオブジェクトと衝突しているかどうかを判定します。<HID=23>
		if (pp == null) return false;
		if (abs(pp.x-(x))*2 < getWidth()+pp.getWidth() ) {
			return abs(pp.y-(y))*2 < getHeight()+pp.getHeight() ;
		}
		return false;
	}
	
	/**
	 * あたり判定
	 * @param pp 相手のオブジェクト
	 * @param xx 
	 * @param yy 
	 * @return 当たっているか　当たっていないか
	 */
	public boolean crashTo(TonyuPlainChar pp, float xx, float yy){
		// 他のオブジェクトと衝突しているかどうかを判定します。<HID=23>
		if (pp == null) return false;
		if (abs(pp.x-(x+xx))*2 < getWidth()+pp.getWidth() ) {
			return abs(pp.y-(y+yy))*2 < getHeight()+pp.getHeight() ;
		}
		return false;
	}

	/** TonyuのcrashTo() (Tonyuのあたり判定を再現) */
	public boolean crashToOld(TonyuPlainChar pp) {
		// 他のオブジェクトと衝突しているかどうかを判定します。<HID=23>
		if (pp == null) return false;
		if (abs(pp.x-(x))*2 < getWidthOld()+pp.getWidthOld() ) {
			return abs(pp.y-(y))*2 < getHeightOld()+pp.getHeightOld() ;
		}
		return false;
	}
	/** TonyuのcrashTo() (Tonyuのあたり判定を再現) */
	public boolean crashToOld(TonyuPlainChar pp, float xx, float yy) {
		// 他のオブジェクトと衝突しているかどうかを判定します。<HID=23>
		if (pp == null) return false;
		if (abs(pp.x-(x+xx))*2 < getWidthOld()+pp.getWidthOld() ) {
			return abs(pp.y-(y+yy))*2 < getHeightOld()+pp.getHeightOld() ;
		}
		return false;
	}
}
