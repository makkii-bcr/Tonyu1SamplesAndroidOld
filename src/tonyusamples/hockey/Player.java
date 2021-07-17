package tonyusamples.hockey;

import android.view.KeyEvent;
import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSpriteChar;

public class Player extends TonyuSpriteChar {

	public Player(float x, float y, int p) {
		super(x, y, p);
	}
	
	int dir, manPlay;
	float rs;
	int moveSW = 0;
	
	@Override
	public void onAppear() {
		dir = 1;
		rs = 0.4f;
		setVisible(0);
	}
	
	@Override
	public void loop() {
		// プレイヤーのラケットを動かす
		if (manPlay == 1) {
			if (moveSW == 0 && TGL.padPush == 0) moveSW = 1;
			if (moveSW == 1 && TGL.padPush == 1) moveSW = 2;
			if (moveSW == 2) {
				// マウスの位置にラケットをもっていく
				GLB.racket.x = TGL.padX * rs + GLB.racket.x * (1 - rs);
				// 相手フィールドに入れないようにする
				if ((GLB.racket.x - TGL.screenWidth / 2) * dir >= 0) GLB.racket.x = TGL.screenWidth / 2;
				GLB.racket.y = TGL.padY * rs + GLB.racket.y * (1 - rs);
			}
		}
		if (getkey(KeyEvent.KEYCODE_MENU) == 1) manPlay = abs(manPlay - 1);
	}

}
