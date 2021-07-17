package tonyusamples.block;

import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSpriteChar;

public class Ball extends TonyuSpriteChar {
	private float vx, vy, od;
	private int life, survive;
	private int pa, mx, my, mmx, mmy;
	private int np;
	private int loop = 0, loop2 = 0;
	public Ball(float x, float y, float vvx, float vvy) {
		super(x, y, GLB.pat_racket+1);
		vx=vvx;
		vy=vvy;
	}

	@Override
	public void onAppear() {
		life = 5;
		survive = 1;
	}
	@Override
	public void loop() {//TGL.mplayer.playSE("racket");
		loop0:
		for (int i=0; i<1; i++) {
			if (loop2 == 0) {
				if (survive == 0) {die(); return;} // 復活できなかった(survive=0だった)場合、死亡
				loop2 = 1;
			}
			// ボールが完全に下に落下するまでは survive=1
			if (loop2 == 1) {
				if (life > 0 && y + vy < TGL.screenHeight-3) loop = 1; else { loop = 0; loop2 = 2; }
				if (loop == 1) {
					if (crashToOld(GLB.racket)) {
						// ラケットに衝突したとき
						if (vy>0) TGL.mplayer.playSE("racket");
						od = dist(vx,vy); // ボールの速さ
						vy = -cos((x-GLB.racket.x) * 2) * od;
						vx =  sin((x-GLB.racket.x) * 2) * od;  // 飛んでいく向き。ボールとラケットのx座標の差による
					}
					pa=getAt(x+vx,y+vy); // これから移動する先にあるマップパターンを読む
				    if (pa!=GLB.pat_block+0 ) {
				    	// 空間以外（壁やブロック）だったら
				    	mmx=mx;mmy=my;
				    	life-=1;
				    	// 跳ね返り
				    	if (getAt(x-vx,y+vy) == GLB.pat_block+0) vx=-vx;
				    	else if (getAt(x+vx,y-vy) == GLB.pat_block+0) vy=-vy;
				    	else {vx=-vx;vy=-vy;}  
				    	// ブロックだったら
				    	if (pa < GLB.pat_block+6) {
				    		np = GLB.pat_block;
				    		if (pa == GLB.pat_block+2) {
				    			// 黄色ブロックだったら、赤ブロックにする
				    			np = GLB.pat_block+1;      
				    			TGL.mplayer.playSE("katai");
				    		} else {
				    			// 黄色でなければ、ブロックの数をへらす
				    			if (GLB.monitor != null) GLB.monitor.blc-=1;
				    			TGL.mplayer.playSE("bpon");
				    		}
				    		TGL.map.set(mmx, mmy, np);
				    		if(pa == GLB.pat_block+4) {
				    			// 白ブロックだったら、新しいボールを発生させる。
				    			appear(new Ball(x, y, -vx, vy));
				    			TGL.mplayer.playSE("multi");
			    			}
				    		if(pa == GLB.pat_block+3 && dist(vx, vy) < 6) {
				    			// 青ブロックだったら、ボールのスピードをあげる。
				    			TGL.mplayer.playSE("speed");
				    			vx = vx * 1.2f; vy = vy * 1.2f;
				    		}
				    	}     
				    } else life=5;
				    x+=vx;
				    y+=vy;
				}
			}
			if (loop2 == 2) {
				if (loop == 0){
					// ボールが下におちたとき
					p += 1; // ボールを赤にする
					survive = 0;
					od = dist(vx, vy);
					vy = -abs(vy) * 1.2f;
				}
				// 放物線を描きながら跳ねる
				if (survive == 0 && (vy<0 || y<TGL.screenHeight)) {
					loop = 1;
				} else {
					loop  = 0;
					loop2 = 0;
				  	p-=1; // ボールを白にする
				  	i--; continue loop0; // 無理やり上へ行く
				}
				if (loop == 1) {
					x+=vx;
					y+=vy;
					// ラケットに当たれば復活する
					if (crashToOld(GLB.racket)) { TGL.mplayer.playSE("racket"); survive = 1; vy = -od; vx = 0;}
					vy+=0.1; // 重力加速度
			  	}
			}
		}
		// ボールの数を検査
		GLB.ballC+=1;
	}
	// あたり判定: xx,yyが壁やブロックだったら、そのキャラクタパターンを返す
	private int getAt(float xx, float yy) {
		int r;
		mx = trunc((xx - vy) / 16);
		my = trunc((yy + vx) / 16);
		r = TGL.map.get(mx,my);
		if (r != GLB.pat_block+0) {
			return r;
		}
		mx = trunc((xx+vy)/16);
		my = trunc((yy-vx)/16);
		r = TGL.map.get(mx,my);
		return r;
	}

}
