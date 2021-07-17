package tonyusamples.hockey2;

import tonyu.kernel.TGL;
import tonyu.kernel.TonyuSpriteChar;

public class Ball extends TonyuSpriteChar {
	float vx, vy;
	private float sx, sy;
	private float spd;
	
	public Ball(float x, float y, int p) {
		super(x, y, p);
	}
	
	@Override
	public void onAppear() {
		TGL.mplayer.playBGM("bgm", true, 0, 5000); //BGM演奏
		GLB.cy = TGL.screenHeight/2;
	}
	
	@Override
	public void loop() {
		// 速度分だけ移動
		x+=vx;
		y+=vy;
		if (kabe(x,y)) {
			// 壁にぶつかった場合
			TGL.mplayer.playSE("bound");
			// 跳ね返り
			if      (!kabe(sx,y)) {x=sx;vx=-vx;}
			else if (!kabe(x,sy)) {y=sy;vy=-vy;}
			else                  {x=sx;y=sy;vx=-vx;vy=-vy;}
		}
		spd=dist(vx,vy);
		if (spd<0.01f) {vx+=0.01f;vy+=0.01f;}
		// ゆっくり減速する
		vx=vx*0.99f;
		vy=vy*0.99f;
		if (spd>30) {
			// 速度30以上にはならないようにする
			vx=vx*30/spd;
			vy=vy*30/spd;
		}
		// 現在の位置を覚える
		sx=x;sy=y;
		
		if (x<GLB.pass.x-30) GLB.pass.enabled = 1;
		// １周したかの判定 。$passは周回を判定するオブジェクト
		if (crashToOld(GLB.pass)) { vx=4;x=GLB.pass.x+32; GLB.pass.lap();}
	}
	@Override
	public void onDead() {
	}
	
	boolean kabe(float xx, float yy) {
		// 壁にぶつかっているかの判定
		int pa=TGL.map.getAt(xx,yy);
		return (pa<GLB.pat_table+9 || pa>GLB.pat_table+14);
	}
}
